package com.example.demo.service.impl;

import com.example.demo.dto.LetterDto;
import com.example.demo.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailServiceImpl implements SendEmailService {
    private final JavaMailSender mailSender;
    public void sendEmail(LetterDto letterDto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("aleksejtalitej@gmail.com");
        mailMessage.setTo(letterDto.getSubscription().getEmail());
        mailMessage.setSubject("DB change notification");
        mailMessage.setText(letterDto.toString());
        mailSender.send(mailMessage);
    }
}
