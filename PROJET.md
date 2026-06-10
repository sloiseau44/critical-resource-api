# critical-resource-api — PROJET.md

## Contexte
API REST Java backend pour la gestion de ressources critiques.
Projet portfolio orienté défense/industrie.

## Stack technique
| Outil | Version |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.14 |
| Maven | wrapper mvnw |
| PostgreSQL | 16 (Docker) |
| Liquibase | migration schéma |
| JWT | jjwt 0.12.6 |
| Springdoc | 2.8.8 |
| Lombok | oui |

## Infrastructure locale
- PostgreSQL via Docker : `docker-compose up -d`
    - Host : localhost:5432
    - BDD : critical_resource_db
    - User : critical_user / critical_pass

## Méthodologie
- TDD (Red → Green → Refactor)
- Une branche par epic
- PR vers main à la fin de chaque epic
- Versioning sémantique

## Avancement

### ✅ Epic 1 — Setup (branche : epic/1-setup — mergée)
- [x] 1.1 Initialiser le projet Maven Spring Boot 21
- [x] 1.2 Configurer PostgreSQL, JPA et Liquibase

### ✅ Epic 2 — Entités & Persistence (mergée)
- [x] 2.1 Modéliser et persister un utilisateur
- [x] 2.2 Modéliser et persister une ressource critique
- [x] 2.3 Modéliser et persister une réservation
- [x] 2.4 Modéliser et persister une entrée d'audit

### 🚧 Epic 3 — Sécurité (branche : epic/3-security)
- [ ] 3.1 Définir les rôles ADMIN, GESTIONNAIRE, OPERATEUR
- [ ] 3.2 Permettre à un utilisateur de se connecter et recevoir un token JWT
- [ ] 3.3 Valider le token JWT sur chaque requête

### ⏳ Epic 4 — API Resources
### ⏳ Epic 5 — API Reservations
### ⏳ Epic 6 — Audit & Docs

## Structure du projet
\```
src/main/java/com/criticalresource/criticalresourceapi/
src/main/resources/
  application.properties
  db/changelog/db.changelog-master.xml
docker-compose.yml
\```