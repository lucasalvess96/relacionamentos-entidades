package com.lombok.praticas.estudos.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

}
