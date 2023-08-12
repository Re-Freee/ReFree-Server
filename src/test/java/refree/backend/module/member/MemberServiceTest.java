package refree.backend.module.member;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.aspectj.bridge.MessageUtil;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import refree.backend.infra.exception.MemberException;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.SingleResponse;
import refree.backend.module.Recipe.Dto.RecipeLikeDto;
import refree.backend.module.member.Dto.MemberPwModifyDto;
import refree.backend.module.member.Dto.MemberPwSearchDto;
import refree.backend.module.member.Dto.MemberSignupDto;
import refree.backend.module.member.MemberRepository;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberController memberController;
    @Autowired
    MemberRepository memberRepository;
    HttpServletResponse response = new MockHttpServletResponse(); // 모의 객체

    @Test
    @Order(1)
    void signup() {
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester1@test.com",
                "12345678", "12345678", "tester");

        SingleResponse result = memberService.signup(memberSignupDto, response);

        assertEquals(result.getMessage(),"REGISTRATION_COMPLETE");
    }

    @Test
    @Order(2)
    void signup_duplicate() throws MemberException {
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester1@test.com",
                "12345678", "12345678", "sametester");

        assertThrows(MemberException.class, () -> {
           memberService.signup(memberSignupDto, response);
        });

    }

    @Test
    @Order(3)
    void signup_wrong_password() throws MemberException {
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester2@test.com",
                "12345678", "00000000", "tester");

        assertThrows(MemberException.class, () -> {
            memberService.signup(memberSignupDto, response);
        });
    }

    @Test
    @Order(4)
    void search() {
        Member member = memberRepository.findByEmail("tester1@test.com").get();
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto(member.getEmail(),
                member.getCertification());

        SingleResponse result = memberService.search(memberPwSearchDto);

        assertEquals(result.getMessage(), "EMAIL_EXIST");
    }

    @Test
    @Order(5)
    void search_not_exist() throws MemberException {
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto("tester3@test.com",
                "notExistMember");

        assertThrows(MemberException.class, () -> {
            memberService.search(memberPwSearchDto);
        });
    }

    @Test
    @Order(6)
    void search_wrong_certification() throws MemberException {
        Member member = memberRepository.findByEmail("tester1@test.com").get();
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto(member.getEmail(),
                "ThisIsWrongCertification");

        assertThrows(MemberException.class, () -> {
            memberService.search(memberPwSearchDto);
        });
    }

    @Test
    @Order(7)
    void modify() {
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto("tester1@test.com",
                "00000000", "00000000");

        SingleResponse result = memberService.modify(memberPwModifyDto);

        assertEquals(result.getMessage(), "CHANGE_PASSWORD");
    }

    @Test
    @Order(8)
    void modify_not_exist(){
        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto("tester3@test.com",
                "00000000", "00000000");

        assertThrows(MemberException.class, () -> {
            memberService.modify(memberPwModifyDto);
        });
    }

    @Test
    @Order(9)
    void modify_bad_request(){
        MemberSignupDto memberSignupDto = new MemberSignupDto("tester2@test.com",
                "12345678", "12345678", "tester2");
        memberService.signup(memberSignupDto, response);

        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto("tester2@test.com",
                "00000000", "00000000");

        assertThrows(MemberException.class, () -> {
            memberService.modify(memberPwModifyDto);
        });
    }

    @Test
    @Order(10)
    void modify_wrong_password(){
        Member member = memberRepository.findByEmail("tester2@test.com").get();
        MemberPwSearchDto memberPwSearchDto = new MemberPwSearchDto(member.getEmail(),
                member.getCertification());

        memberService.search(memberPwSearchDto);

        MemberPwModifyDto memberPwModifyDto = new MemberPwModifyDto("tester2@test.com",
                "00000000", "00000001");

        assertThrows(MemberException.class, () -> {
            memberService.modify(memberPwModifyDto);
        });
    }

    @Test
    @Order(11)
    void recipeLike() {
        Member member = memberRepository.findByEmail("tester1@test.com").get();
        assertEquals(memberController.likedRecipe(member).getStatusCodeValue(), 200);
    }
}