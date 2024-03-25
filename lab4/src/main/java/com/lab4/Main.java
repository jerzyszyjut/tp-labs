package com.lab4;

import com.lab4.model.Mage;
import com.lab4.model.Tower;

public class Main 
{
    public static void main( String[] args )
    {
        Database db = new Database();

        Tower tower_1 = new Tower("Tower of the Magi", 100);
        Mage mage_1 = new Mage("Gandalf", 100, tower_1);
        Mage mage_2 = new Mage("Saruman", 90, tower_1);
        Mage mage_3 = new Mage("Radagast", 80, tower_1);

        Tower tower_2 = new Tower("Tower of the Necromancer", 50);
        Mage mage_4 = new Mage("Sauron", 100, tower_2);
        Mage mage_5 = new Mage("Nazgul", 90, tower_2);
        Mage mage_6 = new Mage("Witch King", 80, tower_2);

        db.insertTower(tower_1);
        db.insertTower(tower_2);

        db.getAllMagesWithPowerHigherThan(90);
        db.getAllTowersWithHeightHigherThan(50);
        db.getAllMagesFromTowerHigherThan(tower_1.getName(), 90);

        db.dumpDatabase();

        db.shutdown();
    }
}
