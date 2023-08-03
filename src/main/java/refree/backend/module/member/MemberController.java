package refree.backend.module.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import refree.backend.infra.jwt.JwtService;
import refree.backend.infra.response.BasicResponse;
import refree.backend.infra.response.SingleResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/home")
    public ResponseEntity<? extends BasicResponse> home(){
        return ResponseEntity.ok().body(new SingleResponse("home"));
    }

    @GetMapping("/test")
    public ResponseEntity<? extends BasicResponse> test(){
        return ResponseEntity.ok().body(new SingleResponse("test"));
    }

    @PostMapping("/signup")
    public ResponseEntity<? extends BasicResponse> signup(){
        memberRepository.save(Member.crateMember("ts04031@gmail.com", "00000000"));
        return ResponseEntity.ok().body(new SingleResponse("signup"));
    }
}
