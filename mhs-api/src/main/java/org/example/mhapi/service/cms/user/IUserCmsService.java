package org.example.mhapi.service.cms.user;

import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.user.UserResponse;

public interface IUserCmsService {
    //find by id
    //update role
    //changePassword
    /**
     * method: register
     * params : userRequest
     * return userResponse
     */
    UserResponse register(UserRequest request);
}
