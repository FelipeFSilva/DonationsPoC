package br.org.donations.creditcardapi.unit.service;

import br.org.donations.creditcardapi.service.JwtService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;
    private String token;

}
