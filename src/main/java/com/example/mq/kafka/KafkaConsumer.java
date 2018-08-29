//package com.example.mq.kafka;
//
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//
//import java.util.Arrays;
//import java.util.Properties;
//
///**
// * @Auther: gsh
// * @Date: 2018/8/7 11:34
// * @Description: 消费者
// */
//public class KafkaConsumer implements Runnable{
//
//
//    /* 定义consumer */
//    private  org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;
//
//    public KafkaConsumer() {
//    }
//
//    public org.apache.kafka.clients.consumer.KafkaConsumer<String, String> getConsumer() {
//        Properties destProp = new Properties();
//        destProp.put("bootstrap.servers", "39.106.28.244:9092");    /* 定义kakfa 服务的地址，不需要将所有broker指定上 */
//        destProp.put("group.id", "xiaoguozi-test"); /* 制定consumer group */
//        destProp.put("enable.auto.commit", "true"); /* 是否自动确认offset */
//        destProp.put("auto.commit.interval.ms","1000"); //自动确认offset的时间间隔
//        destProp.put("session.timeout.ms","30000");
////        destProp.put("auto.offset.reset", autoOffsetReset);
////        destProp.put("max.partition.fetch.bytes", maxFetchBytes);
////        destProp.put("max.poll.records",maxPollRecords);
//        destProp.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        destProp.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(destProp);
//        consumer.subscribe(Arrays.asList("test01","test02"));
////    	System.err.println("semtopic="+semTopic+"coreTopic="+coreTopic+"geteway="+gatewayBhbkTopic+"corebhbk="+coreBhbkTopic);
//        return consumer;
//    }
//
//    @Override
//    public void run() {
//        consumer = getConsumer();
//        while (true) {
//            ConsumerRecords<String,String> records = consumer.poll(100);
//            for (ConsumerRecord<String,String> record: records ) {
//                System.out.printf("接收数据的 offset = %d, key = %s, value = %s",record.offset(),record.key(),record.value());
//            }
//        }
//    }
//
//
//}
