//package com.example.mq.kafka;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * @Auther: gsh
// * @Date: 2018/8/7 10:54
// * @Description: 入门实例 - 生产者 producer
// *
// */
//@Configuration
//@EnableKafka
//@Slf4j
//public class UserKafkaProducer {
//
//    @Value("${kafka.producer.servers}")
//    private String servers;
//    @Value("${kafka.producer.retries}")
//    private int retries;
//    @Value("${kafka.producer.batch.size}")
//    private int batchSize;
//    @Value("${kafka.producer.linger}")
//    private int linger;
//    @Value("${kafka.producer.buffer.memory}")
//    private int bufferMemory;
//
//
//    public Map<String, Object> producerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        props.put(ProducerConfig.RETRIES_CONFIG, retries);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize); //空间上buffer满足>16k即发送消息
//        props.put(ProducerConfig.LINGER_MS_CONFIG, linger); //时间上满足>5ms即发送消息，时间限制和空间限制只要满足其一即可发送消息
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
////        props.put(ProducerConfig.ACKS_CONFIG, "1");   		//all需要leader和所有replica回执确认 0不需要回执确认 1仅需要leader回执确认
//        return props;
//    }
//
//    public ProducerFactory<String, String> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(producerConfigs());
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<String, String>(producerFactory());
//    }
//
//}
