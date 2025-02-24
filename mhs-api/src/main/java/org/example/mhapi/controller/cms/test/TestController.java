package org.example.mhapi.controller.cms.test;

import org.example.mhapi.service.cms.role.RoleCmsServiceImp;
import org.example.mhapi.service.cms.test.TestService;
import org.example.mhcommon.data.response.role.RoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    TestService testService;
//    @GetMapping("/{id}")
//    public Set<String> getListActionCode(@PathVariable Long id, Authentication authentication){
//        return testService.getListActionCode(id,authentication);
//    }
}
