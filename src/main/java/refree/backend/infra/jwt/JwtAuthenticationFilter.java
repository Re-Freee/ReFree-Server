package refree.backend.infra.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import refree.backend.infra.principal.PrincipalDetails;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.member.Dto.MemberLoginDto;
import refree.backend.module.member.Member;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        MemberLoginDto memberLoginDto = new MemberLoginDto();
        try {
            memberLoginDto = objectMapper.readValue(request.getInputStream(), MemberLoginDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Optional<Member> byEmail = jwtService.getOptionalMemberByEmail(memberLoginDto.getEmail());
        String result = jwtService.verifyNewMemberOrNot(byEmail, memberLoginDto);
        if (result.equals("존재하지 않는 회원입니다.") || result.equals("비밀번호가 올바르지 않습니다.")) {
            try {
                setBodyResponse(response, 400, result);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                new PrincipalDetails(byEmail.get()),
                jwtService.passwordEncoding(memberLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(
                authenticationToken
        );

        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        Member member = principalDetails.getMember();

        // accessToken + refreshToken 생성하여 헤더에 추가
        String accessToken = jwtService.createAccessToken(member.getEmail());
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + accessToken);
        // 기존회원이 로그인 시도 하는 경우
        setBodyResponse(response, 200, "LOGIN_COMPLETE");
    }

    private void setBodyResponse(HttpServletResponse response, int code, String message)
            throws IOException {
        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new SingleResponse(code, message)));
    }
}
