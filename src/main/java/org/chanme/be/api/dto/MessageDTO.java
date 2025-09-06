package org.chanme.be.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {
    private String senderName;
    private String receiverName;
    private String message;
}
