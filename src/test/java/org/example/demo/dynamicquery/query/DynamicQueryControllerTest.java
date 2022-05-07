package org.example.demo.dynamicquery.query;

import org.example.demo.dynamicquery.DynamicQueryEntity;
import org.example.demo.dynamicquery.DynamicQueryRepository;
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

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
public class DynamicQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DynamicQueryRepository repository;

    private final DynamicQueryEntity mockEntity1 = new DynamicQueryEntity(
            1L,
            "FN1",
            "LN1"
    );

    private final DynamicQueryEntity mockEntity2 = new DynamicQueryEntity(
            2L,
            "FN2",
            "LN2"
    );

    @BeforeEach
    public void setupAll(){
        repository.save(mockEntity1);
        repository.save(mockEntity2);
    }

    @AfterEach
    public void cleanupAll(){
        repository.deleteAll();
    }

    @Test
    public void givenInvalidIds_whenQueryByIds_shouldReturnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/dynamicquery/search")
                .queryParam("ids", "-1", "-2")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dynamicquery").doesNotExist());
    }

    @Test
    public void givenIds_whenQueryByIds_shouldReturnPeople() throws Exception {
        DynamicQueryEntity save1 = repository.save(mockEntity1);
        DynamicQueryEntity save2 = repository.save(mockEntity2);
        this.mockMvc.perform(get("/dynamicquery/search")
                .queryParam("ids", save2.getId().toString(), save1.getId().toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dynamicquery").isArray())
                .andExpect(jsonPath("$._embedded.dynamicquery", hasSize(2)))
                .andExpect(jsonPath("$._embedded.dynamicquery[0].id", is(save1.getId().intValue())))
                .andExpect(jsonPath("$._embedded.dynamicquery[1].id", is(save2.getId().intValue())));
    }

    @Test
    public void givenInvalidFirstName_whenQueryByFirstName_shouldReturnEmptyArray() throws Exception {
        this.mockMvc.perform(get("/dynamicquery/search")
                .queryParam("firstName", "invalid")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dynamicquery").doesNotExist());
    }

    @Test
    public void givenValidFirstName_whenQueryByFirstName_shouldReturnPerson() throws Exception {
        this.mockMvc.perform(get("/dynamicquery/search")
                .queryParam("firstName", mockEntity1.getFirstName())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dynamicquery").isArray())
                .andExpect(jsonPath("$._embedded.dynamicquery", hasSize(1)))
                .andExpect(jsonPath("$._embedded.dynamicquery[0].firstName", is(mockEntity1.getFirstName())));
    }

    @Test
    public void givenUppercaseName_whenQueryByLastNameIgnoreCase_shouldReturnPerson() throws Exception {
        this.mockMvc.perform(get("/dynamicquery/search")
                .queryParam("lastNameIgnoreCase", mockEntity1.getLastName().toUpperCase())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dynamicquery").isArray())
                .andExpect(jsonPath("$._embedded.dynamicquery", hasSize(1)))
                .andExpect(jsonPath("$._embedded.dynamicquery[0].lastName", is(mockEntity1.getLastName())));
    }

    @Test
    public void givenPartOfName_whenQueryByLastNameLike_shouldReturnPerson() throws Exception {
        this.mockMvc.perform(get("/dynamicquery/search")
                .queryParam("lastNameLike", mockEntity1.getLastName().substring(2))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dynamicquery").isArray())
                .andExpect(jsonPath("$._embedded.dynamicquery", hasSize(1)))
                .andExpect(jsonPath("$._embedded.dynamicquery[0].lastName", is(mockEntity1.getLastName())));
    }
}