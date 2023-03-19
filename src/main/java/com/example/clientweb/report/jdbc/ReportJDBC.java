package com.example.clientweb.report.jdbc;

import com.example.clientweb.pojo.ClientReport;
import com.example.clientweb.report.ReportView;
import com.sun.istack.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportJDBC implements ConnectionCallback<Void> {
    private final ReportView reportView;
    private StringBuilder query = new StringBuilder("SELECT c.fio                         as client,\n" +
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

    public ReportJDBC(ReportView reportView) {
        this.reportView = reportView;
    }

    @Override
    public Void doInConnection(@NotNull Connection con) throws SQLException, DataAccessException {
        try (PreparedStatement preparedStatement = con.prepareStatement(query.toString());
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                reportView.addRow(toReportBody(resultSet));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    private ClientReport toReportBody(ResultSet resultSet) throws SQLException {
        String fio = resultSet.getString("client");
        Integer packageAmount = resultSet.getInt("all_package_number");
        Integer filesAmount = resultSet.getInt("all_file_number");
        return new ClientReport(fio,packageAmount,filesAmount);
    }
}
