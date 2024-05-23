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
import com.cinfin.bam.entity.DirectBillPlan;
import com.cinfin.bam.service.DirectBillPlanService;

@RestController
@RequestMapping("/api/direct-bill-plan")
public class DirectBillPlanController {

  @Autowired
  private DirectBillPlanService service;

  @PostMapping
  public DirectBillPlan create(@RequestBody DirectBillPlan plan) {
    return this.service.save(plan);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    this.service.deleteById(id);
  }

  @GetMapping
  public List<DirectBillPlan> getAll() {
    return this.service.findAll();
  }

  @GetMapping("/{id}")
  public DirectBillPlan getById(@PathVariable Long id) {
    return this.service.findById(id);
  }

  @PutMapping("/{id}")
  public DirectBillPlan update(@PathVariable Long id, @RequestBody DirectBillPlan plan) {
    plan.setId(id);
    return this.service.save(plan);
  }
}
