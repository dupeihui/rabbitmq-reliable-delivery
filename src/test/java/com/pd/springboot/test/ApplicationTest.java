package com.pd.springboot.test;

import com.pd.springboot.entity.Order;
import com.pd.springboot.produer.RabbitOrderSender;
import com.pd.springboot.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RabbitOrderSender rabbitOrderSender;

    @Autowired
    private OrderService orderService;

    @Test
    public void testSender1() throws Exception{
        Order order = new Order();
        order.setId("2021021300000001");
        order.setName("测试订单");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        rabbitOrderSender.sendOrder(order);
    }

    @Test
    public void testCreateOrder1() throws Exception {
        Order order = new Order();
        order.setId("2021021300000007");
        order.setName("测试订单");
        order.setMessageId(System.currentTimeMillis() + "$" + UUID.randomUUID().toString());
        orderService.createOrder(order);
    }

}
