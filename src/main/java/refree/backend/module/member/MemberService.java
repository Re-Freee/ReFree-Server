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

    public SingleResponse search(MemberPwSearchDto memberPwSearchDto) {
        Member member = memberPwSearchDto.toEntity();
        Optional<Member> updateMember = memberRepository.findByEmail(memberPwSearchDto.getEmail());
        member = updateMember.get();

        if (!memberRepository.findByEmail(memberPwSearchDto.getEmail()).isPresent()){
            throw new MemberException("존재하지 않는 계정입니다.");
        }

        member.updateFlag(1);

        memberRepository.save(member);

        return new SingleResponse(200, "EMAIL_EXIST");
    }

    public SingleResponse modify(MemberPwModifyDto memberPwModifyDto) {
        Member member = memberPwModifyDto.toEntity();
        Optional<Member> updateMember = memberRepository.findByEmail(memberPwModifyDto.getEmail());
        member = updateMember.get();

        if (member.getIsChange() == 0){ // Exception (이미 존재하는 것이 확인된 계정에서만 호출되는 API
            throw new MemberException("잘못된 접근입니다.");
        }

        if (!memberPwModifyDto.getNewPassword().equals(memberPwModifyDto.getCheckNewPassword())) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 변경
        member.updatePassword(memberPwModifyDto.getNewPassword());
        member.updateCheckPassword(memberPwModifyDto.getCheckNewPassword());

        System.out.println(memberPwModifyDto.getNewPassword());
        System.out.println(member.getPassword());

        // password Encoding
        member.encodePassword(passwordEncoder);
        member.encodeCheckPassword(passwordEncoder);

        member.updateFlag(0);

        memberRepository.save(member);

        return new SingleResponse(200, "PASSWORD_CHANGE");
    }

    public SingleResponse check(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException("회원 정보를 찾을 수 없습니다."));

        return new SingleResponse(200, member.getEmail());
    }
}