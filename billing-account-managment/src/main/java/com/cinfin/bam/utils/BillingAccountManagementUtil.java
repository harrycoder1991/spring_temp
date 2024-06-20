package com.cinfin.bam.utils;

import java.util.Base64;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import com.cinfin.bam.config.ApplicationPropertiesConfig;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.entity.EdbLookup;
import com.cinfin.bam.repository.EdbLookupRepository;

@Component
public class BillingAccountManagementUtil {

  private static ApplicationPropertiesConfig config;
  private static EdbLookupRepository edbLookupRepository;

  public static String determinePaymentPlan(AccountBillDTO input) {
    String paymentPlanCd = input.getPolicy().getPaymentPlanCd();
    String systemId = input.getPolicy().getSystemId();

    // Default to '1' if paymentPlanCd is not provided
    if (paymentPlanCd == null || paymentPlanCd.isEmpty()) {
      return "1";
    }

    // Lookup if translation is needed based on systemId
    Optional<EdbLookup> translationLookup = edbLookupRepository.findByTableIdAndCode(1, systemId);

    if (translationLookup.isPresent()) {
      int tableId = Integer.parseInt(translationLookup.get().getTranslation());

      // Perform the second lookup to get the translated payment plan
      Optional<EdbLookup> paymentPlanTranslation =
          edbLookupRepository.findByTableIdAndCode(tableId, paymentPlanCd);

      return paymentPlanTranslation.map(EdbLookup::getTranslation).orElse(paymentPlanCd);
    } else {
      // If no translation is needed, use the provided PaymentPlanCd
      return paymentPlanCd;
    }
  }

  public static String encodeUUIDToBase64(String guid) {
    UUID uuid = UUID.fromString(guid);
    byte[] uuidBytes = toBytes(uuid);
    return Base64.getEncoder().encodeToString(uuidBytes);
  }

  public static HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("X-CSC-User-Id", config.getAssureServiceHeaderUserId());
    headers.set("X-Service-request", config.getAssureServiceHeaderRequest());
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    return headers;
  }

  private static byte[] toBytes(UUID uuid) {
    long msb = uuid.getMostSignificantBits();
    long lsb = uuid.getLeastSignificantBits();
    byte[] buffer = new byte[16];
    for (int i = 0; i < 8; i++) {
      buffer[i] = (byte) (msb >>> 8 * (7 - i));
      buffer[8 + i] = (byte) (lsb >>> 8 * (7 - i));
    }
    return buffer;
  }

  @Autowired
  public BillingAccountManagementUtil(ApplicationPropertiesConfig config,
      EdbLookupRepository edbLookupRepository) {
    BillingAccountManagementUtil.config = config;
    BillingAccountManagementUtil.edbLookupRepository = edbLookupRepository;
  }
}
