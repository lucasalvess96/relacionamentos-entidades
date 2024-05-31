package com.lombok.praticas.estudos.person.comum;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.PersonRepository;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateUserTest {

    @Mock
    private PersonRepository personRepository;

    @Test
    @DisplayName("Should throw UnsupportedOperationException when instantiating ValidateUser")
    void testConstructor() {
        Constructor<ValidateUser> constructor = null;
        try {
            constructor = ValidateUser.class.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            fail("Failed to find the constructor: " + e.getMessage());
        }
        try {
            if (constructor != null) {
                constructor.newInstance();
                fail("Expected UnsupportedOperationException was not thrown");
            }
        } catch (InstantiationException | IllegalAccessException e) {
            fail("Failed to instantiate ValidateUser: " + e.getMessage());
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof UnsupportedOperationException) {
                return;
            }
            fail("Expected UnsupportedOperationException was not thrown");
        }
    }

    @Test
    @DisplayName("should validate whether the person exists")
    void testNameExist() {
        when(personRepository.existsByName("John Doe")).thenReturn(true);
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678901");
        assertThrows(ErroRequest.class, () -> ValidateUser.validateUser(personCreateDto, personRepository));
    }
}
