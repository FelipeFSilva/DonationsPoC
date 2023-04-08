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

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET_KEY);
//        authentication = new UsernamePasswordAuthenticationToken(USER_LOGIN, PASSWORD);
//        //token = jwtService.generateToken(authentication);
//    }
//
//    @Test
//    @DisplayName("Deve gerar um token com sucesso")
//    public void generateTokenTest(){
//        String token = "";//jwtService.generateToken(authentication);
//        assertThat(token).isNotEmpty();
//    }
//
//    @Test
//    @DisplayName("Deve retornar 'false' quando não for uma autorização válida")
//    public void isInvalidAuthorizationTest(){
//        boolean result = jwtService.isValidAuthorization("teste");
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    @DisplayName("Deve retornar 'true' quando for uma autorização válida")
//    public void isValidAuthorizationTest(){
//        boolean result = jwtService.isValidAuthorization(BEARER);
//        assertThat(result).isTrue();
//    }
//
//    @Test
//    @DisplayName("Deve retornar 'true' quando o token for válido")
//    public void isTokenValidTest(){
//        boolean tokenValid = jwtService.isTokenValid(token);
//        assertThat(tokenValid).isTrue();
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção quando o token estiver expirado")
//    public void isExpiredTokenTest(){
//        boolean tokenValid = jwtService.isTokenValid(EXPIRED_TOKEN);
//        assertThat(tokenValid).isFalse();
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção quando o token não for válido")
//    public void isTokenInvalidTest(){
//
//        Throwable throwable = org.assertj.core.api.Assertions.catchThrowable(() -> jwtService.isTokenValid(token + "a"));
//        assertThat(throwable).isInstanceOf(AuthorizationException.class);
//    }
//
//    @Test
//    @DisplayName("Deve extrair o usuário do token")
//    public void extractUserLoginTest(){
//
//        String extractedUser = jwtService.extractUserLogin(token);
//        assertThat(extractedUser).isEqualTo(USER_LOGIN);
//    }
//
//    @Test
//    @DisplayName("Deve extrair o token do 'Authorization'")
//    public void extractTokenTest(){
//        String authorization = BEARER + token;
//        String extractedToken = jwtService.extractToken(authorization);
//        assertThat(extractedToken).isEqualTo(token);
//    }
}
