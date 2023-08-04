package refree.backend.module.member;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import refree.backend.infra.exception.MemberException;
import refree.backend.infra.response.SingleResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional
    public SingleResponse signup(MemberSignupDto memberSignupDto) {

        // 이미 존재하는 이메일
        if (memberRepository.existsByEmail(memberSignupDto.getEmail())){
            throw new MemberException("이미 존재하는 계정입니다.");
        }

        // 비밀번호 일치하지 않음
        if (!memberSignupDto.getPassword().equals(memberSignupDto.getCheckPassword())) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        Member member = memberSignupDto.toEntity();
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);

        return new SingleResponse(201, "REGISTRATION_COMPLETE");
    }

    @Transactional
    public SingleResponse search(MemberPwSearchDto memberPwSearchDto) {
        Member member = memberRepository.findByEmail(memberPwSearchDto.getEmail())
                .orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));

        member.updateFlag(1); // 비밀번호를 바꾸기 위한 flag 설정

        return new SingleResponse(200, "EMAIL_EXIST");
    }

    @Transactional
    public SingleResponse modify(MemberPwModifyDto memberPwModifyDto) {
        Member member = memberRepository.findByEmail(memberPwModifyDto.getEmail())
                .orElseThrow(() -> new MemberException("존재하지 않는 계정입니다."));

        if (member.getIsChange() == 0){ // Exception (이미 존재하는 것이 확인된 계정에서만 호출되는 API
            throw new MemberException("잘못된 접근입니다.");
        }

        if (!memberPwModifyDto.getNewPassword().equals(memberPwModifyDto.getCheckNewPassword())) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경 + 계정찾기 flag 0으로 설정
        member.updatePassword(passwordEncoder, memberPwModifyDto.getNewPassword());
        member.updateFlag(0);

        return new SingleResponse(200, "PASSWORD_CHANGE");
    }
}