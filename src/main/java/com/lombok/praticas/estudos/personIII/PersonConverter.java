package com.lombok.praticas.estudos.personIII;

public class PersonConverter {

    public static PersonEntityIII toEntity(PersonDtoIII personDTO) {
        PersonEntityIII person = new PersonEntityIII();
        person.setId(personDTO.id());
        person.setName(personDTO.name());
        person.setAge(personDTO.age());
        person.setCpf(personDTO.cpf());
        return person;
    }

    public static PersonDtoIII toDto(PersonEntityIII person) {
        return new PersonDtoIII(
                person.getId(),
                person.getName(),
                person.getAge(),
                person.getCpf()
        );
    }
}
