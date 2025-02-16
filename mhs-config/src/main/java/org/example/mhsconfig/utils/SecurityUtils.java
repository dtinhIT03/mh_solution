package org.example.mhsconfig.utils;

import org.example.mhcommon.data.model.user.UserAuthorDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static UserAuthorDTO extractUser(Authentication authentication){
//        UserAuthorDTO userAuthorDTO = (UserAuthorDTO) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal(); // instance of userDetails or customDTO
        UserAuthorDTO userAuthorDTO = (UserAuthorDTO) authentication.getPrincipal();
        return userAuthorDTO;
    }
}

