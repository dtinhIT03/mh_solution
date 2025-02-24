package org.example.mhapi.service.cms.test;

import org.example.mhapi.service.cms.role.RoleCmsServiceImp;
import org.example.mhcommon.core.json.JsonArray;
import org.example.mhcommon.data.model.user.UserAuthorDTO;
import org.example.mhcommon.data.response.role.RoleResponse;
import org.example.mhsredis.service.imp.BaseRedisServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    BaseRedisServiceImp baseRedisServiceImp;

     public Optional<ArrayList<String>> getListActionCode(){
         UserAuthorDTO userAuthorDTO = (UserAuthorDTO) SecurityContextHolder
                 .getContext().getAuthentication().getPrincipal();
        if(baseRedisServiceImp.hasKey("PermisisonUser_"+userAuthorDTO.getUserId())){

            Object obj = baseRedisServiceImp
                    .hashGet("PermisisonUser_"+userAuthorDTO.getUserId(),"ListPer");
            ArrayList<String> listPer = (ArrayList<String>) obj;
            return Optional.of(listPer);
        }else {
            return Optional.empty();
        }

//        RoleResponse roleResponse =  roleCmsServiceImp.findById(roleId,authentication);
//        return roleResponse.getPermissions().stream().map(per -> per.getActionCode()).collect(Collectors.toSet());
    }

}
