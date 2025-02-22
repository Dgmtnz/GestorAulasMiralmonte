package com.joange.repository;
import com.joange.model.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByActivoTrue();
    List<Aula> findByPlantaIdplanta(Long idplanta);
    List<Aula> findByPlantaIdplantaAndActivoTrue(Long idplanta);
}