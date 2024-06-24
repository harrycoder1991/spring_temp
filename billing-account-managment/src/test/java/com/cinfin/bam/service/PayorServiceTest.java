import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.lang.reflect.Field;
import java.net.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.cinfin.bam.config.ApplicationPropertiesConfig;
import com.cinfin.bam.dto.requests.AccountBillDTO;
import com.cinfin.bam.dto.requests.AdditionalNameRequest;
import com.cinfin.bam.utils.BillingAccountManagementUtil;

class PayorServiceTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ApplicationPropertiesConfig config;

  @InjectMocks
  private PayorService payorService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(this.config.getAssureBaseUrl()).thenReturn("http://fake-url.com");
    when(this.config.getAssureCreatePayorEndpoint()).thenReturn("/payor");

    // Set static field using reflection
    Field configField = BillingAccountManagementUtil.class.getDeclaredField("config");
    configField.setAccessible(true);
    configField.set(null, this.config);
    when(this.config.getAssureServiceHeaderUserId()).thenReturn("User1");
    when(this.config.getAssureServiceHeaderRequest()).thenReturn("False");
  }


  @Test
  void testCallCreatePayorService_Failure() {
    // Prepare test data
    AccountBillDTO accountBillDTO = new AccountBillDTO(); // initialize with necessary data
    String compressedGuid = "compressedGuid";

    // Prepare mock response
    ResponseEntity<PayorCreationResponse> responseEntity = mock(ResponseEntity.class);
    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

    // Mock configuration
    when(this.config.getAssureServiceHeaderUserId()).thenReturn("User1");
    when(this.config.getAssureServiceHeaderRequest()).thenReturn("False");

    // Mock static method getHttpHeaders
    HttpHeaders mockHeaders = new HttpHeaders();
    try (MockedStatic<BillingAccountManagementUtil> mockedStatic =
        mockStatic(BillingAccountManagementUtil.class)) {
      mockedStatic.when(BillingAccountManagementUtil::getHttpHeaders).thenReturn(mockHeaders);

      // Mock RestTemplate response
      when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
          eq(PayorCreationResponse.class))).thenReturn(responseEntity);

      // Execute the method to be tested
      String partyId = this.payorService.callCreatePayorService(accountBillDTO, compressedGuid);

      // Assertions
      assertNull(partyId);

      // Verify RestTemplate interaction
      verify(this.restTemplate).postForEntity(anyString(), any(HttpEntity.class),
          eq(PayorCreationResponse.class));
    }
  }

}


  @Test
  public void testCallCreateAdditionalNameService_Success1() {
    // Prepare test data
    String partyId = "partyId";
    AdditionalNameRequest additionalNameRequest = new AdditionalNameRequest();
    additionalNameRequest.setType("Doing Business As");
    additionalNameRequest.setFullName("Test Name");

    // Mocking static method getHttpHeaders
    HttpHeaders mockHeaders = new HttpHeaders();
    try (MockedStatic<BillingAccountManagementUtil> mockedStatic =
        Mockito.mockStatic(BillingAccountManagementUtil.class)) {
      mockedStatic.when(BillingAccountManagementUtil::getHttpHeaders).thenReturn(mockHeaders);

      // Prepare mock response
      PayorAdditionalNameCreateResponse payorResponse = new PayorAdditionalNameCreateResponse();
      payorResponse.setSequence("sequence");
      ResponseEntity<PayorAdditionalNameCreateResponse> responseEntity =
          new ResponseEntity<>(payorResponse, HttpStatus.OK);

      // Mock RestTemplate response
      Mockito.when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
          eq(PayorAdditionalNameCreateResponse.class))).thenReturn(responseEntity);

      // Execute the method to be tested
      this.payorService.callCreateAdditionalNameService(partyId, additionalNameRequest);

      // Verify the RestTemplate interaction
      verify(this.restTemplate).postForEntity(anyString(), any(HttpEntity.class),
          eq(PayorAdditionalNameCreateResponse.class));

      // Assertion to ensure execution
      assertNotNull(payorResponse.getSequence(), "Sequence should not be null");
    }
  }

}


  @Test
  void testCallCreateAdditionalNameService_Failure() {
    String partyId = "123";
    AdditionalNameRequest additionalNameRequest = new AdditionalNameRequest();
    ResponseEntity<PayorAdditionalNameCreateResponse> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
    when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
        eq(PayorAdditionalNameCreateResponse.class))).thenReturn(responseEntity);

    assertThrows(RuntimeException.class,
        () -> this.payorService.callCreateAdditionalNameService(partyId, additionalNameRequest));
  }

  @Test
  void testCallCreateAdditionalNameService_Success() {
    String partyId = "123";
    AdditionalNameRequest additionalNameRequest = new AdditionalNameRequest();
    ResponseEntity<PayorAdditionalNameCreateResponse> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(new PayorAdditionalNameCreateResponse(1));
    when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
        eq(PayorAdditionalNameCreateResponse.class))).thenReturn(responseEntity);

    assertDoesNotThrow(
        () -> this.payorService.callCreateAdditionalNameService(partyId, additionalNameRequest));
    verify(this.restTemplate, times(1)).postForEntity(anyString(), any(HttpEntity.class),
        eq(PayorAdditionalNameCreateResponse.class));
  }

  @Test
  void testCallCreateAddressService_Failure() {
    AccountBillDTO accountBillDTO = mock(AccountBillDTO.class);
    String partyId = "123";
    ResponseEntity<CreateAddressResponse> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
    when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
        eq(CreateAddressResponse.class))).thenReturn(responseEntity);

    String addressSequence = this.payorService.callCreateAddressService(accountBillDTO, partyId);
    assertNull(addressSequence);
  }

  @Test
  void testCallCreateAddressService_Success() {
    AccountBillDTO accountBillDTO = mock(AccountBillDTO.class);
    String partyId = "123";
    ResponseEntity<CreateAddressResponse> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(new CreateAddressResponse("1"));
    when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
        eq(CreateAddressResponse.class))).thenReturn(responseEntity);

    String addressSequence = this.payorService.callCreateAddressService(accountBillDTO, partyId);
    assertEquals("1", addressSequence);
  }

  @Test
  void testCallCreatePayorService_Failure() {
    AccountBillDTO accountBillDTO = mock(AccountBillDTO.class);
    String compressedGuid = "compressedGuid";
    ResponseEntity<PayorCreationResponse> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
    when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
        eq(PayorCreationResponse.class))).thenReturn(responseEntity);

    String partyId = this.payorService.callCreatePayorService(accountBillDTO, compressedGuid);
    assertNull(partyId);
  }

  @Test
  void testCallCreatePayorService_Success() {
    AccountBillDTO accountBillDTO = mock(AccountBillDTO.class);
    String compressedGuid = "compressedGuid";
    ResponseEntity<PayorCreationResponse> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(responseEntity.getBody()).thenReturn(new PayorCreationResponse("1"));
    when(this.restTemplate.postForEntity(anyString(), any(HttpEntity.class),
        eq(PayorCreationResponse.class))).thenReturn(responseEntity);

    String partyId = this.payorService.callCreatePayorService(accountBillDTO, compressedGuid);
    assertEquals("1", partyId);
  }

  @Test
  void testCallPayorInfoUpdateService_Failure() {
    String payorId = "123";
    ResponseEntity<String> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
    when(this.restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class),
        eq(String.class))).thenReturn(responseEntity);

    assertDoesNotThrow(() -> this.payorService.callPayorInfoUpdateService(payorId));
    verify(this.restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PATCH),
        any(HttpEntity.class), eq(String.class));
  }

  @Test
  void testCallPayorInfoUpdateService_Success() {
    String payorId = "123";
    ResponseEntity<String> responseEntity = mock(ResponseEntity.class);

    when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
    when(this.restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class),
        eq(String.class))).thenReturn(responseEntity);

    assertDoesNotThrow(() -> this.payorService.callPayorInfoUpdateService(payorId));
    verify(this.restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.PATCH),
        any(HttpEntity.class), eq(String.class));
  }
}
