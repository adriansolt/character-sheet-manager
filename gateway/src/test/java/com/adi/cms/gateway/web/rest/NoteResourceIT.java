package com.adi.cms.gateway.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.adi.cms.gateway.IntegrationTest;
import com.adi.cms.gateway.domain.Note;
import com.adi.cms.gateway.repository.EntityManager;
import com.adi.cms.gateway.repository.NoteRepository;
import com.adi.cms.gateway.service.dto.NoteDTO;
import com.adi.cms.gateway.service.mapper.NoteMapper;
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

/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class NoteResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Note note;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity(EntityManager em) {
        Note note = new Note().description(DEFAULT_DESCRIPTION);
        return note;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        Note note = new Note().description(UPDATED_DESCRIPTION);
        return note;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Note.class).block();
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
        note = createEntity(em);
    }

    @Test
    void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().collectList().block().size();
        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    void createNoteWithExistingId() throws Exception {
        // Create the Note with an existing ID
        note.setId(1L);
        NoteDTO noteDTO = noteMapper.toDto(note);

        int databaseSizeBeforeCreate = noteRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllNotes() {
        // Initialize the database
        noteRepository.save(note).block();

        // Get all the noteList
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
            .value(hasItem(note.getId().intValue()))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNote() {
        // Initialize the database
        noteRepository.save(note).block();

        // Get the note
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, note.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(note.getId().intValue()))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION));
    }

    @Test
    void getNonExistingNote() {
        // Get the note
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewNote() throws Exception {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).block();
        updatedNote.description(UPDATED_DESCRIPTION);
        NoteDTO noteDTO = noteMapper.toDto(updatedNote);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, noteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void putNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(count.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, noteDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(count.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(count.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        partialUpdatedNote.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void fullUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        partialUpdatedNote.description(UPDATED_DESCRIPTION);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedNote.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedNote))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    void patchNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(count.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, noteDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(count.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().collectList().block().size();
        note.setId(count.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(noteDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteNote() {
        // Initialize the database
        noteRepository.save(note).block();

        int databaseSizeBeforeDelete = noteRepository.findAll().collectList().block().size();

        // Delete the note
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, note.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll().collectList().block();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
