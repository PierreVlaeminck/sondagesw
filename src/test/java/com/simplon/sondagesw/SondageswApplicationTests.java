package com.simplon.sondagesw;

import com.simplon.sondagesw.dao.impl.SondagesRepository;
import com.simplon.sondagesw.entity.Sondages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SondageswApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SondageswApplicationTests {

    // Permet de reprendre le port par défaut défini en local pour le serveur à savoir ici 8080.
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SondagesRepository repository;

    /**
     * This method is executed before each @Test method, and it removes all data from the database.
     *
     * @BeforeEach public void setUp()
     */
    @BeforeEach
    public void setUp() {
        repository.deleteAll();
    }

    /**
     * This method creates dummy survey data and tests if the retrieval of all surveys present in the database works.
     *
     * @GetMapping("/rest/sondages") @Test public void testGetSondages()
     */
    @GetMapping("/rest/sondages")
    @Test
    public void testGetSondages() {
        // Create dummy surveys to test
        Sondages sondage1 = new Sondages("Sondage 1", "Description du sondage 1", LocalDate.now(), LocalDate.now().plusDays(7), "Jean");
        Sondages sondage2 = new Sondages("Sondage 2", "Description du sondage 2", LocalDate.now(), LocalDate.now().plusDays(7), "Pierre");
        repository.saveAll(List.of(sondage1, sondage2));

        // Send a GET request to /rest/sondages and retrieve the response
        ResponseEntity<Sondages[]> responseEntity = restTemplate.getForEntity("http://localhost:" + port + "/rest/sondages", Sondages[].class);
        Sondages[] sondages = responseEntity.getBody();

        // Verify that the response contains the two created surveys
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    /**
     * This method checks if adding a survey works correctly in the database.
     *
     * @PostMapping("/rest/sondages") @Test void testAddSondages()
     */
    @PostMapping("/rest/sondages")
    @Test
    void testAddSondages() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Sondages sondages = new Sondages("Test", "Test", LocalDate.now(), LocalDate.now().plusDays(7), "Test");
        HttpEntity<Sondages> request = new HttpEntity<>(sondages, headers);

        ResponseEntity<Sondages> response = restTemplate.postForEntity("http://localhost:" + port + "/rest/sondages", request, Sondages.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("Test", response.getBody().getDescription());
    }

    /**
     * This method creates a survey in the database for testing, and then tests if retrieving a survey from the list works.
     *
     * @GetMapping("/rest/sondages/{id}") @Test void testGetSondage()
     */
    @GetMapping("/rest/sondages/{id}")
    @Test
    void testGetSondage() {
        RestTemplate restTemplate = new RestTemplate();

        Sondages sondages = new Sondages("Test", "Test", LocalDate.now(), LocalDate.now().plusDays(7), "Test");
        Sondages savedSondages = repository.save(sondages);

        ResponseEntity<Sondages> response = restTemplate.getForEntity("http://localhost:" + port + "/rest/sondages/"  + savedSondages.getId(), Sondages.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test", response.getBody().getNom());
    }

    /**
     * This method creates a survey in the database for testing, and then tests if deletion works correctly.
     *
     * @DeleteMapping("/rest/sondages/{id}") @Test void testDelSondage()
     */
    @DeleteMapping("/rest/sondages/{id}")
    @Test
    void testDelSondage() {
        RestTemplate restTemplate = new RestTemplate();

        //Add a test survey
        Sondages sondages = new Sondages("Test", "Test", LocalDate.now(), LocalDate.now().plusDays(7), "Test");
        Sondages savedSondages = repository.save(sondages);

        //Delete the test survey
        restTemplate.delete("http://localhost:" + port + "/rest/sondages/" + savedSondages.getId());

        //Verify that the survey no longer exists
        Optional<Sondages> deletedSondages = repository.findById(savedSondages.getId());
        assertFalse(deletedSondages.isPresent());
    }

    /**
     * PUT request to the specified endpoint.
     *
     * The ID of the Sondages object to be updated.
     */
    @PutMapping("/rest/sondages/{id}")
    @Test
    void testUpdateSondage() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //Add a test survey
        Sondages sondages = new Sondages("Test", "Test", LocalDate.now(), LocalDate.now().plusDays(7), "Test");
        Sondages savedSondages = repository.save(sondages);

        //Modify the test survey
        savedSondages.setDescription("Test modifié");
        HttpEntity<Sondages> request = new HttpEntity<>(savedSondages, headers);
        restTemplate.put("http://localhost:" + port + "/rest/sondages/" + savedSondages.getId(), request, Sondages.class);

        //Verify that the survey has been modified
        Optional<Sondages> updatedSondages = repository.findById(savedSondages.getId());
        assertTrue(updatedSondages.isPresent());
        assertEquals("Test modifié", updatedSondages.get().getDescription());
    }
}