package hello.springs.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    @Transactional
    public void save(Member member) {
        log.info("멤버저장");
        em.persist(member);
    }

    public Optional<Member> findByUsername(String name) {
        return em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                .setParameter("username", name)
                .getResultList().stream().findAny();
    }
}
