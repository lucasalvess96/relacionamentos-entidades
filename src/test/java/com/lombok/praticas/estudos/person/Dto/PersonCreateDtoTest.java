package com.lombok.praticas.estudos.person.Dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PersonCreateDtoTest {
    
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    @Test
    @DisplayName("should check the @NotBlank annotation for the NAME field")
    void testNameNotBlankValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "", "30", "123456789");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);
        
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertEquals("O campo NOME não deve está vazio", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @Pattern annotation for the NAME field")
    void testNamePatternValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John123", "30", "12345678901");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo NOME deve conter apenas letras", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @NotBlank annotation for the AGE field")
    void testAgeNotBlankValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "", "12345678901");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertEquals("O campo IDADE não deve está vazio", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @Size annotation for the AGE field")
    void testAgeSizeValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "3", "123456789");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertEquals("O campo IDADE deve possuir apenas 2 valores", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("should check the @Pattern annotation for the AGE field")
    void testAgePatternValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "jo", "123456789");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);        
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertEquals("O campo IDADE deve conter apenas números", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @NotBlank annotation for the CPF field")
    void testCpfNotBlankValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", "");

        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
        assertEquals("O campo CPF não deve está vazio", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("should check the @Size annotation for the CPF field")
    void testCpfSizeValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", "123456789");
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O campo CPF deve possuir apenas 11 dígitos", violations.iterator().next().getMessage());
    }
    
    @Test
    @DisplayName("should check the @Pattern annotation for the CPF field")
    void testCpfPatternValidation() {
        PersonCreateDto person = new PersonCreateDto(1L, "John Doe", "30", "jodoe");
        
        Set<ConstraintViolation<PersonCreateDto>> violations = validator.validate(person);
        assertFalse(violations.isEmpty());
        assertEquals(2, violations.size());
        assertEquals("O campo CPF deve conter apenas números", violations.iterator().next().getMessage());
    }
}
