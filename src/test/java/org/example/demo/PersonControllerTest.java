package org.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

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

    @BeforeEach
    public void setupAll(){
        repository.save(mockPerson1);
        repository.save(mockPerson2);
    }

    @AfterEach
    public void cleanupAll(){
        repository.deleteAll();
    }

    @Test
    public void givenInvalidIds_whenGetPeople_shouldReturnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/people/search")
                .queryParam("ids", "-1", "-2")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").doesNotExist());
    }

    @Test
    public void givenIds_whenGetPeople_shouldReturnPeople() throws Exception {
        Person save1 = repository.save(mockPerson1);
        Person save2 = repository.save(mockPerson2);
        this.mockMvc.perform(get("/people/search")
                .queryParam("ids", save2.getId().toString(), save1.getId().toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").isArray())
                .andExpect(jsonPath("$._embedded.people", hasSize(2)))
                .andExpect(jsonPath("$._embedded.people[0].id", is(save1.getId().intValue())))
                .andExpect(jsonPath("$._embedded.people[1].id", is(save2.getId().intValue())));
    }

    @Test
    public void givenInvalidName_whenGetPeopleByFirstName_shouldReturnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/people/search")
                .queryParam("firstName", "invalid")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").doesNotExist());
    }

    @Test
    public void givenFirstName_whenGetPeopleByFirstName_shouldReturnPerson() throws Exception {
        this.mockMvc.perform(get("/people/search")
                .queryParam("firstName", mockPerson1.getFirstName())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").isArray())
                .andExpect(jsonPath("$._embedded.people", hasSize(1)))
                .andExpect(jsonPath("$._embedded.people[0].firstName", is(mockPerson1.getFirstName())));
    }

    @Test
    public void givenUppercaseName_whenGetPeopleByLastNameIgnoreCase_shouldReturnPerson() throws Exception {
        this.mockMvc.perform(get("/people/search")
                .queryParam("lastNameIgnoreCase", mockPerson1.getLastName().toUpperCase())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").isArray())
                .andExpect(jsonPath("$._embedded.people", hasSize(1)))
                .andExpect(jsonPath("$._embedded.people[0].lastName", is(mockPerson1.getLastName())));
    }

    @Test
    public void givenPartOfName_whenGetPeopleByLastNameLike_shouldReturnPerson() throws Exception {
        this.mockMvc.perform(get("/people/search")
                .queryParam("lastNameLike", mockPerson1.getLastName().substring(2))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.people").isArray())
                .andExpect(jsonPath("$._embedded.people", hasSize(1)))
                .andExpect(jsonPath("$._embedded.people[0].lastName", is(mockPerson1.getLastName())));
    }
}