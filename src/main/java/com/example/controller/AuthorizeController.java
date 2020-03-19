package com.example.controller;

import com.example.dto.AccessTokenDTO;
import com.example.dto.GithubUser;
import com.example.mapper.UserMapper;
import com.example.model.User;
import com.example.provider.GithubProvider;
import com.example.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

@Slf4j
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserService userService;

    @Value("${github.client_id}")
    private String client_id;

    @Value("${github.client_secret}")
    private String client_secret;

    @Value("${github.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session, HttpServletResponse response) {
        AccessTokenDTO tokenDTO = new AccessTokenDTO();
        tokenDTO.setClient_id(client_id);
        tokenDTO.setClient_secret(client_secret);
        tokenDTO.setCode(code);
        tokenDTO.setRedirect_uri(redirect_uri);
        tokenDTO.setState(state);
        String accessTokenDTO= githubProvider.getAccessTokenDTO(tokenDTO);
        GithubUser githubUser = githubProvider.getGithubUser(accessTokenDTO);
        if (githubUser !=null){
            User user = new User();
            user.setName(githubUser.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(githubUser.getAvatarUrl());
            user.setAccountId(String.valueOf(githubUser.getId()));
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            session.setAttribute("githubUser" ,githubUser);
        }
        log.error("callback github error,{}",githubUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response){
        session.removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
