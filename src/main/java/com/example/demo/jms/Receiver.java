package com.example.demo.jms;

import com.example.demo.data.EventToSubscribe;
import com.example.demo.data.Log;
import com.example.demo.data.Subscription;
import com.example.demo.dto.LetterDto;
import com.example.demo.enums.ChangeTypeEnum;
import com.example.demo.repository.EventToSubscribeRepository;
import com.example.demo.repository.LogRepository;
import com.example.demo.repository.SubscriptionRepository;
import com.example.demo.service.SendEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class Receiver {
    private final LogRepository logRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final EventToSubscribeRepository eventToSubscribeRepository;
    private final SendEmailService sendEmailService;

    @JmsListener(destination = "logger.q")
    public void receive(Log message) {
        logRepository.save(message);
        List<ChangeTypeEnum> eventToSubscribeList = eventToSubscribeRepository
                .findAll()
                .stream().map(EventToSubscribe::getEventType)
                .toList();

        if (! eventToSubscribeList.contains(message.getChangeType()))
            return;
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        List<LetterDto> letterDtoList = subscriptions
                .stream()
                .map((e) -> new LetterDto(message, e))
                .toList();

        letterDtoList.forEach(sendEmailService::sendEmail);
    }
}
