# APESC - Arquivo Público de Santa Catarina

## Visão Geral

O APESC é um sistema de gerenciamento de acervo documental desenvolvido em Spring Boot para o Arquivo Público de Santa Catarina. O sistema permite catalogar, gerenciar e buscar documentos históricos, controlando tipos de documentos, entidades produtoras/receptoras, diagnósticos de restauração e transações documentais.

## Stack Tecnológica

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA** (Hibernate)
- **Jakarta Persistence API**
- **Lombok** (para redução de código boilerplate)
- **MySQL** (banco de dados)
- **Maven** (gerenciamento de dependências)

## Estrutura do Projeto

```
apesc/
├── src/main/java/com/example/apesc/
│   ├── controller/           # Endpoints REST API
│   ├── dto/                  # Data Transfer Objects
│   ├── exception/            # Tratamento de exceções customizadas
│   ├── model/                # Entidades JPA e Enums
│   │   └── enums/            # Enumerações do sistema
│   ├── repository/           # Interfaces Spring Data JPA
│   ├── service/             # Camada de lógica de negócio
│   │   ├── documentaryarchives/  # Serviços de acervo documental
│   │   ├── documenttype/        # Serviços de tipo de documento
│   │   └── impl/               # Implementações dos serviços
│   └── util/                 # Classes utilitárias e validações
│       └── specification/    # Specifications JPA para buscas complexas
├── src/main/resources/
│   ├── application.yml       # Configuração principal
│   └── application-dev.yml   # Configuração de desenvolvimento
└── pom.xml                   # Dependências Maven
```

## Entidades Principais

### 1. AcervoDocumental
Entidade principal que representa um registro no acervo documental.

**Campos:**
- `id`: Identificador único
- `tipoDocumento`: Tipo do documento (obrigatório)
- `entidadeProdutora`: Entidade que produziu o documento (obrigatório)
- `entidadeReceptora`: Entidade que recebeu o documento (opcional)
- `naturezaTransacao`: Natureza da transação (RECEBIDOS_DE, EXPEDIDOS_PARA, RECEBIDOS, EXPEDIDOS)
- `periodo`: Período do documento (obrigatório)
- `estante`: Estante de armazenamento (obrigatório)
- `quantidade`: Quantidade de documentos (obrigatório, > 0)
- `disponibilidade`: Disponibilidade para consulta
- `diagnosticoRestauracao`: Lista de diagnósticos de restauração

### 2. TipoDocumento
Classificação dos tipos de documentos.

**Campos:**
- `id`: Identificador único
- `nomeDocumento`: Nome do tipo de documento
- `abreviacao`: Abreviação do tipo

### 3. EntidadeProdutora
Entidades que produzem ou recebem documentos (governo, secretarias, departamentos, etc.).

**Campos:**
- `id`: Identificador único
- `nome`: Nome da entidade
- `abreviacao`: Abreviação da entidade

### 4. DiagnosticoRestauracao
Diagnósticos de conservação e restauração de documentos.

**Campos:**
- `id`: Identificador único
- `acervoDocumental`: Acervo documental relacionado
- `estadoConservacao`: Estado de conservação
- `procedimentoRestauracao`: Procedimentos de restauração
- `dataDiagnostico`: Data do diagnóstico

## Enums Principais

### NaturezaTransacao
- `RECEBIDOS_DE`: Documentos recebidos de uma entidade (requer entidade receptora)
- `EXPEDIDOS_PARA`: Documentos expedidos para uma entidade (requer entidade receptora)
- `RECEBIDOS`: Documentos recebidos (sem entidade receptora específica)
- `EXPEDIDOS`: Documentos expedidos (sem entidade receptora específica)

## Endpoints da API

### Acervo Documental

#### Criar
```
POST /api/acervo-documental
Content-Type: application/json

{
  "tipoDocumentoId": 1,
  "entidadeProdutoraId": 1,
  "entidadeReceptoraId": 2,
  "naturezaTransacao": "RECEBIDOS_DE",
  "periodo": "1880-1900",
  "estante": "24",
  "quantidade": 10,
  "disponibilidade": true
}
```

#### Listar Todos
```
GET /api/acervo-documental
```

#### Buscar por ID
```
GET /api/acervo-documental/{id}
```

#### Atualizar
```
PATCH /api/acervo-documental/{id}
Content-Type: application/json

{
  "tipoDocumentoId": 1,
  "quantidade": 15
}
```

#### Deletar
```
DELETE /api/acervo-documental/{id}
```

#### Buscar por Tipo de Documento
```
GET /api/acervo-documental/tipo-documento/{tipoDocumentoId}
```

#### Busca Avançada (Specification)
```
GET /api/acervo-documental/search?tipoDocumento=boletins&entidadeProdutora=governo&entidadeReceptora=secretaria&naturezaTransacao=RECEBIDOS_DE
```

**Parâmetros (todos opcionais):**
- `tipoDocumento`: Busca parcial no nome do tipo de documento
- `entidadeProdutora`: Busca parcial no nome da entidade produtora
- `entidadeReceptora`: Busca parcial no nome da entidade receptora
- `naturezaTransacao`: Busca exata (RECEBIDOS_DE, EXPEDIDOS_PARA, RECEBIDOS, EXPEDIDOS)

## Validações Implementadas

### AcervoDocumental

#### Campos Obrigatórios
- Tipo de documento (não pode ser null)
- Entidade produtora (não pode ser null)
- Natureza de transação (não pode ser null)
- Período (não pode ser null ou vazio)
- Estante (não pode ser null ou vazia)
- Quantidade (não pode ser null e deve ser > 0)

#### Regras de Negócio

1. **Duplicidade:** Não permite criar registros duplicados com a mesma combinação de:
   - Tipo de documento
   - Entidade produtora
   - Entidade receptora (se informada)
   - Natureza de transação

2. **Mesmo ID:** Entidade produtora e receptora não podem ter o mesmo ID

3. **Entidade Receptora Obrigatória:**
   - `RECEBIDOS_DE`: Requer entidade receptora
   - `EXPEDIDOS_PARA`: Requer entidade receptora
   - `RECEBIDOS`: Não pode ter entidade receptora
   - `EXPEDIDOS`: Não pode ter entidade receptora

4. **Referências:** Tipo de documento e entidade produtora devem existir no banco

## Camada de Serviço

### AcervoDocumentalService
- `save()`: Cria novo registro com validações
- `update()`: Atualiza registro existente com validações
- `delete()`: Deleta registro
- `findAllWithRelations()`: Lista todos com JOIN FETCH (evita LazyInitializationException)
- `findByIdWithRelations()`: Busca por ID com JOIN FETCH
- `findByTipoDocumento()`: Busca por tipo de documento com JOIN FETCH
- `search()`: Busca avançada usando Specification

### AcervoDocumentalValidation
Classe utilitária que centraliza todas as validações de AcervoDocumental:
- `validateSave()`: Validações para criação
- `validateUpdate()`: Validações para atualização
- `validateDelete()`: Validações para deleção
- `validateFindById()`: Validações para busca por ID
- `validateFindByTipoDocumento()`: Validações para busca por tipo de documento

## Specifications

### AcervoDocumentalSpecification
Permite buscas dinâmicas e complexas usando JPA Specifications:
- Busca parcial em campos de texto (LIKE)
- Case insensitive
- JOIN FETCH para carregar relações
- Combinação de múltiplos critérios com AND

## Configuração

### application.yml
```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:mysql://localhost:3306/apesc_db
    username: root
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
server:
  port: 8080
```

### application-dev.yml
Configurações específicas para ambiente de desenvolvimento.

## Tratamento de Exceções

### CustomException
Exceção customizada que encapsula erros de negócio com:
- Mensagem descritiva (do ErrorConstants)
- Código HTTP apropriado

### ErrorConstants
Enum com todas as mensagens de erro do sistema:
- `TIPO_DOCUMENTO_REQUIRED`
- `ENTIDADE_PRODUTORA_REQUIRED`
- `NATUREZA_TRANSACAO_REQUIRED`
- `PERIODO_REQUIRED`
- `ESTANTE_REQUIRED`
- `QUANTIDADE_REQUIRED`
- `QUANTIDADE_INVALIDA`
- `DUPLICATE_ACERVO`
- `SAME_ENTIDADE_ID`
- `ENTIDADE_RECEPTORA_REQUIRED`
- `NATUREZA_TRANSACAO_INVALIDA`
- E outros...

## Padrões de Projeto

1. **DTO Pattern**: Separação entre entidades de banco e objetos de transferência
2. **Service Layer**: Lógica de negócio separada do controller
3. **Repository Pattern**: Acesso a dados via Spring Data JPA
4. **Validation Layer**: Validações centralizadas em classes utilitárias
5. **Specification Pattern**: Buscas dinâmicas e complexas
6. **Lazy Loading com JOIN FETCH**: Evita LazyInitializationException em consultas

## Como Executar

1. **Pré-requisitos:**
   - Java 17+
   - Maven
   - MySQL

2. **Configurar banco de dados:**
   ```sql
   CREATE DATABASE apesc_db;
   ```

3. **Executar aplicação:**
   ```bash
   mvn spring-boot:run
   ```

4. **Acessar API:**
   - Base URL: `http://localhost:8080/api`

## Observações Importantes

1. **LazyInitializationException**: O sistema usa JOIN FETCH em consultas para evitar problemas de lazy loading quando as relações são acessadas fora da sessão do Hibernate.

2. **Validações**: Todas as validações são aplicadas tanto na criação quanto na atualização, exceto a validação de duplicidade que exclui o próprio registro na atualização.

3. **Transações**: Métodos de escrita são anotados com `@Transactional` para garantir consistência dos dados.

4. **Busca Avançada**: O endpoint `/search` usa JPA Specifications para permitir buscas flexíveis com múltiplos critérios opcionais.

## Estrutura de Commits

O projeto segue uma organização de commits por arquivo/arquitetura para facilitar o rastreamento de mudanças:
- Commits separados para entidades, repositories, services, controllers, validações
- Mensagens de commit descritivas em inglês
- Branch `main` como branch principal
