package com.cinfin.bam.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cinfin.bam.entity.EdbLookup;

@Repository
public interface EdbLookupRepository extends JpaRepository<EdbLookup, Long> {
  Optional<EdbLookup> findByCode(String code);

  Optional<EdbLookup> findByTableIdAndCode(int tableId, String code);
}
