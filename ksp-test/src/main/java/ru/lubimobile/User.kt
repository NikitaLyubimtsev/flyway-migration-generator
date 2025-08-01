package ru.lubimobile

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import ru.lubimobile.annotation.Id
import java.time.LocalDate

@Table
data class User(
    @Id
    val id: Int,
    @Column("Now()")
    val dt: LocalDate,
    val name: String,
    val weight: Double?,

    @Column(name = "first_name")
    val firstName: String,

    @Column(unique = true)
    @GeneratedValue
    @SequenceGenerator(name = "user_version_generator", sequenceName = "version")
    override val ver: Int
) : Versioning


interface Versioning {
    val ver: Int
}