package com.lab1;

public class Main {
    public static void main(String[] args) {
        boolean isSorted;
        MageComparator comparator;

        if (args.length == 0) {
            isSorted = false;
            comparator = null;
        }
        else if (args[0].equals("name")) {
            isSorted = true;
            comparator = null;
        }
        else if (args[0].equals("level")) {
            isSorted = true;
            comparator = new MageComparator();
        }
        else {
            isSorted = false;
            comparator = null;
        }

        Mage mage1 = new Mage("Mage1", 1, 1.0, isSorted, comparator);
        Mage mage2 = new Mage("Mage2", 19, 2.0, isSorted, comparator);
        Mage mage3 = new Mage("Mage3", 19, 3.0, isSorted, comparator);
        Mage mage4 = new Mage("Mage4", 4, 4.0, isSorted, comparator);
        Mage mage5 = new Mage("Mage5", 5, 5.0, isSorted, comparator);
        Mage mage6 = new Mage("Mage6", 6, 6.0, isSorted, comparator);
        Mage mage7 = new Mage("Mage7", 7, 7.0, isSorted, comparator);
        Mage mage8 = new Mage("Mage8", 8, 8.0, isSorted, comparator);
        Mage mage9 = new Mage("Mage9", 9, 9.0, isSorted, comparator);
        Mage mage10 = new Mage("Mage10", 19, 10.0, isSorted, comparator);

        mage1.apprentices.add(mage2);
        mage2.apprentices.add(mage3);
        mage3.apprentices.add(mage6);
        mage3.apprentices.add(mage5);
        mage3.apprentices.add(mage4);
        mage2.apprentices.add(mage7);
        mage2.apprentices.add(mage8);
        mage1.apprentices.add(mage9);
        mage1.apprentices.add(mage10);

        mage1.print(0);
    }
}