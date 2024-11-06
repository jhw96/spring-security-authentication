package nextstep.security;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    @Autowired
    private MemberRepository memberRepository;

    public MemberInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        String[] split = authorization.split(" ");
        String type = split[0];
        String credential = split[1];

        if (!type.equalsIgnoreCase("Basic"))
            throw new AuthenticationException();


        String dcodedString = new String(Base64Utils.decodeFromString(credential));

        String[] token = dcodedString.split(":");

        if(token.length!= 2)
            throw new AuthenticationException();

        String username = token[0];
        String password = token[1];

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        if (!member.getPassword().equals(password))
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");

        return true;

    }
}
