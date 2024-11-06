package nextstep.app;

import nextstep.app.domain.MemberRepository;
import nextstep.security.LoginInterceptor;
import nextstep.security.MemberInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MemberRepository memberRepository;

    public WebConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        interceptorRegistry.addInterceptor(new LoginInterceptor(memberRepository))
                .addPathPatterns("/login");

        interceptorRegistry.addInterceptor(new MemberInterceptor(memberRepository))
                .addPathPatterns("/members");
    }
}
