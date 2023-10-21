package com.lombok.praticas.estudos.person.comum;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidateCpfUserTest {
    
    @Mock
    PersonRepository personRepository;
    
    @Test
    @DisplayName("should validate whether the CPF exists")
    void TestCpfExist(){
        when(personRepository.existsByCpf("12345678901")).thenReturn(true);
        
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678901");
        
        assertThrows(ErroRequest.class, () -> ValidateCpfUser.validateCpfUser(personCreateDto, personRepository));
    }

}
