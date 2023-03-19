package com.example.clientweb.service;

import com.example.clientweb.pojo.ClientReport;
import com.example.clientweb.report.ReportView;

import java.util.List;

public interface ReportService {

    List<ClientReport> getAllReport();

    void generateReport(ReportView reportView) throws Exception;

}
