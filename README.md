# Aplicação de Reset de senha do AD - Backend em Spring Boot

## Pré-requisitos
- Java JDK 11+
- MySQL
- AD Compatível com 2016 (simulei no docker)

## Configuração
1. Clone o repositório: `https://github.com/davicruz23/Projeto-UGTSIC-Back.git`
2. Configure o banco de dados MySQL:
    - Crie um banco de dados chamado `aplicacaoad` pode alterar se preferir
    - Configure as propiedades de conexão em `application.properties`

## Funcionalidades
- Enviar link de reset de senha do usuário do AD por email 
- Alterar senha Atual se conectando com o AD

## Banco de dados
- Banco de dados MySQL para persistência de dados. e preciso configurar para utiliza-lo
- Deixarei o `spring.jpa.hibernate.ddl-auto=update` cria as tabelas e sempre atualiza se ouver alterações no model

## Segurança
- Foi adicionado autenticação do spring security utilizando corretamente o CORS

## Email
1. Configurar um email e senha que o google app fornece (não pode ser a senha normal) tem que gerar uma nova! `application.properties`

## Estrutura do Projeto
- `src/main/java/AplicacaoAdApplication`: para executar a aplicação
- `src/main/resources`: Arquivos de configuração

## Endpoints da API
- `GET /reset-password/validate`: Endpoint faz as validações para liberar a troca da senha
- `POST /forgot-password`: Endpoint que recebe o email e encaminha o link para o usuário
- `POST /reset-password`: Endpoint que vai receber a nova senha se o link for válido
- `POST /change-password`: Endpoint que vai trocar a senha do usuario se ele suber a senha atual

