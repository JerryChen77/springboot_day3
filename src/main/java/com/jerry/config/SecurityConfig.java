package com.jerry.config;

import com.jerry.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * @author Cjl
 * @date 2021/7/22 20:24
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//        //相当于登陆时用BCrypt加密方式对用户密码进行处理
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("qq")
//        //.password(new BCryptPasswordEncoder().encode("123456"))" ，这相当于对内存中的密码进行Bcrypt编码加密。比对时一致，说明密码正确，允许登陆。
//                .password(new BCryptPasswordEncoder().encode("123123")).roles("ADMIN");
//    }

    @Autowired
    MyUserDetailService myUserDetailService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()        // 表单登录
                .loginPage("/login.html")           // 登录页面
                .loginProcessingUrl("/user/login")  // 验证登录的Handler
                .defaultSuccessUrl("/user/users")   // 登录成功的默认页面
                .permitAll()                        // 放行
             .and()
                // 设置不拦截的url
                .authorizeRequests()
                .antMatchers("/user/users")   // 不验证的url
                .permitAll()            // 放行
             .and()
                .authorizeRequests()
                .antMatchers("/user/user","/user/user/{cardId}","/user/deleteUserByCardId/{cardId}","/user/newUser","/newUser.html")
                .hasRole("admin")
             .and()
                // 设置拦截的url
                .authorizeRequests()
                .anyRequest()
                .authenticated()       // 验证

             .and()
                .csrf().disable();    // 禁用跨域伪造请求
//        注销配置
        http
                .logout()                       	// 退出登录设置
                .logoutUrl("/user/logout")      	// 退出的url
                .logoutSuccessUrl("/login.html")    // 退出之后转到的页面
                .deleteCookies("JSESSIONID")        // 清除cookie
                .invalidateHttpSession(true);       // 干掉session
    }


}
