package com.lombok.praticas.estudos.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @MockBean
    PersonService personServiceMock;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonCreateDto personCreateDto;

    private List<PersonCreateDto> persons;

    private PersonSearchDto person;

    private PersonSearchDto personII;

    @BeforeEach
    void setUp() {
        personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678951");
        persons = List.of(
                new PersonCreateDto(1L, "John Doe", "30", "12345678951"),
                new PersonCreateDto(2L, "Jane Doe", "28", "12345678952")
        );
        person = new PersonSearchDto("John Doe");
        personII = new PersonSearchDto("Jane Doe");
    }

    @Test
    @DisplayName("Should create a new person successfully")
    void createPerson() throws Exception {
        PersonCreateDto savedPerson = new PersonCreateDto(2L, "John Doe", "30", "12345678951");
        when(personServiceMock.personCreate(any(PersonCreateDto.class))).thenReturn(savedPerson);
        mockMvc.perform(post("/person/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/person/create/" + savedPerson.id()))
                .andExpect(jsonPath("$.id").value(savedPerson.id()))
                .andExpect(jsonPath("$.name").value(savedPerson.name()))
                .andExpect(jsonPath("$.age").value(savedPerson.age()))
                .andExpect(jsonPath("$.cpf").value(savedPerson.cpf()));
    }

    @Test
    @DisplayName("Should return paginated list of persons")
    void listPersonsPagination() throws Exception {
        Page<PersonCreateDto> page = new PageImpl<>(persons);
        when(personServiceMock.personListPagination(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/person/pagination")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(persons.get(0).id()))
                .andExpect(jsonPath("$.content[0].name").value(persons.get(0).name()))
                .andExpect(jsonPath("$.content[0].age").value(persons.get(0).age()))
                .andExpect(jsonPath("$.content[0].cpf").value(persons.get(0).cpf()))
                .andExpect(jsonPath("$.content[1].id").value(persons.get(1).id()))
                .andExpect(jsonPath("$.content[1].name").value(persons.get(1).name()))
                .andExpect(jsonPath("$.content[1].age").value(persons.get(1).age()))
                .andExpect(jsonPath("$.content[1].cpf").value(persons.get(1).cpf()));
    }

    @Test
    @DisplayName("Should return list of persons")
    void listPersons() throws Exception {
        when(personServiceMock.personList()).thenReturn(persons);
        mockMvc.perform(get("/person/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(persons.get(0).id()))
                .andExpect(jsonPath("$[0].name").value(persons.get(0).name()))
                .andExpect(jsonPath("$[0].age").value(persons.get(0).age()))
                .andExpect(jsonPath("$[0].cpf").value(persons.get(0).cpf()))
                .andExpect(jsonPath("$[1].id").value(persons.get(1).id()))
                .andExpect(jsonPath("$[1].name").value(persons.get(1).name()))
                .andExpect(jsonPath("$[1].age").value(persons.get(1).age()))
                .andExpect(jsonPath("$[1].cpf").value(persons.get(1).cpf()));
    }

    @Test
    @DisplayName("Should update a person successfully")
    void updatePersonSuccess() throws Exception {
        when(personServiceMock.personUpdate(eq(1L), any(PersonCreateDto.class))).thenReturn(personCreateDto);
        mockMvc.perform(put("/person/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(personCreateDto.id()))
                .andExpect(jsonPath("$.name").value(personCreateDto.name()))
                .andExpect(jsonPath("$.age").value(personCreateDto.age()))
                .andExpect(jsonPath("$.cpf").value(personCreateDto.cpf()));
    }

    @Test
    @DisplayName("Should return person details successfully")
    void detailPersonSuccess() throws Exception {
        when(personServiceMock.detailPerson(1L)).thenReturn(Optional.of(personCreateDto));
        mockMvc.perform(get("/person/detail/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(personCreateDto.id()))
                .andExpect(jsonPath("$.name").value(personCreateDto.name()))
                .andExpect(jsonPath("$.age").value(personCreateDto.age()))
                .andExpect(jsonPath("$.cpf").value(personCreateDto.cpf()));
    }

    @Test
    @DisplayName("Should return 404 when person not found")
    void detailPersonNotFound() throws Exception {
        when(personServiceMock.detailPerson(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/person/detail/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return a paginated list of persons")
    void pagedSearchSuccess() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PersonSearchDto> personPage = new PageImpl<>(List.of(person, personII), pageable, 2);
        when(personServiceMock.searchPersonPagination(eq("Doe"), any(Pageable.class))).thenReturn(personPage);
        mockMvc.perform(get("/person/search/pagination")
                        .param("name", "Doe")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value(personPage.getContent().get(0).name()))
                .andExpect(jsonPath("$.content[1].name").value(personPage.getContent().get(1).name()));
    }

    @Test
    @DisplayName("Should return a list of persons")
    void testSearchListSuccess() throws Exception {
        when(personServiceMock.searchListPerson("Doe")).thenReturn(List.of(person, personII));
        mockMvc.perform(get("/person/search/list")
                        .param("name", "Doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    @DisplayName("Should delete a person successfully")
    void testDeletePerson() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/person/delete/{id}", id))
                .andExpect(status().isNoContent());
        verify(personServiceMock, times(1)).deletePerson(id);
    }
}
