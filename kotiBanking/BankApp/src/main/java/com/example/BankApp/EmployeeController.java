package com.example.BankApp;


import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class EmployeeController implements CommandLineRunner {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("""
                    \n1. Create Account
                    2. Display Details
                    3. Raise Salary
                    4. Exit
                    """);

                System.out.print("Enter choice: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> create(sc);
                    case 2 -> display();
                    case 3 -> raiseSalary(sc);
                    case 4 -> {
                        System.out.println("Exiting application...");
                        System.out.println("Thank you for using application.");
                        return;
                    }
                    default -> System.out.println("Invalid choice");
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }


    private void create(Scanner sc) {
        while (true) {
            try {
                System.out.print("Name: ");
                String name = sc.nextLine().trim();

                if (!isValidName(name)) {
                    System.out.println("Name cannot have more than 2 blank spaces.");
                    continue;
                }

                System.out.print("Age: ");
                int age = Integer.parseInt(sc.nextLine());

                if (!isValidAge(age)) {
                    System.out.println("Age must be between 20 and 60.");
                    continue;
                }

                System.out.print("Designation (P/M/T): ");
                char des = sc.nextLine().toUpperCase().charAt(0);

                if (des != 'P' && des != 'M' && des != 'T') {
                    System.out.println("Invalid designation. Allowed: P, M, T.");
                    continue;
                }

                service.createEmployee(name, age, des);

                System.out.print("y to continue, n for menu: ");
                String cont = sc.nextLine();

                if (!cont.equalsIgnoreCase("y")) {
                    return;
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric age.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }


    private void display() {
		if (!service.getAllEmployees().iterator().hasNext()) {
			System.out.println("No employees found.");
			return;
		}
        for (Employee e : service.getAllEmployees()) {
            System.out.println(
                e.getName() + " | " +
                e.getAge() + " | " +
                e.getDesignation() + " | " +
                e.getSalary()
            );
        }
    }

    private void raiseSalary(Scanner sc) {
        System.out.print("Enter name: ");
        String name = sc.nextLine();

        var optionalEmp = service.findByName(name);

        if (optionalEmp.isEmpty()) {
            System.out.println("Employee with name '" + name + "' not found.");
            return;
        }

        int percent;
        while (true) {
            try {
                System.out.print("Enter percentage (1-10): ");
                percent = Integer.parseInt(sc.nextLine());

                if (percent < 1 || percent > 10) {
                    System.out.println("Percentage must be between 1 and 10.");
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        service.updateSalary(optionalEmp.get(), percent);
        System.out.println("Salary updated successfully.");
    }
    
    
    private boolean isValidName(String name) {
        long spaceCount = name.chars().filter(ch -> ch == ' ').count();
        return spaceCount <= 2;
    }

    private boolean isValidAge(int age) {
        return age >= 20 && age <= 60;
    }

}
