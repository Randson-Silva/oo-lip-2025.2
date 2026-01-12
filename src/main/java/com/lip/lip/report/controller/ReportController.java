package com.lip.lip.report.controller;

import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.lip.lip.report.service.ReportService;
import com.lip.lip.user.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getStats(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(reportService.getUserGeneralStats(user.getId()));
    }

    @GetMapping("/schedule/pdf")
    public ResponseEntity<byte[]> downloadSchedule(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        byte[] pdf = reportService.generateSchedulePdf(user.getId());
        return createPdfResponse(pdf, "cronograma_estudos.pdf");
    }

    @GetMapping("/full-report/pdf")
    public ResponseEntity<byte[]> downloadReport(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        byte[] pdf = reportService.generateFullReportPdf(user.getId());
        return createPdfResponse(pdf, "relatorio_detalhado.pdf");
    }

    private ResponseEntity<byte[]> createPdfResponse(byte[] contents, String filename) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", filename);
        return ResponseEntity.ok().headers(headers).body(contents);
    }
}