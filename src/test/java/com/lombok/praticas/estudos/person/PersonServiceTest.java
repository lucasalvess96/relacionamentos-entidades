package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    
    @InjectMocks
    private PersonService personService;
    
    @Mock
    private PersonRepository personRepository;
    
    @Test
    @DisplayName("Should be creation of a person")
    void testPersonCreate() {
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "123456789");
        
        PersonEntity savedPersonEntity = new PersonEntity();
        savedPersonEntity.setId(1L);
        savedPersonEntity.setName("John Doe");
        savedPersonEntity.setAge("30");
        savedPersonEntity.setCpf("123456789");
        
        when(personRepository.save(any(PersonEntity.class))).thenReturn(savedPersonEntity);
        
        PersonCreateDto result = personService.personCreate(personCreateDto);

        assertAll(() -> {
            assertEquals(personCreateDto.id(), result.id());
            assertEquals(personCreateDto.name(), result.name());
            assertEquals(personCreateDto.age(), result.age());
            assertEquals(personCreateDto.cpf(), result.cpf());
        });
    }
}
