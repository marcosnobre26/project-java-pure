
# 🧠📱 SocialML - Rede Social com Machine Learning (Java + DDD + Liquibase)

Projeto de exemplo com arquitetura **DDD (Domain-Driven Design)**, focado na construção de uma **rede social com integração de Machine Learning** (moderação, análise, recomendação etc.), usando:

- ✅ Java (Puro ou com Spring Boot)
- ✅ Liquibase para versionamento de banco
- ✅ Estrutura em camadas: `domain`, `application`, `infrastructure`, `presentation`
- ✅ Machine Learning acoplado via serviço
- ✅ JDBC + MySQL

---

## 📁 Estrutura do Projeto

```text
src/
├── application/       # Casos de uso da aplicação (serviços)
├── domain/            # Entidades, repositórios e regras de negócio
├── infrastructure/    # Integrações técnicas (JDBC, ML, etc.)
├── presentation/      # REST controllers ou CLI
└── Main.java          # Classe principal

resources/
└── db/
    └── changelog/
        └── db.changelog-master.xml  # Arquivo mestre Liquibase
```

---

## ⚙️ Requisitos

- Java 17 ou superior
- MySQL (com banco `storegame` criado)
- Maven (ou `libs/` com os .jar baixados)
- Liquibase CLI (opcional, se quiser rodar via terminal)

---

## 🚀 Como Rodar

### 1. 📦 Clone o repositório

```bash
git clone https://github.com/seu-usuario/socialml-java-ddd.git
cd socialml-java-ddd
```

### 2. 🗃️ Crie o banco MySQL

```sql
CREATE DATABASE storegame;
```

### 3. 📚 Compile o projeto

Se estiver usando `.jar` no diretório `libs/`:

```bash
javac -d bin -cp "libs/*" src/jdbc/LiquibaseMigrationRunner.java
```

### 4. 📥 Estrutura do Liquibase

Verifique se você tem:

```
resources/
└── db/
    └── changelog/
        ├── db.changelog-master.xml
        ├── v1.0.0-create-tables.sql
        └── v1.0.1-add-column.sql
```

Exemplo do `db.changelog-master.xml`:

```xml
<databaseChangeLog
    xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
        http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

    <changeSet id="1" author="marco">
        <sqlFile path="v1.0.0-create-tables.sql" relativeToChangelogFile="true"/>
    </changeSet>

    <changeSet id="2" author="marco">
        <sqlFile path="v1.0.1-add-column.sql" relativeToChangelogFile="true"/>
    </changeSet>
</databaseChangeLog>
```

---

## 🧬 Exemplo de Migration SQL

### 📄 `v1.0.0-create-tables.sql`

```sql
CREATE TABLE users (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    address TEXT
);

CREATE TABLE games (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    platform VARCHAR(100)
);
```

---

## ▶️ Rodando a Migration em Java

### Arquivo `LiquibaseMigrationRunner.java`

```bash
java -cp "bin;libs/*" jdbc.LiquibaseMigrationRunner
```

Se tudo estiver certo, você verá:

```bash
Conexão JDBC estabelecida com sucesso.
Iniciando migrações com Liquibase...
Migrações do Liquibase executadas com sucesso!
```

---

## 🧠 Integração com Machine Learning

Exemplo de serviço:

```java
public class PostClassifier {
    public String classify(String content) {
        return content.contains("violência") ? "inadequado" : "ok";
    }
}
```

Esse serviço pode ser evoluído para usar:

- HuggingFace Transformers via REST
- TensorFlow Java
- ONNX Runtime
- Python Script chamado via `ProcessBuilder`

---

## ✅ Dicas Finais

- Adicione **DTOs** e **validações** na camada `presentation/`
- Reforce a **injeção de dependência** com Spring se desejar
- Escreva **testes de unidade** para camada de domínio

---

## 📌 Exemplo de Execução via Terminal

```bash
# Compile
javac -d bin -cp "libs/*" src/jdbc/LiquibaseMigrationRunner.java

# Execute
java -cp "bin;libs/*" jdbc.LiquibaseMigrationRunner
```

---

## 📖 Licença

MIT © 2025 Marcos Nobre
