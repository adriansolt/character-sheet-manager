package com.adi.cms.character.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.adi.cms.character.IntegrationTest;
import com.adi.cms.character.domain.Character;
import com.adi.cms.character.domain.User;
import com.adi.cms.character.domain.enumeration.Handedness;
import com.adi.cms.character.repository.CharacterRepository;
import com.adi.cms.character.service.dto.CharacterDTO;
import com.adi.cms.character.service.mapper.CharacterMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CharacterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CharacterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Handedness DEFAULT_HANDEDNESS = Handedness.RIGHT;
    private static final Handedness UPDATED_HANDEDNESS = Handedness.LEFT;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/characters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private CharacterMapper characterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCharacterMockMvc;

    private Character character;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createEntity(EntityManager em) {
        Character character = new Character()
            .name(DEFAULT_NAME)
            .weight(DEFAULT_WEIGHT)
            .height(DEFAULT_HEIGHT)
            .points(DEFAULT_POINTS)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .handedness(DEFAULT_HANDEDNESS)
            .campaignId(DEFAULT_CAMPAIGN_ID)
            .active(DEFAULT_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        character.setUser(user);
        return character;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Character createUpdatedEntity(EntityManager em) {
        Character character = new Character()
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .active(UPDATED_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        character.setUser(user);
        return character;
    }

    @BeforeEach
    public void initTest() {
        character = createEntity(em);
    }

    @Test
    @Transactional
    void createCharacter() throws Exception {
        int databaseSizeBeforeCreate = characterRepository.findAll().size();
        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);
        restCharacterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate + 1);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(DEFAULT_HANDEDNESS);
        assertThat(testCharacter.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
        assertThat(testCharacter.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createCharacterWithExistingId() throws Exception {
        // Create the Character with an existing ID
        character.setId(1L);
        CharacterDTO characterDTO = characterMapper.toDto(character);

        int databaseSizeBeforeCreate = characterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCharacterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setName(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        restCharacterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setWeight(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        restCharacterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setHeight(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        restCharacterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = characterRepository.findAll().size();
        // set the field null
        character.setPoints(null);

        // Create the Character, which fails.
        CharacterDTO characterDTO = characterMapper.toDto(character);

        restCharacterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCharacters() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        // Get all the characterList
        restCharacterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(character.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].handedness").value(hasItem(DEFAULT_HANDEDNESS.toString())))
            .andExpect(jsonPath("$.[*].campaignId").value(hasItem(DEFAULT_CAMPAIGN_ID.intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        // Get the character
        restCharacterMockMvc
            .perform(get(ENTITY_API_URL_ID, character.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(character.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.handedness").value(DEFAULT_HANDEDNESS.toString()))
            .andExpect(jsonPath("$.campaignId").value(DEFAULT_CAMPAIGN_ID.intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCharacter() throws Exception {
        // Get the character
        restCharacterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character
        Character updatedCharacter = characterRepository.findById(character.getId()).get();
        // Disconnect from session so that the updates on updatedCharacter are not directly saved in db
        em.detach(updatedCharacter);
        updatedCharacter
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .active(UPDATED_ACTIVE);
        CharacterDTO characterDTO = characterMapper.toDto(updatedCharacter);

        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testCharacter.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testCharacter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, characterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter.handedness(UPDATED_HANDEDNESS).campaignId(UPDATED_CAMPAIGN_ID);

        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testCharacter.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testCharacter.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateCharacterWithPatch() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeUpdate = characterRepository.findAll().size();

        // Update the character using partial update
        Character partialUpdatedCharacter = new Character();
        partialUpdatedCharacter.setId(character.getId());

        partialUpdatedCharacter
            .name(UPDATED_NAME)
            .weight(UPDATED_WEIGHT)
            .height(UPDATED_HEIGHT)
            .points(UPDATED_POINTS)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .handedness(UPDATED_HANDEDNESS)
            .campaignId(UPDATED_CAMPAIGN_ID)
            .active(UPDATED_ACTIVE);

        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCharacter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCharacter))
            )
            .andExpect(status().isOk());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
        Character testCharacter = characterList.get(characterList.size() - 1);
        assertThat(testCharacter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharacter.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testCharacter.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testCharacter.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testCharacter.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testCharacter.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testCharacter.getHandedness()).isEqualTo(UPDATED_HANDEDNESS);
        assertThat(testCharacter.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
        assertThat(testCharacter.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, characterDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCharacter() throws Exception {
        int databaseSizeBeforeUpdate = characterRepository.findAll().size();
        character.setId(count.incrementAndGet());

        // Create the Character
        CharacterDTO characterDTO = characterMapper.toDto(character);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCharacterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(characterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Character in the database
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCharacter() throws Exception {
        // Initialize the database
        characterRepository.saveAndFlush(character);

        int databaseSizeBeforeDelete = characterRepository.findAll().size();

        // Delete the character
        restCharacterMockMvc
            .perform(delete(ENTITY_API_URL_ID, character.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Character> characterList = characterRepository.findAll();
        assertThat(characterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
