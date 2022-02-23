package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Item;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.ItemRepository;
import com.adi.cms.gateway.service.dto.ItemDTO;
import com.adi.cms.gateway.service.mapper.ItemMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Integer DEFAULT_QUALITY = 1;
    private static final Integer UPDATED_QUALITY = 2;

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final Long DEFAULT_CHARACTER_ID = 1L;
    private static final Long UPDATED_CHARACTER_ID = 2L;

    private static final Long DEFAULT_CAMPAIGN_ID = 1L;
    private static final Long UPDATED_CAMPAIGN_ID = 2L;

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .weight(DEFAULT_WEIGHT)
            .quality(DEFAULT_QUALITY)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .characterId(DEFAULT_CHARACTER_ID)
            .campaignId(DEFAULT_CAMPAIGN_ID);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity(EntityManager em) {
        Item item = new Item()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);
        return item;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Item.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void setupCsrf() {
        webTestClient = webTestClient.mutateWith(csrf());
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        item = createEntity(em);
    }

    @Test
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().collectList().block().size();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testItem.getQuality()).isEqualTo(DEFAULT_QUALITY);
        assertThat(testItem.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testItem.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testItem.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testItem.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
    }

    @Test
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId(1L);
        ItemDTO itemDTO = itemMapper.toDto(item);

        int databaseSizeBeforeCreate = itemRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().collectList().block().size();
        // set the field null
        item.setName(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkWeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().collectList().block().size();
        // set the field null
        item.setWeight(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkQualityIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().collectList().block().size();
        // set the field null
        item.setQuality(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllItems() {
        // Initialize the database
        itemRepository.save(item).block();

        // Get all the itemList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(item.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].weight")
            .value(hasItem(DEFAULT_WEIGHT))
            .jsonPath("$.[*].quality")
            .value(hasItem(DEFAULT_QUALITY))
            .jsonPath("$.[*].pictureContentType")
            .value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.[*].picture")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.[*].characterId")
            .value(hasItem(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.[*].campaignId")
            .value(hasItem(DEFAULT_CAMPAIGN_ID.intValue()));
    }

    @Test
    void getItem() {
        // Initialize the database
        itemRepository.save(item).block();

        // Get the item
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, item.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(item.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.weight")
            .value(is(DEFAULT_WEIGHT))
            .jsonPath("$.quality")
            .value(is(DEFAULT_QUALITY))
            .jsonPath("$.pictureContentType")
            .value(is(DEFAULT_PICTURE_CONTENT_TYPE))
            .jsonPath("$.picture")
            .value(is(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .jsonPath("$.characterId")
            .value(is(DEFAULT_CHARACTER_ID.intValue()))
            .jsonPath("$.campaignId")
            .value(is(DEFAULT_CAMPAIGN_ID.intValue()));
    }

    @Test
    void getNonExistingItem() {
        // Get the item
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewItem() throws Exception {
        // Initialize the database
        itemRepository.save(item).block();

        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).block();
        updatedItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, itemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testItem.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testItem.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testItem.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testItem.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testItem.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
    }

    @Test
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, itemDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.save(item).block();

        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.quality(UPDATED_QUALITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testItem.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testItem.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testItem.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testItem.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testItem.getCharacterId()).isEqualTo(DEFAULT_CHARACTER_ID);
        assertThat(testItem.getCampaignId()).isEqualTo(DEFAULT_CAMPAIGN_ID);
    }

    @Test
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        itemRepository.save(item).block();

        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .weight(UPDATED_WEIGHT)
            .quality(UPDATED_QUALITY)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .characterId(UPDATED_CHARACTER_ID)
            .campaignId(UPDATED_CAMPAIGN_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedItem.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testItem.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testItem.getQuality()).isEqualTo(UPDATED_QUALITY);
        assertThat(testItem.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testItem.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testItem.getCharacterId()).isEqualTo(UPDATED_CHARACTER_ID);
        assertThat(testItem.getCampaignId()).isEqualTo(UPDATED_CAMPAIGN_ID);
    }

    @Test
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, itemDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().collectList().block().size();
        item.setId(count.incrementAndGet());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(itemDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteItem() {
        // Initialize the database
        itemRepository.save(item).block();

        int databaseSizeBeforeDelete = itemRepository.findAll().collectList().block().size();

        // Delete the item
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, item.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll().collectList().block();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
