# Лабораторная работа 4 (Java Message Service)
## Организация механизма логирования
Для огранизации механизма логирования была создана следующая модель данных:


В таблице **log_changes** хранятся все логи обновления таблиц tickets, performances, timetable. В таблице **subscriptions** хранится список подписчиков на изменение (их email и имя). 
В таблице **events_to_subscribe** хранится список типов изменения таблиц для которых включения рассылка по почте (одно из значений enum ChangeTypeEnum):

```bash
public enum ChangeTypeEnum {
    INSERT,
    UPDATE,
    DELETE,
    CASCADE_DELETE
}
```
Логирование событий происходит в контроллерах. [PerformanceController](https://github.com/sumrako/ESA_LAB_4/blob/master/src/main/java/com/example/demo/controller/PerformanceController.java), 
[TimetableController](https://github.com/sumrako/ESA_LAB_4/blob/master/src/main/java/com/example/demo/controller/TimetableController.java), [TicketController](https://github.com/sumrako/ESA_LAB_4/blob/master/src/main/java/com/example/demo/controller/TicketController.java). 

## JMS
Для реализации JMS был использован **Spring JMS**, предоставленную Spring платформу интеграции JMS, которая упрощает использование JMS API.Spring Framework позаботится о некоторых низкоуровневых деталях при работе с JMS API. 

Отправлять / получать сообщение в данной лабораторной работе мы будем из **Apache ActiveMQ**, api для работы с которым подключается внедрением следующей зависимости:
```bash
<dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-activemq</artifactId>
</dependency>
```

### Конфигурация
Создание JmsTemplate для продьюсера происходит в [SenderConfig](https://github.com/sumrako/ESA_LAB_4/blob/master/src/main/java/com/example/demo/config/SenderConfig.java) классе. 
Для использования Spring JMS нужно создать экземпляр **ConnectionFactory**, который используется для создания соединений с JMS. В случае ActiveMQ мы используется **ActiveMQConnectionFactory**. В ActiveMQConnectionFactory устанавливается URL-адрес брокера, который подтягивается из application.yml файла с помощью @Value аннотации. Обоочка **CachingConnectionFactory** нужна для того, чтобы сохранить преимущество кэширования сеансов, подключений и производителей, а также автоматического восстановления соединений.

```bash
activemq.broker-url: tcp://localhost:61616
spring.activemq.packages.trust-all=true
```
Для консьюмера при подключении к ActiveMQ, ActiveMQConnectionFactory создается и передается в конструкторе DefaultJmsListenerContainerFactory. Для конфигурации консьюмера создан класс 
[ReceiverConfig](https://github.com/sumrako/ESA_LAB_4/blob/master/src/main/java/com/example/demo/config/ReceiverConfig.java)

### Message Producer
Для отправки сообщений использовался JmsTemplate, который требует ссылки на ConnectionFactory. Для отправки вызывается метод convertAndSend(), который отправляет данный объект по ***logger.q*** назначению, преобразуя объект в сообщение JMS. 

Код реализации:

```bash
@Component
@RequiredArgsConstructor
public class Sender {
    private final JmsTemplate jmsTemplate;

    public void send(Log message) {
        jmsTemplate.convertAndSend("logger.q", message);
    }
}
```

### Message Consumer
**@JmsListener** Аннотация создает контейнер прослушивателя сообщений, используя **JmsListenerContainerFactory**. 

В методе получения сообщения реализована логика механизма логирования и мониторинга изменений. Переданное сообщение типа Log сохраняется в базу данных. Затем, анализируется наличие типа изменения с теми, которые указаны к таблице **events_to_subscribe**. Если о данном типе изменений необхдимо оповещать по электронной почте, то для всех подписчиков из **subscriptions** реализуется отправка сообщения на электронную почту. Формирование текста сообщения происходит в классе [LetterDto](https://github.com/sumrako/ESA_LAB_4/blob/master/src/main/java/com/example/demo/dto/LetterDto.java) в методе ***toString()***:

Код реализации:

```bash
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
```

## Email sender
> Spring Boot предоставляет возможность отправлять электронные письма через SMTP с использованием библиотеки JavaMail. 
 
Код реализации:

```bash
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
```

## Демонстрация работоспособности

### Письма на электронную почту для разных типов изменения
![image](https://github.com/sumrako/ESA_LR_4/assets/67976572/fd73ce41-0970-49e7-a175-cde20ff64dd1)
![image](https://github.com/sumrako/ESA_LR_4/assets/67976572/e5aa7cab-7b20-41ef-b2c1-aca21300561c)
![image](https://github.com/sumrako/ESA_LR_4/assets/67976572/8897a7ec-ca51-4b78-b212-cd226fc2e10c)


