package com.example.clientweb.service.impl;

import com.example.clientweb.db.DbManager;
import com.example.clientweb.pojo.ClientReport;
import com.example.clientweb.report.ReportView;
import com.example.clientweb.report.jdbc.ReportJDBC;
import com.example.clientweb.report.reportModel.ReportFooter;
import com.example.clientweb.report.reportModel.ReportHeader;
import com.example.clientweb.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private DbManager dbManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<ClientReport> getAllReport() {
        List<ClientReport> clientReports = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT c.fio                         as client,\n" +
                "       COALESCE(p.package_number, 0) as all_package_number,\n" +
                "       COALESCE(f.file_number, 0)    as all_file_number\n" +
                "FROM client c\n" +
                "         LEFT JOIN\n" +
                "     (SELECT client_id,\n" +
                "             COUNT(*) as package_number\n" +
                "      FROM client_package\n" +
                "      WHERE actual = true\n" +
                "      GROUP BY client_id) p ON c.id = p.client_id\n" +
                "         LEFT JOIN\n" +
                "     (SELECT cp.client_id,\n" +
                "             COUNT(*) as file_number\n" +
                "      FROM client_package cp\n" +
                "               INNER JOIN\n" +
                "           client_file cf ON cp.id = cf.client_package_id\n" +
                "      WHERE cf.actual = true\n" +
                "      GROUP BY cp.client_id) f ON c.id = f.client_id\n" +
                "WHERE c.actual = true\n" +
                "ORDER BY c.fio asc;");

        try (Connection connection = dbManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String fio = resultSet.getString("client");
                Integer packageAmount = resultSet.getInt("all_package_number");
                Integer filesAmount = resultSet.getInt("all_file_number");
                ClientReport clientReport = new ClientReport(fio,packageAmount,filesAmount);
                clientReports.add(clientReport);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clientReports;
    }

    @Override
    public void generateReport(ReportView reportView) throws Exception {
        ReportHeader reportHeader = new ReportHeader();
        reportHeader.col1 = "Клиент/досье";
        reportHeader.col2 = "Папка (кол-во)";
        reportHeader.col3 = "Файл (кол-во)";

        reportView.start(reportHeader);
        jdbcTemplate.execute(new ReportJDBC(reportView));
        reportView.finish();
    }
}
