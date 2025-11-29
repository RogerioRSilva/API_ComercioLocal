# ⚠️ PROBLEMA: JAVA_HOME Configurado para JRE em vez de JDK

## 🔴 Erro Identificado:
```
No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?
```

**Causa:** A variável `JAVA_HOME` está apontando para um JRE (Java Runtime Environment) em vez de um JDK (Java Development Kit).

**Atual:** `C:\Program Files\Eclipse Adoptium\jre-8.0.442.6-hotspot\`

---

## ✅ SOLUÇÃO

### Opção 1: Instalar JDK 21 (Recomendado)

#### Passo 1: Baixar o JDK 21
Acesse: https://adoptium.net/temurin/releases/?version=21

Ou use o link direto:
- Windows x64: https://adoptium.net/temurin/releases/?os=windows&arch=x64&version=21

#### Passo 2: Instalar o JDK
1. Execute o instalador baixado
2. Escolha o diretório de instalação (padrão: `C:\Program Files\Eclipse Adoptium\jdk-21.x.x-hotspot\`)
3. **IMPORTANTE:** Marque a opção "Add to PATH" e "Set JAVA_HOME"
4. Complete a instalação

#### Passo 3: Verificar a Instalação
Abra um **novo terminal PowerShell** e execute:
```powershell
java -version
javac -version
```

**Resultado esperado:**
```
java version "21.x.x"
javac 21.x.x
```

#### Passo 4: Verificar JAVA_HOME
```powershell
$env:JAVA_HOME
```

**Resultado esperado:**
```
C:\Program Files\Eclipse Adoptium\jdk-21.x.x-hotspot\
```

---

### Opção 2: Configurar JAVA_HOME Manualmente (Se já tem JDK instalado)

Se você já tem um JDK instalado mas o JAVA_HOME está errado:

#### Passo 1: Localizar o JDK
Procure em:
- `C:\Program Files\Java\`
- `C:\Program Files\Eclipse Adoptium\`
- `C:\Program Files\OpenJDK\`

Procure por pastas que contenham **jdk-** (não jre-)

#### Passo 2: Configurar JAVA_HOME Permanentemente
1. Pressione `Win + X` e selecione "Sistema"
2. Clique em "Configurações avançadas do sistema"
3. Clique em "Variáveis de Ambiente"
4. Em "Variáveis do sistema", encontre `JAVA_HOME`
5. Clique em "Editar" (ou "Novo" se não existir)
6. Defina o valor como o caminho do JDK, exemplo:
   ```
   C:\Program Files\Eclipse Adoptium\jdk-21.0.1-hotspot
   ```
7. Clique em "OK" em todas as janelas
8. **Reinicie o terminal** (ou reinicie o PC)

#### Passo 3: Verificar
Abra um **novo terminal** e execute:
```powershell
$env:JAVA_HOME
java -version
javac -version
```

---

### Opção 3: Configurar JAVA_HOME Temporariamente (Para Testes)

Se você só quer testar rapidamente sem alterar configurações do sistema:

```powershell
# Defina o JAVA_HOME temporariamente (substitua pelo caminho correto do seu JDK)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.1-hotspot"

# Adicione o bin do JDK ao PATH
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verifique
java -version
javac -version

# Agora compile o projeto
cd E:\repositorio\API_ComercioLocal\local
./mvnw clean compile
```

**NOTA:** Esta configuração é perdida quando você fecha o terminal.

---

## 🔍 DIFERENÇA ENTRE JRE E JDK

| Característica | JRE (Java Runtime Environment) | JDK (Java Development Kit) |
|----------------|-------------------------------|---------------------------|
| **Propósito** | Executar aplicações Java | Desenvolver aplicações Java |
| **Contém** | JVM + bibliotecas | JRE + compilador + ferramentas |
| **javac** | ❌ Não possui | ✅ Possui |
| **Para desenvolvedores** | ❌ Não | ✅ Sim |
| **Para usuários finais** | ✅ Sim | ✅ Sim (mas é maior) |

**Para desenvolvimento, você PRECISA do JDK!**

---

## 🚀 APÓS CORRIGIR O JAVA_HOME

### 1. Compile o Projeto
```powershell
cd E:\repositorio\API_ComercioLocal\local
./mvnw clean compile
```

### 2. Execute a Aplicação
```powershell
./mvnw spring-boot:run
```

### 3. Acesse a API
- API: http://localhost:8080/api/clientes
- Console H2: http://localhost:8080/h2-console

---

## 📝 VERIFICAÇÃO FINAL

Execute estes comandos para verificar se está tudo OK:

```powershell
# 1. Versão do Java
java -version

# 2. Versão do compilador Java
javac -version

# 3. JAVA_HOME
echo $env:JAVA_HOME

# 4. Listar conteúdo do JAVA_HOME/bin (deve incluir javac.exe)
dir "$env:JAVA_HOME\bin" | Select-String "javac"
```

**Resultado esperado do último comando:**
```
-a----  [data]  [hora]  [tamanho]  javac.exe
```

Se ver `javac.exe`, está tudo certo! ✅

---

## ❓ AINDA TEM PROBLEMAS?

### Problema: "java não é reconhecido como comando"
**Solução:** Adicione `%JAVA_HOME%\bin` à variável PATH do sistema.

### Problema: "Versão do Java incompatível"
**Solução:** Certifique-se de ter JDK 21 ou superior (ou ajuste no pom.xml).

### Problema: "Multiple JDKs instalados"
**Solução:** Use JAVA_HOME para apontar para o JDK desejado.

---

## 📚 LINKS ÚTEIS

- **Download JDK 21:** https://adoptium.net/
- **Verificar Java instalado:** https://www.java.com/verify/
- **Documentação Spring Boot:** https://spring.io/projects/spring-boot
- **Como configurar JAVA_HOME:** https://confluence.atlassian.com/doc/setting-the-java_home-variable-in-windows-8895.html

---

**Após configurar o JDK corretamente, sua API estará pronta para ser compilada e executada! 🚀**

