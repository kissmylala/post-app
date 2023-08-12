package kz.adem.likeservice.kafka;

import kz.adem.likeservice.entity.PostLike;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class PostLikeEventProducer {
   @Autowired
   private NewTopic newTopic;
   @Autowired
   private KafkaTemplate<String, PostLikeMessage> kafkaTemplate;

   public void sendLikeEvent(PostLikeMessage postLikeMessage) {
       log.info("Sending like event to kafka topic: {} with message: {}", newTopic.name(), postLikeMessage);
      Message<PostLikeMessage> message = MessageBuilder
              .withPayload(postLikeMessage)
              .setHeader(KafkaHeaders.TOPIC,newTopic.name())
              .build();
      kafkaTemplate.send(message);
   }


}

