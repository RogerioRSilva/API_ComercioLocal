# 🧪 Testes da API - Exemplos de Requisições

## 📋 Como Testar

Use ferramentas como:
- **Postman**
- **Insomnia**
- **Thunder Client** (extensão VS Code)
- **curl** (linha de comando)

---

## 1️⃣ CLIENTES

### ➕ Criar Cliente
```http
POST http://localhost:8080/api/clientes
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao@email.com",
  "endereco": "Rua A, 123"
}
```

### 📋 Listar Todos os Clientes
```http
GET http://localhost:8080/api/clientes
```

### 🔍 Buscar Cliente por ID
```http
GET http://localhost:8080/api/clientes/1
```

### 🔍 Buscar Cliente por CPF
```http
GET http://localhost:8080/api/clientes/cpf/12345678900
```

### ✏️ Atualizar Cliente
```http
PUT http://localhost:8080/api/clientes/1
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao.santos@email.com",
  "endereco": "Rua A, 123 - Apto 45"
}
```

### ❌ Deletar Cliente
```http
DELETE http://localhost:8080/api/clientes/1
```

---

## 2️⃣ FORNECEDORES

### ➕ Criar Fornecedor
```http
POST http://localhost:8080/api/fornecedores
Content-Type: application/json

{
  "nome": "Distribuidora ABC",
  "cnpj": "12345678000190",
  "telefone": "(11) 3333-4444",
  "email": "contato@abc.com",
  "endereco": "Av. Principal, 1000"
}
```

### 📋 Listar Todos os Fornecedores
```http
GET http://localhost:8080/api/fornecedores
```

### 🔍 Buscar Fornecedor por ID
```http
GET http://localhost:8080/api/fornecedores/1
```

### 🔍 Buscar Fornecedor por CNPJ
```http
GET http://localhost:8080/api/fornecedores/cnpj/12345678000190
```

---

## 3️⃣ PRODUTOS

### ➕ Criar Produto
```http
POST http://localhost:8080/api/produtos
Content-Type: application/json

{
  "nome": "Notebook Dell",
  "descricao": "Notebook Dell Inspiron 15",
  "preco": 3500.00,
  "quantidadeEstoque": 10,
  "fornecedor": {
    "id": 1
  }
}
```

### 📋 Listar Todos os Produtos
```http
GET http://localhost:8080/api/produtos
```

### 🔍 Buscar Produto por ID
```http
GET http://localhost:8080/api/produtos/1
```

### 🔍 Buscar Produtos por Nome
```http
GET http://localhost:8080/api/produtos/buscar?nome=notebook
```

### 🔍 Buscar Produtos por Fornecedor
```http
GET http://localhost:8080/api/produtos/fornecedor/1
```

### ⚠️ Buscar Produtos com Estoque Baixo
```http
GET http://localhost:8080/api/produtos/estoque-baixo?quantidade=5
```

---

## 4️⃣ VENDAS

### ➕ Criar Venda
```http
POST http://localhost:8080/api/vendas
Content-Type: application/json

{
  "cliente": {
    "id": 1
  },
  "valorTotal": 3500.00,
  "itens": [
    {
      "produto": {
        "id": 1
      },
      "quantidade": 1,
      "precoUnitario": 3500.00,
      "subtotal": 3500.00,
      "venda": null
    }
  ]
}
```

### 📋 Listar Todas as Vendas
```http
GET http://localhost:8080/api/vendas
```

### 🔍 Buscar Venda por ID
```http
GET http://localhost:8080/api/vendas/1
```

### 🔍 Buscar Vendas por Cliente
```http
GET http://localhost:8080/api/vendas/cliente/1
```

### 📅 Buscar Vendas por Período
```http
GET http://localhost:8080/api/vendas/periodo?inicio=2025-01-01T00:00:00&fim=2025-12-31T23:59:59
```

---

## 5️⃣ ESTOQUE

### ➕ Criar Item de Estoque
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
  "precoUnitario": 250.00,
  "subtotal": 500.00
}
```

### 📋 Listar Todos os Itens de Estoque
```http
GET http://localhost:8080/api/estoque
```

### 🔍 Buscar Estoque por ID
```http
GET http://localhost:8080/api/estoque/1
```

### 🔍 Buscar Estoque por Venda
```http
GET http://localhost:8080/api/estoque/venda/1
```

### 🔍 Buscar Estoque por Produto
```http
GET http://localhost:8080/api/estoque/produto/1
```

---

## 🗄️ Acessar Console do Banco H2

```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:comercio_local_db
Username: sa
Password: (deixar em branco)
```

---

## 📊 Cenário de Teste Completo

### Passo 1: Criar um Fornecedor
```json
POST /api/fornecedores
{
  "nome": "Tech Supplies Ltda",
  "cnpj": "11222333000144",
  "telefone": "(11) 4444-5555",
  "email": "vendas@techsupplies.com",
  "endereco": "Rua Tech, 500"
}
```

### Passo 2: Criar um Cliente
```json
POST /api/clientes
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "telefone": "(11) 91234-5678",
  "email": "maria@email.com",
  "endereco": "Av. Brasil, 200"
}
```

### Passo 3: Criar Produtos
```json
POST /api/produtos
{
  "nome": "Mouse Gamer",
  "descricao": "Mouse RGB 16000 DPI",
  "preco": 250.00,
  "quantidadeEstoque": 50,
  "fornecedor": {"id": 1}
}

POST /api/produtos
{
  "nome": "Teclado Mecânico",
  "descricao": "Teclado RGB Switch Blue",
  "preco": 450.00,
  "quantidadeEstoque": 30,
  "fornecedor": {"id": 1}
}
```

### Passo 4: Criar uma Venda
```json
POST /api/vendas
{
  "cliente": {"id": 1},
  "valorTotal": 700.00,
  "itens": [
    {
      "produto": {"id": 1},
      "quantidade": 1,
      "precoUnitario": 250.00,
      "subtotal": 250.00
    },
    {
      "produto": {"id": 2},
      "quantidade": 1,
      "precoUnitario": 450.00,
      "subtotal": 450.00
    }
  ]
}
```

---

## ✅ Códigos de Resposta HTTP

- **200 OK** - Sucesso (GET, PUT)
- **201 Created** - Recurso criado (POST)
- **204 No Content** - Deletado com sucesso (DELETE)
- **404 Not Found** - Recurso não encontrado
- **409 Conflict** - Conflito (CPF/CNPJ duplicado)

---

## 🔥 Dicas

1. Sempre crie o Fornecedor ANTES dos Produtos
2. Sempre crie o Cliente ANTES das Vendas
3. Use o Console H2 para visualizar os dados
4. Teste um endpoint de cada vez
5. Verifique os logs da aplicação em caso de erro

