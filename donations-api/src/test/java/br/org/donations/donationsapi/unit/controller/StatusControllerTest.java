package br.org.donations.donationsapi.unit.controller;

import br.org.donations.donationsapi.controller.StatusController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static br.org.donations.donationsapi.utils.TestUtils.BEARER;
import static br.org.donations.donationsapi.utils.TestUtils.generateToken;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest(controllers = StatusController.class)
public class StatusControllerTest {

    @Autowired
    MockMvc mvc;

    private String token;

    @BeforeEach
    public void setUp() {
        token = generateToken();
    }

    @Test
    @DisplayName("Deve obter o status da API")
    public void getApiStatusTest() throws Exception {

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/donations-api/status")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, BEARER + token);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Status").value("up"))
                .andExpect(jsonPath("$.httpStatus").value("200"))
                .andExpect(jsonPath("$.Service").value("donations-api"));
    }

}

