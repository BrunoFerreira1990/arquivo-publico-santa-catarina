package com.example.apesc.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Researcher extends Person {

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nacionality")
    private String nacionality;

    @Column(name = "number_phone")
    private String numberPhone;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "complement")
    private String complement;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "cep")
    private String cep;

    @Column(name = "education_level")
    private String educationLevel;

    @Column(name = "education_institution")
    private String educationInstitution;

    @Column(name = "course")
    private String course;

    @Column(name = "profession")
    private String profession;

    @Column(name = "research_subject")
    private String researchSubject;

    @Column(name = "research_purpose")
    private String researchPurpose;

    @Column(name = "historical_period")
    private String historicalPeriod;

    @Column(name = "study_area")
    private String studyArea;

}
