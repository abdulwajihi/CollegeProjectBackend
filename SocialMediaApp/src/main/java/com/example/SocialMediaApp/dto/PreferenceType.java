package com.example.SocialMediaApp.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preference_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceTypeId;

    @Column(unique = true)
    private String name;



}
