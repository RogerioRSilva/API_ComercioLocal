package api.comercio.local.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTITY - Venda
 *
 * Representa uma venda realizada na loja.
 * Cada venda pertence a um cliente e pode ter vários itens (produtos vendidos).
 *
 * RELACIONAMENTOS:
 * - ManyToOne com Cliente: várias vendas podem pertencer a um mesmo cliente
 * - OneToMany com Estoque: uma venda pode ter vários itens
 */
@Entity
@Table(name = "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    /**
     * CHAVE PRIMÁRIA com auto-incremento
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * RELACIONAMENTO MANY-TO-ONE (Várias Vendas para Um Cliente)
     *
     * @ManyToOne: define o lado DONO do relacionamento
     *   - Este lado contém a FOREIGN KEY (cliente_id) na tabela vendas
     *   - Várias vendas podem pertencer ao mesmo cliente
     *
     * @JoinColumn(name = "cliente_id"):
     *   - define o nome da coluna FK na tabela vendas
     *   - nullable = false: toda venda DEVE ter um cliente
     *
     * Estrutura no banco:
     *   vendas (
     *     id BIGINT PRIMARY KEY,
     *     cliente_id BIGINT NOT NULL,
     *     FOREIGN KEY (cliente_id) REFERENCES clientes(id)
     *   )
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * Data e hora em que a venda foi realizada
     * Preenchida automaticamente antes de salvar no banco
     */
    @Column(nullable = false)
    private LocalDateTime dataVenda;

    /**
     * Valor total da venda
     * Pode ser calculado automaticamente somando os subtotais dos itens
     */
    @Column(nullable = false)
    private BigDecimal valorTotal;

    /**
     * RELACIONAMENTO ONE-TO-MANY (Uma Venda tem Vários Itens)
     *
     * @OneToMany(mappedBy = "venda"):
     *   - define o lado NÃO-DONO do relacionamento
     *   - "venda" é o nome do atributo na classe Estoque que referencia esta classe
     *   - não contém FK, apenas mapeia o relacionamento reverso
     *
     * cascade = CascadeType.ALL:
     *   - quando salvar/atualizar/deletar uma venda, faz o mesmo com os itens
     *   - garante integridade: se deletar a venda, deleta os itens também
     *
     * orphanRemoval = true:
     *   - remove itens órfãos (itens que foram removidos da lista)
     *
     * @Builder.Default:
     *   - inicializa a lista vazia quando usar o Builder do Lombok
     *   - evita NullPointerException
     */
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Estoque> itens = new ArrayList<>();

    /**
     * MÉTODO DE CALLBACK - executado ANTES de persistir no banco
     *
     * @PrePersist: anotação JPA que executa este método antes do INSERT
     *
     * Preenche automaticamente a data da venda com o momento atual
     * Garante que toda venda tenha uma data, mesmo que não seja informada
     */
    @PrePersist
    protected void prePersist() {
        if (dataVenda == null) {
            dataVenda = LocalDateTime.now();
        }
    }
}
