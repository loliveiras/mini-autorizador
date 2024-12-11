package br.com.vr.miniautorizador.cartoes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.vr.miniautorizador.cartoes.model.Cartoes;
import br.com.vr.miniautorizador.cartoes.model.TransacaoCartao;
import br.com.vr.miniautorizador.cartoes.service.CartoesService;
import br.com.vr.miniautorizador.cartoes.validation.CartoesValidator;
import br.com.vr.miniautorizador.enums.CartoesEnum;
import br.com.vr.miniautorizador.exception.CartoesException;

@WebMvcTest(CartoesController.class)
class CartoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartoesService cartoesService;
    
    @Mock
    private CartoesValidator cartoesValidator;

    @Test
    void testRegistrarCartaoQuandoSucessoDeveRetornarCreated() throws Exception {
        Cartoes cartao = new Cartoes("123456789", "senha123");

        when(cartoesService.registrarCartao(any(Cartoes.class))).thenReturn(cartao);

        mockMvc.perform(post("/cartoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCartao\": \"123456789\", \"senha\": \"senha123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value("123456789"))
                .andExpect(jsonPath("$.senha").value("senha123"));
    }
    @Test
    void testRegistrarCartaoQuandoJaExisteDeveRetornarUnprocessableEntity() throws Exception {

        when(cartoesService.registrarCartao(any(Cartoes.class))).thenThrow(new CartoesException(CartoesEnum.CARTAO_JA_REGISTRADO.name()));

        mockMvc.perform(post("/cartoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCartao\": \"123456789\", \"senha\": \"senha123\"}"))
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    void testRegistrarCartaoQuandoNaoAutorizadoDeveRetornarUnauthorized() throws Exception {
    	Cartoes cartao = new Cartoes("123456789", "senha123");
    	
        when(cartoesService.registrarCartao(any(Cartoes.class))).thenReturn(cartao);

        mockMvc.perform(post("/cartoes")
        		.with(httpBasic("user", "pass"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCartao\": \"123456789\", \"senha\": \"senha123\"}"))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void testObterSaldoCartaoQuandoSucessoDeveRetornarOk() throws Exception {
        when(cartoesService.obterSaldoCartao("123456789")).thenReturn(100.0);

        mockMvc.perform(get("/cartoes/123456789")
        		.with(httpBasic("username", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }
    @Test
    void testObterSaldoCartaoQuandoNaoEncontradoDeveRetornarNotFound() throws Exception {
        when(cartoesService.obterSaldoCartao("123456789"))
                .thenThrow(new CartoesException(CartoesEnum.CARTAO_INEXISTENTE.name()));

        mockMvc.perform(get("/cartoes/123456789")
        		.with(httpBasic("username", "password")))
                .andExpect(status().isNotFound());
    }
    @Test
    void testObterSaldoCartaoQuandoNaoAutorizadoDeveRetornarUnauthorized() throws Exception {
        when(cartoesService.obterSaldoCartao("123456789"))
                .thenThrow(new CartoesException(CartoesEnum.CARTAO_INEXISTENTE.name()));

        mockMvc.perform(get("/cartoes/123456789")
        		.with(httpBasic("user", "pass")))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void testTransacaoCartaoQuandoSucessoDeveRetornarCreated() throws Exception {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123", 50.0);
        doNothing().when(cartoesService).autorizarTransacao(transacao);

        mockMvc.perform(post("/transacoes")
        		.with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCartao\": \"123456789\", \"senha\": \"senha123\", \"valor\": 50.0}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(CartoesEnum.OK.name()));
    }
}