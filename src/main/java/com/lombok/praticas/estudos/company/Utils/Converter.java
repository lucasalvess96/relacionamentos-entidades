package com.lombok.praticas.estudos.company.Utils;

import com.lombok.praticas.estudos.contactPerson.ContactPersonDto;
import com.lombok.praticas.estudos.contactPerson.ContactPersonEntity;

public class Converter {

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
