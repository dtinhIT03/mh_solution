package org.example.mhapi.controller.cms.user;

import org.example.mhapi.service.cms.user.UserCmsSerivceImp;
import org.example.mhcommon.data.model.DfResponse;
import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserCmsSerivceImp userCmsSerivceImp;
    @PostMapping("/register")
    public DfResponse<UserResponse> register(@RequestBody UserRequest request){
        UserResponse response = userCmsSerivceImp.register(request);
        return DfResponse.ok(response);
    }
}
