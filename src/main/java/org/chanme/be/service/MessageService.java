package org.chanme.be.service;

import lombok.RequiredArgsConstructor;
import org.chanme.be.api.dto.MessageDTO;
import org.chanme.be.api.dto.QuestionDTO;
import org.chanme.be.domain.contact.ContactRepository;
import org.chanme.be.domain.message.InitialMessageRepository;
import org.chanme.be.domain.message.Message;
import org.chanme.be.domain.message.MessageRepository;
import org.chanme.be.domain.question.Question;
import org.chanme.be.domain.question.QuestionRepository;
import org.chanme.be.domain.user.User;
import org.chanme.be.domain.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final InitialMessageRepository initialMessageRepository;

    public Message DtoToMessage(MessageDTO messageDTO) {
        User send = userRepository.findByMemberCode(messageDTO.getSenderName()).orElse(null);
        Message message = new Message().builder()
                .sender(send)
                .content(messageDTO.getMessage())
                .build();
        return message;
    }

    public MessageDTO MessageToDto(Message message) {
        MessageDTO messageDTO = new MessageDTO().builder()
                .message(message.getContent())
                .receiverName(message.getRecipient().get(0).getNickname())
                .senderName(message.getSender().getName())
                .build();
        return messageDTO;
    }

    public MessageDTO messageReceive(Long questionId) {
        Question question = questionRepository.findById(questionId).orElse(null);
        MessageDTO messageDTO = new MessageDTO().builder()
                .message(initialMessageRepository.findByQuestion(question).getContent())
                .receiverName(initialMessageRepository.findByQuestion(question).getRecipient().getName())
                .senderName(initialMessageRepository.findByQuestion(question).getSender().getNickname())
                .build();
        return messageDTO;
    }

    public MessageDTO messageSend(MessageDTO messageDTO) {
        return MessageToDto(messageRepository.save(DtoToMessage(messageDTO)));
    }
}
