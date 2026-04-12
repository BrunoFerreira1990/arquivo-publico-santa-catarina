# APESC - Docker Setup

Este documento contém instruções para executar a aplicação APESC usando Docker e Docker Compose.

## Pré-requisitos

- Docker
- Docker Compose

## Como executar

### 1. Construir e executar os serviços

```bash
# Construir e iniciar todos os serviços
docker-compose up --build

# Executar em background
docker-compose up -d --build
```

### 2. Verificar os serviços

```bash
# Verificar status dos containers
docker-compose ps

# Ver logs da aplicação
docker-compose logs apesc-app

# Ver logs do banco de dados
docker-compose logs postgres
```

### 3. Parar os serviços

```bash
# Parar todos os serviços
docker-compose down

# Parar e remover volumes (CUIDADO: isso apagará os dados do banco)
docker-compose down -v
```

## Configuração

### Banco de dados
- **Host**: localhost (ou postgres dentro da rede Docker)
- **Porta**: 5432
- **Database**: apesc_db
- **Usuário**: apesc_user
- **Senha**: apesc_password

### Aplicação
- **URL**: http://localhost:8080
- **Porta**: 8080

## Estrutura dos arquivos

- `Dockerfile`: Configuração para construir a imagem da aplicação Spring Boot
- `docker-compose.yml`: Orquestração dos serviços (aplicação + banco)
- `src/main/resources/application.yml`: Configurações da aplicação

## Rede Docker

Os serviços estão conectados através da rede `apesc-network`, permitindo comunicação entre a aplicação e o banco de dados.

## Volumes

- `postgres_data`: Volume persistente para dados do PostgreSQL

## Variáveis de ambiente

As seguintes variáveis podem ser configuradas:

- `SPRING_DATASOURCE_URL`: URL de conexão com o banco
- `SPRING_DATASOURCE_USERNAME`: Usuário do banco
- `SPRING_DATASOURCE_PASSWORD`: Senha do banco
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Configuração do Hibernate
- `SPRING_JPA_SHOW_SQL`: Exibir SQL no console
- `GOOGLE_CLIENT_ID`: ID do cliente OAuth2 Google
- `GOOGLE_CLIENT_SECRET`: Secret do cliente OAuth2 Google


