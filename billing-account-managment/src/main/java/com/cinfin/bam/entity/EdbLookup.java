package com.cinfin.bam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "EDB_LOOKUP")
@Data
public class EdbLookup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TABLE_ID", nullable = false)
  private int tableId;

  @Column(name = "CODE", nullable = false)
  private String code;

  @Column(name = "TRANSLATION", nullable = false)
  private String translation;

  // Getters and Setters
}
