# 🗺️ MAPA VISUAL DA API - Comércio Local

## 📊 VISÃO GERAL DA ESTRUTURA

```
API COMÉRCIO LOCAL
│
├── 📦 ENTIDADES (Model)
│   ├── 👤 Cliente
│   ├── 🏭 Fornecedor
│   ├── 📦 Produto
│   ├── 🛒 Venda
│   └── 📋 Estoque (Item de Venda)
│
├── 🗄️ REPOSITORIES
│   ├── ClienteRepository
│   ├── FornecedorRepository
│   ├── ProdutoRepository
│   ├── VendaRepository
│   └── EstoqueRepository
│
└── 🌐 CONTROLLERS (REST API)
    ├── ClienteController     → /api/clientes
    ├── FornecedorController  → /api/fornecedores
    ├── ProdutoController     → /api/produtos
    ├── VendaController       → /api/vendas
    └── EstoqueController     → /api/estoque
```

---

## 🔗 MAPA DE RELACIONAMENTOS

```
┌──────────────┐
│   Cliente    │
│   (1)        │
└───────┬──────┘
        │
        │ OneToMany
        │ (Um cliente tem várias vendas)
        │
        ▼
┌──────────────┐       ┌──────────────┐
│    Venda     │       │  Fornecedor  │
│   (N)        │       │    (1)       │
└───────┬──────┘       └───────┬──────┘
        │                      │
        │ OneToMany            │ OneToMany
        │                      │
        ▼                      ▼
┌──────────────┐       ┌──────────────┐
│   Estoque    │──────▶│   Produto    │
│ (Item Venda) │ ManyToOne │    (1)       │
│   (N)        │       └──────────────┘
└──────────────┘
    (N)
```

**Legenda:**
- **(1)** = Um
- **(N)** = Muitos
- **→** = FK (Foreign Key)

---

## 📋 TABELAS NO BANCO DE DADOS

### 1️⃣ Tabela: `clientes`
```sql
┌────────────────┬─────────────┬─────────────┐
│     Campo      │    Tipo     │  Restrição  │
├────────────────┼─────────────┼─────────────┤
│ id             │ BIGINT      │ PK, AI      │
│ nome           │ VARCHAR     │ NOT NULL    │
│ cpf            │ VARCHAR     │ UNIQUE      │
│ telefone       │ VARCHAR     │             │
│ email          │ VARCHAR     │             │
│ endereco       │ VARCHAR     │             │
└────────────────┴─────────────┴─────────────┘
```

### 2️⃣ Tabela: `fornecedores`
```sql
┌────────────────┬─────────────┬─────────────┐
│     Campo      │    Tipo     │  Restrição  │
├────────────────┼─────────────┼─────────────┤
│ id             │ BIGINT      │ PK, AI      │
│ nome           │ VARCHAR     │ NOT NULL    │
│ cnpj           │ VARCHAR     │ UNIQUE      │
│ telefone       │ VARCHAR     │             │
│ email          │ VARCHAR     │             │
│ endereco       │ VARCHAR     │             │
└────────────────┴─────────────┴─────────────┘
```

### 3️⃣ Tabela: `produtos`
```sql
┌────────────────────┬─────────────┬─────────────┐
│       Campo        │    Tipo     │  Restrição  │
├────────────────────┼─────────────┼─────────────┤
│ id                 │ BIGINT      │ PK, AI      │
│ nome               │ VARCHAR     │ NOT NULL    │
│ descricao          │ VARCHAR     │             │
│ preco              │ DECIMAL     │ NOT NULL    │
│ quantidade_estoque │ INTEGER     │ NOT NULL    │
│ fornecedor_id      │ BIGINT      │ FK          │
└────────────────────┴─────────────┴─────────────┘
                                      │
                                      └─► fornecedores(id)
```

### 4️⃣ Tabela: `vendas`
```sql
┌────────────────┬──────────────┬─────────────┐
│     Campo      │    Tipo      │  Restrição  │
├────────────────┼──────────────┼─────────────┤
│ id             │ BIGINT       │ PK, AI      │
│ cliente_id     │ BIGINT       │ FK, NOT NULL│
│ data_venda     │ TIMESTAMP    │ NOT NULL    │
│ valor_total    │ DECIMAL      │ NOT NULL    │
└────────────────┴──────────────┴─────────────┘
                   │
                   └─► clientes(id)
```

### 5️⃣ Tabela: `estoque` (Itens de Venda)
```sql
┌─────────────────┬─────────────┬─────────────┐
│     Campo       │    Tipo     │  Restrição  │
├─────────────────┼─────────────┼─────────────┤
│ id              │ BIGINT      │ PK, AI      │
│ venda_id        │ BIGINT      │ FK, NOT NULL│
│ produto_id      │ BIGINT      │ FK, NOT NULL│
│ quantidade      │ INTEGER     │ NOT NULL    │
│ preco_unitario  │ DECIMAL     │ NOT NULL    │
│ subtotal        │ DECIMAL     │ NOT NULL    │
└─────────────────┴─────────────┴─────────────┘
                   │              │
                   │              └─► produtos(id)
                   └─► vendas(id)
```

**Legenda:**
- **PK** = Primary Key (Chave Primária)
- **FK** = Foreign Key (Chave Estrangeira)
- **AI** = Auto Increment (Auto Incremento)

---

## 🌐 ENDPOINTS DA API

### 👤 CLIENTES - `/api/clientes`

```
┌─────────┬────────────────────────────┬──────────────────────┐
│ Método  │         Endpoint           │     Descrição        │
├─────────┼────────────────────────────┼──────────────────────┤
│ GET     │ /api/clientes              │ Listar todos         │
│ GET     │ /api/clientes/{id}         │ Buscar por ID        │
│ GET     │ /api/clientes/cpf/{cpf}    │ Buscar por CPF       │
│ POST    │ /api/clientes              │ Criar novo           │
│ PUT     │ /api/clientes/{id}         │ Atualizar            │
│ DELETE  │ /api/clientes/{id}         │ Deletar              │
└─────────┴────────────────────────────┴──────────────────────┘
```

### 🏭 FORNECEDORES - `/api/fornecedores`

```
┌─────────┬────────────────────────────────┬──────────────────┐
│ Método  │         Endpoint               │   Descrição      │
├─────────┼────────────────────────────────┼──────────────────┤
│ GET     │ /api/fornecedores              │ Listar todos     │
│ GET     │ /api/fornecedores/{id}         │ Buscar por ID    │
│ GET     │ /api/fornecedores/cnpj/{cnpj}  │ Buscar por CNPJ  │
│ POST    │ /api/fornecedores              │ Criar novo       │
│ PUT     │ /api/fornecedores/{id}         │ Atualizar        │
│ DELETE  │ /api/fornecedores/{id}         │ Deletar          │
└─────────┴────────────────────────────────┴──────────────────┘
```

### 📦 PRODUTOS - `/api/produtos`

```
┌─────────┬────────────────────────────────────────┬───────────────────────┐
│ Método  │             Endpoint                   │      Descrição        │
├─────────┼────────────────────────────────────────┼───────────────────────┤
│ GET     │ /api/produtos                          │ Listar todos          │
│ GET     │ /api/produtos/{id}                     │ Buscar por ID         │
│ GET     │ /api/produtos/buscar?nome=x            │ Buscar por nome       │
│ GET     │ /api/produtos/fornecedor/{id}          │ Buscar por fornecedor │
│ GET     │ /api/produtos/estoque-baixo?quantidade │ Produtos c/ estoque < │
│ POST    │ /api/produtos                          │ Criar novo            │
│ PUT     │ /api/produtos/{id}                     │ Atualizar             │
│ DELETE  │ /api/produtos/{id}                     │ Deletar               │
└─────────┴────────────────────────────────────────┴───────────────────────┘
```

### 🛒 VENDAS - `/api/vendas`

```
┌─────────┬────────────────────────────────────────────┬──────────────────┐
│ Método  │               Endpoint                     │   Descrição      │
├─────────┼────────────────────────────────────────────┼──────────────────┤
│ GET     │ /api/vendas                                │ Listar todas     │
│ GET     │ /api/vendas/{id}                           │ Buscar por ID    │
│ GET     │ /api/vendas/cliente/{id}                   │ Vendas do cliente│
│ GET     │ /api/vendas/periodo?inicio=x&fim=y         │ Vendas no período│
│ POST    │ /api/vendas                                │ Criar nova       │
│ PUT     │ /api/vendas/{id}                           │ Atualizar        │
│ DELETE  │ /api/vendas/{id}                           │ Deletar          │
└─────────┴────────────────────────────────────────────┴──────────────────┘
```

### 📋 ESTOQUE (Itens) - `/api/estoque`

```
┌─────────┬───────────────────────────────┬────────────────────────┐
│ Método  │          Endpoint             │      Descrição         │
├─────────┼───────────────────────────────┼────────────────────────┤
│ GET     │ /api/estoque                  │ Listar todos           │
│ GET     │ /api/estoque/{id}             │ Buscar por ID          │
│ GET     │ /api/estoque/venda/{id}       │ Itens de uma venda     │
│ GET     │ /api/estoque/produto/{id}     │ Histórico do produto   │
│ POST    │ /api/estoque                  │ Criar novo             │
│ PUT     │ /api/estoque/{id}             │ Atualizar              │
│ DELETE  │ /api/estoque/{id}             │ Deletar                │
└─────────┴───────────────────────────────┴────────────────────────┘
```

---

## 🧪 EXEMPLOS DE REQUISIÇÕES (COPIE E COLE!)

### 👤 CLIENTES

#### 1. Criar Cliente
```http
POST http://localhost:8080/api/clientes
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "telefone": "(11) 98765-4321",
  "email": "joao@email.com",
  "endereco": "Rua A, 123 - São Paulo/SP"
}
```

#### 2. Listar Todos os Clientes
```http
GET http://localhost:8080/api/clientes
```

#### 3. Buscar Cliente por ID
```http
GET http://localhost:8080/api/clientes/1
```

#### 4. Buscar Cliente por CPF
```http
GET http://localhost:8080/api/clientes/cpf/123.456.789-00
```

#### 5. Atualizar Cliente
```http
PUT http://localhost:8080/api/clientes/1
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "cpf": "123.456.789-00",
  "telefone": "(11) 98765-9999",
  "email": "joao.novo@email.com",
  "endereco": "Rua B, 456 - São Paulo/SP"
}
```

#### 6. Deletar Cliente
```http
DELETE http://localhost:8080/api/clientes/1
```

---

### 🏭 FORNECEDORES

#### 1. Criar Fornecedor
```http
POST http://localhost:8080/api/fornecedores
Content-Type: application/json

{
  "nome": "Dell Computadores Ltda",
  "cnpj": "12.345.678/0001-99",
  "telefone": "(11) 1234-5678",
  "email": "contato@dell.com",
  "endereco": "Av. Paulista, 1000 - São Paulo/SP"
}
```

#### 2. Listar Todos os Fornecedores
```http
GET http://localhost:8080/api/fornecedores
```

#### 3. Buscar Fornecedor por ID
```http
GET http://localhost:8080/api/fornecedores/1
```

#### 4. Buscar Fornecedor por CNPJ
```http
GET http://localhost:8080/api/fornecedores/cnpj/12.345.678/0001-99
```

#### 5. Atualizar Fornecedor
```http
PUT http://localhost:8080/api/fornecedores/1
Content-Type: application/json

{
  "nome": "Dell Computadores Ltda - Matriz",
  "cnpj": "12.345.678/0001-99",
  "telefone": "(11) 1234-9999",
  "email": "matriz@dell.com",
  "endereco": "Av. Paulista, 2000 - São Paulo/SP"
}
```

#### 6. Deletar Fornecedor
```http
DELETE http://localhost:8080/api/fornecedores/1
```

---

### 📦 PRODUTOS

#### 1. Criar Produto
```http
POST http://localhost:8080/api/produtos
Content-Type: application/json

{
  "nome": "Mouse Logitech MX Master 3",
  "descricao": "Mouse sem fio ergonômico para produtividade",
  "preco": 599.90,
  "quantidadeEstoque": 50,
  "fornecedor": {
    "id": 1
  }
}
```

#### 2. Criar Produto SEM Fornecedor
```http
POST http://localhost:8080/api/produtos
Content-Type: application/json

{
  "nome": "Teclado Mecânico RGB",
  "descricao": "Teclado mecânico com iluminação RGB",
  "preco": 299.90,
  "quantidadeEstoque": 30
}
```

#### 3. Listar Todos os Produtos
```http
GET http://localhost:8080/api/produtos
```

#### 4. Buscar Produto por ID
```http
GET http://localhost:8080/api/produtos/1
```

#### 5. Buscar Produtos por Nome (Parcial)
```http
GET http://localhost:8080/api/produtos/buscar?nome=mouse
```

#### 6. Buscar Produtos de um Fornecedor
```http
GET http://localhost:8080/api/produtos/fornecedor/1
```

#### 7. Buscar Produtos com Estoque Baixo (padrão < 10)
```http
GET http://localhost:8080/api/produtos/estoque-baixo
```

#### 8. Buscar Produtos com Estoque Baixo (customizado < 20)
```http
GET http://localhost:8080/api/produtos/estoque-baixo?quantidade=20
```

#### 9. Atualizar Produto
```http
PUT http://localhost:8080/api/produtos/1
Content-Type: application/json

{
  "nome": "Mouse Logitech MX Master 3S",
  "descricao": "Mouse sem fio ergonômico - Nova versão",
  "preco": 649.90,
  "quantidadeEstoque": 45,
  "fornecedor": {
    "id": 1
  }
}
```

#### 10. Deletar Produto
```http
DELETE http://localhost:8080/api/produtos/1
```

---

### 🛒 VENDAS

#### 1. Criar Venda (Simples)
```http
POST http://localhost:8080/api/vendas
Content-Type: application/json

{
  "cliente": {
    "id": 1
  },
  "valorTotal": 899.80
}
```

#### 2. Criar Venda Completa (com Itens)
```http
POST http://localhost:8080/api/vendas
Content-Type: application/json

{
  "cliente": {
    "id": 1
  },
  "valorTotal": 899.80,
  "itens": [
    {
      "produto": { "id": 1 },
      "quantidade": 1,
      "precoUnitario": 599.90,
      "subtotal": 599.90
    },
    {
      "produto": { "id": 2 },
      "quantidade": 1,
      "precoUnitario": 299.90,
      "subtotal": 299.90
    }
  ]
}
```

#### 3. Listar Todas as Vendas
```http
GET http://localhost:8080/api/vendas
```

#### 4. Buscar Venda por ID
```http
GET http://localhost:8080/api/vendas/1
```

#### 5. Buscar Vendas de um Cliente
```http
GET http://localhost:8080/api/vendas/cliente/1
```

#### 6. Buscar Vendas por Período
```http
GET http://localhost:8080/api/vendas/periodo?inicio=2024-01-01T00:00:00&fim=2024-12-31T23:59:59
```

#### 7. Buscar Vendas do Dia Atual
```http
GET http://localhost:8080/api/vendas/periodo?inicio=2024-11-28T00:00:00&fim=2024-11-28T23:59:59
```

#### 8. Atualizar Venda
```http
PUT http://localhost:8080/api/vendas/1
Content-Type: application/json

{
  "cliente": {
    "id": 1
  },
  "valorTotal": 999.80
}
```

#### 9. Deletar Venda
```http
DELETE http://localhost:8080/api/vendas/1
```

---

### 📋 ESTOQUE (Itens de Venda)

#### 1. Criar Item de Venda
```http
POST http://localhost:8080/api/estoque
Content-Type: application/json

{
  "venda": {
    "id": 1
  },
  "produto": {
    "id": 1
  },
  "quantidade": 2,
  "precoUnitario": 599.90
}
```
**Nota:** O subtotal é calculado automaticamente (quantidade × precoUnitario)

#### 2. Listar Todos os Itens
```http
GET http://localhost:8080/api/estoque
```

#### 3. Buscar Item por ID
```http
GET http://localhost:8080/api/estoque/1
```

#### 4. Buscar Itens de uma Venda (Detalhar Venda)
```http
GET http://localhost:8080/api/estoque/venda/1
```

#### 5. Buscar Histórico de Vendas de um Produto
```http
GET http://localhost:8080/api/estoque/produto/1
```

#### 6. Atualizar Item
```http
PUT http://localhost:8080/api/estoque/1
Content-Type: application/json

{
  "venda": {
    "id": 1
  },
  "produto": {
    "id": 1
  },
  "quantidade": 3,
  "precoUnitario": 599.90
}
```

#### 7. Deletar Item
```http
DELETE http://localhost:8080/api/estoque/1
```

---

## 🎯 SEQUÊNCIA RECOMENDADA PARA TESTAR

### Passo 1: Criar Dados Base
```http
1. POST /api/clientes        → Criar Cliente
2. POST /api/fornecedores    → Criar Fornecedor
3. POST /api/produtos        → Criar Produto (com fornecedor)
```

### Passo 2: Testar Consultas
```http
4. GET /api/clientes                    → Listar clientes
5. GET /api/produtos/buscar?nome=mouse  → Buscar produto
6. GET /api/produtos/estoque-baixo      → Estoque baixo
```

### Passo 3: Criar Venda
```http
7. POST /api/vendas          → Criar Venda
8. POST /api/estoque         → Adicionar itens na venda
9. GET /api/estoque/venda/1  → Ver itens da venda
```

### Passo 4: Relatórios
```http
10. GET /api/vendas/cliente/1                   → Vendas do cliente
11. GET /api/vendas/periodo?inicio=...&fim=...  → Vendas por período
12. GET /api/estoque/produto/1                  → Histórico do produto
```

---

## 💡 DICAS PARA USAR NO POSTMAN

### Criar Collection no Postman:

1. **Importe as requisições:**
   - File → Import → Raw Text
   - Cole os exemplos acima

2. **Configure variáveis:**
   ```
   {{baseUrl}} = http://localhost:8080
   {{clienteId}} = 1
   {{produtoId}} = 1
   ```

3. **Use as requisições:**
   ```
   GET {{baseUrl}}/api/clientes/{{clienteId}}
   ```

---

## 💻 EXEMPLOS COM CURL (Terminal/PowerShell)

### 👤 Clientes - cURL

```bash
# 1. Criar Cliente
curl -X POST http://localhost:8080/api/clientes ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"João Silva\",\"cpf\":\"123.456.789-00\",\"telefone\":\"(11) 98765-4321\",\"email\":\"joao@email.com\",\"endereco\":\"Rua A, 123\"}"

# 2. Listar Todos
curl http://localhost:8080/api/clientes

# 3. Buscar por ID
curl http://localhost:8080/api/clientes/1

# 4. Buscar por CPF
curl http://localhost:8080/api/clientes/cpf/123.456.789-00

# 5. Atualizar Cliente
curl -X PUT http://localhost:8080/api/clientes/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"João Silva Atualizado\",\"cpf\":\"123.456.789-00\",\"telefone\":\"(11) 98765-9999\",\"email\":\"joao.novo@email.com\",\"endereco\":\"Rua B, 456\"}"

# 6. Deletar Cliente
curl -X DELETE http://localhost:8080/api/clientes/1
```

### 🏭 Fornecedores - cURL

```bash
# 1. Criar Fornecedor
curl -X POST http://localhost:8080/api/fornecedores ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"Dell Computadores Ltda\",\"cnpj\":\"12.345.678/0001-99\",\"telefone\":\"(11) 1234-5678\",\"email\":\"contato@dell.com\",\"endereco\":\"Av. Paulista, 1000\"}"

# 2. Listar Todos
curl http://localhost:8080/api/fornecedores

# 3. Buscar por ID
curl http://localhost:8080/api/fornecedores/1

# 4. Buscar por CNPJ
curl http://localhost:8080/api/fornecedores/cnpj/12.345.678/0001-99

# 5. Deletar Fornecedor
curl -X DELETE http://localhost:8080/api/fornecedores/1
```

### 📦 Produtos - cURL

```bash
# 1. Criar Produto (com fornecedor)
curl -X POST http://localhost:8080/api/produtos ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"Mouse Logitech\",\"descricao\":\"Mouse sem fio\",\"preco\":599.90,\"quantidadeEstoque\":50,\"fornecedor\":{\"id\":1}}"

# 2. Criar Produto (sem fornecedor)
curl -X POST http://localhost:8080/api/produtos ^
  -H "Content-Type: application/json" ^
  -d "{\"nome\":\"Teclado Mecânico\",\"descricao\":\"Teclado RGB\",\"preco\":299.90,\"quantidadeEstoque\":30}"

# 3. Listar Todos
curl http://localhost:8080/api/produtos

# 4. Buscar por Nome
curl "http://localhost:8080/api/produtos/buscar?nome=mouse"

# 5. Buscar por Fornecedor
curl http://localhost:8080/api/produtos/fornecedor/1

# 6. Estoque Baixo
curl "http://localhost:8080/api/produtos/estoque-baixo?quantidade=20"

# 7. Deletar Produto
curl -X DELETE http://localhost:8080/api/produtos/1
```

### 🛒 Vendas - cURL

```bash
# 1. Criar Venda
curl -X POST http://localhost:8080/api/vendas ^
  -H "Content-Type: application/json" ^
  -d "{\"cliente\":{\"id\":1},\"valorTotal\":899.80}"

# 2. Listar Todas
curl http://localhost:8080/api/vendas

# 3. Buscar por ID
curl http://localhost:8080/api/vendas/1

# 4. Vendas de um Cliente
curl http://localhost:8080/api/vendas/cliente/1

# 5. Vendas por Período
curl "http://localhost:8080/api/vendas/periodo?inicio=2024-01-01T00:00:00&fim=2024-12-31T23:59:59"

# 6. Deletar Venda
curl -X DELETE http://localhost:8080/api/vendas/1
```

### 📋 Estoque - cURL

```bash
# 1. Criar Item
curl -X POST http://localhost:8080/api/estoque ^
  -H "Content-Type: application/json" ^
  -d "{\"venda\":{\"id\":1},\"produto\":{\"id\":1},\"quantidade\":2,\"precoUnitario\":599.90}"

# 2. Listar Todos
curl http://localhost:8080/api/estoque

# 3. Itens de uma Venda
curl http://localhost:8080/api/estoque/venda/1

# 4. Histórico de um Produto
curl http://localhost:8080/api/estoque/produto/1

# 5. Deletar Item
curl -X DELETE http://localhost:8080/api/estoque/1
```

**Nota para PowerShell:** Use `^` para quebra de linha. No Linux/Mac, use `\`.

---

## 🔄 FLUXO DE UMA REQUISIÇÃO

```
┌─────────────────┐
│   CLIENTE       │  (Postman, cURL, Front-end)
│   (HTTP)        │
└────────┬────────┘
         │ GET /api/clientes/1
         ▼
┌──────────────────────────────────────┐
│      CONTROLLER                      │
│  ClienteController.java              │
│  @GetMapping("/{id}")                │
└────────┬─────────────────────────────┘
         │ clienteRepository.findById(1)
         ▼
┌──────────────────────────────────────┐
│      REPOSITORY                      │
│  ClienteRepository.java              │
│  extends JpaRepository               │
└────────┬─────────────────────────────┘
         │ SELECT * FROM clientes WHERE id = 1
         ▼
┌──────────────────────────────────────┐
│      BANCO DE DADOS (H2)             │
│  Tabela: clientes                    │
└────────┬─────────────────────────────┘
         │ Retorna dados
         ▼
┌──────────────────────────────────────┐
│      MODEL/ENTITY                    │
│  Cliente.java                        │
│  (Objeto Java)                       │
└────────┬─────────────────────────────┘
         │ Converte para JSON
         ▼
┌──────────────────────────────────────┐
│      RESPOSTA HTTP                   │
│  Status: 200 OK                      │
│  Body: {"id":1, "nome":"João",...}   │
└──────────────────────────────────────┘
```

---

## 📦 EXEMPLO DE DADOS CONECTADOS

### Cenário: Uma Venda Completa

```
CLIENTE #1
├─ Nome: João Silva
├─ CPF: 123.456.789-00
└─ VENDAS:
   └─ VENDA #1
      ├─ Data: 2024-11-28 14:30:00
      ├─ Valor Total: R$ 175,80
      └─ ITENS:
         ├─ ITEM #1
         │  ├─ Produto: Mouse Logitech (ID: 5)
         │  ├─ Quantidade: 2
         │  ├─ Preço Unit: R$ 25,00
         │  └─ Subtotal: R$ 50,00
         │
         ├─ ITEM #2
         │  ├─ Produto: Teclado Mecânico (ID: 8)
         │  ├─ Quantidade: 1
         │  ├─ Preço Unit: R$ 125,80
         │  └─ Subtotal: R$ 125,80
         │
         └─ Total: R$ 175,80

FORNECEDOR #1
├─ Nome: Logitech Brasil
├─ CNPJ: 12.345.678/0001-99
└─ PRODUTOS:
   ├─ Mouse Logitech (ID: 5)
   │  ├─ Preço: R$ 25,00
   │  └─ Estoque: 48 unidades
   │
   └─ Webcam HD (ID: 12)
      ├─ Preço: R$ 89,90
      └─ Estoque: 30 unidades
```

---

## 🎯 ONDE ESTÁ CADA CONCEITO

### 📍 Anotações JPA
```
Cliente.java
├─ @Entity        → Linha 19
├─ @Table         → Linha 20
├─ @Id            → Linha 39
├─ @GeneratedValue → Linha 40
├─ @Column        → Linhas 50, 60
├─ @OneToMany     → Linha 92
└─ @ManyToOne     → (em Venda.java)
```

### 📍 Anotações Lombok
```
Cliente.java
├─ @Getter         → Linha 21
├─ @Setter         → Linha 22
├─ @NoArgsConstructor → Linha 23
├─ @AllArgsConstructor → Linha 24
└─ @Builder        → Linha 25
```

### 📍 Anotações Spring
```
ClienteController.java
├─ @RestController   → Linha 23
├─ @RequestMapping   → Linha 24
├─ @Autowired        → Linha 32
├─ @GetMapping       → Linhas 42, 60, 77
├─ @PostMapping      → Linha 93
├─ @PutMapping       → Linha 117
├─ @DeleteMapping    → Linha 141
├─ @PathVariable     → Linhas 60, 77, etc
└─ @RequestBody      → Linhas 94, 118
```

---

## ✅ CHECKLIST FINAL

### Estrutura
- ✅ 5 Entidades criadas e comentadas
- ✅ 5 Repositories criados e comentados
- ✅ 5 Controllers criados e comentados
- ✅ application.properties configurado e comentado

### Funcionalidades
- ✅ CRUD completo para todas as entidades
- ✅ Relacionamentos OneToMany e ManyToOne
- ✅ Métodos de busca customizados
- ✅ Validações (CPF e CNPJ únicos)
- ✅ Cálculos automáticos (subtotal, data venda)

### Documentação
- ✅ Comentários explicativos em TODOS os arquivos
- ✅ Guia de avaliação (AVALIACAO_API.md)
- ✅ Guia de resolução JDK (RESOLVER_JDK.md)
- ✅ Guia completo (GUIA_COMPLETO.md)
- ✅ Mapa visual (MAPA_VISUAL.md)

---

**🎉 PARABÉNS! Sua API está completa, funcional e totalmente documentada!**

**📚 Próximos Passos:**
1. Configure o JDK (veja RESOLVER_JDK.md)
2. Execute a aplicação
3. Teste os endpoints
4. Leia os comentários em cada arquivo
5. Experimente modificar e adicionar novas funcionalidades!

*Mapa criado em: 28/11/2024*

