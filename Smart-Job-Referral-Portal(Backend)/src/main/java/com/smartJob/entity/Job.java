package com.smartJob.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String department;
    @Column(name = "company_name")
    private String companyName;

    @ElementCollection // Maps a collection of basic types
    @CollectionTable(name = "job_locations", joinColumns = @JoinColumn(name = "job_id") ) // Creates a separate table
    @Column(name = "location" )
    private List<String> location = new ArrayList<>();

    @Column(name = "experience_required")
    private int experienceRequired;

    @Column(name = "post_date")
    private Date postDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Referral> referrals = new ArrayList<>();

    public Job(String title, String description, String department, String companyName, List<String> location, int experienceRequired, Date postDate , User userId) {
        this.title = title;
        this.description = description;
        this.department = department;
        this.companyName = companyName;
        this.location = location;
        this.experienceRequired = experienceRequired;
        this.postDate = postDate;
        this.userId=userId;
    }
}
