package dekroeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dekroeg.domain.Member;
import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long> {

   List<Member> findByFirstname(String firstname);
}
