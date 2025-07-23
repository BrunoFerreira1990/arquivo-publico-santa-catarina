package com.example.apesc.model;

import com.example.apesc.model.enums.*;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "researcher")
public class Researcher extends Person {

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "nacionality")
    private Nationality nacionality;

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
    private States state;

    @Column(name = "cep")
    private String cep;

    @Column(name = "education_level")
    private EducationLevel educationLevel;

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

    @ElementCollection
    @CollectionTable(
            name = "researcher_historical_period",
            joinColumns = @JoinColumn(name = "researcher_id")
    )
    @Column(name = "historical_period")
    @Enumerated(EnumType.STRING)
    private Set<HistoricalPeriod> historicalPeriods = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "researcher_study_area",
            joinColumns = @JoinColumn(name = "researcher_id")
    )
    @Column(name = "study_area")
    @Enumerated(EnumType.STRING)
    private Set<StudyArea> studyAreas = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "researcher", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ConsultationRecord> consultationRecords = new HashSet<>();

}
