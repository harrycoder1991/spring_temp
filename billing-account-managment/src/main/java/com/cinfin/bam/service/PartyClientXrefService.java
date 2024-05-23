package com.cinfin.bam.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cinfin.bam.entity.PartyClientXref;
import com.cinfin.bam.repository.PartyClientXrefRepository;

@Service
public class PartyClientXrefService {

  @Autowired
  private PartyClientXrefRepository repository;

  public void deleteById(Long id) {
    this.repository.deleteById(id);
  }

  public List<PartyClientXref> findAll() {
    return this.repository.findAll();
  }

  public PartyClientXref findById(Long id) {
    return this.repository.findById(id).orElse(null);
  }

  public PartyClientXref save(PartyClientXref xref) {
    return this.repository.save(xref);
  }
}
