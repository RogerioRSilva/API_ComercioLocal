package api.comercio.local.repository;

import api.comercio.local.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY - EstoqueRepository
 *
 * Interface responsável pela persistência de dados da entidade Estoque.
 *
 * IMPORTANTE: Apesar do nome "Estoque", esta entidade representa os
 * ITENS DE VENDA (produtos vendidos em cada venda).
 *
 * É uma tabela associativa entre Venda e Produto com atributos próprios
 * (quantidade, preço unitário, subtotal).
 */
@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    /**
     * BUSCA ITENS DE UMA VENDA ESPECÍFICA
     *
     * "findByVendaId" é traduzido para:
     * SELECT * FROM estoque WHERE venda_id = ?
     *
     * Navega pelo relacionamento Estoque.venda.id automaticamente.
     *
     * Útil para:
     *   - Detalhar uma venda (listar produtos vendidos)
     *   - Gerar nota fiscal
     *   - Exibir carrinho de compras concluído
     *   - Cálculo do valor total da venda
     *
     * Exemplo de uso:
     *   List<Estoque> itens = repository.findByVendaId(1L);
     *   // Retorna: [
     *   //   { produto: "Mouse", quantidade: 2, precoUnitario: 25.00, subtotal: 50.00 },
     *   //   { produto: "Teclado", quantidade: 1, precoUnitario: 50.00, subtotal: 50.00 }
     *   // ]
     *
     * @param vendaId - ID da venda
     * @return lista de itens (produtos) desta venda
     */
    List<Estoque> findByVendaId(Long vendaId);

    /**
     * BUSCA HISTÓRICO DE VENDAS DE UM PRODUTO
     *
     * "findByProdutoId" é traduzido para:
     * SELECT * FROM estoque WHERE produto_id = ?
     *
     * Navega pelo relacionamento Estoque.produto.id automaticamente.
     *
     * Útil para:
     *   - Histórico de vendas por produto
     *   - Análise de desempenho de produtos
     *   - Identificar produtos mais vendidos
     *   - Relatórios de faturamento por produto
     *   - Análise de preços ao longo do tempo
     *
     * Exemplo de uso:
     *   List<Estoque> historico = repository.findByProdutoId(5L);
     *   // Retorna todas as vezes que o produto ID=5 foi vendido
     *   // Útil para ver:
     *   //   - Quantas unidades foram vendidas no total
     *   //   - Variação de preço ao longo do tempo
     *   //   - Frequência de vendas
     *
     * @param produtoId - ID do produto
     * @return lista de registros de venda deste produto
     */
    List<Estoque> findByProdutoId(Long produtoId);
}

