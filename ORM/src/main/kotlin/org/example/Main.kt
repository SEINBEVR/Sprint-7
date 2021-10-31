package org.example

import org.example.dao.EmployeeDAO
import org.example.entities.Address
import org.example.entities.Department
import org.example.entities.Employee
import org.hibernate.cfg.Configuration


fun main() {
    val sessionFactory = Configuration().configure()
        .addAnnotatedClass(Employee::class.java)
        .addAnnotatedClass(Department::class.java)
        .addAnnotatedClass(Address::class.java)
        .buildSessionFactory()

    sessionFactory.use { sessionFactory ->
        val dao = EmployeeDAO(sessionFactory)

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

        dao.save(emp1)
        dao.save(emp2)

        var found = dao.find(emp1.id)
        println("Найден работник: $found \n")

        found = dao.find(emp2.id)
        println("Найден работник: $found \n")

        var allEmps = dao.findAll()
        println("Все работники: $allEmps")

        dao.delete(emp2.id)

        allEmps = dao.findAll()
        println("Все работники: $allEmps")
    }
}