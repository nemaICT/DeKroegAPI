package dekroeg.util;

import dekroeg.exception.ResourceNotFoundException;
import dekroeg.repository.AddressRepository;
import dekroeg.repository.MemberRepository;
import org.springframework.stereotype.Component;
import dekroeg.domain.Address;
import dekroeg.domain.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class Utils {

    public String formatLocalDateTimeToDatabaseStyle(LocalDateTime localDateTime){
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format((localDateTime));
    }

    public Member findMemberOrThrowNotFound(Long id, MemberRepository memberRepository){
        return memberRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unfortunatly no member was found with id nr " + id ));
    }

    public Address findAddressOrThrowNotFound(Long id, AddressRepository addressRepository){
        return addressRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unfortunatly no address was found with id nr " + id ));
    }
}
