package dekroeg.service;

import dekroeg.repository.AddressRepository;
import dekroeg.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import dekroeg.domain.Address;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AddressService {

    private final Utils utils;
    private final AddressRepository addressRepository;

    // RestTemplate Pageable
    public Page<Address> listAll(Pageable pageable){
        return addressRepository.findAll(pageable);
    }

    public List<Address> findByFirstname(String cityName){
        return addressRepository.findByCityName(cityName);
    }

    public Address findById(Long id){
        return utils.findAddressOrThrowNotFound(id, addressRepository);
    }

    @Transactional
    public Address saveMember(Address address){
        return addressRepository.save(address);
    }

    public void deleteMember(Long id){
        addressRepository.delete(utils.findAddressOrThrowNotFound(id, addressRepository));
    }

    public void updateMember(Address address){
        addressRepository.save(address);
    }
}
