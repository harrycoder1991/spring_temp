package com.cinfin.bam.controller;



import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cinfin.bam.entity.PartyClientXref;
import com.cinfin.bam.service.PartyClientXrefService;

@RestController
@RequestMapping("/api/party-client-xref")
public class PartyClientXrefController {

  @Autowired
  private PartyClientXrefService service;

  @PostMapping
  public PartyClientXref create(@RequestBody PartyClientXref xref) {
    return this.service.save(xref);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    this.service.deleteById(id);
  }

  @GetMapping
  public List<PartyClientXref> getAll() {
    return this.service.findAll();
  }

  @GetMapping("/{id}")
  public PartyClientXref getById(@PathVariable Long id) {
    return this.service.findById(id);
  }

  @PutMapping("/{id}")
  public PartyClientXref update(@PathVariable Long id, @RequestBody PartyClientXref xref) {
    xref.setId(id);
    return this.service.save(xref);
  }
}
