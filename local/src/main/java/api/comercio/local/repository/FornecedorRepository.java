package api.comercio.local.repository;

import api.comercio.local.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * REPOSITORY - FornecedorRepository
 *
 * Interface responsável pela persistência de dados da entidade Fornecedor.
 * Define métodos customizados para buscar fornecedores por CNPJ.
 */
@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    /**
     * VERIFICA SE EXISTE FORNECEDOR COM O CNPJ INFORMADO
     *
     * "existsByCnpj" é traduzido para:
     * SELECT EXISTS(SELECT 1 FROM fornecedores WHERE cnpj = ?)
     *
     * Retorna true ou false sem precisar buscar todos os dados.
     * Mais eficiente que buscar o objeto completo apenas para verificar existência.
     *
     * Útil para:
     *   - Validação antes de cadastrar novo fornecedor
     *   - Evitar duplicação de CNPJ
     *   - Regras de negócio
     *
     * @param cnpj - CNPJ do fornecedor
     * @return true se existe, false caso contrário
     */
    boolean existsByCnpj(String cnpj);

    /**
     * BUSCA FORNECEDOR POR CNPJ
     *
     * "findByCnpj" é traduzido para:
     * SELECT * FROM fornecedores WHERE cnpj = ?
     *
     * Retorna Optional para evitar NullPointerException.
     *
     * Optional é um container que pode ou não conter um valor:
     *   - Optional.of(valor): contém um valor
     *   - Optional.empty(): está vazio
     *
     * Formas de uso:
     *
     * 1. Verificar se existe:
     *    Optional<Fornecedor> result = repository.findByCnpj("12.345.678/0001-99");
     *    if (result.isPresent()) {
     *        Fornecedor fornecedor = result.get();
     *    }
     *
     * 2. Forma funcional:
     *    repository.findByCnpj("12.345.678/0001-99")
     *        .ifPresent(f -> System.out.println(f.getNome()));
     *
     * 3. Com valor padrão:
     *    Fornecedor fornecedor = repository.findByCnpj("12.345.678/0001-99")
     *        .orElse(new Fornecedor());
     *
     * 4. Lançar exceção se não encontrar:
     *    Fornecedor fornecedor = repository.findByCnpj("12.345.678/0001-99")
     *        .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
     *
     * @param cnpj - CNPJ do fornecedor
     * @return Optional contendo o Fornecedor se encontrado, ou Optional.empty()
     */
    Optional<Fornecedor> findByCnpj(String cnpj);
}

