package api.comercio.local;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASSE PRINCIPAL DA APLICAÇÃO SPRING BOOT
 *
 * Esta é a classe de entrada (entry point) da aplicação.
 * Quando executar o projeto, este método main() será chamado.
 *
 * @SpringBootApplication: Anotação composta que inclui:
 *
 *   1. @Configuration:
 *      - Marca esta classe como fonte de definições de beans
 *      - Permite configurar componentes do Spring
 *
 *   2. @EnableAutoConfiguration:
 *      - Habilita a configuração automática do Spring Boot
 *      - Detecta automaticamente bibliotecas no classpath e as configura
 *      - Exemplo: se detectar H2, configura o banco automaticamente
 *
 *   3. @ComponentScan:
 *      - Escaneia o pacote atual e subpacotes em busca de componentes
 *      - Detecta: @Controller, @Service, @Repository, @Component
 *      - Neste caso, escaneia: api.comercio.local e todos os subpacotes
 *
 * ESTRUTURA DE PACOTES DETECTADA:
 *   api.comercio.local
 *   ├── controller   → @RestController (Controllers REST)
 *   ├── model        → @Entity (Entidades JPA)
 *   ├── repository   → @Repository (Repositories JPA)
 *   └── service      → @Service (Camada de negócio - se houver)
 *
 * PARA EXECUTAR A APLICAÇÃO:
 *   1. Via IDE: Run > Run 'App'
 *   2. Via Maven: mvn spring-boot:run
 *   3. Via JAR: java -jar target/local-0.0.1-SNAPSHOT.jar
 *
 * Após iniciar, acesse:
 *   - API REST: http://localhost:8080/api
 *   - Console H2: http://localhost:8080/h2-console
 */
@SpringBootApplication
public class App {

    /**
     * MÉTODO MAIN - PONTO DE ENTRADA DA APLICAÇÃO
     *
     * SpringApplication.run() faz:
     *   1. Cria o ApplicationContext (container do Spring)
     *   2. Registra todos os beans (@Component, @Service, @Repository, etc.)
     *   3. Configura automaticamente o servidor web (Tomcat embutido)
     *   4. Configura o banco de dados (H2)
     *   5. Mapeia os endpoints REST dos Controllers
     *   6. Inicia o servidor na porta configurada (8080)
     *
     * @param args - argumentos da linha de comando (opcional)
     */
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
