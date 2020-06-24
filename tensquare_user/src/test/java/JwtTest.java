import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class JwtTest {


    /**
     *
     */
    @Test
    public void test1() throws InterruptedException {

        // 获取当前时间
        long nowTime = System.currentTimeMillis();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666") // 设置唯一标识符
                .setIssuedAt(new Date()) // token 签发时间
                .setSubject("张三") // token 面向的用户
                .signWith(SignatureAlgorithm.HS256, "itcast") // 设置签名的算法 盐值

                //自定义信息,自己想怎么写就怎么写
                .claim("roles", "admin")
                // 给令牌设置有效时间 30s
                .setExpiration(new Date(nowTime + 300 * 1000l));

        System.out.println(jwtBuilder.compact());
    }

    @Test
    public void test2() throws InterruptedException {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJpYXQiOjE1OTEzNDMxOTUsInN1YiI6IuW8oOS4iSJ9.iMF3YMN7WFMPTFHCLM0JPR6l85luA2S-Qis5nCD8J5k";

        Claims claims = Jwts.parser()
                .setSigningKey("itcast")
                .parseClaimsJws(token)
                .getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getSubject());

    }
}
