package org.example.mhcommon.data.response.permission;

import lombok.Data;
import org.example.mhcommon.data.response.BaseResponse;

import java.time.LocalDateTime;

@Data
public class PermissionResponse extends BaseResponse {
    private Long id;
    private String actionName;
    private String actionCode;
    private Boolean checkAction;
    private LocalDateTime deletedAt;
    private String description;
}
