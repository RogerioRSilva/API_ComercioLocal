package api.comercio.local.repository;

import api.comercio.local.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY - EnderecoRepository
 *
 * Interface responsável pela persistência de dados da entidade Endereco.
 *
 * NOTA: Geralmente endereços são gerenciados através de Cliente/Fornecedor
 * devido ao CASCADE, mas este repository pode ser útil para:
 *   - Consultas de endereços
 *   - Validação de CEP
 *   - Relatórios geográficos
 *   - Busca por cidade/estado
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    /**
     * BUSCA ENDEREÇOS POR CEP
     *
     * "findByCep" é traduzido para:
     * SELECT * FROM enderecos WHERE cep = ?
     *
     * Útil para:
     *   - Validar se CEP já está cadastrado
     *   - Autocompletar endereço a partir do CEP
     *   - Integração com APIs de CEP (ViaCEP)
     *
     * @param cep - CEP a ser buscado (formato: "12345-678")
     * @return lista de endereços com este CEP
     */
    List<Endereco> findByCep(String cep);

    /**
     * BUSCA ENDEREÇOS POR CIDADE
     *
     * "findByCidade" é traduzido para:
     * SELECT * FROM enderecos WHERE cidade = ?
     *
     * Útil para:
     *   - Relatórios de clientes/fornecedores por cidade
     *   - Análise geográfica de vendas
     *   - Cálculo de frete por região
     *
     * @param cidade - nome da cidade
     * @return lista de endereços desta cidade
     */
    List<Endereco> findByCidade(String cidade);

    /**
     * BUSCA ENDEREÇOS POR ESTADO
     *
     * "findByEstado" é traduzido para:
     * SELECT * FROM enderecos WHERE estado = ?
     *
     * Útil para:
     *   - Relatórios por estado
     *   - Análise de cobertura geográfica
     *   - Segmentação regional
     *
     * @param estado - sigla do estado (UF): "SP", "RJ", "MG"
     * @return lista de endereços deste estado
     */
    List<Endereco> findByEstado(String estado);

    /**
     * BUSCA ENDEREÇOS POR CIDADE E ESTADO
     *
     * "findByCidadeAndEstado" é traduzido para:
     * SELECT * FROM enderecos WHERE cidade = ? AND estado = ?
     *
     * Mais preciso que buscar só por cidade (pode haver cidades com mesmo nome)
     *
     * @param cidade - nome da cidade
     * @param estado - sigla do estado (UF)
     * @return lista de endereços da cidade/estado especificado
     */
    List<Endereco> findByCidadeAndEstado(String cidade, String estado);
}

