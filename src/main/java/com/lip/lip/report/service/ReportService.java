package com.lip.lip.report.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lip.lip.disicipline.dtos.response.DisciplinePerformanceDto;
import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.studylog.entity.StudyLog;
import com.lip.lip.studylog.repository.StudyLogRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final StudyLogRepository studyLogRepository;
    private final RevisionRepository revisionRepository;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - EEEE",
            new Locale("pt", "BR"));

    // --- RELATÓRIO ESTRATÉGICO (MANTIDO CONFORME COMBINADO) ---
    public byte[] generateFullReportPdf(Long userId) {
        List<StudyLog> logs = studyLogRepository.findByUserIdOrderByStudyDateDesc(userId);
        List<Revision> revisions = revisionRepository.findByUserId(userId);
        List<DisciplinePerformanceDto> disciplineStats = studyLogRepository.getStudiesPerDiscipline(userId);
        return buildDetailedReport(logs, revisions, disciplineStats);
    }

    // --- NOVO CRONOGRAMA: PLANNER DE REVISÕES FUTURAS ---
    public byte[] generateSchedulePdf(Long userId) {
        List<Revision> futureRevisions = revisionRepository.findByUserId(userId).stream()
                .filter(r -> !r.isCompleted() && !r.getScheduledDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(Revision::getScheduledDate))
                .collect(Collectors.toList());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, new Color(79, 70, 229));
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
        Font itemFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.DARK_GRAY);
        Font discFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(79, 70, 229));

        Paragraph title = new Paragraph("MEU PLANNER DE REVISÕES", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(30);
        document.add(title);

        if (futureRevisions.isEmpty()) {
            document.add(new Paragraph("Nenhuma revisão agendada para os próximos dias."));
        } else {
            Map<LocalDate, List<Revision>> grouped = futureRevisions.stream()
                    .collect(Collectors.groupingBy(Revision::getScheduledDate, TreeMap::new, Collectors.toList()));

            for (Map.Entry<LocalDate, List<Revision>> entry : grouped.entrySet()) {
                PdfPTable dayTable = new PdfPTable(1);
                dayTable.setWidthPercentage(100);
                dayTable.setSpacingBefore(10);

                PdfPCell dayCell = new PdfPCell(
                        new Phrase(entry.getKey().format(dateFormatter).toUpperCase(), dateFont));
                dayCell.setBackgroundColor(new Color(79, 70, 229));
                dayCell.setPadding(8);
                dayCell.setBorder(Rectangle.NO_BORDER);
                dayTable.addCell(dayCell);
                document.add(dayTable);

                for (Revision rev : entry.getValue()) {
                    PdfPTable taskTable = new PdfPTable(1); // Mudado para 1 coluna apenas
                    taskTable.setWidthPercentage(100);

                    // Conteúdo da Revisão sem o checkbox [ ]
                    Phrase p = new Phrase();
                    p.add(new Chunk("• ", discFont)); // Adicionei um marcador de ponto discreto
                    p.add(new Chunk(rev.getStudyLog().getDiscipline().getName() + " - ", discFont));
                    p.add(new Chunk(rev.getStudyLog().getTheme() + " (Revisão #" + rev.getRevisionNumber() + ")",
                            itemFont));

                    PdfPCell textCell = new PdfPCell(p);
                    textCell.setBorder(Rectangle.BOTTOM);
                    textCell.setBorderColor(new Color(230, 230, 230)); // Cinza bem clarinho
                    textCell.setPadding(10);
                    taskTable.addCell(textCell);

                    document.add(taskTable);
                }
                document.add(new Paragraph(" "));
            }
        }

        document.close();
        return out.toByteArray();
    }

    // --- LÓGICA DO RELATÓRIO DETALHADO (PRIVADA PARA ORGANIZAÇÃO) ---
    private byte[] buildDetailedReport(List<StudyLog> logs, List<Revision> revisions,
            List<DisciplinePerformanceDto> disciplineStats) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new Color(30, 41, 59));
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(79, 70, 229));
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);

        Paragraph title = new Paragraph("RELATÓRIO ESTRATÉGICO DE DESEMPENHO", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // 1. DASHBOARD DE SAÚDE
        LocalDate hoje = LocalDate.now();
        long totalRev = revisions.size();
        long concluidas = revisions.stream().filter(Revision::isCompleted).count();
        long atrasadas = revisions.stream().filter(r -> !r.isCompleted() && r.getScheduledDate().isBefore(hoje))
                .count();
        long recuperadas = revisions.stream().filter(
                r -> r.isCompleted() && r.getCompletedAt() != null && r.getCompletedAt().isAfter(r.getScheduledDate()))
                .count();

        document.add(new Paragraph("1. INDICADORES DE EFICIÊNCIA", sectionFont));
        PdfPTable healthTable = new PdfPTable(4);
        healthTable.setWidthPercentage(100);
        healthTable.setSpacingBefore(10);
        addHealthCard(healthTable, "Total de Revisões", String.valueOf(totalRev), Color.WHITE);
        addHealthCard(healthTable, "Atrasadas Hoje", String.valueOf(atrasadas), new Color(254, 226, 226));
        addHealthCard(healthTable, "Recuperadas (Pós-Atraso)", String.valueOf(recuperadas), new Color(254, 243, 199));
        addHealthCard(healthTable, "Taxa de Sucesso", (totalRev > 0 ? (concluidas * 100 / totalRev) : 0) + "%",
                new Color(220, 252, 231));
        document.add(healthTable);

        // 2. PERFORMANCE POR DISCIPLINA
        document.add(new Paragraph("\n2. ANÁLISE POR DISCIPLINA", sectionFont));
        PdfPTable discTable = new PdfPTable(4);
        discTable.setWidthPercentage(100);
        discTable.setSpacingBefore(10);
        addTableHeader(discTable, new String[] { "Disciplina", "Sessões", "Revisões Feitas", "% Esforço" });
        for (DisciplinePerformanceDto d : disciplineStats) {
            discTable.addCell(new Phrase(d.disciplineName(), bodyFont));
            discTable.addCell(new Phrase(String.valueOf(d.totalStudies()), bodyFont));
            discTable.addCell(new Phrase(String.valueOf(d.completedRevisions()), bodyFont));
            String perc = (logs.size() > 0) ? (d.totalStudies() * 100 / logs.size()) + "%" : "0%";
            discTable.addCell(new Phrase(perc, bodyFont));
        }
        document.add(discTable);

        // 3. LOGS DETALHADOS
        document.add(new Paragraph("\n3. LOGS DE ESTUDO DETALHADOS", sectionFont));
        PdfPTable logTable = new PdfPTable(5);
        logTable.setWidthPercentage(100);
        logTable.setSpacingBefore(10);
        try {
            logTable.setWidths(new int[] { 4, 3, 4, 2, 6 });
        } catch (Exception e) {
        }
        addTableHeader(logTable, new String[] { "Data", "Disciplina", "Tópico", "Tempo", "Notas" });
        for (StudyLog log : logs) {
            logTable.addCell(new Phrase(log.getStudyDate().format(dateFormatter), bodyFont));
            logTable.addCell(new Phrase(log.getDiscipline().getName(), bodyFont));
            logTable.addCell(new Phrase(log.getTheme(), bodyFont));
            logTable.addCell(
                    new Phrase(String.format("%.1f", (double) log.getDurationMinutes() / 60.0) + "h", bodyFont));
            logTable.addCell(new Phrase(log.getNotes() != null ? log.getNotes() : "-", bodyFont));
        }
        document.add(logTable);

        document.close();
        return out.toByteArray();
    }

    private void addHealthCard(PdfPTable table, String label, String value, Color bgColor) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(bgColor);
        cell.setPadding(10);
        cell.addElement(new Phrase(label, FontFactory.getFont(FontFactory.HELVETICA, 8, Color.DARK_GRAY)));
        cell.addElement(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK)));
        table.addCell(cell);
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(
                    new Phrase(h, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE)));
            cell.setBackgroundColor(new Color(30, 41, 59));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }
    }

    public Map<String, Object> getUserGeneralStats(Long userId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudies", studyLogRepository.countTotalStudiesByUserId(userId));
        stats.put("completedRevisions", revisionRepository.countCompletedRevisionsByUserId(userId));
        stats.put("performanceByDiscipline", studyLogRepository.getStudiesPerDiscipline(userId));
        return stats;
    }
}