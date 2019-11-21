package com.clearavenue.fdadi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clearavenue.fdadi.model.Medication;

@Repository
public interface MedicationRepository extends MongoRepository<Medication, ObjectId> {

	Optional<Medication> findByMedicationName(String med);
}
