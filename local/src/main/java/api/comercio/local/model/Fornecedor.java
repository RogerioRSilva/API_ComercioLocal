package api.comercio.local.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * ENTITY - Fornecedor
 *
 * Representa os fornecedores que fornecem produtos para a loja.
 * Cada fornecedor pode fornecer vários produtos.
 *
 * RELACIONAMENTOS:
 * - OneToMany com Produto: um fornecedor pode ter vários produtos
 * - OneToOne com Endereco: um fornecedor tem um endereço
 */
@Entity
@Table(name = "fornecedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fornecedor {

    /**
     * CHAVE PRIMÁRIA auto-incrementada
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * NOME DO FORNECEDOR (obrigatório)
     *
     * Exemplos: "Dell Computadores Ltda", "Logitech Brasil", "Microsoft Corporation"
     */
    @Column(nullable = false)
    private String nome;

    /**
     * CNPJ (Cadastro Nacional de Pessoa Jurídica)
     *
     * Deve ser único no banco de dados (não pode ter dois fornecedores com mesmo CNPJ)
     * Formato: 12.345.678/0001-99
     */
    @Column(unique = true)
    private String cnpj;

    /**
     * TELEFONE DE CONTATO
     *
     * Exemplo: "(11) 1234-5678", "(11) 91234-5678"
     */
    private String telefone;

    /**
     * E-MAIL DE CONTATO
     *
     * Exemplo: "contato@dell.com", "vendas@logitech.com"
     */
    private String email;

    /**
     * RELACIONAMENTO ONE-TO-ONE (Um Fornecedor tem Um Endereço)
     *
     * @OneToOne: define um relacionamento de um-para-um
     *   - Um Fornecedor possui um único Endereço
     *   - Um Endereço pertence a apenas um Fornecedor
     *
     * @JoinColumn(name = "endereco_id"):
     *   - cria uma coluna FK na tabela fornecedores chamada "endereco_id"
     *   - esta coluna armazena o ID do endereço associado
     *   - este é o lado DONO do relacionamento
     *
     * cascade = CascadeType.ALL:
     *   - operações no Fornecedor são propagadas para o Endereço
     *   - ao salvar Fornecedor, salva o Endereço automaticamente
     *   - ao deletar Fornecedor, deleta o Endereço também
     *
     * orphanRemoval = true:
     *   - se remover o endereço do fornecedor, o endereço é deletado do banco
     *
     * Exemplo de uso:
     *   Endereco endereco = Endereco.builder()
     *       .logradouro("Av. Paulista")
     *       .numero("2000")
     *       .bairro("Bela Vista")
     *       .cidade("São Paulo")
     *       .estado("SP")
     *       .cep("01310-000")
     *       .build();
     *
     *   Fornecedor fornecedor = Fornecedor.builder()
     *       .nome("Dell Computadores Ltda")
     *       .cnpj("12.345.678/0001-99")
     *       .endereco(endereco)
     *       .build();
     *
     *   fornecedorRepository.save(fornecedor);
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    /**
     * RELACIONAMENTO ONE-TO-MANY (Um Fornecedor tem Vários Produtos)
     *
     * @OneToMany: define um relacionamento de um-para-muitos
     *   - Um Fornecedor pode fornecer vários Produtos
     *   - Vários Produtos pertencem a um Fornecedor
     *
     * mappedBy = "fornecedor":
     *   - indica que o lado DONO do relacionamento é a classe Produto
     *   - o atributo "fornecedor" na classe Produto contém a FK
     *   - este é o lado INVERSO (não-dono) do relacionamento
     *
     * @Builder.Default:
     *   - inicializa a lista vazia quando usar o Builder
     *   - evita NullPointerException
     */
    @OneToMany(mappedBy = "fornecedor")
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    /**
     * MÉTODO AUXILIAR - Adiciona produto mantendo consistência bidirecional
     *
     * Ao adicionar um produto ao fornecedor, também define o fornecedor no produto.
     * Mantém ambos os lados do relacionamento sincronizados.
     *
     * @param produto - produto a ser associado ao fornecedor
     */
    public void addProduto(Produto produto) {
        produtos.add(produto);
        produto.setFornecedor(this);
    }

    /**
     * MÉTODO AUXILIAR - Remove produto mantendo consistência
     *
     * @param produto - produto a ser removido do fornecedor
     */
    public void removeProduto(Produto produto) {
        produtos.remove(produto);
        produto.setFornecedor(null);
    }
}

