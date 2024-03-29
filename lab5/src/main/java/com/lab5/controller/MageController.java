package com.lab5.controller;

import com.lab5.model.Mage;
import com.lab5.repository.MageRepository;

import java.util.Optional;

public class MageController {
    private final MageRepository repository;
    public MageController(MageRepository repository) {
        this.repository = repository;
    }
    public String find(String name) {
        Optional<Mage> mage = repository.find(name);
        if (mage.isPresent()) {
            return mage.get().toString();
        } else {
            return MageControllerStatus.NOT_FOUND.toString();
        }
    }
    public String delete(String name) {
        try {
            repository.delete(name);
            return MageControllerStatus.DONE.toString();
        } catch (IllegalArgumentException e) {
            return MageControllerStatus.NOT_FOUND.toString();
        }
    }
    public String save(String name, int level) {
        try {
            repository.save(new Mage(name, level));
            return MageControllerStatus.DONE.toString();
        } catch (IllegalArgumentException e) {
            return MageControllerStatus.BAD_REQUEST.toString();
        }
    }
}
