package javaframework.order_food_manage.jwt;

import javaframework.order_food_manage.service.impl.UserDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Filter OncePerRequestFilter là một filter được kích hoạt cho mỗi một lần request đến
//JwtAuthenticationFilter Có nhiệm vụ kiểm tra request của người dùng trước khi nó tới đích.
// Nó sẽ lấy Header Authorization ra và kiểm tra xem chuỗi JWT người dùng gửi lên có hợp lệ không.

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        // Lấy jwt từ request
        String token = getJwtFromRequest(request);
        // Lấy username từ chuỗi jwt
        if( StringUtils.hasText(token) ) username = jwtUtils.getUsernameFromToken(token);
        if ( username != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
            // Lấy thông tin người dùng từ id
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if( jwtUtils.validateToken(token,userDetails)) {
                //Create auth object (contains credentials) which will be used by auth manager
                UsernamePasswordAuthenticationToken
                        auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Nếu người dùng hợp lệ, set thông tin cho Seturity Context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        //Lấy chuỗi header có key Authorization
        String headerAuth = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if ( StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
