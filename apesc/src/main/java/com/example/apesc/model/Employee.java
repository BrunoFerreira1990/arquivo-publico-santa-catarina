package com.example.apesc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee")
public class Employee extends Person{

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "position")
    private String position;

    @Column(name = "section")
    private String section;

}
