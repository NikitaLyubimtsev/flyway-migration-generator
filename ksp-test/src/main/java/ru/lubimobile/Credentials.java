package ru.lubimobile;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import ru.lubimobile.annotation.ColumnName;

import java.time.LocalDateTime;


@Table(name = "spr_credentials")
public class Credentials {
    @Id
    int id;

    @Column(name = "password_hash", nullable = false)
    String passwordHash;

    @Column(name = "create_at", nullable = false)
    LocalDateTime createAt;

    @ColumnName(name = "account_id")
    Integer accountId = null;
}
