package refree.backend.infra.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import refree.backend.infra.principal.PrincipalDetails;
import refree.backend.module.member.Member;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            jwtService.checkAccessHeaderValid(request);
            String accessToken = request.getHeader(JwtProperties.HEADER_STRING)
                    .replace(JwtProperties.TOKEN_PREFIX, "");

            try {
                jwtService.checkTokenValid(accessToken);
            } catch (TokenExpiredException e) {
                request.setAttribute(JwtProperties.EXCEPTION, "ACCESS_TOKEN_EXPIRED");
            }

            String email = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build()
                    .verify(accessToken).getClaim("email").asString();
            Member memberByEmail = jwtService.getMemberByEmail(email);

            PrincipalDetails principalDetails = new PrincipalDetails(memberByEmail);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    principalDetails,
                    null,
                    principalDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenExpiredException e) {
            request.setAttribute(JwtProperties.EXCEPTION, "JWT_NOT_VALID");
        } catch (IllegalArgumentException e) {
            request.setAttribute(JwtProperties.EXCEPTION, "JWT_ACCESS_NOT_VALID");
        } catch (Exception e) {
            request.setAttribute(JwtProperties.EXCEPTION, "RUNTIME_ERROR");
        }
        chain.doFilter(request, response);
    }
}
