package br.com.vr.miniautorizador.cartoes.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;



import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import br.com.vr.miniautorizador.cartoes.model.Cartao;
import br.com.vr.miniautorizador.cartoes.service.CartaoService;
import br.com.vr.miniautorizador.enums.MiniAutorizadorEnum;
import br.com.vr.miniautorizador.exception.MiniAutorizadorException;
import br.com.vr.miniautorizador.transacoes.ValidarCartao;
import br.com.vr.miniautorizador.transacoes.model.TransacaoCartao;

@WebMvcTest(CartoesController.class)
class CartoesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartaoService cartaoService;
    
    @Mock
    private ValidarCartao validarCartao;

    @Test
    void testRegistrarCartao_QuandoSucesso_DeveRetornarCreated() throws Exception {
        Cartao cartao = new Cartao("123456789", "senha123");

        when(cartaoService.registrarCartao(any(Cartao.class))).thenReturn(cartao);

        mockMvc.perform(post("/cartoes")
                .with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCartao\": \"123456789\", \"senha\": \"senha123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCartao").value("123456789"))
                .andExpect(jsonPath("$.senha").value("senha123"));
    }

    @Test
    void testObterSaldoCartao_QuandoSucesso_DeveRetornarOk() throws Exception {
        when(cartaoService.obterSaldoCartao("123456789")).thenReturn(100.0);

        mockMvc.perform(get("/cartoes/123456789")
        		.with(httpBasic("username", "password")))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));
    }

    @Test
    void testObterSaldoCartao_QuandoNaoEncontrado_DeveRetornarNotFound() throws Exception {
        when(cartaoService.obterSaldoCartao("123456789"))
                .thenThrow(new MiniAutorizadorException(MiniAutorizadorEnum.CARTAO_INEXISTENTE.name()));

        mockMvc.perform(get("/cartoes/123456789")
        		.with(httpBasic("username", "password")))
                .andExpect(status().isNotFound());
    }

    @Test
    void testTransacaoCartao_QuandoSucesso_DeveRetornarCreated() throws Exception {
        TransacaoCartao transacao = new TransacaoCartao("123456789", "senha123", 50.0);
        doNothing().when(cartaoService).processarTransacao(transacao);

        mockMvc.perform(post("/transacoes")
        		.with(httpBasic("username", "password"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCartao\": \"123456789\", \"senha\": \"senha123\", \"valor\": 50.0}"))
                .andExpect(status().isCreated())
                .andExpect(content().string(MiniAutorizadorEnum.OK.name()));
    }
}
