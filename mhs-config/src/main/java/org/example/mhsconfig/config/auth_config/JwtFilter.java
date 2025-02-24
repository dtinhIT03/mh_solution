package org.example.mhsconfig.config.auth_config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.mhcommon.core.json.JsonObject;
import org.example.mhcommon.data.model.user.UserAuthorDTO;
import org.example.mhscommons.data.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;


@Configuration
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private JwtUtil jwtUtill;
    @Autowired
    public JwtFilter(JwtUtil jwtUtil){
        this.jwtUtill = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //check header has authorization ?
//        noopAuthenticationManager();
        if(!hasAuthorizationBearer(request)){
            filterChain.doFilter(request,response);
            return;
        }
        String token = getAccessToken(request);
        log.error("log token {}",token);
        //validateToken
        if(!jwtUtill.validateAccessToken(token)){
            filterChain.doFilter(request,response);
            return;
        }
        //set authentication context
        setAuthenticationContext(token,request);
        log.info("Authentication for user: {}", SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request,response);
    }
    private boolean hasAuthorizationBearer(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(isEmpty(header) || !header.startsWith("Bearer ")){
            return false;
        }
        return true;
    }
    private String getAccessToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null){
            String token = header.split(" ")[1].trim();
            return token;
        }
        return "err";

    }
    private void setAuthenticationContext(String token,HttpServletRequest request){
        UserAuthorDTO userDetails = getUserDetails(token);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,getAuthorities(userDetails));
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    private UserAuthorDTO getUserDetails(String token){
        String subject = jwtUtill.getSubject(token);
        if(subject == null) return null;
        return new JsonObject(subject)
                .mapTo(UserAuthorDTO.class);
    }
    private List<SimpleGrantedAuthority> getAuthorities(UserAuthorDTO userInfo){
        return Optional.ofNullable(userInfo.getRoles())
                .orElse(Collections.emptyList())
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.toUpperCase()))
                .collect(Collectors.toList());
    }

    private AuthenticationManager noopAuthenticationManager(){
        return authentication -> {
            throw new AuthenticationServiceException("authentication default is disable");
        };
    }
}
