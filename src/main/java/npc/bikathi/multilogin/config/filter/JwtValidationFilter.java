package npc.bikathi.multilogin.config.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import npc.bikathi.multilogin.type.InternalEntityAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtValidationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(jwt != null) {
            try {
                String secret = "uIka39tkKkR9vSuWVKnQxnjkvZGlbGHmilYMki14X6ZC5nQaNsivVVXvGLfb";
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();;
                String username = (String) claims.get("username");
                String authorities = (String) claims.get("authorities");

                InternalEntityAuthenticationToken authentication =
                    new InternalEntityAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch(Exception ex) {
                throw new BadCredentialsException("Invalid token received!");
            }
        }
        filterChain.doFilter(request, response);
    }
}
