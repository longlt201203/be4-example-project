package com.be4.qltc;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HelloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Double score;
}
