package com.pd.springboot.produer;

import com.pd.springboot.constant.Constants;
import com.pd.springboot.entity.Order;
import com.pd.springboot.mapper.BrokerMessageLogMapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RabbitOrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    //回调函数：confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
            System.out.println("correlationData:" + correlationData);
            String messageId = correlationData.getId();
            if(ack) {
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, Constants.ORDER_SEND_SUCCESS, new Date());
            } else {
                //失败则进行具体的后续操作：重试或者补偿等手段
                System.out.println("异常处理......");
            }
        }
    };

    //发送消息方法调用：构建自定义对象消息
    public void sendOrder(Order order)throws Exception{
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //消息唯一Id
        CorrelationData correlationData = new CorrelationData(order.getMessageId());
        rabbitTemplate.convertAndSend("order-exchange","order.abc",order,correlationData);
    }
}
