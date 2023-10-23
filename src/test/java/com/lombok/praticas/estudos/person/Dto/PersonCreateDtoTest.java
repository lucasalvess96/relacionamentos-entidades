package com.lombok.praticas.estudos.person.Dto;

import com.lombok.praticas.estudos.person.PersonEntity;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PersonCreateDtoTest {
    
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    @DisplayName("should check the @NotBlank annotation for the NAME field")
    void testNameNotBlankValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, null, "30", "12345678966");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"name");
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo NOME não deve está vazio", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @Pattern annotation for the NAME field")
    void testNamePatternValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John123", "30", "12345678901");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"name");
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo NOME deve conter apenas letras", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Should validate a valid NAME")
    void testValidName() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", "12345678902");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"name");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should check the @NotBlank annotation for the AGE field")
    void testAgeNotBlankValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", null, "12345678905");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person, "age");
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo IDADE não deve está vazio", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @Size annotation for the AGE field")
    void testAgeSizeValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "3", "12345678978");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"age");
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo IDADE deve possuir apenas 2 dígitos",
                violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("should check the @Pattern annotation for the AGE field")
    void testAgePatternValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "jo", "12345678985");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"age");        
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo IDADE deve conter apenas números", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Should validate a valid AGE")
    void testValidAge() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "32", "12345678988");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"age");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("should check the @NotBlank annotation for the CPF field")
    void testCpfNotBlankValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", null);

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"cpf");
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo CPF não deve está vazio", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @Size annotation for the CPF field")
    void testCpfSizeValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", "123456789");
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"cpf");

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo CPF deve possuir apenas 11 dígitos", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("should check the @Pattern annotation for the CPF field")
    void testCpfPatternValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", "123456789ds");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"cpf");
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo CPF deve conter apenas números", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("Should validate a valid CPF")
    void testValidCpf() {        
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "22", "12345678980");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validateProperty(person,"cpf");
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should create PersonCreateDto from PersonEntity")
    void testPersonCreateDtoFromPersonEntity() {
        PersonEntity personEntity = new PersonEntity(1L, "John Doe", "30", "12345678991");
        PersonCreateDto personCreateDto = new PersonCreateDto(personEntity);

        assertEquals(personEntity.getId(), personCreateDto.id());
        assertEquals(personEntity.getName(), personCreateDto.name());
        assertEquals(personEntity.getAge(), personCreateDto.age());
        assertEquals(personEntity.getCpf(), personCreateDto.cpf());
    }
}
