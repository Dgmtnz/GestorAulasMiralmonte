package com.joange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.joange.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    List<Curso> findByActivoTrue();
} 