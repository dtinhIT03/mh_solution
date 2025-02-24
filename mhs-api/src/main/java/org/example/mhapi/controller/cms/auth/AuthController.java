package org.example.mhapi.controller.cms.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.example.mhapi.service.cms.auth.AuthServiceImp;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.request.auth.LoginRequest;
import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.TokenResponse;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhcommon.data.response.user_info_response.UserInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;

@RestController
public class AuthController {
    @Autowired
    private AuthServiceImp authServiceImp;
    //authenticate
    @PostMapping("/login")
    public DfResponse<TokenResponse> authenticate(@RequestBody LoginRequest loginRequest){
        TokenResponse tokenResponse = authServiceImp.authenticate(loginRequest);
        return DfResponse.ok(tokenResponse);
    }
    //logout
    @GetMapping("cms/auth/logout")
    public DfResponse<TokenResponse> logout(HttpServletRequest request){
        TokenResponse tokenResponse = authServiceImp.logoutToken(request);
        return DfResponse.ok(tokenResponse);

    }
    //getInfo of me
    @GetMapping("/user/me")
    public ResponseEntity<DfResponse<UserInfoResponse>> getInfo(){
        UserInfoResponse userInfoResponse = authServiceImp.getInfo();

        return DfResponse
                .okEntity(userInfoResponse);
    }


}
