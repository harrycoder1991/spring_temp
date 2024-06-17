package com.cinfin.bam.utils;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.cinfin.bam.entity.EdbLookup;
import com.cinfin.bam.repository.EdbLookupRepository;

@Component
public class PartyClientXrefUtils {

  public static String encodeUUIDToBase64(String guidString) {
    try {
      UUID uuid = UUID.fromString(guidString);
      ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
      byteBuffer.putLong(uuid.getMostSignificantBits());
      byteBuffer.putLong(uuid.getLeastSignificantBits());
      return Base64.getUrlEncoder().withoutPadding().encodeToString(byteBuffer.array());
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid GUID format: " + guidString, e);
    }
  }

  @Autowired
  private EdbLookupRepository edbLookupRepository;

  public String determinePaymentPlan(AccountInputDTO input) {
    String paymentPlanCd = input.getPaymentPlanCd();
    String systemId = input.getSystemId();

    // Default to '1' if paymentPlanCd is not provided
    if (paymentPlanCd == null || paymentPlanCd.isEmpty()) {
      return "1";
    }

    // Lookup if translation is needed based on systemId
    Optional<EdbLookup> translationLookup =
        this.edbLookupRepository.findByTableIdAndCode(1, systemId);

    if (translationLookup.isPresent()) {
      int tableId = Integer.parseInt(translationLookup.get().getTranslation());
      // Perform the second lookup to get the translated payment plan
      Optional<EdbLookup> paymentPlanTranslation =
          this.edbLookupRepository.findByTableIdAndCode(tableId, paymentPlanCd);
      return paymentPlanTranslation.map(EdbLookup::getTranslation).orElse(paymentPlanCd);
    } else {
      // If no translation is needed, use the provided PaymentPlanCd
      return paymentPlanCd;
    }
  }
}
