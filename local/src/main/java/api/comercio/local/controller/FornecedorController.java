package api.comercio.local.controller;

import api.comercio.local.model.Fornecedor;
import api.comercio.local.repository.FornecedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER - FornecedorController
 *
 * Gerencia as requisições HTTP relacionadas a fornecedores.
 * Endpoints: /api/fornecedores
 *
 * Funcionalidades:
 *   - CRUD completo de fornecedores
 *   - Busca por CNPJ
 *   - Validação de CNPJ duplicado
 */
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorController {

    /**
     * INJEÇÃO DE DEPENDÊNCIA
     * Spring injeta automaticamente uma instância do FornecedorRepository
     */
    @Autowired
    private FornecedorRepository fornecedorRepository;

    /**
     * GET /api/fornecedores
     *
     * Lista todos os fornecedores cadastrados
     *
     * @return ResponseEntity com status 200 e lista de fornecedores
     */
    @GetMapping
    public ResponseEntity<List<Fornecedor>> listarTodos() {
        List<Fornecedor> fornecedores = fornecedorRepository.findAll();
        return ResponseEntity.ok(fornecedores);
    }

    /**
     * GET /api/fornecedores/{id}
     *
     * Busca um fornecedor específico pelo ID
     *
     * @param id - ID do fornecedor capturado da URL
     * @return ResponseEntity com status 200 (OK) se encontrado, ou 404 (Not Found)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Fornecedor> buscarPorId(@PathVariable Long id) {
        return fornecedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/fornecedores/cnpj/{cnpj}
     *
     * Busca um fornecedor pelo CNPJ
     *
     * Útil para:
     *   - Verificar se fornecedor já está cadastrado
     *   - Busca rápida por documento
     *   - Validação de dados
     *
     * Exemplo de uso:
     *   GET /api/fornecedores/cnpj/12.345.678/0001-99
     *
     * @param cnpj - CNPJ do fornecedor capturado da URL
     * @return ResponseEntity com status 200 se encontrado, ou 404 se não existir
     */
    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Fornecedor> buscarPorCnpj(@PathVariable String cnpj) {
        return fornecedorRepository.findByCnpj(cnpj)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/fornecedores
     *
     * Cria um novo fornecedor no banco de dados
     *
     * Validação: não permite cadastrar dois fornecedores com o mesmo CNPJ
     *
     * Exemplo de JSON:
     * {
     *   "nome": "Dell Computadores Ltda",
     *   "cnpj": "12.345.678/0001-99",
     *   "telefone": "(11) 1234-5678",
     *   "email": "contato@dell.com",
     *   "endereco": "Av. Paulista, 1000 - SP"
     * }
     *
     * @param fornecedor - dados do fornecedor recebidos no corpo da requisição
     * @return ResponseEntity com status 201 (Created) se sucesso, ou 409 (Conflict) se CNPJ duplicado
     */
    @PostMapping
    public ResponseEntity<Fornecedor> criar(@RequestBody Fornecedor fornecedor) {
        // Valida se o CNPJ já está cadastrado
        if (fornecedorRepository.existsByCnpj(fornecedor.getCnpj())) {
            // Retorna status 409 (Conflict) - conflito de dados
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorSalvo);
    }

    /**
     * PUT /api/fornecedores/{id}
     *
     * Atualiza os dados de um fornecedor existente
     *
     * @param id - ID do fornecedor a ser atualizado
     * @param fornecedor - novos dados do fornecedor recebidos no corpo da requisição
     * @return ResponseEntity com status 200 e fornecedor atualizado, ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @RequestBody Fornecedor fornecedor) {
        if (!fornecedorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        fornecedor.setId(id);
        Fornecedor fornecedorAtualizado = fornecedorRepository.save(fornecedor);
        return ResponseEntity.ok(fornecedorAtualizado);
    }

    /**
     * DELETE /api/fornecedores/{id}
     *
     * Remove um fornecedor do banco de dados
     *
     * ATENÇÃO: Se o fornecedor tiver produtos associados, pode ocorrer erro
     * de integridade referencial, dependendo da estratégia de CASCADE configurada.
     *
     * Alternativas:
     *   - Usar "soft delete" (marcar como inativo)
     *   - Configurar CASCADE para atualizar produtos (setando fornecedor como null)
     *   - Validar se há produtos antes de deletar
     *
     * @param id - ID do fornecedor a ser deletado
     * @return ResponseEntity com status 204 (No Content) se deletado, ou 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!fornecedorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        fornecedorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

