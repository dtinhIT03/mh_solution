package org.example.mhcommon.data.response.permission;

import java.time.LocalDateTime;

public class PermissionResponse {
    private Long id;
    private String actionName;
    private String actionCode;
    private Boolean checkAction;
    private LocalDateTime deletedAt;
    private String description;
}
