package com.lab1;

import java.util.Comparator;

public class MageComparator implements Comparator<Mage> {
    @Override
    public int compare(Mage mage1, Mage mage2) {
        if (mage1.level != mage2.level) {
            return mage1.level - mage2.level;
        }
        if (mage1.name.compareTo(mage2.name) != 0)
        {
            return mage1.name.compareTo(mage2.name);

        }
        return (int) (mage1.power - mage2.power);
    }
}
