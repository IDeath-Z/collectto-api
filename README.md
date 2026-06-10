<p align="center">
  <img src="https://raw.githubusercontent.com/IDeath-Z/collectto-api/main/assets/logo.png" alt="Collectto" width="120"/>
</p>

> Backend da rede social para colecionadores — organize seu acervo e conecte-se com outros entusiastas.

Collectto é uma rede social mobile voltada especificamente para colecionadores. A plataforma resolve um problema real: redes sociais genéricas não oferecem ferramentas para catalogar um acervo. No Collectto, cada item registra dados como data de aquisição e histórico de uso, além de atributos totalmente personalizáveis — um colecionador de vinis cadastra prensagem e estado da capa; um de action figures, escala e fabricante — sem que a plataforma precise prever cada nicho. Este repositório contém a API REST que alimenta o aplicativo.

---

## Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.4.3 |
| Segurança | Spring Security + JWT (Auth0 java-jwt 4.4) |
| Banco de dados | PostgreSQL 16 |
| ORM | Spring Data JPA / Hibernate |
| Migrações | Flyway |
| Storage | Oracle Cloud Object Storage |
| Documentação | SpringDoc OpenAPI 2.8 (Swagger UI) |
| Containerização | Docker + Docker Compose |
| Hospedagem | VPS Linux |

---

## CI/CD

O repositório possui pipeline completo via **GitHub Actions**:

- **Testes** — executados automaticamente em todo Pull Request para a `main`
- **Deploy** — acionado após merge na `main`, só ocorre se todos os testes passarem; conecta via SSH à VPS e sobe o container atualizado com Docker Compose

---

## Arquitetura

O projeto segue os princípios da **Clean Architecture**, com separação clara entre as camadas de apresentação, aplicação, domínio e infraestrutura.

```
src/main/java/com/collectto/api_collectto/
├── presentation/          # Controllers REST e DTOs
├── application/           # Use Cases (regras de negócio)
├── domain/                # Entidades, enums e ports (interfaces)
└── infrastructure/        # Implementações: JPA, JWT, BCrypt, Oracle Storage
```

---

## Endpoints

A autenticação é gerenciada pelo Spring Security. Todos os endpoints exigem JWT no header `Authorization: Bearer {token}`, exceto os marcados como públicos.

### Autenticação
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/auth/login` | Autentica o usuário e retorna access + refresh token | público |
| `POST` | `/auth/refresh` | Renova o access token a partir do refresh token | público |

### Usuários
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/users/create` | Cria uma nova conta | público |
| `GET` | `/users/{userId}` | Retorna dados públicos de um usuário | ✅ |
| `GET` | `/users/me` | Retorna dados do usuário autenticado | ✅ |
| `PATCH` | `/users/update` | Atualiza o perfil do usuário autenticado | ✅ |
| `PATCH` | `/users/password` | Altera a senha do usuário autenticado | ✅ |
| `DELETE` | `/users/me` | Desativa a conta do usuário autenticado | ✅ |
| `POST` | `/users/follow/{followedId}` | Envia solicitação de seguir um usuário | ✅ |
| `DELETE` | `/users/follow/{followedId}` | Deixa de seguir um usuário | ✅ |
| `PATCH` | `/users/follow/{followerId}/accept` | Aceita uma solicitação de seguir pendente | ✅ |
| `PATCH` | `/users/follow/{followerId}/decline` | Recusa uma solicitação de seguir pendente | ✅ |

### Coleções
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/collections/create` | Cria uma nova coleção | ✅ |
| `GET` | `/collections/{collectionId}` | Retorna dados de uma coleção | ✅ |
| `GET` | `/collections/by-user/{userId}` | Lista coleções de um usuário (paginado) | ✅ |
| `PATCH` | `/collections/update/{collectionId}` | Atualiza uma coleção | ✅ |
| `DELETE` | `/collections/{collectionId}` | Remove uma coleção | ✅ |
| `POST` | `/collections/follow/{collectionId}` | Segue uma coleção | ✅ |
| `DELETE` | `/collections/follow/{collectionId}` | Deixa de seguir uma coleção | ✅ |

### Itens
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/items/create` | Adiciona um item a uma coleção | ✅ |
| `GET` | `/items/{collectionId}/{itemId}` | Retorna dados de um item | ✅ |
| `GET` | `/items/by-collection/{collectionId}` | Lista itens de uma coleção (paginado) | ✅ |
| `PATCH` | `/items/update/{itemId}` | Atualiza um item | ✅ |
| `DELETE` | `/items/{itemId}` | Remove um item | ✅ |
| `POST` | `/items/like/{itemId}` | Curte um item | ✅ |
| `DELETE` | `/items/like/{itemId}` | Remove curtida de um item | ✅ |
| `GET` | `/items/likes/{itemId}` | Lista usuários que curtiram um item (paginado) | ✅ |
| `POST` | `/items/comment/{itemId}` | Comenta em um item | ✅ |
| `DELETE` | `/items/comment/{commentId}` | Remove um comentário | ✅ |
| `GET` | `/items/comments/{itemId}` | Lista comentários de um item (paginado) | ✅ |

### Social
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `GET` | `/social/explore` | Lista cards públicos para exploração (paginado) | ✅ |
| `GET` | `/social/feed` | Feed personalizado do usuário (paginado) | ✅ |
| `GET` | `/social/search?term=` | Busca global de usuários e coleções (paginado) | ✅ |

### Notificações
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `GET` | `/notifications` | Lista notificações do usuário autenticado (paginado) | ✅ |

### Upload
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/uploads/presigned-urls` | Gera URLs pré-assinadas para upload direto ao Oracle Cloud (válidas por 5 min) | ✅ |

---

## Rodando localmente

### Pré-requisito

- Docker e Docker Compose

### 1. Clone o repositório

```bash
git clone https://github.com/IDeath-Z/collectto-api.git
cd collectto-api
```

### 2. Configure as variáveis de ambiente

```bash
cp .env.example .env
```

Edite o arquivo `.env`:

```env
# Banco de dados
DB_NAME=
DB_USER=
DB_PASSWORD=

# JWT
JWT_SECRET=

# Oracle Cloud Storage
ORACLE_TENANT_ID=
ORACLE_USER_ID=
ORACLE_FINGERPRINT=
ORACLE_REGION=
ORACLE_NAMESPACE=
ORACLE_BUCKET=
ORACLE_PRIVATE_KEY=
```

### 3. Suba os containers

O Docker cuida do build da aplicação e do banco de dados automaticamente.

```bash
docker-compose up -d
```

### 4. Acesse

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Modelo de dados

![Diagrama ER](https://raw.githubusercontent.com/IDeath-Z/collectto-api/main/assets/diagrama_er_collectto_31_05.jpg)

O banco possui 8 entidades principais gerenciadas pelo Flyway:

- **User** — nome, username, e-mail, senha em BCrypt, bio, data de nascimento, foto de perfil e imagem de fundo, contadores de seguidores/seguindo; suporta soft delete via `deactivated_at`
- **Collection** — nome, descrição, visibilidade (pública/privada), imagem de capa, tags e contador de seguidores; suporta soft delete via `deactivated_at`
- **Item** — nome, descrição, data de aquisição, último uso, URLs de mídia em array nativo do PostgreSQL (`TEXT[]`), atributos customizáveis em JSONB, tags, contadores de curtidas e comentários; suporta soft delete via `deactivated_at`
- **Tag** — etiquetas reutilizáveis para coleções e itens; busca fuzzy habilitada via extensão `pg_trgm`
- **User_Follow / Collection_Follow** — relacionamentos N:N de seguir; User_Follow possui status PENDING/ACCEPTED
- **Item_Like / Item_Comment** — interações sociais nos itens
- **Notification** — notificações com contexto (follow, curtida, comentário), referência ao objeto relacionado e flag de leitura

---

## Testes

Os testes estão sendo implementados progressivamente com foco na camada de domínio, garantindo que as regras de negócio das entidades se comportem corretamente de forma isolada, sem dependência de frameworks ou infraestrutura.

Atualmente cobertos: `UserTest` e `CollectionTest`.

---

## Sobre o projeto

Projeto desenvolvido em equipe durante a graduação em Engenharia de Computação na UNISO, onde fui responsável por toda a arquitetura e desenvolvimento do backend e da infraestrutura — incluindo configuração da VPS, containerização com Docker e pipeline de CI/CD. O MVP cobre cinco módulos: gestão de perfis, coleções, catalogação de itens, engajamento social e notificações.

Este repositório cobre exclusivamente o backend. O aplicativo mobile foi desenvolvido por outros integrantes da equipe e está disponível em [Raymonds-dev/Collectto](https://github.com/Raymonds-dev/Collectto).
