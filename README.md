# critical-resource-api

API REST de gestion de ressources critiques (véhicules, équipements, salles), développée en Java 21 / Spring Boot 3.5 avec une approche **Test-Driven Development** stricte.

Projet portfolio orienté secteur défense et industrie, démontrant la maîtrise du développement backend Java en autonomie.

---

## Stack technique

- **Java 21** / **Spring Boot 3.5**
- **Spring Security** + **JWT** (jjwt 0.12.6)
- **Spring Data JPA** / **Hibernate** / **PostgreSQL 16**
- **Liquibase** — migrations versionnées
- **JUnit 5**, **Mockito**, **AssertJ**, **MockMvc**
- **Testcontainers** — tests d'intégration
- **Springdoc OpenAPI** — documentation Swagger UI
- **Docker** / **Docker Compose**
- **Maven**

---

## Démarrage rapide

### Prérequis
- Docker Desktop

### Lancer l'application

```bash
git clone https://github.com/sloiseau44/critical-resource-api
cd critical-resource-api
docker-compose up --build
```

L'application démarre sur `http://localhost:8080`

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

### Credentials par défaut

| Champ | Valeur |
|---|---|
| Username | `admin` |
| Password | `admin123` |

---

## Authentification

Toutes les requêtes (sauf `/auth/login`) nécessitent un token JWT dans le header `Authorization`.

### 1. Obtenir un token

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

Réponse :
```
eyJhbGciOiJIUzI1NiJ9...
```

### 2. Utiliser le token

```bash
curl -X GET http://localhost:8080/resources \
  -H "Authorization: Bearer <votre_token>"
```

---

## Endpoints

### Resources

| Méthode | URL | Description | Rôle requis |
|---|---|---|---|
| GET | `/resources` | Lister toutes les ressources | Tous |
| GET | `/resources/{id}` | Détail d'une ressource | Tous |
| POST | `/resources` | Créer une ressource | ADMIN, GESTIONNAIRE |
| PUT | `/resources/{id}` | Modifier une ressource | ADMIN, GESTIONNAIRE |
| DELETE | `/resources/{id}` | Désactiver une ressource | ADMIN |

#### Créer une ressource

```bash
curl -X POST http://localhost:8080/resources \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Véhicule VB-01",
    "description": "Véhicule blindé de reconnaissance",
    "category": "VEHICLE"
  }'
```

#### Catégories disponibles
`VEHICLE`, `EQUIPMENT`, `ROOM`

#### Statuts disponibles
`AVAILABLE`, `MAINTENANCE`, `DISABLED`

---

### Reservations

| Méthode | URL | Description | Rôle requis |
|---|---|---|---|
| POST | `/reservations` | Créer une réservation | Tous |
| GET | `/reservations?username=xxx` | Lister les réservations | Tous |
| PUT | `/reservations/{id}/cancel` | Annuler une réservation | Tous |

> ADMIN et GESTIONNAIRE voient toutes les réservations. Un OPERATEUR ne voit que les siennes.

#### Créer une réservation

```bash
curl -X POST http://localhost:8080/reservations \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "resourceId": 1,
    "startDate": "2026-08-01",
    "endDate": "2026-08-05"
  }'
```

---

### Audit

| Méthode | URL | Description | Rôle requis |
|---|---|---|---|
| GET | `/audit-logs` | Historique des actions sensibles | ADMIN |

```bash
curl -X GET http://localhost:8080/audit-logs \
  -H "Authorization: Bearer <token>"
```

---

## Rôles

| Rôle | Droits |
|---|---|
| `ADMIN` | Accès complet à tous les endpoints |
| `GESTIONNAIRE` | Créer et modifier des ressources, voir toutes les réservations |
| `OPERATEUR` | Réserver des ressources, voir et annuler ses propres réservations |

---

## Variables d'environnement

| Variable | Défaut | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/critical_resource_db` | URL de la base de données |
| `DB_USERNAME` | `critical_user` | Utilisateur PostgreSQL |
| `DB_PASSWORD` | `critical_pass` | Mot de passe PostgreSQL |
| `JWT_SECRET` | `criticalresourceapisecretkey1234567890abcdef` | Clé secrète JWT |
| `JWT_EXPIRATION` | `86400000` | Durée du token en ms (24h) |
| `ADMIN_USERNAME` | `admin` | Username de l'admin par défaut |
| `ADMIN_EMAIL` | `admin@criticalresource.com` | Email de l'admin par défaut |
| `ADMIN_PASSWORD` | `admin123` | Mot de passe de l'admin par défaut |

---

## Architecture

```
src/main/java/com/criticalresource/criticalresourceapi/
├── auth/                    # Authentification JWT
├── config/                  # Configuration Spring (Security, OpenAPI, DataInitializer)
└── domain/
    ├── audit/               # Journal d'audit
    ├── reservation/         # Gestion des réservations
    ├── resource/            # Gestion des ressources critiques
    └── user/                # Utilisateurs et rôles
```

Chaque domaine suit le pattern **Controller → Service → Repository** avec séparation stricte entités / DTOs (Request / Response).

---

## Tests

```bash
# Tests unitaires uniquement
mvn test -Dtest="!*IntegrationTest,!*RepositoryTest" -DfailIfNoTests=false

# Tous les tests (nécessite Docker)
mvn verify
```

**Couverture :**
- Tests unitaires : services et controllers (Mockito, MockMvc)
- Tests de repository : JPA avec base H2 / PostgreSQL
- Tests d'intégration : end-to-end avec Testcontainers

---

## Gestion de projet

Le développement a suivi une méthodologie agile avec :
- Un board **GitHub Projects** en mode Kanban
- Une branche **Git** par epic avec pull request à chaque intégration
- Un versionnage **sémantique** (SemVer)
- Une démarche **TDD** stricte (Red → Green → Refactor)