package org.example.mhcommon.data.request.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class PermissionRequest {
    private Long id;
    private String actionName;
    private String actionCode;
    private Boolean checkAction;
    private LocalDateTime deletedAt;
    private String description;

}
