package com.jerry.service;

import com.jerry.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author Cjl
 * @date 2021/7/23 14:05
 */
@Component
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<? extends GrantedAuthority> authorities=null;

        com.jerry.entity.User user1 = userDao.findByUsername(username);
        if (user1 != null) {
            if (user1.getIsAdmin()==1){
                authorities=AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_admin");
            }else{
                authorities=AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_user");
            }
            User user = new User(username,passwordEncoder.encode(user1.getAccountPassword()), authorities);
            return user;
        }
        return null;
    }
}
