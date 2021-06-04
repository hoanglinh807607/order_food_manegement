package javaframework.order_food_manage.config;

import javaframework.order_food_manage.jwt.AuthEntryPointJwt;
import javaframework.order_food_manage.jwt.JwtAuthenticationFilter;
import javaframework.order_food_manage.service.impl.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration //Báo cho container spring boot biết đây là 1 class config. Config sẽ chạy trước khi chương trình chạy
@EnableWebSecurity  //Báo spring boot sử dụng class này để config cho security thay vì mặt định
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    //1 class @Component is will be create object and auto Inject into IOC
    //1 @Bean is use at method level. this passwordEncoder method will auto inject and create BCryptPasswordEncoder Inject into IOC
    //Ở đây nó yêu cầu phải có 1 mã hóa password Để khi người dùng nhập password nó sẽ tự động mã hóa và so sánh vào trong csdl
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();     //Bỏ bảo mật csrf
        http
                .authorizeRequests()
                    .antMatchers("/*").permitAll()
                    .antMatchers("/").hasAnyAuthority("user","admin","adminall")  //Bất kỳ request nào cũng cho phép vào để không bị kẹt ở trang login nữa
                    .antMatchers("/admin/**").hasAnyAuthority("admin","adminall") //Authorization vào /admin phải có quyền hasAnyCode. Phần này là code trong role được xác nhận bên UserDetailService
                    .antMatchers("/cart/pay").authenticated()
                    .and()
//                .exceptionHandling()
//                    .authenticationEntryPoint( (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Error: Unauthorized"))
//                    .and()
//                .sessionManagement()
//                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
                .formLogin()
                    .loginProcessingUrl("/j_spring_security_check")
                    .loginPage("/login") //Chỉ định đường dẫn form login
                    .successHandler(customSuccessHandler)     //Đường dẫn sau khi đăng nhập thành công sẽ đi đến
                    .failureUrl("/login?alert=danger&message=User or Password incorrect")  //Chuyển đến URL này khi sai thông tin username and password
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")
                    .and()
                    .exceptionHandling().accessDeniedPage("/login?alert=danger&message=not authorized")    // Khi người dùng đã login, với vai trò XX.
                    .and()                                                    // Nhưng truy cập vào trang yêu cầu vai trò YY
                    .rememberMe().key("hoanglinhabc").tokenValiditySeconds(3*24*60*60);                                                    // Ngoại lệ AccessDeniedException sẽ ném ra.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //Bỏ bảo mật csrf
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("*"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
