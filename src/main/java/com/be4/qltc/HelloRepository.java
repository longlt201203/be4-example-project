package com.be4.qltc;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<HelloEntity, Integer> {
    boolean existsByName(String name);
}
