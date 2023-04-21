package com.simplon.sondagesw.controller;

import com.simplon.sondagesw.dao.impl.SondagesRepository;
import com.simplon.sondagesw.entity.Sondages;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * This class represents a REST controller for managing surveys in a database.
 */
@Valid
@RestController
public class SondagesController {

    private final SondagesRepository repo;

    /**
     * Constructs a new SondagesController instance.
     *
     * @param fr the SondagesRepository to use for data access
     */
    public SondagesController(SondagesRepository fr) {
        this.repo = fr;
    }

    /**
     * Retrieves all the surveys from the database.
     *
     * @return a List of Sondages objects representing all the surveys in the database
     */
    @GetMapping("/rest/sondages")
    public List<Sondages> getSondages() {
        return repo.findAll();
    }

    /**
     * Adds a new survey to the database.
     *
     * @param newSondages the Sondages object representing the new survey to add
     * @return the Sondages object representing the newly created survey in the database
     * @throws IllegalArgumentException if the length of the description is less than 3 characters or if the start date is before today's date
     */
    @PostMapping("/rest/sondages")
    public Sondages addSondages(@RequestBody @Valid Sondages newSondages) {
        // newSondages.setStartDate(LocalDate.now());
        return repo.save(newSondages);
    }

    /**
     * Retrieves a survey by its ID from the database.
     *
     * @param id the ID of the survey to retrieve
     * @return the Sondages object representing the survey with the given ID in the database
     */
    @GetMapping("/rest/sondages/{id}")
    public Sondages getSondage(@PathVariable long id) {
        return repo.findById(id).orElse(new Sondages("Inconnu", "Inconnu", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 2), "inconnu"));
    }

    /**
     * Deletes a survey by its ID from the database.
     *
     * @param id the ID of the survey to delete
     */
    @DeleteMapping("/rest/sondages/{id}")
    public void delSondage(@PathVariable long id) {
        repo.deleteById(id);
    }

    /**
     * Updates a survey in the database.
     *
     * @param newSondage the Sondages object representing the updated survey
     * @param id         the ID of the survey to update
     * @return the Sondages object representing the updated survey in the database
     * @throws IllegalArgumentException if the end date is before the start date
     */
    @PutMapping("/rest/sondages/{id}")
    public Sondages updateSondage(@RequestBody @Valid Sondages newSondage, @PathVariable long id) {
        return repo.findById(id)
                .map(sondages -> {
                    sondages.setDescription(newSondage.getDescription());
                    sondages.setQuestion(newSondage.getQuestion());
                    sondages.setStartDate(newSondage.getStartDate());
                    sondages.setEndDate(newSondage.getEndDate());
                    sondages.setNom(newSondage.getNom());
                    if (sondages.getEndDate().isBefore(sondages.getStartDate())) {
                        throw new IllegalArgumentException("La date de fin ne peut pas être avant la date de début.");
                    }
                    return repo.save(sondages);
                })
                .orElseGet(() -> {
                    newSondage.setId(id);
                    return repo.save(newSondage);
                });
    }
}
