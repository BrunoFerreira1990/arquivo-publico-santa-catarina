package com.example.apesc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "document_collections")
public class DocumentCollections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer documentTypeId;

    private String producingAgencyId;

    private String recipientAgencyId;

    @Column(name = "transaction_nature")
    private String transactionNature;

    @Column(name = "period")
    private String period;

    @Column(name = "archival_shelf")
    private String archivalShelf;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "disponibility")
    private Boolean disponibility;

}
