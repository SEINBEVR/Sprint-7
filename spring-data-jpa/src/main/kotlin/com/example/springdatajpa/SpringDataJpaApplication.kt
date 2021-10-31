package com.example.springdatajpa

import com.example.springdatajpa.entities.Address
import com.example.springdatajpa.entities.Department
import com.example.springdatajpa.entities.Employee
import com.example.springdatajpa.repository.EmployeeRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SpringDataJpaApplication(
    private val employeeRepository: EmployeeRepository
): CommandLineRunner {
    override fun run(vararg args: String?) {
        val dep1 = Department(name = "HR")
        val dep2 = Department(name = "IT")

        val emp1 = Employee(
            fullname = "Ivanov Ivan",
            address = Address(address = "Pushkina"),
            salary = 60000, department = dep1)
        val emp2 = Employee(
            fullname = "Petrov Petr",
            address = Address(address = "Centrslnaya"),
            salary = 80000, department = dep2)
        val emp3 = Employee(
            fullname = "Annikova Anna",
            address = Address(address = "Ugnaya"),
            salary = 85000, department = dep2)

        employeeRepository.saveAll(listOf(emp1, emp2, emp3))

        var emps = employeeRepository.findAll()
        val empsFromIT = employeeRepository.findByDepartmentId(dep2.id)

        println("Список всех сотрудников: $emps")
        println("Список сотрудников из отдела IT: $empsFromIT")

        emp3.department = null
        employeeRepository.delete(emp3)
        emps = employeeRepository.findAll()
        println("Cписок сотрудников после увольнения Анны: $emps")

        emp2.salary = 120000
        employeeRepository.save(emp2)
        println("Данные по Пётру после повышения зарплаты: ${employeeRepository.findById(emp2.id)}")
    }
}

fun main(args: Array<String>) {
    runApplication<SpringDataJpaApplication>(*args)
}
