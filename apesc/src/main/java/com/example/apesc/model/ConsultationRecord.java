package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ConsultationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "researcher_id")
    private String researcherId;

    @Column(name = "research_date")
    private LocalDate researchDate;

    @Column(name = "consultation_type")
    private String consultationType;

    @Column(name = "document_repository_id")
    private String documentRepositoryId;

    @Column(name = "period")
    private String period;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "register_date")
    private LocalDateTime registerDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "update_employee_id")
    private String updateEmployeeId;

}
