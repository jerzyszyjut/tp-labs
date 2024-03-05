package com.lab1;

import java.util.Map;
import java.util.Set;

public class Mage implements Comparable<Mage> {
    final String name;
    final int level;
    final double power;
    Set<Mage> apprentices;

    public Mage(String name, int level, double power, boolean isSorted, MageComparator comparator) {
        this.name = name;
        this.level = level;
        this.power = power;

        if (isSorted) {
            if (comparator != null) {
                this.apprentices = new java.util.TreeSet<>(comparator);
            }
            else {
                this.apprentices = new java.util.TreeSet<>();
            }
        }
        else {
            this.apprentices = new java.util.HashSet<>();
        }
    }

    public Integer getNumberOfApprentices(Map<Mage, Integer> map) {
        Integer apprenticesCount = 0;
        for (Mage apprentice : apprentices) {
            apprenticesCount++;
            apprenticesCount += apprentice.getNumberOfApprentices(map);
        }

        map.put(this, apprenticesCount);

        return apprenticesCount;
    }

    public String toString() {
        return "Mage{name='" + name + "', level=" + level + ", power=" + power + "}";
    }

    public void addApprentice(Mage apprentice) {
        apprentices.add(apprentice);
    }

    public void print(int depth) {
        System.out.println("-".repeat(depth) + this);
        for (Mage apprentice : apprentices) {
            apprentice.print(depth + 1);
        }
    }

    @Override
    public int hashCode() {
        return (31 * name.hashCode() + 67 * level + 97 * (int) power) * 2137;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Mage mage = (Mage) object;

        if (level != mage.level) {
            return false;
        }
        if (Double.compare(mage.power, power) != 0) {
            return false;
        }
        return name.equals(mage.name);
    }

    @Override
    public int compareTo(Mage object) {
        if (this.name.equals(object.name)) {
            if (this.level == object.level) {
                return Double.compare(this.power, object.power);
            }
            else {
                return Integer.compare(this.level, object.level);
            }
        }
        else {
            return this.name.compareTo(object.name);
        }
    }
}