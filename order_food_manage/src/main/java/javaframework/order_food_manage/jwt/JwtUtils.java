package javaframework.order_food_manage.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Sau khi có các thông tin về người dùng, chúng ta cần mã hóa thông tin người dùng thành chuỗi JWT.
// Tôi sẽ tạo ra một class JwtTokenProvider để làm nhiệm vụ này.

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private final String JWT_SECRET = "foodApp";

    //Thời gian có hiệu lực của chuỗi jwt
    private final long JWT_EXPIRATION = 604800000L;

//    // Tạo ra jwt từ thông tin user
//    public String generateJwtToken(Authentication authentication){
//        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
//        Map<String,Object> claims = new HashMap<>();
//        // Tạo chuỗi json web token từ id của user.
//        return Jwts.builder().setClaims(claims)
//                    .setSubject(userPrincipal.getUsername())
//                    .setIssuedAt(new Date(System.currentTimeMillis()))           // Ngày khởi tạo token
//                    .setExpiration(new Date(System.currentTimeMillis()+JWT_EXPIRATION))  // Hạn sử dụng token
//                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // Sử dụng mã hóa gì
//                    .compact();
//    }
//
//    // Lấy thông tin user từ jwt
//    public String getUserNameFromJWT(String token) {
//        return Jwts.parser()
//                .setSigningKey(JWT_SECRET)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
//            return true;
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty.");
//        }
//        return false;
//    }

    // retrieve user from jwt token
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieving any information from token we will need the secret
    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //Generate token for user
    public String generateToken(UserDetails userDetails){
        Map<String,Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    //while creating the token
    //1. Define claims of the token, like issuer, Expiration, Subject, and the ID
    //2. Sign the  JWT using the HS512 al gorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject){
        return Jwts.builder().setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))           // Ngày khởi tạo token
                    .setExpiration(new Date(System.currentTimeMillis()+JWT_EXPIRATION))  // Hạn sử dụng token
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // Sử dụng mã hóa gì
                    .compact();
    }

    //Check username in token truyền về có phải là username được tạo ra từ đăng nhập trước đó không
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
