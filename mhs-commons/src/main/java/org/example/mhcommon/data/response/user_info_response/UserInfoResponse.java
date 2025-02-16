package org.example.mhcommon.data.response.user_info_response;

import lombok.Data;
import org.example.mhscommons.data.tables.pojos.User;

@Data
public class UserInfoResponse {
    private User user;
    private String token;
}
