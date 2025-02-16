package org.example.mhcommon.data.request.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.mhscommons.data.tables.pojos.User;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UserRequest {
    String name;
    String email;
    String password;
    String phone;

}
