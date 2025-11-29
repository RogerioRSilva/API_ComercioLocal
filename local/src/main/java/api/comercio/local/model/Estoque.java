package api.comercio.local.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * ENTITY - Estoque (ENTIDADE ASSOCIATIVA / TABELA DE JUNÇÃO)
 *
 * Esta entidade representa os ITENS DE UMA VENDA.
 * É uma tabela associativa entre Venda e Produto, mas com atributos próprios.
 *
 * RELACIONAMENTOS:
 * - ManyToOne com Venda: cada item pertence a uma venda
 * - ManyToOne com Produto: cada item referencia um produto
 *
 * CONCEITO: Relacionamento Many-to-Many com atributos extras
 *
 * Sem atributos extras, teríamos:
 *   Venda <---> Produto (tabela de junção simples: venda_id, produto_id)
 *
 * Com atributos extras (quantidade, preço, subtotal), criamos uma ENTIDADE própria:
 *   Venda <--> ItemVenda/Estoque <--> Produto
 *
 * Estrutura no banco:
 *   estoque (
 *     id BIGINT PRIMARY KEY,
 *     venda_id BIGINT NOT NULL,
 *     produto_id BIGINT NOT NULL,
 *     quantidade INT NOT NULL,
 *     preco_unitario DECIMAL(19,2) NOT NULL,
 *     subtotal DECIMAL(19,2) NOT NULL,
 *     FOREIGN KEY (venda_id) REFERENCES vendas(id),
 *     FOREIGN KEY (produto_id) REFERENCES produtos(id)
 *   )
 */
@Entity
@Table(name = "estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estoque {

    /**
     * CHAVE PRIMÁRIA com auto-incremento
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * RELACIONAMENTO MANY-TO-ONE com Venda
     *
     * Vários itens podem pertencer à mesma venda
     * Este lado contém a FK venda_id
     */
    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    /**
     * RELACIONAMENTO MANY-TO-ONE com Produto
     *
     * Vários itens podem referenciar o mesmo produto
     * Este lado contém a FK produto_id
     */
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /**
     * Quantidade do produto vendido neste item
     */
    @Column(nullable = false)
    private Integer quantidade;

    /**
     * Preço unitário do produto no momento da venda
     * Armazena o preço histórico (pode ser diferente do preço atual)
     */
    @Column(nullable = false)
    private BigDecimal precoUnitario;

    /**
     * Subtotal deste item (quantidade * precoUnitario)
     * Pode ser calculado automaticamente
     */
    @Column(nullable = false)
    private BigDecimal subtotal;

    /**
     * MÉTODO DE CALLBACK - executado ANTES de persistir no banco
     *
     * Calcula automaticamente o subtotal se não foi informado
     */
    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (quantidade != null && precoUnitario != null) {
            subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
}

