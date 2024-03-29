package com.lab5.repository;

import com.lab5.model.Mage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class MageRepository {
    private final Collection<Mage> collection;

    public MageRepository() {
        this.collection = new ArrayList<>();
    }
    public Optional<Mage> find(String name) {
        for (Mage m : collection) {
            if (m.getName().equals(name)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
    }

    public void delete(String name) throws IllegalArgumentException {
        for (Mage m : collection) {
            if (m.getName().equals(name)) {
                collection.remove(m);
                return;
            }
        }
        throw new IllegalArgumentException("Mage with name " + name + " not found");
    }

    public void save(Mage mage) throws IllegalArgumentException {
        for (Mage m : collection) {
            if (m.getName().equals(mage.getName())) {
                throw new IllegalArgumentException("Mage with name " + mage.getName() + " already exists");
            }
        }
        collection.add(mage);
    }
}
