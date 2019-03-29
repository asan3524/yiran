package com.yiran.base.oauth2.service;

import com.google.gson.Gson;
import com.yiran.base.system.client.user.UserFuture;
import com.yiran.base.system.object.UserQo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class LoginUserDetailsService implements UserDetailsService {

    @Autowired
    private UserFuture userFuture;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String json = userFuture.findByName(userName).join();
        UserQo userQo = new Gson().fromJson(json, UserQo.class);
        if (userQo == null) {
            throw new UsernameNotFoundException("UserName " + userName + " not found");
        }
        return new SecurityUser(userQo);
    }
}
