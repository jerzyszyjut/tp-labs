package com.lab4.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Tower {
    @Id
    private String name;

    private int height;

    @OneToMany(mappedBy = "tower", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Mage> mages;

    public Tower() {
    }

    public Tower(String name, int height) {
        this.name = name;
        this.height = height;
        this.mages = new ArrayList<Mage>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void addMage(Mage mage) {
        mages.add(mage);
    }

    public void removeMage(Mage mage) {
        mages.remove(mage);
    }

    public void clearMages() {
        mages.clear();
    }

    @Override
    public String toString() {
        return "Tower{" +
                "name='" + name + '\'' +
                ", height=" + height +
                ", mages=" + mages +
                '}';
    }
}
