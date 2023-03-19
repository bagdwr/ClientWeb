package com.example.clientweb.report;

import com.example.clientweb.pojo.ClientReport;
import com.example.clientweb.report.reportModel.ReportFooter;
import com.example.clientweb.report.reportModel.ReportHeader;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportExcel implements ReportView {
    private final OutputStream outputStream;
    private int rowNum = 0;
    private XSSFWorkbook workbook;
    private XSSFSheet spreadsheet;
    private XSSFRow row;
    private XSSFCell cell;
    private Integer allFileAmount=0;
    private Integer allPackageAmount=0;

    public ReportExcel(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    private XSSFCell createCell(int index, String word) {
        cell = row.createCell(index, CellType.STRING);
        cell.setCellValue(word);
        return cell;
    }

    @Override
    public void start(ReportHeader reportHeader) throws Exception {
        workbook = new XSSFWorkbook();
        spreadsheet = workbook.createSheet("Client Data ");
        spreadsheet.setColumnWidth(0, 40 * 256);
        spreadsheet.setColumnWidth(1, 25 * 256);
        spreadsheet.setColumnWidth(2, 25 * 256);
        row = spreadsheet.createRow(rowNum);
        CellStyle boldStyle = workbook.createCellStyle();
        Font cellFont = workbook.createFont();
        cellFont.setBold(true);
        boldStyle.setFont(cellFont);

        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Отчет клиентов");
        cell.setCellStyle(boldStyle);
        rowNum += 2;
        row = spreadsheet.createRow(rowNum);

        createCell(0, reportHeader.col1).setCellStyle(boldStyle);
        createCell(1, reportHeader.col2).setCellStyle(boldStyle);
        createCell(2, reportHeader.col3).setCellStyle(boldStyle);
    }

    @Override
    public void addRow(ClientReport reportBody) throws Exception {
        rowNum++;
        row = spreadsheet.createRow(rowNum);
        createCell(0, reportBody.fio);
        createCell(1, String.valueOf(reportBody.packageAmount));
        createCell(2, String.valueOf(reportBody.filesAmount));

        allPackageAmount = allPackageAmount+reportBody.packageAmount;
        allFileAmount = allFileAmount+reportBody.filesAmount;
    }

    @Override
    public void finish() throws Exception {
        rowNum++;
        row = spreadsheet.createRow(rowNum);
        createCell(0,"Итого:");
        createCell(1,String.valueOf(allPackageAmount));
        createCell(2,String.valueOf(allFileAmount));
        rowNum++;
        row = spreadsheet.createRow(rowNum);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        createCell(0,"Сгенерировано в " + sdf.format(new Date()));
        workbook.write(outputStream);
    }
}
