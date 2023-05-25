package com.project.SpringTGBot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name = "tg_data") //привязываемся к существующей таблице с готовыми колонками
public class User {

    @Id
    private long id; //BigInt
    @NotNull
    private String name; //Text
    @NotNull
    private int msg_numb; //Integer
}