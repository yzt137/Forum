package com.example.controller;

import com.example.dto.AccessTokenDTO;
import com.example.dto.GithubUser;
import com.example.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client_id}")
    private String client_id;

    @Value("${github.client_secret}")
    private String client_secret;

    @Value("${github.redirect_uri}")
    private String redirect_uri;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
        AccessTokenDTO tokenDTO = new AccessTokenDTO();
        tokenDTO.setClient_id(client_id);
        tokenDTO.setClient_secret(client_secret);
        tokenDTO.setCode(code);
        tokenDTO.setRedirect_uri(redirect_uri);
        tokenDTO.setState(state);
        String accessTokenDTO= githubProvider.getAccessTokenDTO(tokenDTO);
        GithubUser user = githubProvider.getGithubUser(accessTokenDTO);
        if (user!=null){
            session.setAttribute("user",user);
        }
        return "redirect:/";
    }
}
