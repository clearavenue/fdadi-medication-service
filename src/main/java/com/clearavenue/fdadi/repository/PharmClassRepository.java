package com.clearavenue.fdadi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clearavenue.fdadi.model.PharmClass;

@Repository
public interface PharmClassRepository extends JpaRepository<PharmClass, Long> {

	Optional<PharmClass> findByPharmClassName(String name);
}
