package api.comercio.local.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * MODEL/ENTITY - Camada de Modelo de Dados
 *
 * Esta classe representa a entidade "Cliente" no banco de dados.
 * Cada instância desta classe corresponde a uma linha na tabela "clientes".
 *
 * ANOTAÇÕES JPA (Jakarta Persistence API):
 *
 * @Entity: marca esta classe como uma entidade JPA (será mapeada para uma tabela)
 * @Table(name = "clientes"): define o nome da tabela no banco de dados
 *
 * ANOTAÇÕES LOMBOK (reduz código boilerplate):
 *
 * @Getter: gera automaticamente todos os métodos getters (getNome(), getCpf(), etc.)
 * @Setter: gera automaticamente todos os métodos setters (setNome(), setCpf(), etc.)
 * @NoArgsConstructor: gera um construtor sem parâmetros (necessário para o JPA)
 * @AllArgsConstructor: gera um construtor com todos os parâmetros
 * @Builder: implementa o padrão Builder para criar objetos de forma fluente
 *   Exemplo: Cliente cliente = Cliente.builder()
 *                                     .nome("João")
 *                                     .cpf("123.456.789-00")
 *                                     .build();
 */
@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    /**
     * CHAVE PRIMÁRIA
     *
     * @Id: marca este campo como chave primária da tabela
     * @GeneratedValue: configura a geração automática do ID
     * strategy = GenerationType.IDENTITY:
     *   - usa auto-incremento do banco de dados (AUTO_INCREMENT no MySQL/H2)
     *   - o banco gera o ID automaticamente ao inserir um novo registro
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * COLUNA COM RESTRIÇÃO NOT NULL
     *
     * @Column(nullable = false): este campo não pode ser nulo no banco de dados
     * Equivale a: nome VARCHAR(255) NOT NULL
     */
    @Column(nullable = false)
    private String nome;

    /**
     * COLUNA COM RESTRIÇÃO UNIQUE
     *
     * @Column(unique = true): garante que não haverá dois clientes com o mesmo CPF
     * O banco criará um índice único nesta coluna
     * Equivale a: cpf VARCHAR(255) UNIQUE
     */
    @Column(unique = true)
    private String cpf;

    /**
     * COLUNAS SIMPLES
     *
     * Quando não especificamos @Column, o JPA cria a coluna automaticamente
     * com o mesmo nome do atributo e tipo correspondente ao tipo Java.
     */
    private String telefone;

    private String email;

    /**
     * RELACIONAMENTO ONE-TO-ONE (Um Cliente tem Um Endereço)
     *
     * @OneToOne: define um relacionamento de um-para-um
     *   - Um Cliente possui um único Endereço
     *   - Um Endereço pertence a apenas um Cliente
     *
     * @JoinColumn(name = "endereco_id"):
     *   - cria uma coluna FK na tabela clientes chamada "endereco_id"
     *   - esta coluna armazena o ID do endereço associado
     *   - este é o lado DONO do relacionamento
     *
     * cascade = CascadeType.ALL:
     *   - operações no Cliente são propagadas para o Endereço
     *   - ao salvar Cliente, salva o Endereço automaticamente
     *   - ao deletar Cliente, deleta o Endereço também
     *
     * orphanRemoval = true:
     *   - se remover o endereço do cliente, o endereço é deletado do banco
     *   - útil para evitar endereços órfãos (sem dono)
     *
     * Estrutura no banco:
     *   clientes (
     *     id BIGINT PRIMARY KEY,
     *     nome VARCHAR(255),
     *     cpf VARCHAR(255),
     *     endereco_id BIGINT,
     *     FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
     *   )
     *
     * Exemplo de uso:
     *   Endereco endereco = Endereco.builder()
     *       .logradouro("Rua das Flores")
     *       .numero("1000")
     *       .bairro("Centro")
     *       .cidade("São Paulo")
     *       .estado("SP")
     *       .cep("12345-678")
     *       .build();
     *
     *   Cliente cliente = Cliente.builder()
     *       .nome("João Silva")
     *       .cpf("123.456.789-00")
     *       .endereco(endereco)
     *       .build();
     *
     *   // Ao salvar o cliente, o endereço é salvo automaticamente (cascade)
     *   clienteRepository.save(cliente);
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    /**
     * RELACIONAMENTO ONE-TO-MANY (Um Cliente tem Várias Vendas)
     *
     * @OneToMany: define um relacionamento de um-para-muitos
     *   - Um Cliente pode ter várias Vendas
     *   - Várias Vendas pertencem a um Cliente
     *
     * mappedBy = "cliente":
     *   - indica que o lado DONO do relacionamento é a classe Venda
     *   - o atributo "cliente" na classe Venda é quem contém a FK (foreign key)
     *   - este é o lado INVERSO (não-dono) do relacionamento
     *   - evita a criação de uma tabela de junção desnecessária
     *
     * Lado DONO (Venda):
     *   @ManyToOne
     *   @JoinColumn(name = "cliente_id")
     *   private Cliente cliente;
     *
     * Lado INVERSO (Cliente):
     *   @OneToMany(mappedBy = "cliente")
     *   private List<Venda> vendas;
     *
     * IMPORTANTE: inicializar a lista para evitar NullPointerException
     */
    @OneToMany(mappedBy = "cliente")
    private List<Venda> vendas = new ArrayList<>();

    /**
     * MÉTODO AUXILIAR para manter a consistência bidirecional
     *
     * Quando adicionar uma venda ao cliente, também define o cliente na venda.
     * Isso mantém ambos os lados do relacionamento sincronizados.
     */
    public void addVenda(Venda venda) {
        vendas.add(venda);
        venda.setCliente(this);
    }

    /**
     * MÉTODO AUXILIAR para remover vendas mantendo consistência
     */
    public void removeVenda(Venda venda) {
        vendas.remove(venda);
        venda.setCliente(null);
    }
}
