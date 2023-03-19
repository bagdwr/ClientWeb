package com.example.clientweb.report;

import com.example.clientweb.pojo.ClientReport;
import com.example.clientweb.report.reportModel.ReportFooter;
import com.example.clientweb.report.reportModel.ReportHeader;

public interface ReportView {
    void start(ReportHeader reportHeader) throws Exception;

    void addRow(ClientReport reportBody) throws Exception;

    void finish() throws Exception;
}
