package refree.backend.infra.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import refree.backend.infra.exception.MemberException;
import refree.backend.module.member.Member;
import refree.backend.module.member.MemberLoginDto;
import refree.backend.module.member.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

@Getter
@Service
@RequiredArgsConstructor
public class JwtService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Bad Request"));
    }

    public Optional<Member> getOptionalMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public String verifyNewMemberOrNot(Optional<Member> byEmail, MemberLoginDto memberLoginDto) {
        // email 존재 확인
        if (byEmail.isEmpty()) {
            return "존재하지 않는 회원";
        }
        if (!passwordEncoder.matches(memberLoginDto.getPassword(), byEmail.get().getPassword())) {
            return "비밀번호가 올바르지 않습니다";
        }
        return "existing";
    }

    public String createAccessToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("email", email)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public String passwordEncoding(String password) {
        return passwordEncoder.encode(password);
    }

    public void checkTokenValid(String token) {
        JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
    }

    public void checkAccessHeaderValid(HttpServletRequest request) {
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);
        if (accessToken == null || !accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
            throw new IllegalArgumentException("JWT_ACCESS_NOT_VALID");
        }
    }
}
