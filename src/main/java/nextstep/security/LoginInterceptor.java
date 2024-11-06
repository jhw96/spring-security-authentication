package nextstep.security;

import nextstep.app.domain.Member;
import nextstep.app.domain.MemberRepository;
import nextstep.app.ui.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor{

    public static final String SPRING_SECURITY_CONTEXT_KEY = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private final MemberRepository memberRepository;

    public LoginInterceptor(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String username = parameterMap.get("username")[0];
        String password = parameterMap.get("password")[0];

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(AuthenticationException::new);

        if(!member.getPassword().equals(password) ){
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        request.getSession().setAttribute(SPRING_SECURITY_CONTEXT_KEY,member);

        return true;
    }
}
