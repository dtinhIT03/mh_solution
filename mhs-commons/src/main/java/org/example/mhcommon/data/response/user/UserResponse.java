package org.example.mhcommon.data.response.user;

import lombok.Data;
import org.example.mhcommon.data.response.BaseResponse;
import org.example.mhcommon.data.response.role.RoleResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserResponse extends BaseResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String facebookLink;
    private String alias;
    private String empCode;
    private String avatar;
    private LocalDateTime regisDate;
    private String password;
    private String role;
    private List<RoleResponse> roles;
}
