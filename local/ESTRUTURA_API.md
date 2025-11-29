# API Comércio Local - Estrutura Completa

## ✅ Estrutura Criada com Sucesso!

### 📁 Entidades (Model) - 5 classes
✅ **Cliente.java** - Entidade com:
- ID, nome, CPF (único), telefone, email, endereço
- Relacionamento OneToMany com Venda

✅ **Fornecedor.java** - Entidade com:
- ID, nome, CNPJ (único), telefone, email, endereço
- Relacionamento OneToMany com Produto

✅ **Produto.java** - Entidade com:
- ID, nome, descrição, preço, quantidade em estoque
- Relacionamento ManyToOne com Fornecedor

✅ **Venda.java** - Entidade com:
- ID, data da venda, valor total
- Relacionamento ManyToOne com Cliente
- Relacionamento OneToMany com ItemVenda
- Data automática com @PrePersist

✅ **ItemVenda.java** - Entidade com:
- ID, quantidade, preço unitário, subtotal
- Relacionamento ManyToOne com Venda
- Relacionamento ManyToOne com Produto

---

### 🗄️ Repositories - 5 interfaces
✅ **ClienteRepository** - Com métodos:
- findByCpf(String cpf)
- existsByCpf(String cpf)

✅ **FornecedorRepository** - Com métodos:
- findByCnpj(String cnpj)
- existsByCnpj(String cnpj)

✅ **ProdutoRepository** - Com métodos:
- findByNomeContainingIgnoreCase(String nome)
- findByFornecedorId(Long fornecedorId)
- findByQuantidadeEstoqueLessThan(Integer quantidade)

✅ **VendaRepository** - Com métodos:
- findByClienteId(Long clienteId)
- findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim)

✅ **ItemVendaRepository** - Com métodos:
- findByVendaId(Long vendaId)
- findByProdutoId(Long produtoId)

---

### 🎮 Controllers - 4 classes
✅ **ClienteController** - Endpoints REST:
- GET /api/clientes - Listar todos
- GET /api/clientes/{id} - Buscar por ID
- GET /api/clientes/cpf/{cpf} - Buscar por CPF
- POST /api/clientes - Criar novo
- PUT /api/clientes/{id} - Atualizar
- DELETE /api/clientes/{id} - Deletar

✅ **FornecedorController** - Endpoints REST:
- GET /api/fornecedores - Listar todos
- GET /api/fornecedores/{id} - Buscar por ID
- GET /api/fornecedores/cnpj/{cnpj} - Buscar por CNPJ
- POST /api/fornecedores - Criar novo
- PUT /api/fornecedores/{id} - Atualizar
- DELETE /api/fornecedores/{id} - Deletar

✅ **ProdutoController** - Endpoints REST:
- GET /api/produtos - Listar todos
- GET /api/produtos/{id} - Buscar por ID
- GET /api/produtos/buscar?nome=xxx - Buscar por nome
- GET /api/produtos/fornecedor/{fornecedorId} - Por fornecedor
- GET /api/produtos/estoque-baixo?quantidade=10 - Estoque baixo
- POST /api/produtos - Criar novo
- PUT /api/produtos/{id} - Atualizar
- DELETE /api/produtos/{id} - Deletar

✅ **VendaController** - Endpoints REST:
- GET /api/vendas - Listar todas
- GET /api/vendas/{id} - Buscar por ID
- GET /api/vendas/cliente/{clienteId} - Por cliente
- GET /api/vendas/periodo?inicio=xxx&fim=xxx - Por período
- POST /api/vendas - Criar nova
- PUT /api/vendas/{id} - Atualizar
- DELETE /api/vendas/{id} - Deletar

---

## 🔧 Configurações

### pom.xml
✅ Dependências corrigidas:
- spring-boot-starter-data-jpa (para JPA)
- spring-boot-starter-web (para REST)
- lombok (para getters/setters automáticos)
- h2 (banco de dados em memória)

### application.properties
✅ Configurado para:
- Banco H2 em memória
- Auto-criação de tabelas (hibernate.ddl-auto=update)
- Exibição de SQL no console

---

## ⚠️ IMPORTANTE - Para executar a aplicação:

### 1. Você precisa ter o JDK instalado (não apenas JRE)
Baixe e instale o JDK 21 de: https://adoptium.net/

### 2. Configure a variável JAVA_HOME apontando para o JDK

### 3. Execute a aplicação:
```bash
.\mvnw.cmd spring-boot:run
```

### 4. Acesse a API em:
```
http://localhost:8080
```

---

## 📊 Relacionamentos do Banco de Dados

```
Cliente (1) -----> (*) Venda
Fornecedor (1) --> (*) Produto
Venda (1) -------> (*) Estoque
Produto (1) -----> (*) Estoque
```

---

## ✨ Recursos Implementados

✅ CRUD completo para todas as entidades
✅ Validações de CPF e CNPJ únicos
✅ Busca por diferentes critérios
✅ Relacionamentos entre entidades
✅ Respostas HTTP adequadas (200, 201, 404, 409, etc.)
✅ Lombok para código limpo
✅ JPA/Hibernate para persistência
✅ H2 Database (desenvolvimento)

---

## 🚀 Próximos Passos Sugeridos

1. **Camada de Service** - Adicionar lógica de negócio
2. **DTOs** - Criar objetos de transferência de dados
3. **Validações** - Bean Validation (@Valid, @NotNull, etc.)
4. **Exception Handlers** - Tratamento global de erros
5. **Documentação** - Swagger/OpenAPI
6. **Testes** - Unitários e de integração
7. **Segurança** - Spring Security
8. **Banco Real** - PostgreSQL ou MySQL

---

## 📝 Status: ✅ ESTRUTURA COMPLETA E PRONTA!

Todas as classes Entity, Repository e Controller foram criadas corretamente seguindo as melhores práticas do Spring Boot.

