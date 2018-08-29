//package com.example.mq.kafka;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//
//import java.util.Properties;
//
///**
// * @Auther: gsh
// * @Date: 2018/8/7 10:54
// * @Description: 入门实例 - 生产者 producer
// *
// */
//public class UserKafkaProducer extends Thread {
//
//    private final KafkaProducer<Integer,String> producer;
//    private final String topic;
//    private final Properties props = new Properties();
//
//
//    public UserKafkaProducer(String topic) {
////        props.put("metadata.broker.list","39.106.28.244:9092");
//        props.put("bootstrap.servers","39.106.28.244:9092");
//        props.put("retries",0);
//        props.put("batch.size",16384);
//        props.put("linger.ms",1);
//        props.put("buffer.memory",33554432);
//        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
//        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
//
////        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "39.106.28.244:9092");
////        props.put(ProducerConfig.ACKS_CONFIG, "1");   		//all需要leader和所有replica回执确认 0不需要回执确认 1仅需要leader回执确认
////        props.put(ProducerConfig.RETRIES_CONFIG, 1);  		//重试一次
////        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); 	//空间上buffer满足>16k即发送消息
////        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);      	//时间上满足>5ms即发送消息，时间限制和空间限制只要满足其一即可发送消息
////        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 1073741824); //1G
////        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
////        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//
//        producer = new KafkaProducer<Integer, String>(props);
//        this.topic = topic;
//    }
//
//    @Override
//    public void run(){
//        int messageNo = 1;
//        while (true) {
//            String messageStr = new String("Message_"+messageNo);
//            System.out.println("Send:" + messageStr);
//            //返回的是Future<RecordMetadata>,异步发送
//            producer.send(new ProducerRecord<Integer, String>(topic,messageStr));
//            messageNo++;
//            try {
//                sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
