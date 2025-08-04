package com.smartJob.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Alternative
    private String fileName; // e.g., "a1b2c3d4-resume.pdf"
    private String storedPath;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "referral_id" )
    private Referral referral;
}
