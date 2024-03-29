package com.lab5.repository;

import com.lab5.model.Mage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class MageRepositoryTest {

    private MageRepository mageRepository;

    @BeforeEach
    void setUp() {
        mageRepository = new MageRepository();
    }

    @Test
    void testDelete_NotFound() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> mageRepository.delete("Gandalf"));
    }

    @Test
    void testFind_NotFound() {
        Optional<Mage> foundMage = mageRepository.find("Gandalf");
        assertThat(foundMage).isEmpty();
    }

    @Test
    void testSave_AlreadyExists() {
        Mage mage1 = new Mage("Merlin", 10);
        Mage mage2 = new Mage("Merlin", 20);
        mageRepository.save(mage1);
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> mageRepository.save(mage2))
                .withMessage("Mage with name Merlin already exists");
    }
}
