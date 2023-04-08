package br.org.donations.creditcardapi.unit.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserDetailsServiceImplTest {


//
//    @BeforeEach
//    public void setUp(){
//        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(userDetailsService, "username", USER_LOGIN);
//        ReflectionTestUtils.setField(userDetailsService, "password", PASSWORD);
//        ReflectionTestUtils.setField(userDetailsService, "passwordEncoder", new BCryptPasswordEncoder());
//    }

//    @Test
//    @DisplayName("Deve carregar um usuário com sucesso")
//    public void loadUserByUsernameTest(){
//        UserDetails userDetails = userDetailsService.loadUserByUsername(USER_LOGIN);
//        assertThat(userDetails).isNotNull();
//        assertThat(userDetails.getUsername()).isEqualTo(USER_LOGIN);
//    }
//
//    @Test
//    @DisplayName("Deve lançar exceção quando for informado usuário com nome diferente do aceito pela aplicação")
//    public void loadUserByUsernameExceptionTest(){
//        Throwable throwable = org.assertj.core.api.Assertions.catchThrowable(() -> userDetailsService.loadUserByUsername("teste"));
//        assertThat(throwable).isInstanceOf(UsernameNotFoundException.class);
//    }

}
