package com.example.BankApp;

import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void createEmployee(String name, int age, char designation) {
        double salary = switch (designation) {
            case 'P' -> 20000;
            case 'M' -> 25000;
            case 'T' -> 15000;
            default -> 0;
        };

        repository.save(new Employee(name, age, designation, salary));
    }

    public Iterable<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public boolean raiseSalary(String name, int percent) {
        Optional<Employee> optional = repository.findByName(name);

        if (optional.isEmpty()) {
            return false;
        }

        Employee emp = optional.get();
        double increment = emp.getSalary() * percent / 100;
        emp.setSalary(emp.getSalary() + increment);

        repository.save(emp);
        return true;
    }
    
    public Optional<Employee> findByName(String name) {
        return repository.findByName(name);
    }

    public void updateSalary(Employee emp, int percent) {
        double increment = emp.getSalary() * percent / 100;
        emp.setSalary(emp.getSalary() + increment);
        repository.save(emp);
    }

}

