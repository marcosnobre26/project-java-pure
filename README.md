# Nome do Projeto: Project Java Pure (ou um nome mais descritivo)

Este projeto demonstra uma aplicação Java simples utilizando JDBC para conexão com banco de dados MySQL e Liquibase para gerenciamento de migrações de schema.

## Funcionalidades
- Conexão com banco de dados MySQL.
- Gerenciamento de schema com Liquibase.
- Exemplo de criação de tabelas (`produto`, `Users`, `Games`).
- Exemplo de adição de colunas (`preco` na tabela `Games`).

## Tecnologias Utilizadas
- Java (JDK 8 ou superior)
- JDBC (MySQL Connector/J)
- Liquibase (para migrações de banco de dados)
- MySQL (como banco de dados)

## Configuração do Ambiente
1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/marcosnobre26/project-java-pure.git](https://github.com/marcosnobre26/project-java-pure.git)
    cd project-java-pure
    ```
2.  **Baixe as dependências:**
    * Obtenha os JARs do MySQL Connector/J e Liquibase Core (e suas dependências, como `opencsv`, `jaxb-runtime`, etc.) e coloque-os na pasta `libs/` na raiz do projeto.
        * [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
        * [Liquibase Core](https://liquibase.com/download) (e suas dependências transitivas, se não usar um gerenciador de pacotes)
3.  **Configuração do Banco de Dados:**
    * Crie um banco de dados MySQL chamado `storegame`.
    * Configure o usuário (`root`) e senha (`""` - vazia, ou a que você usa) no `jdbc.LiquibaseMigrationRunner.java`.
4.  **Estrutura de Migrações:**
    * Os arquivos de migração XML e SQL estão localizados em `db/changelog/`.
5.  **Compile o Projeto:**
    Na raiz do projeto:
    ```bash
    javac -d bin -cp "libs/*" -sourcepath src src/jdbc/App.java src/jdbc/LiquibaseMigrationRunner.java src/jdbc/TesteConexao.java src/jdbc/TesteMigration.java src/jdbc/TestaDriverExplicit.java src/jdbc/TestaDrivers.java src/jdbc/controller/*.java src/jdbc/model/*.java
    ```
6.  **Executar Migrações do Liquibase:**
    Na raiz do projeto:
    ```bash
    java -cp "bin;libs/*;." jdbc.LiquibaseMigrationRunner
    ```
    Isso aplicará as migrações definidas nos arquivos XML e SQL ao seu banco de dados.

## Contribuição
Sinta-se à vontade para contribuir com melhorias ou correções.

## Licença
[Opcional: Adicione a licença do seu projeto aqui, ex: MIT License]