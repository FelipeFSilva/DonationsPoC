package br.org.donations.billetapi.utils;

import br.org.donations.billetapi.dto.AddressDTO;
import br.org.donations.billetapi.dto.BilletDonationDTO;
import br.org.donations.billetapi.dto.DonorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

public class TestUtils {

    private static String BILLET_SEND_DONATION_URI = "/billet/send-donation";

    public static BilletDonationDTO createDonationInputDTO() {
        return BilletDonationDTO.builder()
                .donor(DonorDTO.builder()
                        .name("Fulano")
                        .documentNumber("00011122233")
                        .email("teste@email.com").build())
                .address(AddressDTO.builder()
                        .addressNumber("12345678")
                        .city("cidade")
                        .number(100)
                        .state("RR")
                        .street("rua maneira")
                        .build())
                .value(BigDecimal.valueOf(100.00))
                .build();
    }

    public static MockHttpServletRequestBuilder sendBilletDonationPostRequest(String json) {
        return MockMvcRequestBuilders
                .post(BILLET_SEND_DONATION_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }

    public static String getJson(ObjectMapper objectMapper, BilletDonationDTO donationDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(donationDTO);
    }
}
