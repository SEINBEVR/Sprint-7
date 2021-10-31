package org.example.entities

import javax.persistence.*

@Entity
class Department(
    @Id
    @GeneratedValue
    var id: Long = 0,

    var name: String,
) {
    override fun toString(): String {
        return "Department(id=$id, name='$name')"
    }
}