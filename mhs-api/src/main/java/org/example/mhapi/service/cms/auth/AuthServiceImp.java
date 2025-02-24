package org.example.mhapi.service.cms.auth;

import static org.example.mhcommon.data.constant.message.MessageResponse.*;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.mhapi.service.cms.AbsCmsService;
import org.example.mhapi.service.cms.role.RoleCmsServiceImp;
import org.example.mhcommon.data.model.user.UserAuthorDTO;
import org.example.mhcommon.data.request.auth.LoginRequest;
import org.example.mhcommon.data.request.user.UserRequest;
import org.example.mhcommon.data.response.TokenResponse;
import org.example.mhcommon.data.response.user.UserResponse;
import org.example.mhcommon.data.response.user_info_response.UserInfoResponse;
import org.example.mhscommons.data.tables.pojos.Role;
import org.example.mhscommons.data.tables.pojos.User;
import org.example.mhsconfig.config.auth_config.JwtUtil;
import org.example.mhsconfig.config.exception.ApiException;
import org.example.mhsredis.service.BaseRedisService;
import org.example.mhsrepository.repository.user.UserRepositoryImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthServiceImp extends AbsCmsService implements IAuthService {
    private UserRepositoryImp userRepositoryImpl;
    private PasswordEncoder passwordEncoder;
    private RoleCmsServiceImp roleCmsServiceImp;
    private BaseRedisService baseRedisService;
    private JwtUtil jwtUtil;


    @Autowired
    public AuthServiceImp(UserRepositoryImp userRepositoryImpl, PasswordEncoder passwordEncoder, JwtUtil jwtUtil
    ,RoleCmsServiceImp roleCmsServiceImp,BaseRedisService baseRedisService) {
        this.userRepositoryImpl = userRepositoryImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.baseRedisService=baseRedisService;
        this.roleCmsServiceImp=roleCmsServiceImp;
    }

    @Override
    public TokenResponse authenticate(LoginRequest loginRequest) {
        try{
        //check existed user, check password, get list role and generate token
        User user =  userRepositoryImpl.findUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ApiException(USERNAME_NOT_FOUND,401)) ;

        Boolean checkPassword = passwordEncoder.matches(loginRequest.getPassword(),user.getPassword());
        if(!checkPassword) throw new ApiException(INVALID_AUTHENTICATION_INFO,401);
        List<Role> roles = userRepositoryImpl.getRoles(user.getId());
        List<String> roleName = roles.stream().map(role -> role.getRoleName()).collect(Collectors.toList());
        Set<String> listActionCode =  roleCmsServiceImp.getListActionCode(Long.valueOf(roles.get(0).getId()));
        baseRedisService.hashSet("PermisisonUser_"+user.getId(),"ListPer",listActionCode);
        String token =  jwtUtil.generateToken(user,roleName);
        return new TokenResponse(token);
        } catch (ApiException exception) {
            log.error("[ERROR] authenticate {} " + exception.getMessage());
            throw new ApiException(exception.getMessage(), exception.getCode());
        }
    }



    @Override
    public TokenResponse refreshToken(String token) {
        String newToken = jwtUtil.refreshToken(token);
        return new TokenResponse(newToken);
    }

    @Override
    public TokenResponse logoutToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        TokenResponse tokenResponse = new TokenResponse(token);
        UserAuthorDTO userAuthorDTO = (UserAuthorDTO) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        baseRedisService.delete("PermisisonUser_"+userAuthorDTO.getUserId(),"ListPer");
        return tokenResponse;
    }

    //getInfo
    public UserInfoResponse getInfo(){
        UserAuthorDTO userAuthorDTO = (UserAuthorDTO) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        Optional<User> userOptional = userRepositoryImpl.findById(userAuthorDTO.getUserId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            user.setPassword("");

            return toUserUserInfoResponse(user);
        } else {
            throw new UsernameNotFoundException("User not found with ID: " + userAuthorDTO.getUserId());
        }
    }
    private UserInfoResponse toUserUserInfoResponse(User user) {
        UserInfoResponse response = new UserInfoResponse();

        response.setUser(user);

        return response;
    }

    @Override
    protected String getPermissionCode() {
        return null;
    }
}
