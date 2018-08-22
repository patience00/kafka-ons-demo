package com.kafka.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:通天晓107
 * @date :2018-02-13 11:40
 * @version: 1.0
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private boolean enableAutoCommit;

    @Value("${spring.kafka.listener.concurrency}")
    private Integer concurrency;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private String autoCommitInterval;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String key;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String value;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;

    @Value("${path.app}")
    private String appPath;



    @Bean
    public ConsumerFactory<String, String> consumerFactory() {

        Map<String, Object> configProps = new HashMap<>(11);
        System.setProperty("java.security.auth.login.config", appPath + "/src/main/resources/kafka_client_jaas.conf");
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        configProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, appPath + "/src/main/resources/kafka.client.truststore.jks");
        configProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "KafkaOnsClient");
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        configProps.put(SaslConfigs.SASL_MECHANISM, "ONS");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        configProps.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, key);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, value);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }


}
