package com.example.demo.config;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JMSConfig {
    @Bean(name="createSession")
    public Session createSession() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connection = connectionFactory.createConnection();
        return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    @Bean(name="createTopic")
    public Topic createLoggerTopic(@Qualifier ("createSession") Session session) throws JMSException {
        return session.createTopic("logger");
    }

    @Bean(name = "producer")
    public MessageProducer createProducer(@Qualifier ("createSession") Session session,
                                          @Qualifier ("createTopic") Topic topic) throws JMSException {
        return session.createProducer(topic);
    }

    @Bean(name = "consumer")
    public MessageConsumer createConsumer(@Qualifier ("createSession") Session session,
                                          @Qualifier ("createTopic") Topic topic) throws JMSException {
        MessageConsumer consumer = session.createConsumer(topic);
        return consumer;
    }
}
