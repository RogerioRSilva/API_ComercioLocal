package api.comercio.local.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * ENTITY - Produto
 *
 * Representa os produtos disponíveis para venda na loja.
 * Cada produto pode ter um fornecedor associado.
 *
 * RELACIONAMENTO:
 * - ManyToOne com Fornecedor: vários produtos podem ser do mesmo fornecedor
 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    /**
     * CHAVE PRIMÁRIA auto-incrementada
     */
    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @ManyToOne
    /**
     * NOME DO PRODUTO (obrigatório)
     *
     * Exemplos: "Notebook Dell", "Mouse Logitech", "Teclado Mecânico"
     */
