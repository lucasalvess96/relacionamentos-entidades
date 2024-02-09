package com.lombok.praticas.estudos.person.comum;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.PersonRepository;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateUserTest {

    @Mock
    private PersonRepository personRepository;

    @Test
    @DisplayName("should validate whether the person exists")
    void testNameExist() {
        when(personRepository.existsByName("John Doe")).thenReturn(true);
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678901");
        assertThrows(ErroRequest.class, () -> ValidateUser.validateUser(personCreateDto, personRepository));
    }
}
