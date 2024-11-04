package nextstep.app.ui;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class MemberController {

    private final MemberRepository memberRepository;

    public MemberController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/members")
    public ResponseEntity<List<Member>> list(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        String[] split = authorization.split(" ");
        String type = split[0];
        String credential = split[1];

        if (!type.equalsIgnoreCase("Basic"))
            throw new AuthenticationException();

        try {

            String dcodedString = new String(Base64Utils.decodeFromString(credential));

            String[] token = dcodedString.split(":");

            String username = token[0];
            String password = token[1];

            Member member = memberRepository.findByEmail(username)
                    .orElseThrow(AuthenticationException::new);

            if (!member.getPassword().equals(password))
                throw new AuthenticationException("비밀번호가 일치하지 않습니다.");

            List<Member> members = memberRepository.findAll();

            return ResponseEntity.ok(members);
        } catch (Exception e){
            throw new AuthenticationException();
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
