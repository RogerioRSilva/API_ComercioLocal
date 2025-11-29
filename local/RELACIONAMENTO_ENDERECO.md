# 🏠 **Relacionamento OneToOne - Endereco**

## ✅ **O QUE FOI IMPLEMENTADO?**

Criamos a entidade **Endereco** e estabelecemos um relacionamento **OneToOne** (um-para-um) com **Cliente** e **Fornecedor**.

---

## 📋 **ESTRUTURA IMPLEMENTADA**

### **1. Entidade Endereco (nova classe)**
```
enderecos
├── id (PK)
├── cep
├── logradouro
├── numero
├── complemento
├── bairro
├── cidade
├── estado
└── pais
```

### **2. Relacionamentos**
```
Cliente (1) ←→ (1) Endereco
Fornecedor (1) ←→ (1) Endereco
```

---

## 🎯 **COMO FUNCIONA O RELACIONAMENTO OneToOne?**

### **No Cliente/Fornecedor (lado DONO):**
```java
@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
@JoinColumn(name = "endereco_id")
private Endereco endereco;
```

**Significa:**
- Cliente tem uma **Foreign Key** chamada `endereco_id`
- A FK aponta para o ID do endereço na tabela `enderecos`
- Este é o lado **DONO** do relacionamento (contém a FK)

### **Estrutura no Banco de Dados:**
```sql
-- Tabela enderecos
CREATE TABLE enderecos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cep VARCHAR(9),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    pais VARCHAR(50)
);

-- Tabela clientes
CREATE TABLE clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(255) UNIQUE,
    telefone VARCHAR(255),
    email VARCHAR(255),
    endereco_id BIGINT,  -- ⬅️ FK para enderecos
    FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);

-- Tabela fornecedores
CREATE TABLE fornecedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(255) UNIQUE,
    telefone VARCHAR(255),
    email VARCHAR(255),
    endereco_id BIGINT,  -- ⬅️ FK para enderecos
    FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);
```

---

## 💡 **EXPLICAÇÃO DO CASCADE E ORPHAN REMOVAL**

### **cascade = CascadeType.ALL**
```java
@OneToOne(cascade = CascadeType.ALL)
```

**Significa que operações no Cliente/Fornecedor são replicadas no Endereço:**

| Operação | O que acontece |
|----------|----------------|
| `save(cliente)` | Salva o cliente E o endereço automaticamente |
| `update(cliente)` | Atualiza o cliente E o endereço |
| `delete(cliente)` | Deleta o cliente E o endereço |

**Sem cascade**, você precisaria fazer:
```java
// Sem cascade - trabalhoso!
enderecoRepository.save(endereco);
cliente.setEndereco(endereco);
clienteRepository.save(cliente);
```

**Com cascade - automático!**
```java
// Com cascade - simples!
cliente.setEndereco(endereco);
clienteRepository.save(cliente); // Salva os dois!
```

### **orphanRemoval = true**
```java
@OneToOne(orphanRemoval = true)
```

**Significa que se remover o endereço do cliente, ele é deletado do banco:**

```java
cliente.setEndereco(null); // Remove o endereço do cliente
clienteRepository.save(cliente); 
// O endereço é automaticamente DELETADO do banco!
```

**Útil para evitar endereços órfãos (sem dono) no banco de dados.**

---

## 🚀 **EXEMPLOS PRÁTICOS DE USO**

### **1. Criar Cliente com Endereço**
```java
// Criar o endereço
Endereco endereco = Endereco.builder()
    .cep("01310-100")
    .logradouro("Avenida Paulista")
    .numero("1578")
    .complemento("Andar 5")
    .bairro("Bela Vista")
    .cidade("São Paulo")
    .estado("SP")
    .build();

// Criar o cliente com endereço
Cliente cliente = Cliente.builder()
    .nome("João Silva")
    .cpf("123.456.789-00")
    .telefone("(11) 98765-4321")
    .email("joao@email.com")
    .endereco(endereco)  // ⬅️ Associa o endereço
    .build();

// Salvar (salva cliente E endereço por causa do cascade)
clienteRepository.save(cliente);
```

### **2. Criar Fornecedor com Endereço**
```java
Endereco endereco = Endereco.builder()
    .cep("04711-904")
    .logradouro("Av. das Nações Unidas")
    .numero("12901")
    .bairro("Brooklin Paulista")
    .cidade("São Paulo")
    .estado("SP")
    .build();

Fornecedor fornecedor = Fornecedor.builder()
    .nome("Dell Computadores Ltda")
    .cnpj("12.345.678/0001-99")
    .telefone("(11) 3000-1234")
    .email("contato@dell.com")
    .endereco(endereco)
    .build();

fornecedorRepository.save(fornecedor);
```

### **3. Atualizar Endereço de um Cliente**
```java
// Buscar cliente
Cliente cliente = clienteRepository.findById(1L).get();

// Atualizar o endereço
cliente.getEndereco().setNumero("2000");
cliente.getEndereco().setComplemento("Apto 101");

// Salvar (atualiza automaticamente o endereço por causa do cascade)
clienteRepository.save(cliente);
```

### **4. Trocar Endereço de um Cliente**
```java
Cliente cliente = clienteRepository.findById(1L).get();

// Criar novo endereço
Endereco novoEndereco = Endereco.builder()
    .cep("12345-678")
    .logradouro("Rua Nova")
    .numero("500")
    .cidade("Rio de Janeiro")
    .estado("RJ")
    .build();

// Trocar endereço
cliente.setEndereco(novoEndereco);

// Salvar (o endereço antigo é deletado - orphanRemoval)
// E o novo endereço é salvo - cascade
clienteRepository.save(cliente);
```

### **5. Remover Endereço**
```java
Cliente cliente = clienteRepository.findById(1L).get();

// Remove o endereço
cliente.setEndereco(null);

// Salvar (endereço é deletado do banco - orphanRemoval)
clienteRepository.save(cliente);
```

---

## 📊 **EXEMPLO DE JSON PARA API**

### **POST /api/clientes - Criar cliente com endereço**
```json
{
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "telefone": "(11) 98765-4321",
  "email": "joao@email.com",
  "endereco": {
    "cep": "01310-100",
    "logradouro": "Avenida Paulista",
    "numero": "1578",
    "complemento": "Andar 5",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP"
  }
}
```

### **Resposta:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "123.456.789-00",
  "telefone": "(11) 98765-4321",
  "email": "joao@email.com",
  "endereco": {
    "id": 1,
    "cep": "01310-100",
    "logradouro": "Avenida Paulista",
    "numero": "1578",
    "complemento": "Andar 5",
    "bairro": "Bela Vista",
    "cidade": "São Paulo",
    "estado": "SP",
    "pais": "Brasil"
  },
  "vendas": []
}
```

---

## 🎓 **VANTAGENS DE SEPARAR ENDERECO EM ENTIDADE**

✅ **Reutilização** - Mesmo modelo para Cliente e Fornecedor  
✅ **Organização** - Código mais limpo e separado  
✅ **Validação** - Facilita validar CEP, estado, etc.  
✅ **Queries** - Pode buscar por cidade, estado, CEP  
✅ **Integração** - Fácil integrar com APIs de CEP (ViaCEP)  
✅ **Relatórios** - Análise geográfica de vendas  
✅ **Escalabilidade** - Facilita adicionar novos campos  

---

## 🔍 **MÉTODOS ÚTEIS CRIADOS**

### **No EnderecoRepository:**
```java
// Buscar por CEP
List<Endereco> enderecos = enderecoRepository.findByCep("01310-100");

// Buscar por cidade
List<Endereco> enderecos = enderecoRepository.findByCidade("São Paulo");

// Buscar por estado
List<Endereco> enderecos = enderecoRepository.findByEstado("SP");

// Buscar por cidade e estado
List<Endereco> enderecos = enderecoRepository.findByCidadeAndEstado("São Paulo", "SP");
```

### **Na classe Endereco:**
```java
// Método getEnderecoCompleto() - retorna endereço formatado
String endereco = cliente.getEndereco().getEnderecoCompleto();
// Retorna: "Av. Paulista, 1578, Andar 5 - Bela Vista - São Paulo/SP - CEP: 01310-100"
```

---

## ✅ **RESUMO**

| Item | Descrição |
|------|-----------|
| **Entidade Criada** | Endereco.java |
| **Relacionamento** | OneToOne com Cliente e Fornecedor |
| **Lado Dono** | Cliente e Fornecedor (têm a FK) |
| **Cascade** | ALL (salva/atualiza/deleta junto) |
| **Orphan Removal** | true (deleta endereço órfão) |
| **Repository** | EnderecoRepository.java |
| **Campos** | cep, logradouro, numero, complemento, bairro, cidade, estado, pais |

**Sua API agora tem um sistema completo de endereços!** 🏠✨

