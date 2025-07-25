package com.example.apesc.model;

import com.example.apesc.model.enums.Genres;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "gender")
    private Genres gender;

    @Column(name = "email")
    private String email;

}
