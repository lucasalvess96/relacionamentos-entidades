package com.lombok.praticas.estudos.persoon.utils;

import com.lombok.praticas.estudos.address.AddressEntity;
import com.lombok.praticas.estudos.address.dto.AddressDto;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Esta classe de utilitário não pode ser instanciada.");
    }

    public static AddressDto convertEntityToDtoAddress(AddressEntity addressEntity) {
        return new AddressDto(
                addressEntity.getId(),
                addressEntity.getStreet(),
                addressEntity.getNumber(),
                addressEntity.getCity()
        );
    }
}
