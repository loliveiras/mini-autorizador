package br.com.vr.miniautorizador.cartoes.service;


import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.repository.CartoesRepository;
import br.com.vr.miniautorizador.cartoes.service.CartaoService;
import br.com.vr.miniautorizador.cartoes.service.Impl.CartoesServiceImpl;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.ValidarCartao;
import br.com.vr.miniautorizador.transacoes.model.TransacaoCartao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CartoesServiceImplTest {

    @Mock
    private CartoesRepository cartoesRepository;

    @Mock
    private ValidarCartao validarCartao;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CartoesServiceImpl cartoesService;

    private Cartao cartao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartao = new Cartao("123456789", "senha123");
        cartao.setSaldo(500.0);
    }

    @Test
    void testRegistrarCartao() throws MiniAutorizadorException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(false);
        when(passwordEncoder.encode(cartao.getSenha())).thenReturn("senhaCodificada");

        Cartao cartaoRegistrado = cartoesService.registrarCartao(cartao);

        assertNotNull(cartaoRegistrado);
        assertEquals("senhaCodificada", cartaoRegistrado.getSenha());
        assertEquals(500.0, cartaoRegistrado.getSaldo());
        verify(cartoesRepository, times(1)).save(cartao);
    }
    @Test
    void testObterSaldoCartao() throws MiniAutorizadorException {
        when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));

        double saldo = cartoesService.obterSaldoCartao(cartao.getNumeroCartao());

        assertEquals(500.0, saldo);
    }
    @Test
    void testProcessarTransacao() throws MiniAutorizadorException {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123", 100.0);

        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), eq(Cartao.class))).thenReturn(cartao);
        cartao.setSaldo(cartao.getSaldo() - transacao.getValorTransacao());

        cartoesService.processarTransacao(transacao);

        assertEquals(400.0, cartao.getSaldo());
        verify(mongoTemplate, times(1)).findAndModify(any(Query.class), any(Update.class), eq(Cartao.class));
    }
    @Test
    void testRegistrarCartaoExistente() throws MiniAutorizadorException {
        when(validarCartao.validarCartaoExistente(cartao, cartoesRepository)).
        thenThrow(new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_JA_EXISTE.name()));

        MiniAutorizadorException exception = assertThrows(MiniAutorizadorException.class, () -> {
            cartoesService.registrarCartao(cartao);
        });

        assertEquals(MiniAutorizadorEnum.CARTAO_JA_EXISTE.name(), exception.getMessage());
    }
    @Test
    void testObterSaldoCartaoNaoExistente() throws MiniAutorizadorException {
        when(cartoesRepository.findById(cartao.getNumeroCartao())).thenReturn(java.util.Optional.empty());
        
        doThrow(new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()))
        .when(validarCartao).validarCartaoInexistente(cartao.getNumeroCartao(), cartoesRepository);

        MiniAutorizadorException exception = assertThrows(MiniAutorizadorException.class, () -> {
            cartoesService.obterSaldoCartao(cartao.getNumeroCartao());
        });

        assertEquals(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
    }
    @Test
    void testProcessarTransacao_QuandoSenhaInvalida_DeveLancarExcecao() throws MiniAutorizadorException {
        cartao.setSenha("senhaCodificada");
        
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senhaIncorreta", 100.0);

        when(cartoesRepository.findById(transacao.getNumeroCartao())).thenReturn(java.util.Optional.of(cartao));
        when(passwordEncoder.matches(transacao.getSenhaCartao(), cartao.getSenha())).thenReturn(false);
        
        cartoesService.processarTransacao(transacao);

        assertEquals(MiniAutorizadorEnum.SENHA_INVALIDA.name(), "SENHA_INVALIDA");
    }
    @Test
    void testProcessarTransacao_QuandoCartaoInexistente_DeveLancarExcecao() throws MiniAutorizadorException {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123", 100.0);

        doThrow(new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()))
            .when(validarCartao)
            .validaTransacaoCartao(transacao, cartoesRepository);

        MiniAutorizadorException exception = assertThrows(MiniAutorizadorException.class, () -> {
            cartoesService.processarTransacao(transacao);
        });

        assertEquals(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name(), exception.getMessage());
    }
    @Test
    void testProcessarTransacaoSaldoInsuficiente() throws MiniAutorizadorException {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123",  1000.0);
        
        doThrow(new MiniAutorizadorException(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name()))
        .when(validarCartao).validarSaldoDisponivelCartao(null);

        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), eq(Cartao.class))).thenReturn(null);
        
        MiniAutorizadorException exception = assertThrows(MiniAutorizadorException.class, () -> {
            cartoesService.processarTransacao(transacao);
        });

        assertEquals(MiniAutorizadorEnum.SALDO_INSUFICIENTE.name(), exception.getMessage());
    }
    @Test
    void testRegistrarCartao_QuandoErroNaCriptografia_DeveLancarExcecao() throws MiniAutorizadorException {
        when(passwordEncoder.encode(cartao.getSenha())).thenThrow(new RuntimeException("Erro na criptografia"));

        assertThrows(RuntimeException.class, () -> {
            cartoesService.registrarCartao(cartao);
        });
    }
    @Test
    public void testValidarCartaoExistente_QuandoCartaoJaExiste_DeveLancarExcecao() throws MiniAutorizadorException {
        when(cartoesRepository.existsById(cartao.getNumeroCartao())).thenReturn(true);

        doThrow(new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_JA_EXISTE.name()))
            .when(validarCartao).validarCartaoExistente(cartao, cartoesRepository);

        MiniAutorizadorException exception = assertThrows(MiniAutorizadorException.class, () -> {
            validarCartao.validarCartaoExistente(cartao, cartoesRepository);
        });

        assertEquals(MiniAutorizadorEnum.CARTAO_JA_EXISTE.name(), exception.getMessage());
    }
}