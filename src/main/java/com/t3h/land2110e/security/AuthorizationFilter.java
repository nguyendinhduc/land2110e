package com.t3h.land2110e.security;

import com.t3h.land2110e.entity.UserProfileEntity;
import com.t3h.land2110e.model.response.ResponseException;
import com.t3h.land2110e.model.response.UserContext;
import com.t3h.land2110e.repository.UserProfileRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends AbstractAuthenticationProcessingFilter {
    private UserProfileRepository userProfileRepository;

    public AuthorizationFilter(RequestMatcher matcher, UserProfileRepository userProfileRepository) {
        super(matcher);
        this.userProfileRepository = userProfileRepository;
    }

    //xac thuc, verify: xác định xem api vừa call có được đến controller không
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader("Authorization");
        if (token == null){
            throw new ResponseException("Token invalid");
        }
        token = token.substring("Bearer ".length());
        UserProfileEntity user = parseToken( token);
        return new UsernamePasswordAuthenticationToken(user, null);
    }
    private UserProfileEntity parseToken(String token) throws ResponseException, AuthenticationException{
        //giải mã token để lấy thông tin
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("123a@")
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            UserProfileEntity user = userProfileRepository.findOneByUsername(username);
            if ( user == null ){
                throw new ResponseException("User not exist");
            }
            return user;
        }catch (ExpiredJwtException e){
            throw new AuthenticationException("Expired token", e) {

            };
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken)authResult;
        UserProfileEntity userProfile = (UserProfileEntity)auth.getPrincipal();
        UserContext userContext = new UserContext();
        userContext.setId(userProfile.getId());
        userContext.setEmail(userProfile.getEmail());
        userContext.setUername(userProfile.getUsername());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(new JwtAuthenticationToken(userContext));
        SecurityContextHolder.setContext(context);
        if (!response.isCommitted()) {
            chain.doFilter(request, response);
        }
    }

    //lây id của user hiện vừa call api
    public static int getCurrentUserId(){
        UserContext user = (UserContext)SecurityContextHolder
                .getContext().getAuthentication().getCredentials();
        return user.getId();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
