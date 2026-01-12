package com.lip.lip.email.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String name, String token) {
        // CORREÇÃO: Usar o endpoint correto do AuthController
        String verificationLink = "http://localhost:5173/verificar-email?token=" + token;

        String subject = "Confirme seu e-mail - Sistema de Estudos";

        String body = """
                Olá %s,

                Obrigado por se cadastrar no Sistema de Estudos!

                Para confirmar seu e-mail e ativar sua conta, clique no link abaixo:
                %s

                Este link expira em 24 horas.

                Caso não tenha sido você, ignore este e-mail.

                Atenciosamente,
                Equipe Sistema de Estudos
                """
                .formatted(name, verificationLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("noreply@sistemadeestudos.com"); // Configure isso no application.properties

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String name, String token) {
        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        String subject = "Redefinição de Senha - Sistema de Estudos";

        String body = """
                Olá %s,

                Recebemos uma solicitação para redefinir sua senha.

                Para criar uma nova senha, clique no link abaixo:
                %s

                Este link expira em 24 horas.

                Se você não solicitou a redefinição de senha, ignore este e-mail.

                Atenciosamente,
                Equipe Sistema de Estudos
                """
                .formatted(name, resetLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("noreply@sistemadeestudos.com");

        mailSender.send(message);
    }

    public void sendPasswordResetCodeEmail(String to, String name, String code) {

        String subject = "Código de recuperação de senha";

        String body = """
                Olá %s,

                Recebemos uma solicitação para redefinir sua senha.

                Seu código de recuperação é:

                %s

                Este código expira em 15 minutos.

                Se você não solicitou, ignore este email.

                Atenciosamente,
                Equipe Sistema de Estudos
                """
                .formatted(name, code);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("noreply@sistemadeestudos.com");

        mailSender.send(message);
    }

    // Adicione estes métodos ao seu EmailService.java

    public void sendDailyTaskEmail(String to, String name, String tasks) {
        String subject = "📅 Suas revisões de hoje - " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM"));
        String body = """
                Olá %s,

                Hoje é dia de revisar os seguintes tópicos:

                %s

                Mantenha o ritmo! Bons estudos.
                """.formatted(name, tasks);

        sendSimpleEmail(to, subject, body);
    }

    public void sendOverdueAlertEmail(String to, String name, String overdueTasks) {
        String subject = "⚠️ Alerta: Você possui revisões atrasadas";
        String body = """
                Olá %s,

                Notamos que você possui revisões pendentes que já passaram do prazo:

                %s

                Tente regularizar seu cronograma para não perder a curva de esquecimento!
                """.formatted(name, overdueTasks);

        sendSimpleEmail(to, subject, body);
    }

    // Método privado para evitar repetição de código (boilerplate)
    private void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom("noreply@sistemadeestudos.com");
        mailSender.send(message);
    }
}