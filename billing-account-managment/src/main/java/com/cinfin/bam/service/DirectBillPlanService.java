package com.cinfin.bam.service;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cinfin.bam.entity.DirectBillPlan;
import com.cinfin.bam.repository.DirectBillPlanRepository;

@Service
public class DirectBillPlanService {

  @Autowired
  private DirectBillPlanRepository repository;

  public void deleteById(Long id) {
    this.repository.deleteById(id);
  }

  public List<DirectBillPlan> findAll() {
    return this.repository.findAll();
  }

  public DirectBillPlan findById(Long id) {
    return this.repository.findById(id).orElse(null);
  }

  public DirectBillPlan save(DirectBillPlan plan) {
    return this.repository.save(plan);
  }
}
