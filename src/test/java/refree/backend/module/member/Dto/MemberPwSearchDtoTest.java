package refree.backend.module.member.Dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import refree.backend.infra.exception.MemberException;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.RecipeLike.RecipeLikeRepository;
import refree.backend.module.member.Member;
import refree.backend.module.member.MemberRepository;
import refree.backend.module.member.MemberService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberPwSearchDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;
    private static Member member;

    @BeforeAll
    public static void init(){
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        member = Member.builder().email("tester1@test.com")
                .password("12345678").checkPassword("12345678")
                .nickname("tester").certification("testcertification")
                .isChange(0).build();
    }
    @AfterAll
    public static void close() { factory.close(); }

    @Test
    void search_blank_email(){
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto(null,
                member.getCertification());

        validator.validate(memberPwSearchDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일을 입력해 주세요.");
        });
    }

    @Test
    void search_long_email(){
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto("testertestertestertestertester@test.com",
                member.getCertification());

        validator.validate(memberPwSearchDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일은 40자 내로 입력해 주세요.");
        });
    }

    @Test
    void search_wrong_email(){
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto("testertestertestertestertester",
                member.getCertification());

        validator.validate(memberPwSearchDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일 형식으로 입력해 주세요.");
        });
    }

    @Test
    void search_blank_certification(){
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto(member.getEmail(),
                null);

        validator.validate(memberPwSearchDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("인증번호를 입력해 주세요.");
        });
    }


}