package dekroeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dekroeg.domain.UserAuthorization;


public interface UserAuthorizationRepository extends JpaRepository<UserAuthorization, Long> {

   UserAuthorization findByUsername(String username);
}
