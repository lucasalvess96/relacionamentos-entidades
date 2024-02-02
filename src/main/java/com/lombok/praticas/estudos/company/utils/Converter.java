package com.lombok.praticas.estudos.company.utils;

import com.lombok.praticas.estudos.contactperson.ContactPersonDto;
import com.lombok.praticas.estudos.contactperson.ContactPersonEntity;

public class Converter {

    private Converter() {
        throw new UnsupportedOperationException("Está classe não pode ser instâncida");
    }

    public static ContactPersonDto convertEntityToDto(ContactPersonEntity contactPersonEntity) {
        if (contactPersonEntity != null)
            return new ContactPersonDto(contactPersonEntity.getFirstName(), contactPersonEntity.getLastName());
        return null;
    }

    public static ContactPersonEntity convertDtoToEntity(ContactPersonDto contactPersonDto) {
        if (contactPersonDto != null)
            return new ContactPersonEntity(contactPersonDto.firstName(), contactPersonDto.lastName());
        return null;
    }
}
