package com.example.apesc.model;

import com.example.apesc.model.enums.ConsultationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultation_record")
public class ConsultationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "researcher_id", nullable = false)
    private Researcher researcher;

    @Column(name = "research_date")
    private LocalDate researchDate;

    @Column(name = "consultation_type")
    private ConsultationType consultationType;

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
