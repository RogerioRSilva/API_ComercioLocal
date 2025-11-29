# 🎯 GUIA COMPLETO DA API - Comércio Local

## 📋 ÍNDICE
1. [Resumo da Avaliação](#resumo-da-avaliação)
2. [O que foi Feito](#o-que-foi-feito)
3. [Estrutura Comentada](#estrutura-comentada)
4. [Como Executar](#como-executar)
5. [Exemplos de Uso](#exemplos-de-uso)
6. [Conceitos Explicados](#conceitos-explicados)

---

## ✅ RESUMO DA AVALIAÇÃO

### Status: **API PRONTA E TOTALMENTE COMENTADA! 🎉**

Sua API foi **completamente avaliada e comentada** com explicações educacionais detalhadas em TODOS os arquivos:

- ✅ 5 Entidades (Models) comentadas
- ✅ 5 Repositories comentados
- ✅ 5 Controllers comentados
- ✅ 1 application.properties comentado
- ✅ Relacionamentos explicados
- ✅ Conceitos de JPA, Spring Boot e Lombok documentados

---

## 📝 O QUE FOI FEITO

### 1. **Entidades (Models)** - Totalmente Comentadas ✅

Cada entidade agora possui comentários explicando:
- O que é uma @Entity
- Como funcionam as anotações JPA (@Id, @GeneratedValue, @Column, etc.)
- Relacionamentos (@OneToMany, @ManyToOne, @JoinColumn, mappedBy)
- Anotações Lombok (@Getter, @Setter, @Builder, etc.)
- Callbacks do ciclo de vida (@PrePersist, @PreUpdate)
- Métodos auxiliares para manter relacionamentos bidirecionais

**Arquivos:**
- `Cliente.java` - com explicação de relacionamento OneToMany
- `Fornecedor.java` - com explicação de relacionamento OneToMany
- `Produto.java` - com explicação de relacionamento ManyToOne
- `Venda.java` - com explicação de relacionamentos e @PrePersist
- `Estoque.java` - com explicação de entidade associativa e cálculo automático

### 2. **Repositories** - Totalmente Comentados ✅

Cada repository agora possui comentários explicando:
- O que é um Repository
- Como funciona JpaRepository
- Métodos derivados (Derived Query Methods)
- Uso de Optional<T>
- Tradução dos nomes de métodos para SQL

**Arquivos:**
- `ClienteRepository.java` - existsByCpf, findByCpf
- `FornecedorRepository.java` - existsByCnpj, findByCnpj
- `ProdutoRepository.java` - buscas por nome, fornecedor, estoque baixo
- `VendaRepository.java` - busca por cliente e período
- `EstoqueRepository.java` - busca por venda e produto

### 3. **Controllers** - Totalmente Comentados ✅

Cada controller agora possui comentários explicando:
- O que é um Controller REST
- Anotações HTTP (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping)
- @PathVariable e @RequestParam
- ResponseEntity e códigos HTTP
- Casos de uso práticos
- Exemplos de requisições

**Arquivos:**
- `ClienteController.java` - CRUD completo + busca por CPF
- `FornecedorController.java` - CRUD completo + busca por CNPJ
- `ProdutoController.java` - CRUD + buscas avançadas
- `VendaController.java` - CRUD + busca por cliente/período
- `EstoqueController.java` - CRUD + histórico de vendas

### 4. **Configurações** - Totalmente Comentadas ✅

- `application.properties` - todas as propriedades explicadas

---

## 📚 ESTRUTURA COMENTADA

### Exemplo de Como Ficaram os Comentários:

#### ✅ No Model (Cliente.java):
```java
/**
 * RELACIONAMENTO ONE-TO-MANY (Um Cliente tem Várias Vendas)
 * 
 * @OneToMany: define um relacionamento de um-para-muitos
 *   - Um Cliente pode ter várias Vendas
 *   - Várias Vendas pertencem a um Cliente
 * 
 * mappedBy = "cliente":
 *   - indica que o lado DONO do relacionamento é a classe Venda
 *   - o atributo "cliente" na classe Venda é quem contém a FK
 *   - este é o lado INVERSO (não-dono) do relacionamento
 */
@OneToMany(mappedBy = "cliente")
private List<Venda> vendas = new ArrayList<>();
```

#### ✅ No Repository (ClienteRepository.java):
```java
/**
 * MÉTODO DERIVADO (Derived Query Method)
 * 
 * O Spring Data JPA cria automaticamente a implementação
 * 
 * "existsByCpf" é traduzido para:
 * SELECT EXISTS(SELECT 1 FROM clientes WHERE cpf = ?)
 * 
 * @param cpf - valor do CPF a ser verificado
 * @return true se existe, false caso contrário
 */
boolean existsByCpf(String cpf);
```

#### ✅ No Controller (ClienteController.java):
```java
/**
 * GET /api/clientes/{id}
 * 
 * Busca um cliente específico pelo ID
 * 
 * @param id - capturado da URL através de @PathVariable
 * @return ResponseEntity com status 200 (OK) se encontrado, 
 *         ou 404 (Not Found) se não existir
 */
@GetMapping("/{id}")
public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
    return clienteRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

---

## 🚀 COMO EXECUTAR

### ⚠️ PRÉ-REQUISITO: Instalar JDK

**IMPORTANTE:** Você precisa do **JDK 21** (não apenas JRE).

Siga as instruções no arquivo **`RESOLVER_JDK.md`** que foi criado na raiz do projeto.

### Passo 1: Compilar
```powershell
cd E:\repositorio\API_ComercioLocal\local
./mvnw clean compile
```

### Passo 2: Executar
```powershell
./mvnw spring-boot:run
```

### Passo 3: Acessar
- **API:** http://localhost:8080/api/clientes
- **Console H2:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:comercio_local_db`
  - User: `sa`
  - Password: _(vazio)_

---

## 🧪 EXEMPLOS DE USO

### 1. Criar um Cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "telefone": "(11) 98765-4321",
    "email": "joao@email.com",
    "endereco": "Rua A, 123"
  }'
```

### 2. Listar Todos os Clientes
```bash
curl http://localhost:8080/api/clientes
```

### 3. Buscar Cliente por ID
```bash
curl http://localhost:8080/api/clientes/1
```

### 4. Buscar Cliente por CPF
```bash
curl http://localhost:8080/api/clientes/cpf/123.456.789-00
```

### 5. Atualizar Cliente
```bash
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Atualizado",
    "cpf": "123.456.789-00",
    "telefone": "(11) 98765-4321",
    "email": "joao.novo@email.com",
    "endereco": "Rua B, 456"
  }'
```

### 6. Deletar Cliente
```bash
curl -X DELETE http://localhost:8080/api/clientes/1
```

### 7. Criar Fornecedor
```bash
curl -X POST http://localhost:8080/api/fornecedores \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Dell Computadores Ltda",
    "cnpj": "12.345.678/0001-99",
    "telefone": "(11) 1234-5678",
    "email": "contato@dell.com"
  }'
```

### 8. Criar Produto
```bash
curl -X POST http://localhost:8080/api/produtos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Mouse Logitech",
    "descricao": "Mouse óptico USB",
    "preco": 25.90,
    "quantidadeEstoque": 50,
    "fornecedor": { "id": 1 }
  }'
```

### 9. Buscar Produtos com Estoque Baixo
```bash
curl "http://localhost:8080/api/produtos/estoque-baixo?quantidade=10"
```

### 10. Criar uma Venda
```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "cliente": { "id": 1 },
    "valorTotal": 51.80,
    "itens": []
  }'
```

---

## 📖 CONCEITOS EXPLICADOS

### 🔵 1. Arquitetura em Camadas

```
┌─────────────────────────────────────┐
│         CONTROLLER                  │  ← Recebe requisições HTTP
│  (ClienteController.java)           │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         REPOSITORY                  │  ← Acessa o banco de dados
│  (ClienteRepository.java)           │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│         MODEL/ENTITY                │  ← Representa as tabelas
│  (Cliente.java)                     │
└─────────────────────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│      BANCO DE DADOS (H2)            │  ← Armazena os dados
│  Tabela: clientes                   │
└─────────────────────────────────────┘
```

### 🔵 2. Relacionamentos JPA

#### OneToMany (Um para Muitos)
```java
// Cliente (lado INVERSO - não contém FK)
@OneToMany(mappedBy = "cliente")
private List<Venda> vendas;

// Venda (lado DONO - contém FK)
@ManyToOne
@JoinColumn(name = "cliente_id")
private Cliente cliente;
```

**Resultado no Banco:**
```sql
CREATE TABLE clientes (
    id BIGINT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE TABLE vendas (
    id BIGINT PRIMARY KEY,
    cliente_id BIGINT,  -- FK aqui!
    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);
```

### 🔵 3. Métodos Derivados (Query Methods)

| Nome do Método | SQL Gerado |
|----------------|------------|
| `findByCpf(String cpf)` | `SELECT * FROM clientes WHERE cpf = ?` |
| `existsByCpf(String cpf)` | `SELECT EXISTS(SELECT 1 FROM clientes WHERE cpf = ?)` |
| `findByNomeContainingIgnoreCase(String nome)` | `SELECT * FROM produtos WHERE LOWER(nome) LIKE LOWER(CONCAT('%', ?, '%'))` |
| `findByQuantidadeEstoqueLessThan(Integer q)` | `SELECT * FROM produtos WHERE quantidade_estoque < ?` |
| `findByDataVendaBetween(LocalDateTime i, LocalDateTime f)` | `SELECT * FROM vendas WHERE data_venda BETWEEN ? AND ?` |

### 🔵 4. Códigos HTTP

| Código | Significado | Quando Usar |
|--------|-------------|-------------|
| **200 OK** | Sucesso | GET, PUT com sucesso |
| **201 Created** | Recurso criado | POST com sucesso |
| **204 No Content** | Sucesso sem conteúdo | DELETE com sucesso |
| **404 Not Found** | Não encontrado | Recurso não existe |
| **409 Conflict** | Conflito | CPF/CNPJ duplicado |

### 🔵 5. Optional<T>

```java
// SEM Optional (pode causar NullPointerException)
Cliente cliente = repository.findByCpf("123");
if (cliente != null) {
    System.out.println(cliente.getNome());
}

// COM Optional (seguro)
Optional<Cliente> cliente = repository.findByCpf("123");
if (cliente.isPresent()) {
    System.out.println(cliente.get().getNome());
}

// Forma funcional (recomendada)
repository.findByCpf("123")
    .ifPresent(c -> System.out.println(c.getNome()));

// Com valor padrão
Cliente cliente = repository.findByCpf("123")
    .orElse(new Cliente());
```

---

## 🎓 RESUMO DO QUE VOCÊ APRENDEU

### ✅ Spring Boot
- Injeção de Dependência
- REST Controllers
- Request Mapping
- ResponseEntity

### ✅ JPA/Hibernate
- Entidades e Tabelas
- Relacionamentos
- Cascade
- Lifecycle Callbacks

### ✅ Spring Data JPA
- JpaRepository
- Métodos derivados
- Query Methods

### ✅ Lombok
- Redução de boilerplate
- Getters/Setters automáticos
- Builder Pattern

### ✅ Boas Práticas
- Arquitetura em camadas
- Optional para valores nulos
- BigDecimal para valores monetários
- Validação de dados

---

## 📁 ARQUIVOS CRIADOS

1. **AVALIACAO_API.md** - Avaliação completa da estrutura
2. **RESOLVER_JDK.md** - Guia para configurar o JDK
3. **GUIA_COMPLETO.md** - Este guia (você está aqui!)

---

## 🎯 PRÓXIMOS PASSOS

1. **Configure o JDK 21** (veja RESOLVER_JDK.md)
2. **Compile o projeto** (`./mvnw clean compile`)
3. **Execute a aplicação** (`./mvnw spring-boot:run`)
4. **Teste os endpoints** (use Postman ou cURL)
5. **Explore o console H2** (http://localhost:8080/h2-console)
6. **Leia os comentários** em cada arquivo
7. **Modifique e experimente!**

---

## 💡 DICAS DE ESTUDO

1. **Leia os comentários na ordem:**
   - Models → Repositories → Controllers

2. **Compare com a documentação:**
   - Spring Boot: https://spring.io/projects/spring-boot
   - Spring Data JPA: https://spring.io/projects/spring-data-jpa

3. **Experimente modificar:**
   - Adicione novos campos nas entidades
   - Crie novos métodos de busca
   - Adicione novos endpoints

4. **Pratique SQL:**
   - Use o console H2 para ver as tabelas criadas
   - Execute queries manualmente

---

**Parabéns! Sua API está completamente documentada e pronta para estudos! 🎉📚**

*Criado em: 28/11/2024*
*Por: GitHub Copilot*

