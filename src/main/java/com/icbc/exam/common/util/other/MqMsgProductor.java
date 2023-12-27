package com.icbc.exam.common.util.other;

import com.icbc.exam.common.config.MqProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


@Component
@Data
@Slf4j
public class MqMsgProductor {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MqProperties mqProperties;

    /**
     * 发送消息
     * @param quene 接收人用户名
     * @param content 文本
     **/
    public void send(final String quene, final String content) {
        try {

            jmsTemplate.send(mqProperties.getQuene().getCollectionAlert() + quene
                    , new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            TextMessage textMessage = session.createTextMessage(content);
                            return textMessage;
                        }
                    });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<String> getSurplus(final String name) {
        List<String> contList = jmsTemplate.browse(name, new BrowserCallback<List<String>>() {
            public List<String> doInJms(Session session, QueueBrowser browser) {
                List<String> messContentList = new ArrayList<>();
                try {
                    MessageConverter messageConverter = new SimpleMessageConverter();

                    Enumeration<Message> messageEnum = browser.getEnumeration();
                    while (messageEnum.hasMoreElements()) {
                        Message msg = messageEnum.nextElement();
                        String text = (String) messageConverter.fromMessage(msg);
                        messContentList.add(text);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                return messContentList;
            }
        });
        return contList;
    }

}