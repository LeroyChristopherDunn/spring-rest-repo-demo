package org.example.demo.inlinenestedentity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.example.demo.TestUtils.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
public class InlineNestedEntityRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParentEntityRepository parentRepository;

    @Autowired
    private InlineNestedEntityRepository inlineNestedEntityRepository;

    private final ParentEntity mockParentEntity1 = new ParentEntity(
            "mockParentId1",
            "mockParentEntity1",
            new ArrayList<>(Arrays.asList(
                    new InlineNestedEntity(0L,"nested 1.1", "mockParentId1"),
                    new InlineNestedEntity(0L,"nested 1.2", "mockParentId1")
            ))
    );

    private final ParentEntity mockParentEntity2 = new ParentEntity(
            "mockParentId2",
            "mockParentEntity1",
            new ArrayList<>(Arrays.asList(
                    new InlineNestedEntity(0L,"nested 1.1", "mockParentId2"),
                    new InlineNestedEntity(0L,"nested 1.2", "mockParentId2")
            ))
    );

    @AfterEach
    public void cleanup(){
        inlineNestedEntityRepository.deleteAll();
        parentRepository.deleteAll();
    }

    @Test
    public void givenParentWithNested_whenGetParent_shouldReturnNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        this.mockMvc.perform(get("/parententity/" + savedParent.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inlineNestedEntities").isArray())
                .andExpect(jsonPath("$.inlineNestedEntities", hasSize(mockParentEntity1.getInlineNestedEntities().size())))
                .andExpect(jsonPath("$.inlineNestedEntities[0].name", is(mockParentEntity1.getInlineNestedEntities().get(0).getName())))
                .andExpect(jsonPath("$.inlineNestedEntities[1].name", is(mockParentEntity1.getInlineNestedEntities().get(1).getName())));
    }

    @Test
    public void givenParentWithNoNested_whenGetParent_shouldNotReturnNested() throws Exception {
        List<InlineNestedEntity> oldNested = mockParentEntity1.getInlineNestedEntities();
        mockParentEntity1.setInlineNestedEntities(Collections.emptyList());
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        this.mockMvc.perform(get("/parententity/" + savedParent.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inlineNestedEntities").isArray())
                .andExpect(jsonPath("$.inlineNestedEntities", hasSize(0)));

        mockParentEntity1.setInlineNestedEntities(oldNested);
    }

    @Test
    public void givenNoParent_whenPostParentWithNested_shouldSaveNested() throws Exception {
        this.mockMvc.perform(
                post("/parententity")
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(mockParentEntity1.getId());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().size(), nested.size());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(0).getName(), nested.get(0).getName());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(1).getName(), nested.get(1).getName());
    }

    @Test
    public void givenParent_whenPostParentWithNested_shouldOverwriteNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);
        List<InlineNestedEntity> oldNested = mockParentEntity1.getInlineNestedEntities();
        InlineNestedEntity newNested = new InlineNestedEntity("overwrite", savedParent.getId());
        mockParentEntity1.setInlineNestedEntities(Collections.singletonList(newNested));

        this.mockMvc.perform(
                post("/parententity")
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(1, nested.size());
        Assertions.assertEquals(newNested.getName(), nested.get(0).getName());

        mockParentEntity1.setInlineNestedEntities(oldNested);
    }

    @Test
    public void givenParent_whenPostParentEmptyNested_shouldOverwriteTags() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);
        List<InlineNestedEntity> oldNested = mockParentEntity1.getInlineNestedEntities();
        mockParentEntity1.setInlineNestedEntities(Collections.emptyList());

        this.mockMvc.perform(
                post("/parententity")
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> neested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(0, neested.size());

        mockParentEntity1.setInlineNestedEntities(oldNested);
    }

    @Test
    public void givenNoParent_whenPutParentWithNested_shouldSaveNested() throws Exception {
        this.mockMvc.perform(
                put("/parententity/" + mockParentEntity1.getId())
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$").doesNotExist());

        Iterable<InlineNestedEntity> nestedIterable = inlineNestedEntityRepository.findAll();
        List<InlineNestedEntity> nested = new ArrayList<>();
        nestedIterable.forEach(nested::add);
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().size(), nested.size());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(0).getName(), nested.get(0).getName());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(1).getName(), nested.get(1).getName());
    }

    @Test
    public void givenParent_whenPutParentWithNested_shouldOverwriteNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);
        List<InlineNestedEntity> oldNested = mockParentEntity1.getInlineNestedEntities();
        InlineNestedEntity newNested = new InlineNestedEntity("overwrite", savedParent.getId());
        mockParentEntity1.setInlineNestedEntities(Collections.singletonList(newNested));

        this.mockMvc.perform(
                put("/parententity/" + savedParent.getId())
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(1, nested.size());
        Assertions.assertEquals(newNested.getName(), nested.get(0).getName());
    }

    @Test
    public void givenParent_whenPutParentWithEmptyNested_shouldOverwriteNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);
        List<InlineNestedEntity> oldNested = mockParentEntity1.getInlineNestedEntities();
        mockParentEntity1.setInlineNestedEntities(Collections.emptyList());

        this.mockMvc.perform(
                put("/parententity/" + savedParent.getId())
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(0, nested.size());
    }

    @Test
    public void givenNoParent_whenPatchParentWithNested_should404() throws Exception {
        this.mockMvc.perform(
                patch("/parententity/" + mockParentEntity1.getId())
                        .content(asJsonString(mockParentEntity1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenParent_whenPatchParentWithEmptyNested_shouldRemoveNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        HashMap<String, Object> patch = new HashMap<>();
        patch.put("title", "updated");
        patch.put("inlineNestedEntities", Collections.emptyList());

        this.mockMvc.perform(
                patch("/parententity/" + savedParent.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(0, nested.size());
    }

    @Test
    public void givenParent_whenPatchParentWithExistingNested_shouldOverwriteNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        InlineNestedEntity nestedUpdate = savedParent.getInlineNestedEntities().get(0);
        nestedUpdate.setName("nested update");
        HashMap<String, Object> patch = new HashMap<>();
        patch.put("title", "updated");
        patch.put("inlineNestedEntities", Collections.singletonList(nestedUpdate));

        this.mockMvc.perform(
                patch("/parententity/" + savedParent.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(1, nested.size());
        Assertions.assertEquals(nestedUpdate.getName(), nested.get(0).getName());
    }

    @Test
    public void givenParent_whenPatchParentWithNewNested_shouldError() {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        InlineNestedEntity newNested = new InlineNestedEntity("overwrite", savedParent.getId());
        HashMap<String, Object> patch = new HashMap<>();
        patch.put("title", "updated");
        patch.put("inlineNestedEntities", Collections.singletonList(newNested));

        Assertions.assertThrows(Exception.class, () -> {
            this.mockMvc.perform(
                    patch("/parententity/" + savedParent.getId())
                            .content(asJsonString(patch))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful());
        });

        //nested entities unchanged
        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().size(), nested.size());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(0).getName(), nested.get(0).getName());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(1).getName(), nested.get(1).getName());
    }

    @Test
    public void givenParentWithNested_whenPatchOtherField_shouldNotRemoveNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        HashMap<String, Object> patch = new HashMap<>();
        patch.put("title", "updated");

        this.mockMvc.perform(
                patch("/parententity/" + savedParent.getId())
                        .content(asJsonString(patch))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().size(), nested.size());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(0).getName(), nested.get(0).getName());
        Assertions.assertEquals(mockParentEntity1.getInlineNestedEntities().get(1).getName(), nested.get(1).getName());
    }

    @Test
    public void givenParentWithNested_whenDeleteParent_shouldDeletedNested() throws Exception {
        ParentEntity savedParent = parentRepository.save(mockParentEntity1);

        this.mockMvc.perform(
                delete("/parententity/" + savedParent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

        List<InlineNestedEntity> nested = inlineNestedEntityRepository.findAllByParentEntityId(savedParent.getId());
        Assertions.assertEquals(0, nested.size());
    }
}