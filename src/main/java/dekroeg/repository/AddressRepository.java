package dekroeg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dekroeg.domain.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByCityName(String cityName);
}
