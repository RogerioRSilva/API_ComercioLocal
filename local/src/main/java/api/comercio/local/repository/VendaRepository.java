package api.comercio.local.repository;

import api.comercio.local.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REPOSITORY - VendaRepository
 *
 * Interface responsável pela persistência de dados da entidade Venda.
 * Define métodos customizados para consultas específicas de vendas.
 */
@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    /**
     * BUSCA VENDAS DE UM CLIENTE ESPECÍFICO
     *
     * "findByClienteId" é traduzido para:
     * SELECT * FROM vendas WHERE cliente_id = ?
     *
     * Navega pelo relacionamento Venda.cliente.id automaticamente.
     *
     * Útil para:
     *   - Histórico de compras do cliente
     *   - Relatório de vendas por cliente
     *   - Análise de comportamento de compra
     *
     * @param clienteId - ID do cliente
     * @return lista de vendas realizadas por este cliente
     */
    List<Venda> findByClienteId(Long clienteId);
    
    /**
     * BUSCA VENDAS EM UM PERÍODO (ENTRE DUAS DATAS)
     *
     * "findByDataVendaBetween" é traduzido para:
     * SELECT * FROM vendas WHERE data_venda BETWEEN ? AND ?
     *
     * Padrão:
     *   - findBy: indica busca
     *   - DataVenda: propriedade da entidade
     *   - Between: operador que verifica se está entre dois valores (inclusive)
     *
     * IMPORTANTE: a comparação é INCLUSIVA (>= inicio AND <= fim)
     *
     * Útil para:
     *   - Relatórios mensais, semanais, anuais
     *   - Fechamento de caixa
     *   - Análise de vendas por período
     *   - Dashboards e gráficos
     *   - Comissões de vendedores
     *
     * Exemplo de uso:
     *   LocalDateTime inicio = LocalDateTime.of(2024, 11, 1, 0, 0);
     *   LocalDateTime fim = LocalDateTime.of(2024, 11, 30, 23, 59);
     *   List<Venda> vendas = repository.findByDataVendaBetween(inicio, fim);
     *   // Retorna todas as vendas de novembro/2024
     *
     * @param inicio - data/hora inicial do período (inclusivo)
     * @param fim - data/hora final do período (inclusivo)
     * @return lista de vendas realizadas no período especificado
     */
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
}
     *   - Análise de vendas por período
     *   - Fechamento de caixa
     *   - Relatórios gerenciais
     *
     * Exemplo de uso:
     *   LocalDateTime inicio = LocalDateTime.of(2024, 1, 1, 0, 0);
     *   LocalDateTime fim = LocalDateTime.of(2024, 1, 31, 23, 59);
     *   repository.findByDataVendaBetween(inicio, fim)
     *   Retorna todas as vendas de janeiro de 2024
     *
     * @param inicio - data/hora inicial do período (inclusivo)
     * @param fim - data/hora final do período (inclusivo)
     * @return lista de vendas realizadas no período especificado
     */
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
}

