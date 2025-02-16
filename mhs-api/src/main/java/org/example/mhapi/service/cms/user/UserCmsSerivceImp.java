package org.example.mhapi.service.cms.user;

import org.example.mhapi.service.cms.AbsCmsService;
import org.example.mhcommon.data.mappers.user.UserMapper;
import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhscommons.data.tables.pojos.User;
import org.example.mhsconfig.config.exception.ApiException;
import org.example.mhsrepository.repository.user.UserRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.example.mhcommon.data.constant.message.MessageResponse.REGISTER_DUPLICATE_EMAIL;
import static org.example.mhcommon.data.constant.message.MessageResponse.SERVER_ERROR;

@Service
public class UserCmsSerivceImp extends AbsCmsService<UserRequest,UserResponse,User,Long,UserRepositoryImp, UserMapper>
        implements IUserCmsService {
    PasswordEncoder passwordEncoder;
    UserRepositoryImp userRepositoryImp;

    @Autowired
    public UserCmsSerivceImp(PasswordEncoder passwordEncoder, UserRepositoryImp userRepositoryImp,UserMapper userMapper) {
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryImp = userRepositoryImp;
        this.mapper = userMapper;
    }

    @Override
    public UserResponse register(UserRequest request) {
        //check mail existed ? , validate  password ? , encode password.
        Optional<User> user = userRepositoryImp.findUserByEmail(request.getEmail());
        if(user.isPresent()) throw new ApiException(REGISTER_DUPLICATE_EMAIL,401);
        String password = passwordEncoder.encode(request.getPassword());

        User user1 = new User().setEmail(request.getEmail())
                .setPassword(password)
                .setName(request.getName())
                .setPhone(request.getPhone());
        User user2 =  userRepositoryImp.insertReturning(user1)
                .orElseThrow(() -> new ApiException(SERVER_ERROR,400));
        UserResponse userResponse= mapper.toResponse(user2);
        return userResponse;
    }

    @Override
    protected String getPermissionCode() {
        return "user";
    }
}
