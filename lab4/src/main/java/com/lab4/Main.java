package com.lab4;

import java.util.Scanner;

import com.lab4.model.Mage;
import com.lab4.model.Tower;

public class Main 
{
    public static void main( String[] args )
    {
        Database db = new Database();

        Tower tower_1 = new Tower("Tower of the Magi", 100);
        new Mage("Gandalf", 100, tower_1);
        new Mage("Saruman", 90, tower_1);
        new Mage("Radagast", 80, tower_1);

        Tower tower_2 = new Tower("Tower of the Necromancer", 50);
        new Mage("Sauron", 100, tower_2);
        new Mage("Nazgul", 90, tower_2);
        new Mage("Witch King", 80, tower_2);

        db.insertTower(tower_1);
        db.insertTower(tower_2);

        Scanner scanner = new Scanner(System.in);

        boolean queryMode = true;

        while (queryMode) {
            String input = scanner.nextLine().trim();

            if(input.equals("am"))
            {
                System.out.println("Tower :");
                String towerName = scanner.nextLine().trim();
                System.out.println("Name :");
                String name = scanner.nextLine().trim();
                System.out.println("Power :");
                int power = Integer.parseInt(scanner.nextLine().trim());
                Mage mage = new Mage(name, power, db.getTower(towerName));
                db.insertMage(mage);
            } else if (input.equals("at")) {
                System.out.println("Name :");
                String name = scanner.nextLine().trim();
                System.out.println("Height :");
                int height = Integer.parseInt(scanner.nextLine().trim());
                Tower tower = new Tower(name, height);
                db.insertTower(tower);
            } else if (input.equals("rm")) {
                System.out.println("Mage name :");
                String name = scanner.nextLine().trim();
                db.deleteMage(name);
            } else if (input.equals("rt")) {
                System.out.println("Tower name :");
                String name = scanner.nextLine().trim();
                db.deleteTower(name);
            } else if (input.equals("x"))
            {
                queryMode = false;
            }
            db.logDatabase();
        }

        scanner.close();

        System.out.println("Mages with power higher than 90");

        for(Mage mage : db.getAllMagesWithPowerHigherThan(90))
        {
            System.out.println(mage.toString());
        }

        System.out.println("Towers with height higher than 90");

        for(Tower tower : db.getAllTowersWithHeightHigherThan(90))
        {
            System.out.println(tower.toString());
        }

        System.out.println("Mages from " + tower_1.getName() + " with power higher than 90");

        for(Mage mage : db.getAllMagesFromTowerHigherThan(tower_1.getName(), 90))
        {
            System.out.println(mage.toString());
        }

        db.dumpDatabase();

        db.shutdown();
    }
}
