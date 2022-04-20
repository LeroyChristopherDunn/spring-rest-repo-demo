package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository repository;

    private final Person mockPerson1 = new Person(
            1L,
            "FN1",
            "LN!"
    );

    private final Person mockPerson2 = new Person(
            2L,
            "FN2",
            "LN2"
    );

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanup(){
        repository.deleteAll();
    }

    @Test
    public void givenNoPeople_whenGetPeople_shouldReturnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/people"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").isArray())
                .andExpect(jsonPath("$._embedded.people", hasSize(0)));
    }

    @Test
    public void givenPeople_whenGetPeoople_shouldReturnPeople() throws Exception {

        Person save1 = repository.save(mockPerson1);
        Person save2 = repository.save(mockPerson2);

        this.mockMvc.perform(get("/people"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").isArray())
                .andExpect(jsonPath("$._embedded.people", hasSize(2)))
                .andExpect(jsonPath("$._embedded.people[0].id", is(save1.getId().intValue())))
                .andExpect(jsonPath("$._embedded.people[1].id", is(save2.getId().intValue())));
    }

    @Test
    public void givenNoPeople_whenGetPersonById_shouldReturn404() throws Exception {
        this.mockMvc.perform(get("/people/" + mockPerson1.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void givenPerson_whenGetPersonById_shouldReturnPerson() throws Exception {

        Person save1 = repository.save(mockPerson1);

        this.mockMvc.perform(get("/people/" + save1.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(save1.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(save1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(save1.getLastName())));
    }

    @Test
    public void givenNoPersonAndNoAcceptHeader_whenPost_shouldNotReturnPerson() throws Exception {

        this.mockMvc.perform(
                post("/people")
                        .content(asJsonString(mockPerson1))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void givenNoPersonAndAcceptHeader_whenPost_shouldReturnPerson() throws Exception {

        this.mockMvc.perform(
                post("/people")
                        .content(asJsonString(mockPerson1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is(mockPerson1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(mockPerson1.getLastName())));
    }

    @Test
    public void givenNoPerson_whenPost_shouldSave() throws Exception {

        this.mockMvc.perform(
                post("/people")
                        .content(asJsonString(mockPerson1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        // can't fetch by ID since it is auto generated
        Iterable<Person> personIterable = repository.findAll();
        List<Person> people = new ArrayList<>();
        personIterable.forEach(people::add);

        Assertions.assertEquals(people.size(), 1);
        Assertions.assertEquals(people.get(0).getFirstName(), mockPerson1.getFirstName());
        Assertions.assertEquals(people.get(0).getLastName(), mockPerson1.getLastName());
    }

    @Test
    public void givenConflictPerson_whenPost_shouldOverwrite() throws Exception {

        Person save1 = repository.save(mockPerson1);
        Long mockPerson2Id = mockPerson2.getId();
        mockPerson2.setId(save1.getId());

        this.mockMvc.perform(
                post("/people")
                        .content(asJsonString(mockPerson2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is(mockPerson2.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(mockPerson2.getLastName())));

        Optional<Person> personOptional = repository.findById(save1.getId());
        Assertions.assertTrue(personOptional.isPresent());
        Assertions.assertEquals(personOptional.get().getFirstName(), mockPerson2.getFirstName());
        Assertions.assertEquals(personOptional.get().getLastName(), mockPerson2.getLastName());

        mockPerson2.setId(mockPerson2Id);
    }

    @Test
    public void givenNoPerson_whenPut_shouldSave() throws Exception {

        this.mockMvc.perform(
                put("/people/" + mockPerson1.getId())
                        .content(asJsonString(mockPerson1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is(mockPerson1.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(mockPerson1.getLastName())));

        // can't fetch by ID since it is auto generated
        Iterable<Person> personIterable = repository.findAll();
        List<Person> people = new ArrayList<>();
        personIterable.forEach(people::add);

        Assertions.assertEquals(people.size(), 1);
        Assertions.assertEquals(people.get(0).getFirstName(), mockPerson1.getFirstName());
        Assertions.assertEquals(people.get(0).getLastName(), mockPerson1.getLastName());
    }

    @Test
    public void givenConflictPerson_whenPut_shouldOverwrite() throws Exception {

        Person save1 = repository.save(mockPerson1);
        Long mockPerson2Id = mockPerson2.getId();
        mockPerson2.setId(save1.getId());

        this.mockMvc.perform(
                put("/people/" + save1.getId())
                        .content(asJsonString(mockPerson2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is(mockPerson2.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(mockPerson2.getLastName())));

        Optional<Person> personOptional = repository.findById(save1.getId());
        Assertions.assertTrue(personOptional.isPresent());
        Assertions.assertEquals(personOptional.get().getFirstName(), mockPerson2.getFirstName());
        Assertions.assertEquals(personOptional.get().getLastName(), mockPerson2.getLastName());

        mockPerson2.setId(mockPerson2Id);
    }

    @Test
    public void givenNoPerson_whenPatch_should404() throws Exception {

        this.mockMvc.perform(
                patch("/people/" + mockPerson1.getId())
                        .content(asJsonString(mockPerson1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());

        Assertions.assertFalse(repository.findById(mockPerson1.getId()).isPresent());
    }

    @Test
    public void givenPerson_whenPatch_shouldUpdate() throws Exception {

        Person save1 = repository.save(mockPerson1);

        this.mockMvc.perform(
                patch("/people/" + save1.getId())
                        .content("{\"firstName\": \"updated\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName", is("updated")))
                .andExpect(jsonPath("$.lastName", is(mockPerson1.getLastName())));

        //noinspection OptionalGetWithoutIsPresent
        Person person = repository.findById(save1.getId()).get();
        Assertions.assertEquals(person.getFirstName(), "updated");
        Assertions.assertEquals(person.getLastName(), mockPerson1.getLastName());
    }

    @Test
    public void givenNoPerson_whenDelete_should404() throws Exception {

        this.mockMvc.perform(delete("/people/" + mockPerson1.getId()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void givenPerson_whenDelete_shouldDelete() throws Exception {

        Person save = repository.save(mockPerson1);

        this.mockMvc.perform(delete("/people/" + save.getId()))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());

        Optional<Person> personOptional = repository.findById(save.getId());
        Assertions.assertFalse(personOptional.isPresent());
    }
}