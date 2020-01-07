package com.clearavenue.fdadi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clearavenue.fdadi.model.Medication;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

	Optional<Medication> findByMedicationName(String med);
}
