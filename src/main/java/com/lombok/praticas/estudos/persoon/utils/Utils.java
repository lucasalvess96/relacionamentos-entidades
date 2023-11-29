package com.lombok.praticas.estudos.persoon.utils;

import com.lombok.praticas.estudos.address.AddressEntity;
import com.lombok.praticas.estudos.address.dto.AddressDto;

public class Utils {

    public static AddressDto convertEntityToDtoAddress(AddressEntity addressEntity) {
        return new AddressDto(addressEntity.getStreet(), addressEntity.getNumber(), addressEntity.getCity());
    }
}
