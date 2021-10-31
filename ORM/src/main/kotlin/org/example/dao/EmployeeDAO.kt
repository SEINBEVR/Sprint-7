package org.example.dao

import org.example.entities.Employee
import org.hibernate.SessionFactory

class EmployeeDAO(private val sessionFactory: SessionFactory) {

    fun save(employee: Employee) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            session.save(employee)
            session.transaction.commit()
        }
    }

    fun find(id: Long): Employee? {
        val result: Employee?
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.get(Employee::class.java, id)
            session.transaction.commit()
        }
        return result
    }

    fun delete(id: Long) {
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val emp = find(id)
            emp!!.department = null
            session.remove(emp)
            session.transaction.commit()
        }
    }

    fun findAll(): List<Employee> {
        val result: List<Employee>
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            result = session.createQuery("from Employee").list() as List<Employee>
            session.transaction.commit()
        }
        return result
    }
}