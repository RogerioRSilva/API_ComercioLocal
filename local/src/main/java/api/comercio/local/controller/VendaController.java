package api.comercio.local.controller;

import api.comercio.local.model.Venda;
import api.comercio.local.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CONTROLLER - VendaController
 *
 * Gerencia as requisições HTTP relacionadas a vendas.
 * Endpoints: /api/vendas
 *
 * Funcionalidades:
 *   - CRUD completo de vendas
 *   - Busca por cliente
 *   - Busca por período (data inicial e final)
 */
@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    /**
     * INJEÇÃO DE DEPENDÊNCIA
     * Spring injeta automaticamente uma instância do VendaRepository
     */
    @Autowired
    private VendaRepository vendaRepository;

    /**
     * GET /api/vendas
     *
     * Lista todas as vendas cadastradas
     *
     * @return ResponseEntity com status 200 e lista de vendas
     */
    @GetMapping
    public ResponseEntity<List<Venda>> listarTodos() {
        List<Venda> vendas = vendaRepository.findAll();
        return ResponseEntity.ok(vendas);
    }

    /**
     * GET /api/vendas/{id}
     *
     * Busca uma venda específica pelo ID
     *
     * @param id - ID da venda capturado da URL
     * @return ResponseEntity com status 200 (OK) se encontrado, ou 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Long id) {
        return vendaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/vendas/cliente/{clienteId}
     *
     * Busca todas as vendas de um cliente específico
     *
     * Útil para:
     *   - Histórico de compras do cliente
     *   - Análise de comportamento de compra
     *   - Programa de fidelidade
     *   - Relatórios de vendas por cliente
     *
     * @param clienteId - ID do cliente
     * @return ResponseEntity com status 200 e lista de vendas do cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venda>> buscarPorCliente(@PathVariable Long clienteId) {
        // Busca vendas onde cliente.id = clienteId
        List<Venda> vendas = vendaRepository.findByClienteId(clienteId);
        return ResponseEntity.ok(vendas);
    }

    /**
     * GET /api/vendas/periodo?inicio=2024-01-01T00:00:00&fim=2024-12-31T23:59:59
     *
     * Busca vendas realizadas em um período específico
     *
     * @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME):
     *   - Converte automaticamente a string da URL para LocalDateTime
     *   - Formato esperado: yyyy-MM-ddTHH:mm:ss
     *   - Exemplo: 2024-11-28T10:30:00
     *
     * Útil para:
     *   - Relatórios mensais/anuais
     *   - Análise de vendas por período
     *   - Fechamento de caixa
     *   - Dashboards e gráficos
     *
     * Exemplo de uso:
     *   GET /api/vendas/periodo?inicio=2024-11-01T00:00:00&fim=2024-11-30T23:59:59
     *   Retorna todas as vendas de novembro de 2024
     *
     * @param inicio - data/hora inicial do período
     * @param fim - data/hora final do período
     * @return ResponseEntity com status 200 e lista de vendas no período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<Venda>> buscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        // findByDataVendaBetween: busca vendas onde data_venda está entre inicio e fim
        List<Venda> vendas = vendaRepository.findByDataVendaBetween(inicio, fim);
        return ResponseEntity.ok(vendas);
    }

    /**
     * POST /api/vendas
     *
     * Cria uma nova venda no banco de dados
     *
     * A data da venda é preenchida automaticamente pelo @PrePersist na entidade
     *
     * Exemplo de JSON SIMPLES (sem itens):
     * {
     *   "cliente": { "id": 1 },
     *   "valorTotal": 899.80
     * }
     *
     * Exemplo de JSON COMPLETO (com itens):
     * {
     *   "cliente": { "id": 1 },
     *   "valorTotal": 899.80,
     *   "itens": [
     *     {
     *       "produto": { "id": 1 },
     *       "quantidade": 2,
     *       "precoUnitario": 299.90,
     *       "subtotal": 599.80
     *     },
     *     {
     *       "produto": { "id": 2 },
     *       "quantidade": 1,
     *       "precoUnitario": 300.00,
     *       "subtotal": 300.00
     *     }
     *   ]
     * }
     *
     * @param venda - dados da venda recebidos no corpo da requisição
     * @return ResponseEntity com status 201 (Created) e a venda salva
     */
    @PostMapping
    public ResponseEntity<Venda> criar(@RequestBody Venda venda) {
        // O método @PrePersist da entidade Venda irá preencher automaticamente a dataVenda
        Venda vendaSalva = vendaRepository.save(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalva);
    }

    /**
     * PUT /api/vendas/{id}
     *
     * Atualiza os dados de uma venda existente
     *
     * ATENÇÃO: Atualizar vendas pode não ser uma boa prática em sistemas reais
     * Considere criar um histórico de alterações ou não permitir edição
     *
     * @param id - ID da venda a ser atualizada
     * @param venda - novos dados da venda recebidos no corpo da requisição
     * @return ResponseEntity com status 200 e venda atualizada, ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Venda> atualizar(@PathVariable Long id, @RequestBody Venda venda) {
        if (!vendaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        venda.setId(id);
        Venda vendaAtualizada = vendaRepository.save(venda);
        return ResponseEntity.ok(vendaAtualizada);
    }

    /**
     * DELETE /api/vendas/{id}
     *
     * Remove uma venda do banco de dados
     *
     * ATENÇÃO: Deletar vendas pode não ser adequado em ambientes de produção!
     *
     * Problemas:
     *   - Perde histórico de vendas
     *   - Pode causar inconsistências no estoque
     *   - Dificulta auditoria e análises
     *
     * Alternativas recomendadas:
     *   - Usar "soft delete" (marcar como cancelada/inativa)
     *   - Criar um campo "status" (ativa, cancelada, devolvida)
     *   - Manter histórico de todas as operações
     *
     * CASCADE configurado: ao deletar a venda, deleta também os itens (estoque)
     *
     * @param id - ID da venda a ser deletada
     * @return ResponseEntity com status 204 (No Content) se deletada, ou 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!vendaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        vendaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

