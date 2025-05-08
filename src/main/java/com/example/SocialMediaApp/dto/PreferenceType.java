package com.example.SocialMediaApp.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preference_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreferenceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true,name = "name")
    private String name;



}
