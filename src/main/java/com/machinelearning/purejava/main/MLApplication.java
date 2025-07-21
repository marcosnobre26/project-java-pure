package com.machinelearning.purejava.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Esta é a classe principal que inicia a aplicação Spring Boot.
 * A anotação @SpringBootApplication habilita a autoconfiguração, a varredura
 * de componentes e define esta classe como uma fonte de configuração.
 */
@SpringBootApplication
public class MLApplication {

    /**
     * O método main agora tem uma única responsabilidade: delegar para a
     * classe SpringApplication para iniciar o servidor web e configurar a aplicação.
     * @param args Argumentos de linha de comando (geralmente não usados aqui).
     */
    public static void main(String[] args) {
        SpringApplication.run(MLApplication.class, args);
    }
}