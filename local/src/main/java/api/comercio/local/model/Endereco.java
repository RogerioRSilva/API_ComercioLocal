package api.comercio.local.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITY - Endereco
 *
 * Representa um endereço completo que pode ser associado a Cliente ou Fornecedor.
 *
 * RELACIONAMENTOS:
 * - OneToOne com Cliente: um cliente tem um endereço
 * - OneToOne com Fornecedor: um fornecedor tem um endereço
 *
 * CONCEITO: Separar o endereço em uma entidade própria traz vantagens:
 *   - Reutilização de código
 *   - Facilita validação de CEP
 *   - Permite adicionar mais campos (complemento, referência, etc.)
 *   - Melhor organização do banco de dados
 *   - Facilita integrações com APIs de CEP (ViaCEP, etc.)
 */
@Entity
@Table(name = "enderecos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {

    /**
     * CHAVE PRIMÁRIA com auto-incremento
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * CEP (Código de Endereçamento Postal)
     * Formato: 12345-678
     */
    @Column(length = 9)
    private String cep;

    /**
     * Logradouro (Rua, Avenida, etc.)
     * Exemplo: "Rua das Flores", "Avenida Paulista"
     */
    @Column(length = 255)
    private String logradouro;

    /**
     * Número do endereço
     * Exemplo: "1000", "S/N" (sem número)
     */
    @Column(length = 20)
    private String numero;

    /**
     * Complemento (opcional)
     * Exemplo: "Apto 101", "Bloco B", "Sala 5"
     */
    @Column(length = 100)
    private String complemento;

    /**
     * Bairro
     * Exemplo: "Centro", "Jardim Paulista"
     */
    @Column(length = 100)
    private String bairro;

    /**
     * Cidade
     * Exemplo: "São Paulo", "Rio de Janeiro"
     */
    @Column(length = 100)
    private String cidade;

    /**
     * Estado (UF)
     * Exemplo: "SP", "RJ", "MG"
     */
    @Column(length = 2)
    private String estado;

    /**
     * País (opcional, padrão: Brasil)
     */
    @Column(length = 50)
    @Builder.Default
    private String pais = "Brasil";

    /**
     * MÉTODO AUXILIAR - Retorna endereço completo formatado
     *
     * Útil para exibir o endereço completo em relatórios, notas fiscais, etc.
     *
     * Exemplo de retorno:
     * "Rua das Flores, 1000, Apto 101 - Centro - São Paulo/SP - CEP: 12345-678"
     *
     * @return String com endereço formatado
     */
    public String getEnderecoCompleto() {
        StringBuilder endereco = new StringBuilder();

        if (logradouro != null) {
            endereco.append(logradouro);
        }

        if (numero != null) {
            endereco.append(", ").append(numero);
        }

        if (complemento != null && !complemento.isEmpty()) {
            endereco.append(", ").append(complemento);
        }

        if (bairro != null) {
            endereco.append(" - ").append(bairro);
        }

        if (cidade != null && estado != null) {
            endereco.append(" - ").append(cidade).append("/").append(estado);
        }

        if (cep != null) {
            endereco.append(" - CEP: ").append(cep);
        }

        return endereco.toString();
    }
}

