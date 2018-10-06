package br.com.tenorio.dm111projetofinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
public class Dm111ProjetoFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(Dm111ProjetoFinalApplication.class, args);
	}
}
