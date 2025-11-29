package api.comercio.local.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.comercio.local.model.Cliente;
import api.comercio.local.repository.ClienteRepository;

/**
 * CONTROLLER - Camada de Controle da API REST
 *
 * Responsável por receber as requisições HTTP dos clientes (ex: Postman, navegador, front-end)
 * e retornar as respostas adequadas.
 *
 * @RestController: indica que esta classe é um controller REST (retorna JSON automaticamente)
 * @RequestMapping: define o caminho base para todos os endpoints deste controller (/api/clientes)
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    /**
     * INJEÇÃO DE DEPENDÊNCIA
     *
     * @Autowired: o Spring automaticamente cria e injeta uma instância do ClienteRepository
     * Isso é chamado de "Inversão de Controle" (IoC) - o Spring gerencia a criação dos objetos
     */
    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * GET /api/clientes
     *
     * Lista todos os clientes cadastrados no banco de dados
     *
     * @return ResponseEntity com status 200 (OK) e a lista de clientes no corpo da resposta
     */
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        // findAll() é um método do JpaRepository que busca todos os registros da tabela
        List<Cliente> clientes = clienteRepository.findAll();

        // ResponseEntity.ok() retorna status 200 (OK) com o corpo da resposta
        return ResponseEntity.ok(clientes);
    }

    /**
     * GET /api/clientes/{id}
     *
     * Busca um cliente específico pelo ID
     *
     * @param id - capturado da URL através de @PathVariable
     * @return ResponseEntity com status 200 (OK) se encontrado, ou 404 (Not Found) se não existir
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        // findById() retorna um Optional<Cliente> (pode conter ou não um valor)
        return clienteRepository.findById(id)
                // Se encontrar, mapeia para ResponseEntity com status 200
                .map(ResponseEntity::ok)
                // Se não encontrar, retorna status 404 (Not Found)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/clientes/cpf/{cpf}
     *
     * Busca um cliente pelo CPF (método customizado definido no Repository)
     *
     * @param cpf - capturado da URL
     * @return ResponseEntity com status 200 se encontrado, ou 404 se não existir
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarPorCpf(@PathVariable String cpf) {
        // findByCpf() é um método customizado que criamos no ClienteRepository
        return clienteRepository.findByCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/clientes
     *
     * Cria um novo cliente no banco de dados
     *
     * @param cliente - dados recebidos no corpo da requisição (JSON) através de @RequestBody
     * @return ResponseEntity com status 201 (Created) e o cliente salvo, ou 409 (Conflict) se CPF já existir
     */
    @PostMapping
    public ResponseEntity<Cliente> criar(@RequestBody Cliente cliente) {
        // Valida se o CPF já está cadastrado para evitar duplicação
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            // Retorna status 409 (Conflict) indicando conflito de dados
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // save() persiste o objeto no banco de dados
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // Retorna status 201 (Created) com o cliente salvo no corpo da resposta
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }

    /**
     * PUT /api/clientes/{id}
     *
     * Atualiza os dados de um cliente existente
     *
     * @param id - ID do cliente a ser atualizado
     * @param cliente - novos dados do cliente recebidos no corpo da requisição
     * @return ResponseEntity com status 200 e cliente atualizado, ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        // Verifica se o cliente existe antes de atualizar
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Define o ID do cliente para garantir que está atualizando o correto
        cliente.setId(id);

        // save() também serve para atualizar (se o ID já existir, faz UPDATE ao invés de INSERT)
        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return ResponseEntity.ok(clienteAtualizado);
    }

    /**
     * DELETE /api/clientes/{id}
     *
     * Remove um cliente do banco de dados
     *
     * ATENÇÃO: Esta operação é irreversível!
     *
     * Em produção, considere usar "soft delete" (marcar como inativo ao invés de deletar)
     * para manter histórico e integridade referencial
     *
     * @param id - ID do cliente a ser deletado
     * @return ResponseEntity com status 204 (No Content) se deletado, ou 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Verifica se o cliente existe antes de tentar deletar
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // deleteById() remove o registro do banco de dados
        clienteRepository.deleteById(id);

        // Retorna status 204 (No Content) - sucesso sem corpo na resposta
        return ResponseEntity.noContent().build();
    }
}
        Cliente clienteSalvo = clienteRepository.save(cliente);

        // Retorna status 201 (Created) indicando que o recurso foi criado com sucesso
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
    }

    /**
     * PUT /api/clientes/{id}
     *
     * Atualiza todos os dados de um cliente existente
     *
     * @param id - ID do cliente a ser atualizado
     * @param cliente - novos dados do cliente
     * @return ResponseEntity com status 200 (OK) e o cliente atualizado, ou 404 se não existir
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        // Verifica se o cliente existe antes de atualizar
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Define o ID do cliente para garantir que estamos atualizando o registro correto
        cliente.setId(id);

        // save() com ID existente faz UPDATE no banco de dados
        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return ResponseEntity.ok(clienteAtualizado);
    }

    /**
     * DELETE /api/clientes/{id}
     *
     * Remove um cliente do banco de dados
     *
     * @param id - ID do cliente a ser removido
     * @return ResponseEntity com status 204 (No Content) se removido, ou 404 se não existir
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Verifica se o cliente existe antes de tentar deletar
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // deleteById() remove o registro do banco de dados
        clienteRepository.deleteById(id);

        // Retorna status 204 (No Content) indicando sucesso sem corpo de resposta
        return ResponseEntity.noContent().build();
    }
}

