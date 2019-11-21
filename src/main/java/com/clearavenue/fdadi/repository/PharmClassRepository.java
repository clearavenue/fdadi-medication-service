package com.clearavenue.fdadi.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clearavenue.fdadi.model.PharmClass;

@Repository
public interface PharmClassRepository extends MongoRepository<PharmClass, ObjectId> {

	Optional<PharmClass> findByPharmClassName(String name);
}
