package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 接收web请求，发送消息到kafka
 * @author  gsh
 * @date  2019/1/5 下午4:13
 **/
@RestController
@RequestMapping("/kafka")
@Slf4j
public class CollectController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String sendKafka(HttpServletRequest request, HttpServletResponse response) {
        try {
            String message = request.getParameter("message");
            log.info("kafka的消息={}",message);
            kafkaTemplate.send("test", message);
            log.info("发送kafka成功.");
            return "发送kafka成功";
        } catch (Exception e) {
            log.error("发送kafka失败", e);
            return "发送kafka失败";
        }
    }

    @RequestMapping(value = "/sendToFlinkTopic/{name}/{message}", method = RequestMethod.GET)
    public @ResponseBody String send(@PathVariable("name") final String name, @PathVariable("message") final String message) {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = simpleDateFormat.format(new Date());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("message", message);
        jsonObject.put("time", timeStr);
        jsonObject.put("timeLong", System.currentTimeMillis());
        jsonObject.put("bizID", UUID.randomUUID());

        String sendMessage = jsonObject.toJSONString();

        ListenableFuture future = kafkaTemplate.send("test01", sendMessage);
        future.addCallback(o -> System.out.println("send message success : " + sendMessage),
                throwable -> System.out.println("send message fail : " + sendMessage));

        return "send message to [" +  name + "] success (" + timeStr + ")";
    }
}
