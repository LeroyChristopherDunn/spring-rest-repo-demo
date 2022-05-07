package org.example.demo.validatedentity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.example.demo.TestUtils.asJsonString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ValidatedEntityValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ValidatedEntityRepository repository;

    private final ValidatedEntity entity = new ValidatedEntity(
            0L,
            "not blank string",
            "not whitespace string",
            "not null",
            true,
            new Date(),
            20
    );

    private ValidatedEntity savedEntity;

    @BeforeEach
    public void setup(){
        savedEntity = repository.save(entity);
    }

    @AfterEach
    public void cleanup(){
        repository.deleteAll();
    }

    @Test
    public void givenEmptyStringOnNotBlankField_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("notBlankString", "");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenWhitespaceStringOnNotBlankField_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("notBlankString", "   ");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullOnNotBlankField_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("notBlankString", null);

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenWhitespaceOnNotWhitespacePattern_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("notWhitespaceString", "     ");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenWhitespacePaddedStringOnNotWhitespacePattern_whenPatch_shouldUpdate() throws Exception {

        String update = "  update    ";
        Map<String, Object> patch = new HashMap<>();
        patch.put("notWhitespaceString", update);

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.notWhitespaceString", is(update)));
    }


    @Test
    public void givenNullOnNotNullField_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("notNull", null);

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNullOnNullableField_whenPatch_shouldUpdate() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("bool", null);

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.bool", is(nullValue())));
    }


    @Test
    public void givenValidBoolField_whenPatch_shouldUpdate() throws Exception {

        Boolean oldBool = entity.getBool();
        entity.setBool(true);
        ValidatedEntity saved = repository.save(entity);

        Map<String, Object> patch = new HashMap<>();
        patch.put("bool", false);

        this.mockMvc.perform(
                patch("/validated/" + saved.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.bool", is(false)));

        entity.setBool(oldBool);
    }

    @Test
    public void givenInvalidBoolField_whenPatch_shouldReturnBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("bool", "invalid");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenInvalidDate_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("date", "invalid");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenInvalidInt_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("positiveInt", "invalid");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenNegativeIntOnNonNullPositiveInt_whenPatch_shouldBadRequest() throws Exception {

        Map<String, Object> patch = new HashMap<>();
        patch.put("positiveInt", -100);

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void givenInvalidRequest_whenPost_shouldBadRequest() throws Exception {

        entity.setNotBlankString(" ");

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(entity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenInvalidRequest_whenPut_shouldBadRequest() throws Exception {

        entity.setNotNull(null);

        this.mockMvc.perform(
                patch("/validated/" + savedEntity.getId())
                        .content(asJsonString(entity))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}