package br.com.vr.miniautorizador.cartoes.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.model.TransacaoCartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.validation.CartoesValidator;
import br.com.vr.miniautorizador.enums.CartoesEnum;
import br.com.vr.miniautorizador.exception.CartoesException;

class CartoesValidatorTest {

    @Mock
    private CartoesRepository cartoesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CartoesValidator cartoesValidator;

    private Cartoes cartao;
    private TransacaoCartao transacao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartao = new Cartoes("123456789", "senha123");
        cartao.setSaldo(500.0);
        transacao = new TransacaoCartao("123456789", "senha123", 100.0);
    }
    @Test
    void testValidarCartaoExistenteQuandoCartaoNaoExistirDeveRetornarFalse() throws CartoesException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);

        boolean resultado = cartoesValidator.validarCartaoExistente(cartao, cartoesRepository);

        assertFalse(resultado);
    }
    @Test
    void testValidarCartaoExistenteQuandoCartaoExistirDeveLancarExcecao() {
    	when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);
    	
    	CartoesException exception = assertThrows(CartoesException.class, () -> {
    		cartoesValidator.validarCartaoExistente(cartao, cartoesRepository);
    	});
    	
    	assertEquals(CartoesEnum.CARTAO_JA_REGISTRADO.name(), exception.getMessage());
    }
    @Test
    void testValidarCartaoInexistenteQuandoCartaoNaoExistirDeveLancarExcecao() {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesValidator.validarCartaoInexistente(cartao.getNumeroCartao(), cartoesRepository);
        });

        assertEquals(CartoesEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
    }
    @Test
    void testValidarCartaoInexistenteQuandoCartaoExistirDeveRetornarTrue() throws CartoesException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);

        boolean resultado = cartoesValidator.validarCartaoInexistente(cartao.getNumeroCartao(), cartoesRepository);

        assertTrue(resultado);
    }
    @Test
    void testValidarTransacaoCartaoQuandoCartaoInexistenteDeveLancarExcecao() throws CartoesException {
        when(cartoesRepository.existsById(transacao.getNumeroCartao())).thenReturn(false);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesValidator.validarTransacaoCartao(transacao, cartoesRepository);
        });

        assertEquals(CartoesEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
    }
    @Test
    void testValidarTransacaoCartaoQuandoSenhaInvalidaDeveLancarExcecao() throws CartoesException {
        when(cartoesRepository.existsById(transacao.getNumeroCartao())).thenReturn(true);
        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(passwordEncoder.matches(transacao.getSenhaCartao(), cartao.getSenha())).thenReturn(false);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesValidator.validarTransacaoCartao(transacao, cartoesRepository);
        });

        assertEquals(CartoesEnum.SENHA_INVALIDA.name(), exception.getMessage());
    }
    @Test
    void testValidarTransacaoCartaoQuandoCartaoValidoDeveRetornarSemExcecao() throws CartoesException {
        when(cartoesRepository.existsById(transacao.getNumeroCartao())).thenReturn(true);
        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(passwordEncoder.matches(transacao.getSenhaCartao(), cartao.getSenha())).thenReturn(true);

        cartoesValidator.validarTransacaoCartao(transacao, cartoesRepository);
    }
    @Test
    void testValidarSaldoDisponivelCartaoQuandoSaldoInsuficienteDeveLancarExcecao() throws CartoesException {
        cartao.setSaldo(50.0);
        when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesValidator.validarSaldoDisponivelCartao(null);
        });

        assertEquals(CartoesEnum.SALDO_INSUFICIENTE.name(), exception.getMessage());
    }
    @Test
    void testValidarSaldoDisponivelCartaoQuandoSaldoSuficienteDevePassarSemExcecao() throws CartoesException {
        cartao.setSaldo(500.0);
        when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));

        cartoesValidator.validarSaldoDisponivelCartao(cartao);
    }
    @Test
    void testValidarSenhaCartaoQuandoSenhaIncorretaDeveLancarExcecao() throws CartoesException {
        transacao.setSenhaCartao("senhaErrada");
        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(passwordEncoder.matches(transacao.getSenhaCartao(), cartao.getSenha())).thenReturn(false);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesValidator.validarSenhaCartao(transacao, cartoesRepository);
        });

        assertEquals(CartoesEnum.SENHA_INVALIDA.name(), exception.getMessage());
    }
    @Test
    void testValidarSenhaCartaoQuandoSenhaCorretaDevePassarSemExcecao() throws CartoesException {
        transacao.setSenhaCartao("senha123");
        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(passwordEncoder.matches(transacao.getSenhaCartao(), cartao.getSenha())).thenReturn(true);

        cartoesValidator.validarSenhaCartao(transacao, cartoesRepository);
    }
}