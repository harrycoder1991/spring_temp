package com.cinfin.bam.repository;


import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cinfin.bam.entity.DirectBillPlan;

@Repository
public interface DirectBillPlanRepository extends JpaRepository<DirectBillPlan, Long> {

  @Query(
      value = "SELECT TOP 1 * FROM DIRECT_BILL_PLAN DIBIPL "
          + "WHERE DIBIPL.HQ_STATE IN (:hqState, 'XX') "
          + "AND DIBIPL.POLICY_PKG_TYPE = :policyPkgType "
          + "AND DIBIPL.POLICY_PREFIX IN (:policyPrefix, 'XXX') "
          + "AND DIBIPL.PMT_PLAN = :pmtPlan " + "AND DIBIPL.COLLECT_METH = :collectMeth "
          + "AND DIBIPL.PLAN_EFF_DT <= :planEffDt " + "AND DIBIPL.PLAN_EXP_DT > :planExpDt "
          + "ORDER BY DIBIPL.HQ_STATE, DIBIPL.POLICY_PKG_TYPE, DIBIPL.POLICY_PREFIX ASC",
      nativeQuery = true)
  DirectBillPlan findDirectBillPlan(@Param("hqState") String hqState,
      @Param("policyPkgType") String policyPkgType, @Param("policyPrefix") String policyPrefix,
      @Param("pmtPlan") String pmtPlan, @Param("collectMeth") String collectMeth,
      @Param("planEffDt") Date planEffDt, @Param("planExpDt") Date planExpDt);
}
