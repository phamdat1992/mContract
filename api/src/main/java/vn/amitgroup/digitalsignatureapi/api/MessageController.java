package vn.amitgroup.digitalsignatureapi.api;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.amitgroup.digitalsignatureapi.dto.response.Notify;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    @MessageMapping("/notify.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public Notify sendMessage(@Payload Notify notify) {
        return notify;
    }

    @MessageMapping("/notify.addUser")
    @SendTo("/topic/publicNotifyRoom")
    public Notify addUser(@Payload Notify notify, SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", notify.getSender());
        return notify;
    }
}
