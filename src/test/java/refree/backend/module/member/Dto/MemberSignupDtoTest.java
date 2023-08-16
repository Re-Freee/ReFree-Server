package refree.backend.module.member.Dto;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberSignupDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init(){
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @AfterAll
    public static void close() { factory.close(); }

    @Test
    void signup_wrong_nickname() {
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester1@test.com",
                "12345678", "12345678", "t");

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("닉네임은 2~8자 이내로 입력하셔야 합니다.");
        });
    }

    @Test
    void signup_long_email(){
        MemberSignupDto memberSignupDto = new MemberSignupDto("testertestertestertestertester@test.com",
                "12345678", "12345678", "tester");

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일은 40자 내로 입력해 주세요.");
        });
    }

    @Test
    void signup_wrong_email(){
        MemberSignupDto memberSignupDto = new MemberSignupDto("testertestertestertestertester",
                "12345678", "12345678", "tester");

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일 형식으로 입력해 주세요.");
        });
    }

    @Test
    void signup_wrong_password(){
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester1@test.com",
                "1234", "1234", "tester");

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("비밀번호는 8자 이상 입력하셔야 합니다.");
        });
    }

    @Test
    void signup_blank_password(){
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester1@test.com",
                null, null, "tester");

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("비밀번호를 입력해 주세요.");
        });
    }

    @Test
    void signup_blank_email(){
        MemberSignupDto memberSignupDto = new MemberSignupDto(null,
                "12345678", "12345678", "tester");

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("이메일을 입력해 주세요.");
        });
    }

    @Test
    void signup_blank_nickname(){
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester1@test.com",
                "12345678", "12345678", null);

        validator.validate(memberSignupDto).forEach(error -> {
            assertThat(error.getMessage()).isEqualTo("닉네임을 입력해 주세요.");
        });
    }


}