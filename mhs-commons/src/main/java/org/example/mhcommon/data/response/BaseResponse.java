package org.example.mhcommon.data.response;

import lombok.Data;

import java.util.Set;

@Data
public abstract class BaseResponse {
    private Set<String> itemPermissions;
}
