package api.comercio.local.repository;

import api.comercio.local.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY - ProdutoRepository
 *
 * Interface responsável pela persistência de dados da entidade Produto.
 * Estende JpaRepository para herdar métodos CRUD básicos.
 *
 * MÉTODOS CUSTOMIZADOS:
 * Define métodos de busca específicos usando Query Methods do Spring Data JPA
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    /**
     * BUSCA POR NOME (case-insensitive, busca parcial)
     *
     * "findByNomeContainingIgnoreCase" é traduzido para:
     * SELECT * FROM produtos WHERE LOWER(nome) LIKE LOWER(CONCAT('%', ?, '%'))
     *
     * Padrão:
     *   - findBy: indica busca
     *   - Nome: propriedade da entidade
     *   - Containing: busca parcial (LIKE %valor%)
     *   - IgnoreCase: ignora maiúsculas/minúsculas
     *
     * Exemplo de uso:
     *   repository.findByNomeContainingIgnoreCase("mouse")
     *   Retorna: ["Mouse Logitech", "Mouse Gamer", "mouse usb"]
     *
     * @param nome - termo de busca (pode ser parcial)
     * @return lista de produtos cujo nome contém o termo buscado
     */
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    /**
     * BUSCA PRODUTOS DE UM FORNECEDOR ESPECÍFICO
     *
     * "findByFornecedorId" é traduzido para:
     * SELECT * FROM produtos WHERE fornecedor_id = ?
     *
     * Padrão:
     *   - findBy: indica busca
     *   - Fornecedor: relacionamento na entidade Produto
     *   - Id: propriedade da entidade relacionada (Fornecedor)
     *
     * O Spring Data JPA navega automaticamente pelos relacionamentos:
     *   Produto.fornecedor.id
     *
     * Útil para:
     *   - Listar todos os produtos de um fornecedor
     *   - Análise de fornecedores
     *   - Relatórios de compras por fornecedor
     *
     * @param fornecedorId - ID do fornecedor
     * @return lista de produtos fornecidos por este fornecedor
     */
    List<Produto> findByFornecedorId(Long fornecedorId);

    /**
     * BUSCA PRODUTOS COM ESTOQUE BAIXO
     *
     * "findByQuantidadeEstoqueLessThan" é traduzido para:
     * SELECT * FROM produtos WHERE quantidade_estoque < ?
     *
     * Padrão:
     *   - findBy: indica busca
     *   - QuantidadeEstoque: propriedade da entidade
     *   - LessThan: operador de comparação (<)
     *
     * OUTROS OPERADORES DISPONÍVEIS:
     *   - LessThan: menor que (<)
     *   - LessThanEqual: menor ou igual (<=)
     *   - GreaterThan: maior que (>)
     *   - GreaterThanEqual: maior ou igual (>=)
     *   - Between: entre dois valores
     *   - IsNull: valor nulo
     *   - IsNotNull: valor não nulo
     *
     * Útil para:
     *   - Alertas de reposição de estoque
     *   - Relatórios de produtos com estoque crítico
     *   - Gestão de compras
     *   - Dashboard de estoque
     *
     * Exemplo de uso:
     *   repository.findByQuantidadeEstoqueLessThan(10)
     *   Retorna todos os produtos com menos de 10 unidades
     *
     * @param quantidade - limite de quantidade em estoque
     * @return lista de produtos com estoque abaixo do limite
     */
    List<Produto> findByQuantidadeEstoqueLessThan(Integer quantidade);
}
     *
     * Spring Data JPA navega automaticamente pelo relacionamento:
     *   Produto.fornecedor.id
     *
     * @param fornecedorId - ID do fornecedor
     * @return lista de produtos fornecidos por este fornecedor
     */
    List<Produto> findByFornecedorId(Long fornecedorId);
    
    /**
     * BUSCA PRODUTOS COM ESTOQUE BAIXO
     *
     * "findByQuantidadeEstoqueLessThan" é traduzido para:
     * SELECT * FROM produtos WHERE quantidade_estoque < ?
     *
     * Padrão:
     *   - findBy: indica busca
     *   - QuantidadeEstoque: propriedade da entidade
     *   - LessThan: operador de comparação (<)
     *
     * Outros operadores disponíveis:
     *   - GreaterThan: maior que (>)
     *   - LessThanEqual: menor ou igual (<=)
     *   - GreaterThanEqual: maior ou igual (>=)
     *   - Between: entre dois valores
     *
     * Útil para alertas de reposição de estoque.
     *
     * Exemplo de uso:
     *   repository.findByQuantidadeEstoqueLessThan(10)
     *   Retorna produtos com menos de 10 unidades em estoque
     *
     * @param quantidade - limite de estoque
     * @return lista de produtos com estoque abaixo do limite
     */
    List<Produto> findByQuantidadeEstoqueLessThan(Integer quantidade);
}

