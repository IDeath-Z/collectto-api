<p align="center">
  <img src="https://raw.githubusercontent.com/IDeath-Z/collectto-api/main/assets/logo.png" alt="Collectto" width="120"/>
</p>

> Backend da rede social para colecionadores — organize seu acervo e conecte-se com outros entusiastas.

Collectto é uma **rede social mobile** voltada especificamente para colecionadores. A plataforma resolve um problema real: as redes sociais genéricas não oferecem ferramentas para catalogar itens com detalhes como data de aquisição, condição de conservação, valor estimado e atributos personalizados. Este repositório contém a **API REST** que alimenta o aplicativo.

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
| Documentação | SpringDoc OpenAPI 2.3 (Swagger UI) |
| Containerização | Docker + Docker Compose |
| Hospedagem | VPS Linux |

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
| `POST` | `/auth/login` | Autentica o usuário e retorna um JWT | público |

### Usuários
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/users/create` | Cria uma nova conta | público |
| `GET` | `/users/{userId}` | Retorna dados de um usuário | ✅ |
| `PATCH` | `/users/profile` | Atualiza o perfil do usuário autenticado | ✅ |

### Coleções
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/collections/create` | Cria uma nova coleção | ✅ |

### Itens
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/items/create` | Adiciona um item a uma coleção | ✅ |

### Upload
| Método | Rota | Descrição | Auth |
|--------|------|-----------|------|
| `POST` | `/uploads/presigned-urls` | Gera URLs pré-assinadas para upload direto ao Oracle Cloud (válidas por 5 min) | ✅ |

> **Projeto em desenvolvimento ativo.** Novos endpoints de interação social (curtidas, comentários, seguir) e notificações serão adicionados nas próximas iterações.

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

O banco possui 8 entidades principais gerenciadas pelo Flyway:

- **User** — dados do usuário (UUID, username, e-mail, senha em BCrypt, bio, foto de perfil)
- **Collection** — coleção de um usuário (visibilidade pública/privada, imagem de capa)
- **Item** — item dentro de uma coleção (datas, URLs de mídia, atributos customizáveis em JSONB)
- **Tag** — etiquetas reutilizáveis para coleções e itens
- **User_Follow / Collection_Follow** — relacionamentos N:N de seguir
- **Item_Like / Item_Comment** — interações sociais nos itens
- **Notifications** — central de notificações da plataforma

---

## Sobre o projeto

Collectto é um **projeto acadêmico integrador** desenvolvido na Universidade de Sorocaba (UNISO) — Engenharia de Computação. O MVP cobre cinco módulos: gestão de perfis, coleções, catalogação de itens, engajamento social e notificações.
