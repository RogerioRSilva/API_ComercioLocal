package api.comercio.local.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import api.comercio.local.model.Cliente;

/**
 * REPOSITORY - Camada de Persistência de Dados
 *
 * O Repository é responsável por fazer a comunicação com o banco de dados.
 * É a camada que executa as operações de CRUD (Create, Read, Update, Delete).
 *
 * @Repository: marca esta interface como um componente de persistência do Spring
 *
 * JpaRepository<Cliente, Long>:
 *   - Cliente: tipo da entidade que este repository gerencia
 *   - Long: tipo do ID da entidade (definido com @Id na classe Cliente)
 *
 * Ao estender JpaRepository, automaticamente temos acesso a vários métodos prontos:
 *   - save(entity): salva ou atualiza
 *   - findById(id): busca por ID
 *   - findAll(): busca todos
 *   - deleteById(id): remove por ID
 *   - existsById(id): verifica se existe
 *   - count(): conta registros
 *   - e muitos outros...
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    /**
     * MÉTODO DERIVADO (Derived Query Method)
     *
     * O Spring Data JPA cria automaticamente a implementação deste método
     * baseando-se no nome do método.
     *
     * "existsByCpf" é traduzido para:
     * SELECT EXISTS(SELECT 1 FROM clientes WHERE cpf = ?)
     *
     * Padrão: exists + By + NomeDaPropriedade
     *
     * @param cpf - valor do CPF a ser verificado
     * @return true se existe um cliente com este CPF, false caso contrário
     */
    boolean existsByCpf(String cpf);

    /**
     * MÉTODO DERIVADO com Optional
     *
     * "findByCpf" é traduzido para:
     * SELECT * FROM clientes WHERE cpf = ?
     *
     * Retorna Optional<Cliente> ao invés de Cliente diretamente.
     *
     * VANTAGENS DO OPTIONAL:
     *   - Evita NullPointerException
     *   - Deixa explícito que o resultado pode não existir
     *   - Permite programação funcional (map, filter, orElse, etc.)
     *
     * FORMAS DE USO:
     *
     * 1. Verificar se existe:
     *    Optional<Cliente> resultado = repository.findByCpf("123.456.789-00");
     *    if (resultado.isPresent()) {
     *        Cliente cliente = resultado.get();
     *    }
     *
     * 2. Forma funcional (mais elegante):
     *    repository.findByCpf("123.456.789-00")
     *        .ifPresent(cliente -> System.out.println(cliente.getNome()));
     *
     * 3. Com valor padrão:
     *    Cliente cliente = repository.findByCpf("123.456.789-00")
     *        .orElse(new Cliente());
     *
     * 4. Lançar exceção se não encontrar:
     *    Cliente cliente = repository.findByCpf("123.456.789-00")
     *        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
     *
     * 5. Mapeamento (usado nos Controllers):
     *    return repository.findByCpf(cpf)
     *        .map(ResponseEntity::ok)
     *        .orElse(ResponseEntity.notFound().build());
     *
     * @param cpf - CPF do cliente a ser buscado
     * @return Optional contendo o Cliente se encontrado, ou Optional.empty() se não existir
     */
    Optional<Cliente> findByCpf(String cpf);
}
     *
     * "findByCpf" é traduzido para:
     * SELECT * FROM clientes WHERE cpf = ?
     *
     * Optional<Cliente>: representa um container que pode ou não conter um valor.
     * É uma boa prática para evitar NullPointerException.
     *
     * Uso:
     *   Optional<Cliente> resultado = repository.findByCpf("123.456.789-00");
     *   if (resultado.isPresent()) {
     *       Cliente cliente = resultado.get();
     *   }
     *
     * Ou de forma funcional:
     *   repository.findByCpf("123.456.789-00")
     *       .ifPresent(cliente -> System.out.println(cliente.getNome()));
     *
     * @param cpf - valor do CPF a ser buscado
     * @return Optional contendo o Cliente se encontrado, ou Optional.empty() se não existir
     */
    Optional<Cliente> findByCpf(String cpf);
}
