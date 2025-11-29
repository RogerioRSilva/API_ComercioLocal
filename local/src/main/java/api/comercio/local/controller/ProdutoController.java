package api.comercio.local.controller;

import api.comercio.local.model.Produto;
import api.comercio.local.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER - ProdutoController
 *
 * Gerencia as requisições HTTP relacionadas a produtos.
 * Endpoints: /api/produtos
 *
 * Funcionalidades:
 *   - CRUD completo de produtos
 *   - Busca por nome (parcial, case-insensitive)
 *   - Busca por fornecedor
 *   - Listagem de produtos com estoque baixo
 */
@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    /**
     * INJEÇÃO DE DEPENDÊNCIA
     * Spring injeta automaticamente uma instância do ProdutoRepository
     */
    @Autowired
    private ProdutoRepository produtoRepository;

    /**
     * GET /api/produtos
     *
     * Lista todos os produtos cadastrados
     *
     * @return ResponseEntity com status 200 e lista de produtos
     */
    @GetMapping
    public ResponseEntity<List<Produto>> listarTodos() {
        List<Produto> produtos = produtoRepository.findAll();
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/{id}
     *
     * Busca um produto específico pelo ID
     *
     * @param id - ID do produto capturado da URL
     * @return ResponseEntity com status 200 (OK) se encontrado, ou 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/produtos/buscar?nome=mouse
     *
     * Busca produtos por nome (busca parcial, ignora maiúsculas/minúsculas)
     *
     * @RequestParam: captura parâmetro da query string (?nome=valor)
     *
     * Exemplo de uso:
     *   GET /api/produtos/buscar?nome=mouse
     *   Retorna: ["Mouse Logitech", "Mouse Gamer", "Mouse USB"]
     *
     * @param nome - termo de busca (pode ser parte do nome)
     * @return ResponseEntity com status 200 e lista de produtos encontrados
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Produto>> buscarPorNome(@RequestParam String nome) {
        // findByNomeContainingIgnoreCase: método gerado pelo Spring Data JPA
        // - Containing: busca parcial (LIKE %nome%)
        // - IgnoreCase: não diferencia maiúsculas de minúsculas
        List<Produto> produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/fornecedor/{fornecedorId}
     *
     * Busca todos os produtos de um fornecedor específico
     *
     * Útil para:
     *   - Listar catálogo de produtos por fornecedor
     *   - Análise de fornecedores
     *   - Gestão de estoque por fornecedor
     *
     * @param fornecedorId - ID do fornecedor
     * @return ResponseEntity com status 200 e lista de produtos do fornecedor
     */
    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<Produto>> buscarPorFornecedor(@PathVariable Long fornecedorId) {
        // Busca produtos onde fornecedor.id = fornecedorId
        List<Produto> produtos = produtoRepository.findByFornecedorId(fornecedorId);
        return ResponseEntity.ok(produtos);
    }

    /**
     * GET /api/produtos/estoque-baixo?quantidade=10
     *
     * Busca produtos com estoque abaixo de uma quantidade específica
     *
     * @RequestParam(defaultValue = "10"): se não informar, usa 10 como padrão
     *
     * Útil para:
     *   - Alertas de reposição de estoque
     *   - Relatórios de produtos com estoque crítico
     *   - Gestão de compras
     *
     * Exemplo de uso:
     *   GET /api/produtos/estoque-baixo (retorna produtos com estoque < 10)
     *   GET /api/produtos/estoque-baixo?quantidade=20 (retorna produtos com estoque < 20)
     *
     * @param quantidade - limite de estoque (padrão: 10)
     * @return ResponseEntity com status 200 e lista de produtos com estoque baixo
     */
    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<Produto>> buscarEstoqueBaixo(
            @RequestParam(defaultValue = "10") Integer quantidade) {
        // findByQuantidadeEstoqueLessThan: busca produtos onde quantidade_estoque < valor
        List<Produto> produtos = produtoRepository.findByQuantidadeEstoqueLessThan(quantidade);
        return ResponseEntity.ok(produtos);
    }

    /**
     * POST /api/produtos
     *
     * Cria um novo produto no banco de dados
     *
     * O produto pode ser cadastrado COM ou SEM fornecedor
     * Se incluir o fornecedor, enviar apenas o ID: { "fornecedor": { "id": 1 } }
     *
     * Exemplo de JSON:
     * {
     *   "nome": "Mouse Gamer",
     *   "descricao": "Mouse RGB 16000 DPI",
     *   "preco": 299.90,
     *   "quantidadeEstoque": 50,
     *   "fornecedor": { "id": 1 }
     * }
     *
     * @param produto - dados do produto recebidos no corpo da requisição
     * @return ResponseEntity com status 201 (Created) e o produto salvo
     */
    @PostMapping
    public ResponseEntity<Produto> criar(@RequestBody Produto produto) {
        Produto produtoSalvo = produtoRepository.save(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    /**
     * PUT /api/produtos/{id}
     *
     * Atualiza os dados de um produto existente
     *
     * @param id - ID do produto a ser atualizado
     * @param produto - novos dados do produto recebidos no corpo da requisição
     * @return ResponseEntity com status 200 e produto atualizado, ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        produto.setId(id);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return ResponseEntity.ok(produtoAtualizado);
    }

    /**
     * DELETE /api/produtos/{id}
     *
     * Remove um produto do banco de dados
     *
     * ATENÇÃO: Se o produto já foi vendido (tem registros na tabela estoque),
     * pode haver erro de integridade referencial.
     *
     * Solução: usar "soft delete" ou CASCADE para gerenciar relacionamentos
     *
     * @param id - ID do produto a ser deletado
     * @return ResponseEntity com status 204 (No Content) se deletado, ou 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!produtoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        produtoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

