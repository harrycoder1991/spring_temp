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
@Table(name = "jharve4_PARTY_CLIENT_XREF")
@Data
public class PartyClientXref implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "EXTERNAL_REF_ID")
  private String externalRefId;

  @Column(name = "PARTY_GUID")
  private String partyGuid;

  @Column(name = "PARTY_ROLE")
  private String partyRole;

  @Column(name = "DXC_ROLE")
  private String dxcRole;

  @Column(name = "POST_DT_TS")
  @Temporal(TemporalType.TIMESTAMP)
  private Date postDtTs;

  // Getters and Setters
}
