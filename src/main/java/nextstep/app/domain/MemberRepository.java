package nextstep.app.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findByEmail(String email);

    List<Member> findAll();

    void save(Member member);
}
