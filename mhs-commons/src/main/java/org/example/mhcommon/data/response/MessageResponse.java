package org.example.mhcommon.data.response;

import lombok.Data;

@Data
public class MessageResponse extends BaseResponse {
    private String content;

    public MessageResponse(String content) {
        this.content = content;
    }
}
