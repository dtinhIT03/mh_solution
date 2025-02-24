package org.example.mhapi.service.cms.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.el.parser.Token;
import org.example.mhcommon.data.request.auth.LoginRequest;
import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.TokenResponse;
import org.example.mhcommon.data.response.user.UserResponse;

public interface IAuthService {
    /**
     * method : authenticate
     * params : username,password
     * return : token response
     */
    TokenResponse authenticate(LoginRequest loginRequest);

    /**
     * method: refreshToken
     * params: token
     * return: token response
     */
    TokenResponse refreshToken(String token);

    TokenResponse logoutToken(HttpServletRequest request);
}
