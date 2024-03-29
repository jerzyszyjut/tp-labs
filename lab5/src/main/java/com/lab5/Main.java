package com.lab5;

import com.lab5.controller.MageController;
import com.lab5.repository.MageRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MageRepository repository = new MageRepository();
        MageController controller = new MageController(repository);

        label:
        while (true) {
            String[] command = scanner.nextLine().split(" ");
            switch (command[0]) {
                case "find":
                    System.out.println(controller.find(command[1]));
                    break;
                case "delete":
                    System.out.println(controller.delete(command[1]));
                    break;
                case "save":
                    System.out.println(controller.save(command[1], Integer.parseInt(command[2])));
                    break;
                case "exit":
                    break label;
            }
        }

        scanner.close();
    }
}
