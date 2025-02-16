package org.example.mhcommon.data.model.user;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserAuthorDTO {
    private Long userId;
    private List<String> roles;
}