package com.lip.lip.email.scheduler;

import com.lip.lip.email.service.EmailService;
import com.lip.lip.revision.entity.Revision;
import com.lip.lip.revision.repository.RevisionRepository;
import com.lip.lip.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudyNotificationScheduler {

        private final RevisionRepository revisionRepository;
        private final EmailService emailService;

        @Scheduled(cron = "0 0 7 * * *")
        public void notifyDailyRevisions() {
                LocalDate hoje = LocalDate.now();
                List<Revision> todaysRevisions = revisionRepository.findAll().stream()
                                .filter(r -> !r.isCompleted() && r.getScheduledDate().equals(hoje))
                                .collect(Collectors.toList());

                Map<User, List<Revision>> groupedByUser = todaysRevisions.stream()
                                .collect(Collectors.groupingBy(Revision::getUser));

                groupedByUser.forEach((user, revisions) -> {
                        String tasks = revisions.stream()
                                        .map(r -> "- " + r.getStudyLog().getDiscipline().getName() + ": "
                                                        + r.getStudyLog().getTheme())
                                        .collect(Collectors.joining("\n"));

                        emailService.sendDailyTaskEmail(user.getEmail(), user.getName(), tasks);
                });
        }

        @Scheduled(cron = "0 0 9 * * *")
        public void notifyOverdueRevisions() {
                LocalDate hoje = LocalDate.now();
                List<Revision> overdueRevisions = revisionRepository.findAll().stream()
                                .filter(r -> !r.isCompleted() && r.getScheduledDate().isBefore(hoje))
                                .collect(Collectors.toList());

                Map<User, List<Revision>> groupedByUser = overdueRevisions.stream()
                                .collect(Collectors.groupingBy(Revision::getUser));

                groupedByUser.forEach((user, revisions) -> {
                        String tasks = revisions.stream()
                                        .map(r -> "- " + r.getStudyLog().getDiscipline().getName() + ": "
                                                        + r.getStudyLog().getTheme() + " (Vencido em "
                                                        + r.getScheduledDate() + ")")
                                        .collect(Collectors.joining("\n"));

                        emailService.sendOverdueAlertEmail(user.getEmail(), user.getName(), tasks);
                });
        }
}