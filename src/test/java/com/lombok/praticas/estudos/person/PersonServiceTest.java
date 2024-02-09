package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class PersonServiceTest {

    @Mock
    Pageable pageable;

    @Mock
    private PersonRepository personRepositoryMock;

    @InjectMocks
    private PersonService personServiceMock;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository personRepository;

    private PersonEntity personEntity;

    private PersonCreateDto personCreateDto;

    @BeforeEach
    void setUp() {
        personEntity = new PersonEntity(1L, "John Doe", "30", "123456789");
        personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "123456789");
    }

    @Test
    @DisplayName("Should be creation of a personMock")
    void testPersonCreateMock() {
        PersonCreateDto personCreateDto = this.personCreateDto;
        PersonEntity savedPersonEntity = this.personEntity;
        when(personRepositoryMock.save(any(PersonEntity.class))).thenReturn(savedPersonEntity);
        PersonCreateDto result = personServiceMock.personCreate(personCreateDto);
        verify(personRepositoryMock, times(1)).save(any(PersonEntity.class));
        assertAll("result",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(personCreateDto.id(), result.id(), "Ids should match"),
                () -> assertEquals(personCreateDto.name(), result.name(), "Names should match"),
                () -> assertEquals(personCreateDto.age(), result.age(), "Ages should match"),
                () -> assertEquals(personCreateDto.cpf(), result.cpf(), "CPFs should match")
        );
    }

    @Test
    @DisplayName("Should be creation of a person with real value")
    @Transactional
    void testPersonCreate() {
        PersonCreateDto result = personService.personCreate(personCreateDto);
        assertNotNull(result, "Result should not be null");
        Optional<PersonEntity> savedPersonOptional = personRepository.findById(result.id());
        assertTrue(savedPersonOptional.isPresent(), "Saved person should exist in the database");
        PersonEntity savedPerson = savedPersonOptional.get();
        assertEquals(personCreateDto.id(), savedPerson.getId(), "Ids should match");
        assertEquals(personCreateDto.name(), savedPerson.getName(), "Names should match");
        assertEquals(personCreateDto.age(), savedPerson.getAge(), "Ages should match");
        assertEquals(personCreateDto.cpf(), savedPerson.getCpf(), "CPFs should match");
    }

    @ParameterizedTest
    @CsvSource({
            "1, John Doe, 30, 12345678966",
            "2, Alice, 25, 98765432101",
            "3, Bob, 40, 55555555555",
            "4, Cris, 32, 01653212980"
    })
    @DisplayName("Should create a personMock with different data")
    void testPersonCreateMockWithDifferentData(Long id, String name, int age, String cpf) {
        PersonCreateDto personCreateDto = new PersonCreateDto(id, name, String.valueOf(age), cpf);
        PersonEntity personEntity = new PersonEntity(id, name, String.valueOf(age), cpf);
        when(personRepositoryMock.save(any(PersonEntity.class))).thenReturn(personEntity);
        PersonCreateDto result = personServiceMock.personCreate(personCreateDto);
        assertAll("result",
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertEquals(personCreateDto.id(), result.id(), "Ids should match"),
                () -> assertEquals(personCreateDto.name(), result.name(), "Names should match"),
                () -> assertEquals(personCreateDto.age(), result.age(), "Ages should match"),
                () -> assertEquals(personCreateDto.cpf(), result.cpf(), "CPFs should match")
        );
    }

    @ParameterizedTest
    @CsvSource({
            "1, John, 30, 12345678966",
            "2, Alice, 25, 98765432100",
            "3, Bob, 40, 55555555555",
            "4, Cris, 32, 01653212980",
    })
    @DisplayName("Should create a person with real value with different data")
    void testPersonCreate(Long id, String name, int age, String cpf) {
        PersonCreateDto personCreateDto = new PersonCreateDto(id, name, String.valueOf(age), cpf);
        PersonCreateDto result = personService.personCreate(personCreateDto);
        assertNotNull(result.id(), "Result ID should not be null");
        Optional<PersonEntity> savedPersonOptional = personRepository.findById(result.id());
        assertTrue(savedPersonOptional.isPresent(), "Saved person should exist in the database");
        PersonEntity savedPerson = savedPersonOptional.get();
        assertNotNull(savedPerson.getId(), "Saved person ID should not be null");
        assertEquals(personCreateDto.name(), savedPerson.getName(), "Names should match");
        assertEquals(personCreateDto.age(), savedPerson.getAge(), "Ages should match");
        assertEquals(personCreateDto.cpf(), savedPerson.getCpf(), "CPFs should match");
    }

    @Test
    @DisplayName("Should test person list pagination")
    void testPersonListPagination() {
        List<PersonEntity> personEntities = Collections.singletonList(new PersonEntity(1L, "John Doe", "30",
                "123456789"));
        Page<PersonEntity> personEntityPage = new PageImpl<>(personEntities, pageable, 1);
        when(personRepositoryMock.findAll(pageable)).thenReturn(personEntityPage);
        Page<PersonCreateDto> result = personServiceMock.personListPagination(pageable);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).name());
    }

    @Test
    @DisplayName("Should test person list pagination when the list is empty")
    void testPersonListPaginationEmpty() {
        List<PersonEntity> emptyList = Collections.emptyList();
        Page<PersonEntity> emptyPage = new PageImpl<>(emptyList, pageable, 0);
        when(personRepositoryMock.findAll(pageable)).thenReturn(emptyPage);
        Page<PersonCreateDto> result = personServiceMock.personListPagination(pageable);
        assertEquals(0, result.getContent().size());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return list of PersonCreateDto")
    void testPersonList() {
        List<PersonEntity> personEntities = Arrays.asList(
                this.personEntity,
                new PersonEntity(2L, "Jane Smith", "25", "987654321")
        );
        when(personRepositoryMock.findAll()).thenReturn(personEntities);
        List<PersonCreateDto> result = personServiceMock.personList();
        assertEquals(2, result.size());
        PersonCreateDto firstPerson = result.get(0);
        assertAll(
                () -> assertEquals(1L, firstPerson.id()),
                () -> assertEquals("John Doe", firstPerson.name()),
                () -> assertEquals("30", firstPerson.age()),
                () -> assertEquals("123456789", firstPerson.cpf())
        );
        PersonCreateDto secondPerson = result.get(1);
        assertAll(
                () -> assertEquals(2L, secondPerson.id()),
                () -> assertEquals("Jane Smith", secondPerson.name()),
                () -> assertEquals("25", secondPerson.age()),
                () -> assertEquals("987654321", secondPerson.cpf())
        );
    }

    @Test
    @DisplayName("Should test person list with empty result")
    void testPersonListEmpty() {
        when(personRepositoryMock.findAll()).thenReturn(Collections.emptyList());
        List<PersonCreateDto> result = personServiceMock.personList();
        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    @DisplayName("Should return the List of a person with real data")
    void listPersonRealData() {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                List<PersonCreateDto> result = personService.personList();
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("John Doe", result.get(0).name());
                assertEquals("Clean Code", result.get(1).name());
            }
        });
    }

    @Test
    @Transactional
    @DisplayName("Should return an empty list when no person are in the real database")
    void listBookRealDataEmptyList() {
        List<PersonCreateDto> result = personService.personList();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update a person")
    void testPersonUpdate() {
        Long id = 1L;
        PersonCreateDto personCreateDto = new PersonCreateDto(id, "John Doe", "30", "12345678912");
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        personEntity.setName("John Doe atualizado");
        personEntity.setAge("34");
        personEntity.setCpf("02345678977");
        when(personRepositoryMock.findById(id)).thenReturn(Optional.of(personEntity));
        when(personRepositoryMock.save(any(PersonEntity.class))).thenReturn(personEntity);
        PersonCreateDto updatedPerson = personServiceMock.personUpdate(id, personCreateDto);
        assertAll(() -> {
            assertEquals(personCreateDto.id(), updatedPerson.id());
            assertEquals(personCreateDto.name(), updatedPerson.name());
            assertEquals(personCreateDto.age(), updatedPerson.age());
            assertEquals(personCreateDto.cpf(), updatedPerson.cpf());
        });
    }

    @Test
    @DisplayName("Should throw an exception when the ID is not found")
    void testPersonUpdateNotFound() {
        Long id = 1L;
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678912");
        when(personRepositoryMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(ErroRequest.class, () -> personServiceMock.personUpdate(id, personCreateDto));
    }

    @Test
    @DisplayName("Should return PersonCreateDto object for a valid ID")
    void testDetailPersonWithValidId() {
        Long id = 1L;
        PersonEntity personEntity = new PersonEntity(1L, "John Doe", "30", "123456789");
        Optional<PersonEntity> optionalPersonEntity = Optional.of(personEntity);
        when(personRepositoryMock.findById(id)).thenReturn(optionalPersonEntity);
        Optional<PersonCreateDto> result = personServiceMock.detailPerson(id);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().name());
        assertEquals("30", result.get().age());
        assertEquals("123456789", result.get().cpf());
    }

    @Test
    @DisplayName("Should throw ErroRequest for an invalid ID")
    void testDetailPersonWithInvalidId() {
        Long id = 2L;
        when(personRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ErroRequest.class, () -> personServiceMock.detailPerson(id));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Should search person pagination")
    void testSearchPersonPagination() {
        Pageable pageable = Pageable.unpaged();
        String name = "John";
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(1L);
        personEntity.setName("John Doe");
        Page<PersonEntity> personEntityPage = new PageImpl<>(Collections.singletonList(personEntity));
        when(personRepositoryMock.findByNameContainingIgnoreCase(any(String.class), any(Pageable.class)))
                .thenReturn(personEntityPage);
        Page<PersonSearchDto> result = personServiceMock.searchPersonPagination(name, pageable);
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).name());
    }

    @Test
    @DisplayName("Should not find search result")
    void testSearchPersonNotFound() {
        Pageable pageable = Pageable.unpaged();
        String name = "Jane";
        when(personRepositoryMock.findByNameContainingIgnoreCase(any(String.class), any(Pageable.class)))
                .thenReturn(Page.empty());
        Page<PersonSearchDto> result = personServiceMock.searchPersonPagination(name, pageable);
        assertEquals(0, result.getContent().size());
    }

    @Test
    @DisplayName("Should search list of persons")
    void testSearchListPerson() {
        String name = "John";
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(1L);
        personEntity.setName("John Doe");
        List<PersonEntity> personEntities = Collections.singletonList(personEntity);
        when(personRepositoryMock.findByNameContainingIgnoreCase(any(String.class))).thenReturn(personEntities);
        List<PersonSearchDto> result = personServiceMock.searchListPerson(name);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).name());
    }

    @Test
    @DisplayName("Should handle empty search result")
    void testSearchListPersonWithEmptyResult() {
        String name = "Jane";
        when(personRepositoryMock.findByNameContainingIgnoreCase(any(String.class))).thenReturn(Collections.emptyList());
        List<PersonSearchDto> result = personServiceMock.searchListPerson(name);
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Should delete person with valid ID")
    void testDeletePersonWithValidId() {
        Long id = 1L;
        when(personRepositoryMock.existsById(id)).thenReturn(true);
        assertDoesNotThrow(() -> personServiceMock.deletePerson(id));
    }

    @Test
    @DisplayName("Should throw error for non-existent ID")
    void testDeletePersonWithInvalidId() {
        Long id = 2L;
        when(personRepositoryMock.existsById(id)).thenReturn(false);
        assertThrows(ErroRequest.class, () -> personServiceMock.deletePerson(id));
    }
}
