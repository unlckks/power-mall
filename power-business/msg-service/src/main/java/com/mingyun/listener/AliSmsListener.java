package com.mingyun.listener;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.mingyun.constant.MqConstant;
import com.mingyun.model.AliSmsModel;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: MingYun
 * @Date: 2023-04-11 20:05
 */
@Component
@RocketMQMessageListener(topic = MqConstant.ALI_SMS_TOPIC,consumerGroup = MqConstant.ALI_SMS_CONSUMER_GROUP,namespace = MqConstant.MQ_NAME_SPACE)
public class AliSmsListener implements RocketMQListener<MessageExt> {

    @Autowired
    private Client client ;
    /**
     * 生产者传递手机号、全名等
     * 消费者传递接收到的消息
     * @param
     */
    @Override
    public void onMessage(MessageExt message) {
        AliSmsModel aliSmsModel = JSON.parseObject(new String(message.getBody()), AliSmsModel.class);
        SendSmsRequest smsRequest = new SendSmsRequest();
        smsRequest.setPhoneNumbers(aliSmsModel.getPhoneNumber());
        smsRequest.setSignName(aliSmsModel.getSignName());
        smsRequest.setTemplateCode(aliSmsModel.getTemplateCode());
        smsRequest.setTemplateParam(aliSmsModel.getTemplateParam());
        SendSmsResponse sendSmsResponse =  null;
        try {
            sendSmsResponse = client.sendSms(smsRequest) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //todo 保存
        System.out.println(sendSmsResponse.getBody());
    }
}
