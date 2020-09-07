package dekroeg.service;

import dekroeg.repository.UserAuthorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAuthorizationDetailsService implements UserDetailsService {

    private final UserAuthorizationRepository userAuthorizationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userAuthorizationRepository.findByUsername(username))
                .orElseThrow(()-> new UsernameNotFoundException("User not found!"));
    }
}
