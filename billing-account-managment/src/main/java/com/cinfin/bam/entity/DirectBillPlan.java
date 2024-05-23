package com.cinfin.bam.entity;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "DIRECT_BILL_PLAN")
@Data
public class DirectBillPlan implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "HQ_STATE")
  private String hqState;

  @Column(name = "BIL_TYPE_CD")
  private String bilTypeCd;

  @Column(name = "BIL_CLASS_CD")
  private String bilClassCd;

  @Column(name = "POLICY_PLAN")
  private String policyPlan;

  @Column(name = "EFT_PLAN")
  private String eftPlan;

  @Column(name = "POLICY_PKG_TYPE")
  private String policyPkgType;

  @Column(name = "POLICY_PREFIX")
  private String policyPrefix;

  @Column(name = "PMT_PLAN")
  private String pmtPlan;

  @Column(name = "COLLECT_METH")
  private String collectMeth;

  @Column(name = "PLAN_EFF_DT")
  @Temporal(TemporalType.DATE)
  private Date planEffDt;

  @Column(name = "PLAN_EXP_DT")
  @Temporal(TemporalType.DATE)
  private Date planExpDt;

  // Getters and Setters
}
