package ru.lubimobile;

import ru.lubimobile.annotation.Id;
import ru.lubimobile.annotation.Table;

import java.time.LocalDateTime;

@Table(name = "spr_credentials")
public class Credentials {
    @Id
    int id;

    String passwordHash;

    LocalDateTime createAt;

    Integer accountId = null;
}
