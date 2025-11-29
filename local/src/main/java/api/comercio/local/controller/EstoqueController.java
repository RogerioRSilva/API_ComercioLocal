package api.comercio.local.controller;

import api.comercio.local.model.Estoque;
import api.comercio.local.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER - EstoqueController
 *
 * Gerencia as requisições HTTP relacionadas a itens de venda (estoque).
 * Endpoints: /api/estoque
 *
 * IMPORTANTE: Apesar do nome "Estoque", esta entidade representa os
 * ITENS DE VENDA (produtos vendidos em cada venda), não o estoque físico.
 *
 * É uma entidade associativa entre Venda e Produto com atributos próprios.
 *
 * Funcionalidades:
 *   - CRUD completo de itens de venda
 *   - Busca itens por venda (detalhar venda)
 *   - Busca histórico de vendas por produto
 */
@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    /**
     * INJEÇÃO DE DEPENDÊNCIA
     * Spring injeta automaticamente uma instância do EstoqueRepository
     */
    @Autowired
    private EstoqueRepository estoqueRepository;

    /**
     * GET /api/estoque
     *
     * Lista todos os itens de venda cadastrados
     *
     * ATENÇÃO: pode retornar muitos registros em produção
     * Considere implementar paginação usando Pageable
     *
     * @return ResponseEntity com status 200 e lista de itens
     */
    @GetMapping
    public ResponseEntity<List<Estoque>> listarTodos() {
        List<Estoque> itens = estoqueRepository.findAll();
        return ResponseEntity.ok(itens);
    }

    /**
     * GET /api/estoque/{id}
     *
     * Busca um item específico pelo ID
     *
     * @param id - ID do item capturado da URL
     * @return ResponseEntity com status 200 (OK) se encontrado, ou 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarPorId(@PathVariable Long id) {
        return estoqueRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/estoque/venda/{vendaId}
     *
     * Busca todos os itens de uma venda específica
     *
     * Útil para:
     *   - Detalhar uma venda (listar produtos vendidos)
     *   - Gerar nota fiscal com itens
     *   - Exibir carrinho de compras concluído
     *
     * Exemplo de uso:
     *   GET /api/estoque/venda/1
     *   Retorna: [
     *     { "produto": "Mouse", "quantidade": 2, "precoUnitario": 25.00 },
     *     { "produto": "Teclado", "quantidade": 1, "precoUnitario": 50.00 }
     *   ]
     *
     * @param vendaId - ID da venda
     * @return ResponseEntity com status 200 e lista de itens da venda
     */
    @GetMapping("/venda/{vendaId}")
    public ResponseEntity<List<Estoque>> buscarPorVenda(@PathVariable Long vendaId) {
        // Busca itens onde venda.id = vendaId
        List<Estoque> itens = estoqueRepository.findByVendaId(vendaId);
        return ResponseEntity.ok(itens);
    }

    /**
     * GET /api/estoque/produto/{produtoId}
     *
     * Busca histórico de vendas de um produto específico
     *
     * Útil para:
     *   - Análise de vendas por produto
     *   - Histórico de preços (ver precoUnitario ao longo do tempo)
     *   - Identificar produtos mais vendidos
     *   - Relatórios de desempenho de produtos
     *
     * Exemplo de uso:
     *   GET /api/estoque/produto/5
     *   Retorna todos os itens de venda onde o produto ID = 5 foi vendido
     *
     * @param produtoId - ID do produto
     * @return ResponseEntity com status 200 e lista de itens com esse produto
     */
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Estoque>> buscarPorProduto(@PathVariable Long produtoId) {
        // Busca itens onde produto.id = produtoId
        List<Estoque> itens = estoqueRepository.findByProdutoId(produtoId);
        return ResponseEntity.ok(itens);
    }

    /**
     * POST /api/estoque
     *
     * Cria um novo item de venda no banco de dados
     *
     * IMPORTANTE: Normalmente, itens são criados junto com a venda através
     * de CASCADE. Mas este endpoint permite adicionar itens individualmente.
     *
     * O subtotal pode ser calculado automaticamente pelo @PrePersist
     *
     * Exemplo de JSON:
     * {
     *   "venda": { "id": 1 },
     *   "produto": { "id": 3 },
     *   "quantidade": 2,
     *   "precoUnitario": 49.90
     * }
     *
     * @param estoque - dados do item recebidos no corpo da requisição
     * @return ResponseEntity com status 201 (Created) e o item salvo
     */
    @PostMapping
    public ResponseEntity<Estoque> criar(@RequestBody Estoque estoque) {
        // O método @PrePersist da entidade irá calcular o subtotal automaticamente
        Estoque estoqueSalvo = estoqueRepository.save(estoque);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueSalvo);
    }

    /**
     * PUT /api/estoque/{id}
     *
     * Atualiza os dados de um item de venda existente
     *
     * ATENÇÃO: Editar itens de venda pode causar inconsistências!
     * Considere se realmente é necessário permitir esta operação.
     *
     * @param id - ID do item a ser atualizado
     * @param estoque - novos dados do item recebidos no corpo da requisição
     * @return ResponseEntity com status 200 e item atualizado, ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Estoque> atualizar(@PathVariable Long id, @RequestBody Estoque estoque) {
        if (!estoqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        estoque.setId(id);
        Estoque estoqueAtualizado = estoqueRepository.save(estoque);
        return ResponseEntity.ok(estoqueAtualizado);
    }

    /**
     * DELETE /api/estoque/{id}
     *
     * Remove um item de venda do banco de dados
     *
     * ATENÇÃO: Deletar itens de venda pode causar problemas!
     *
     * Problemas potenciais:
     *   - Valor total da venda fica incorreto
     *   - Histórico de venda incompleto
     *   - Problemas com nota fiscal
     *
     * Considere:
     *   - Não permitir exclusão de itens após a venda
     *   - Criar um campo "status" (ativo, cancelado)
     *   - Recalcular o valor total da venda após deletar
     *
     * @param id - ID do item a ser deletado
     * @return ResponseEntity com status 204 (No Content) se deletado, ou 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!estoqueRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        estoqueRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

