package com.lab5.controller;

import com.lab5.model.Mage;
import com.lab5.repository.MageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MageControllerTest {

    private MageController mageController;

    @Mock
    private MageRepository mockedRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mageController = new MageController(mockedRepository);
    }

    @Test
    void testDelete_MageDeleted() {
        when(mockedRepository.find("Gandalf")).thenReturn(Optional.of(new Mage("Gandalf", 10)));
        assertThat(mageController.delete("Gandalf")).isEqualTo(MageControllerStatus.DONE.toString());
        verify(mockedRepository, times(1)).delete("Gandalf");
    }

    @Test
    void testDelete_MageNotFound() {
        doThrow(new IllegalArgumentException()).when(mockedRepository).delete("Gandalf");
        assertThat(mageController.delete("Gandalf")).isEqualTo(MageControllerStatus.NOT_FOUND.toString());
    }

    @Test
    void testFind_MageNotFound() {
        when(mockedRepository.find("Gandalf")).thenReturn(Optional.empty());
        assertThat(mageController.find("Gandalf")).isEqualTo(MageControllerStatus.NOT_FOUND.toString());
    }

    @Test
    void testFind_MageFound() {
        Mage mage = new Mage("Merlin", 10);
        when(mockedRepository.find("Merlin")).thenReturn(Optional.of(mage));
        assertThat(mageController.find("Merlin")).isEqualTo(mage.toString());
    }

    @Test
    void testSave_ValidInput() {
        assertThat(mageController.save("Gandalf", 10)).isEqualTo(MageControllerStatus.DONE.toString());
        verify(mockedRepository, times(1)).save(new Mage("Gandalf", 10));
    }

    @Test
    void testSave_DuplicateMage() {
        doThrow(new IllegalArgumentException()).when(mockedRepository).save(new Mage("Gandalf", 10));
        assertThat(mageController.save("Gandalf", 10)).isEqualTo(MageControllerStatus.BAD_REQUEST.toString());
    }
}
