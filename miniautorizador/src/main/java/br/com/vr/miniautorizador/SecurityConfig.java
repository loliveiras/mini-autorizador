package br.com.vr.miniautorizador;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	/*
	 * NESTA CLASSE PODEMOS UTILIZAR UMA CONSULTA AO BANCO DE DADOS OU ALGUMA CONFIGURAÇÃO DE SERVIDOR PARA QUE AS CREDENCIAIS SEJAM VALIDADAS 
	 * E NÃO FIQUEM EXPOSITAS NO CÓDIGO.
	*/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        	.authorizeRequests()
            .antMatchers("/cartoes/**", "/transacoes/**").authenticated()  
            .anyRequest().permitAll() 
            .and()
            .httpBasic();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("username")
            .password(passwordEncoder().encode("password"))
            .roles("USER")
            .and()
            .withUser("username")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN");
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}