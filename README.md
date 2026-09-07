# 📚 Book Station

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-22-red.svg)](https://angular.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-✓-2496ED.svg)](https://www.docker.com/)

Aplicação full-stack para gerenciamento de bibliotecas e livrarias com autenticação JWT e CRUD completo.


---

## 🎯 Sobre o Projeto

**Book Station** é uma aplicação completa que permite aos usuários gerenciar uma biblioteca ou uma livraria. O sistema conta com autenticação JWT e um CRUD completo, com interface intuitiva que permite o acesso às funcionalidades da aplicação.

### Objetivo
- ✅ Criar conta e fazer login com autenticação JWT (páginas internas protegidas)
- ✅ Listar livros com busca por título
- ✅ Criar, editar e excluir livros
- ➕ Dockerizado para fácil execução

---

## 🛠️ Tecnologias

### Backend
| Tecnologia          | Versão     | Descrição                   |
|---------------------|------------|-----------------------------|
| **Java**            | 17         | Linguagem de programação    |
| **Spring Boot**     | 3.5.4      | Framework principal         |
| **Spring Security** | -          | Autenticação e autorização  |
| **Spring Data JPA** | -          | ORM e acesso a dados        |
| **JWT**             | 0.11.5     | Tokens de autenticação      |
| **PostgreSQL**      | 17         | Banco de dados              |
| **Flyway**          | -          | Migrações do banco          |
| **Lombok**          | -          | Redução de boilerplate      |
| **Maven**           | -          | Gerenciador de dependências |

### Frontend
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Angular** | 22 | Framework frontend |
| **TypeScript** | - | Linguagem |
| **CSS** | - | Estilização |
| **ngx-toastr** | - | Notificações |
| **Angular JWT** | - | Helper para JWT |

### Infraestrutura
| Tecnologia | Descrição |
|------------|-----------|
| **Docker** | Containerização |
| **Docker Compose** | Orquestração dos serviços |

---

## 📋 Requisitos

- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://adoptium.net/) (para desenvolvimento local)
- [Node.js 20+](https://nodejs.org/) (para desenvolvimento local)
- [PostgreSQL 17](https://www.postgresql.org/) (para desenvolvimento local)

---

## 🚀 Como Executar

## Opção 1: Docker

###  1. Clone o repositório e acesse o diretório principal
    git clone https://github.com/caiopgaspar/book-manager-desafio-full-stack

    cd book-manager-desafio-full-stack

### 2. Inicie os containers
    docker-compose up --build

### 3. Acesse a aplicação
#### Frontend:
    http://localhost:4200


## Opção 2: Execução Local (Desenvolvimento)

### 1. Entre na pasta do backend
    cd backend

### 2. Configure o banco de dados no ***application.properties*** (ou use variáveis de ambiente)

### 3. Execute com Maven
    mvn clean install
    mvn spring-boot:run

### 1. Entre na pasta do frontend
    cd frontend/frontend-app

### 2. Instale as dependências
    npm install

### 3. Execute o servidor de desenvolvimento
    ng serve

### 4. Acesse: 
    http://localhost:4200


---

# 📡 Endpoints da API

| Método   | Endpoint         | Descrição                            | Autenticação |
|----------|------------------|--------------------------------------|--------------|
| POST     | `/auth/register` | Criar usuário                        | ❌ Público    |
| POST     | `/auth/login`    | Fazer login (retorna JWT)            | ❌ Público    |
| GET      | `/books`         | Listar livros                        | ✅ JWT        |
| GET      | `/books`         | Listar livros (com busca por título) | ✅ JWT        |
| POST     | `/books/create`  | Criar livro                          | ✅ JWT        |
| GET      | `/books/{id}`    | Buscar livro por ID                  | ✅ JWT        |
| PUT      | `/books/{id}`    | Atualizar livro                      | ✅ JWT        |
| DELETE   | `/books/{id}`    | Remover livro                        | ✅ JWT        |


---
## 📁 Estrutura do Projeto

A estrutura escolhida fornece uma clara separação de responsabilidades e garante uma manutenibilidade e clareza do código. 

    book-manager-desafio-full-stack/
    ├── backend/
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── java/com/bookmanager/backend/
    │   │   │   │   ├── config/                         # Configurações (CORS, Security)
    │   │   │   │   ├── controller/                     # REST Controllers
    │   │   │   │   ├── domain/                         # Entidades
    │   │   │   │   ├── dto/                            # Data Transfer Objects
    │   │   │   │   ├── repository/                     # Repositories
    │   │   │   │   ├── security/                       # JWT e autenticação
    │   │   │   │   └── service/                        # Regras de negócio
    │   │   │   └── resources/
    │   │   │       ├── db/migration/                   # Migrações Flyway
    │   │   │       └── application.properties
    │   │   └── test/
    │   ├── Dockerfile
    │   └── pom.xml
    ├── frontend/frontend-app
    │   ├── src/app/
    │   │   ├── core/                                   # Serviços e lógica
    │   │   ├── features/                               # Páginas (login, books)
    │   │   ├── shared/                                 # Componentes compartilhados
    │   │   └── app.routes.ts, app.ts, app.config.ts    # Configurações de rotas e execução
    │   ├── Dockerfile
    │   ├── nginx.conf
    │   ├── angular.json
    │   └── package.json
    ├── docker-compose.yml
    └── README.md

## ⚡ Funcionalidades

    🔸 Autenticação JWT e Páginas protegidas

    🔸 Registro de usuários e login

    🔸 CRUD completo de livros

    🔸 Busca de livros por título  

    🔸 Dockerização completa da aplicação (backend + frontend + banco PostgreSQL)

    🔸 Migrações e versionamento do banco com Flyway

    🔸 CORS configurado para comunicação frontend-backend

    🔸 Paginação na listagem de livros

    🔸 Notificações toast para feedback ao usuário 



---

