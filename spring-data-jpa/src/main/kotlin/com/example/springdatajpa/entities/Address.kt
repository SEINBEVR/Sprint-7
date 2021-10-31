package com.example.springdatajpa.entities

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Address(

    @Id
    @GeneratedValue
    var id: Long = 0,

    var address: String
)