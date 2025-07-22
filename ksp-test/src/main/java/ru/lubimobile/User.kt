package ru.lubimobile

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import ru.lubimobile.annotation.ColumnName
import java.time.LocalDate

@Table
data class User(
    @Id
    val id: Int,
    val dt: LocalDate,
    val name: String,
    val weight: Double?,

    @ColumnName(name = "first_name")
    @Column(name = "first_name", nullable = false)
    val firstName: String,

    @Column(unique = true)
    @GeneratedValue
    override val ver: Int
) : Versioning


interface Versioning {
    val ver: Int
}