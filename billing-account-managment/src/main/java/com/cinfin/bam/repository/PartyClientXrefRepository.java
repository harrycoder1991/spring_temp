package com.cinfin.bam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cinfin.bam.entity.PartyClientXref;

@Repository
public interface PartyClientXrefRepository extends JpaRepository<PartyClientXref, Long> {
}
