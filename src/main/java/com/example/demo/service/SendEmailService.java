package com.example.demo.service;

import com.example.demo.dto.LetterDto;

public interface SendEmailService {
    void sendEmail(LetterDto letterDto);
}
