package com.example.springdatajpa.repository

import com.example.springdatajpa.entities.Employee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository: CrudRepository<Employee, Long> {

    fun findByDepartmentId(id: Long): List<Employee>
}