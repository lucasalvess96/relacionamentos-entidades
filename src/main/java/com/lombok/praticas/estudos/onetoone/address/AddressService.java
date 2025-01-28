package com.lombok.praticas.estudos.onetoone.address;

import com.lombok.praticas.estudos.onetoone.address.dto.AddressDto;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public AddressEntity saveAddress(AddressDto addressDto) {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(addressDto.id());
        addressEntity.setStreet(addressDto.street());
        addressEntity.setNumber(addressDto.number());
        addressEntity.setCity(addressDto.city());

        return addressRepository.save(addressEntity);
    }
}
