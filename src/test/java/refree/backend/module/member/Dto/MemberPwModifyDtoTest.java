package refree.backend.module.member.Dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import refree.backend.module.member.Member;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberPwModifyDtoTest {
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
    void modify_blank_email(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto(null,
                member.getPassword(), member.getCheckPassword());

        validator.validate(memberPwModifyDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일을 입력해 주세요.");
        });
    }

    @Test
    void modify_long_email(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto("testertestertestertestertester@test.com",
                member.getPassword(), member.getCheckPassword());

        validator.validate(memberPwModifyDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일은 40자 내로 입력해 주세요.");
        });
    }

    @Test
    void modify_wrong_email(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto("testertestertestertestertester",
                member.getPassword(), member.getCheckPassword());

        validator.validate(memberPwModifyDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일 형식으로 입력해 주세요.");
        });
    }

    @Test
    void modify_blank_password(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto(member.getEmail(),
               null, member.getCheckPassword());

        validator.validate(memberPwModifyDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("새 비밀번호를 입력해 주세요.");
        });
    }

    @Test
    void modify_wrong_password(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto(member.getEmail(),
                "1234", "1234");

        validator.validate(memberPwModifyDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("비밀번호는 8자 이상이어야 합니다.");
        });
    }

    @Test
    void modify_blank_checkPassword(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto(member.getEmail(),
                "00000000", null);

        validator.validate(memberPwModifyDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("새 비밀번호를 다시 입력해 주세요.");
        });
    }
}