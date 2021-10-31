package org.example.entities

import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Employee(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var fullname: String,

    @OneToOne(cascade = [CascadeType.ALL])
    var address: Address,

    var salary: Int,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "department_id")
    var department: Department?,

    @CreationTimestamp
    var createdTime: LocalDateTime? = null
) {
    override fun toString(): String {
        return "Employee(id=$id, fullname='$fullname', address=$address, salary=$salary, department=$department, createdTime=$createdTime)"
    }
}

