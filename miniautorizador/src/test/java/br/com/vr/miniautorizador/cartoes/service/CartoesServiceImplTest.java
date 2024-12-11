package br.com.vr.miniautorizador.cartoes.service;


import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.model.TransacaoCartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.Impl.CartoesServiceImpl;
import br.com.vr.miniautorizador.cartoes.validation.CartoesValidator;
import br.com.vr.miniautorizador.enums.CartoesEnum;
import br.com.vr.miniautorizador.exception.CartoesException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

class CartoesServiceImplTest {

    @Mock
    private CartoesRepository cartoesRepository;

    @Mock
    private CartoesValidator cartoesValidator;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CartoesServiceImpl cartoesService;

    private Cartoes cartao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartao = new Cartoes("123456789", "senha123");
        cartao.setSaldo(500.0);
    }

    @Test
    void deveRegistrarCartaoComSucessoQuandoNaoExistirNoSistema() throws CartoesException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);
        when(passwordEncoder.encode(cartao.getSenha())).thenReturn("senhaCodificada");

        Cartoes cartaoRegistrado = cartoesService.registrarCartao(cartao);

        assertNotNull(cartaoRegistrado);
        assertEquals("senhaCodificada", cartaoRegistrado.getSenha());
        assertEquals(500.0, cartaoRegistrado.getSaldo());
        verify(cartoesRepository, times(1)).save(cartao);
    }
    @Test
    void deveRetornarSaldoDoCartaoQuandoExistirNoSistema() throws CartoesException {
        when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));

        double saldo = cartoesService.obterSaldoCartao(cartao.getNumeroCartao());

        assertEquals(500.0, saldo);
    }
    @Test
    void deveProcessarTransacaoComSucessoQuandoCartaoExistirESaldoSuficiente() throws CartoesException {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123", 100.0);

        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), eq(Cartoes.class))).thenReturn(cartao);
        cartao.setSaldo(cartao.getSaldo() - transacao.getValorTransacao());

        cartoesService.autorizarTransacao(transacao);

        assertEquals(400.0, cartao.getSaldo());
        verify(mongoTemplate, times(1)).findAndModify(any(Query.class), any(Update.class), eq(Cartoes.class));
    }
    @Test
    void deveLancarExcecaoQuandoCartaoJaExistirNoSistema() throws CartoesException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);
        
        doThrow(new CartoesException(CartoesEnum.CARTAO_JA_REGISTRADO.name()))
            .when(cartoesValidator).validarCartaoExistente(cartao, cartoesRepository);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesService.registrarCartao(cartao);
        });

        assertEquals(CartoesEnum.CARTAO_JA_REGISTRADO.name(), exception.getMessage());
        verify(cartoesValidator, times(1)).validarCartaoExistente(cartao, cartoesRepository);
    }
    @Test
    void deveLancarExcecaoQuandoCartaoNaoExistirNoSistema() throws CartoesException {
        when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(java.util.Optional.empty());
        
        doThrow(new CartoesException(CartoesEnum.CARTAO_INEXISTENTE.name()))
        .when(cartoesValidator).validarCartaoInexistente(cartao.getNumeroCartao(), cartoesRepository);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesService.obterSaldoCartao(cartao.getNumeroCartao());
        });

        assertEquals(CartoesEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoSenhaCartaoForInvalida() throws CartoesException {
        cartao.setSenha("senhaCodificada");
        
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senhaIncorreta", 100.0);

        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(passwordEncoder.matches(transacao.getSenhaCartao(), cartao.getSenha())).thenReturn(false);
        
        cartoesService.autorizarTransacao(transacao);

        assertEquals(CartoesEnum.SENHA_INVALIDA.name(), "SENHA_INVALIDA");
    }
    @Test
    void deveLancarExcecaoQuandoCartaoNaoExistirParaProcessarTransacao() throws CartoesException {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123", 100.0);

        doThrow(new CartoesException(CartoesEnum.CARTAO_INEXISTENTE.name()))
            .when(cartoesValidator)
            .validarTransacaoCartao(transacao, cartoesRepository);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesService.autorizarTransacao(transacao);
        });

        assertEquals(CartoesEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoSaldoDoCartaoForInsuficienteParaTransacao() throws CartoesException {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123",  1000.0);
        
        doThrow(new CartoesException(CartoesEnum.SALDO_INSUFICIENTE.name()))
        .when(cartoesValidator).validarSaldoDisponivelCartao(null);

        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), eq(Cartoes.class))).thenReturn(null);
        
        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesService.autorizarTransacao(transacao);
        });

        assertEquals(CartoesEnum.SALDO_INSUFICIENTE.name(), exception.getMessage());
    }
    @Test
    void deveLancarExcecaoQuandoHouverErroNaCriptografiaDaSenhaDoCartao() throws CartoesException {
        when(passwordEncoder.encode(cartao.getSenha())).thenThrow(new RuntimeException("Erro na criptografia"));

        assertThrows(RuntimeException.class, () -> {
            cartoesService.registrarCartao(cartao);
        });
    }
    @Test
    void deveLancarExcecaoQuandoCartaoJaExistirNoSistemaDuranteValidacao() throws CartoesException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);

        doThrow(new CartoesException(CartoesEnum.CARTAO_JA_REGISTRADO.name()))
            .when(cartoesValidator).validarCartaoExistente(cartao, cartoesRepository);

        CartoesException exception = assertThrows(CartoesException.class, () -> {
            cartoesValidator.validarCartaoExistente(cartao, cartoesRepository);
        });

        assertEquals(CartoesEnum.CARTAO_JA_REGISTRADO.name(), exception.getMessage());
    }
}