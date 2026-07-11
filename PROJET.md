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

## Démarrage rapide
1. `docker-compose up -d` — démarrer PostgreSQL
2. `mvn verify` — lancer tous les tests

## Avancement

### ✅ Epic 1 — Setup (mergée)
- [x] 1.1 Initialiser le projet Maven Spring Boot 21
- [x] 1.2 Configurer PostgreSQL, JPA et Liquibase

### ✅ Epic 2 — Entités & Persistence (mergée)
- [x] 2.1 Modéliser et persister un utilisateur
- [x] 2.2 Modéliser et persister une ressource critique
- [x] 2.3 Modéliser et persister une réservation
- [x] 2.4 Modéliser et persister une entrée d'audit

### ✅ Epic 3 — Sécurité (mergée)
- [x] 3.1 Définir les rôles ADMIN, GESTIONNAIRE, OPERATEUR
- [x] 3.2 Permettre à un utilisateur de se connecter et recevoir un token JWT
- [x] 3.3 Valider le token JWT sur chaque requête et rejeter les accès non autorisés

### ✅ Epic 4 — API Resources (mergée)
- [x] 4.1 Lister toutes les ressources avec possibilité de filtrer par statut et catégorie
- [x] 4.2 Permettre à un GESTIONNAIRE de créer une nouvelle ressource critique
- [x] 4.3 Consulter le détail complet d'une ressource par son identifiant
- [x] 4.4 Permettre à un GESTIONNAIRE de modifier les informations d'une ressource
- [x] 4.5 Désactiver une ressource sans la supprimer physiquement de la base

### ✅ Epic 5 — API Reservations (mergée)
- [x] 5.1 Permettre à un OPERATEUR de réserver une ressource sur une plage horaire
- [x] 5.2 Lister les réservations selon les droits de l'utilisateur connecté
- [x] 5.3 Permettre l'annulation d'une réservation avec vérification des droits

### ✅ Epic 6 — Audit & Docs (mergée)
- [x] 6.1 Exposer l'historique complet des actions sensibles, accessible ADMIN uniquement
- [x] 6.2 Documenter tous les endpoints avec OpenAPI et Swagger UI
- [x] 6.3 Couvrir les scénarios critiques avec des tests d'intégration Testcontainers

markdown## Avancement

### ✅ Epic 1 — Setup (mergée)
- [x] 1.1 Initialiser le projet Maven Spring Boot 21
- [x] 1.2 Configurer PostgreSQL, JPA et Liquibase

### ✅ Epic 2 — Entités & Persistence (mergée)
- [x] 2.1 Modéliser et persister un utilisateur
- [x] 2.2 Modéliser et persister une ressource critique
- [x] 2.3 Modéliser et persister une réservation
- [x] 2.4 Modéliser et persister une entrée d'audit

### ✅ Epic 3 — Sécurité (mergée)
- [x] 3.1 Définir les rôles ADMIN, GESTIONNAIRE, OPERATEUR
- [x] 3.2 Permettre à un utilisateur de se connecter et recevoir un token JWT
- [x] 3.3 Valider le token JWT sur chaque requête et rejeter les accès non autorisés

### ✅ Epic 4 — API Resources (mergée)
- [x] 4.1 Lister toutes les ressources avec possibilité de filtrer par statut et catégorie
- [x] 4.2 Permettre à un GESTIONNAIRE de créer une nouvelle ressource critique
- [x] 4.3 Consulter le détail complet d'une ressource par son identifiant
- [x] 4.4 Permettre à un GESTIONNAIRE de modifier les informations d'une ressource
- [x] 4.5 Désactiver une ressource sans la supprimer physiquement de la base

### ✅ Epic 5 — API Reservations (mergée)
- [x] 5.1 Permettre à un OPERATEUR de réserver une ressource sur une plage horaire
- [x] 5.2 Lister les réservations selon les droits de l'utilisateur connecté
- [x] 5.3 Permettre l'annulation d'une réservation avec vérification des droits

### ✅ Epic 6 — Audit & Docs (mergée)
- [x] 6.1 Exposer l'historique complet des actions sensibles, accessible ADMIN uniquement
- [x] 6.2 Documenter tous les endpoints avec OpenAPI et Swagger UI
- [x] 6.3 Couvrir les scénarios critiques avec des tests d'intégration Testcontainers

### ✅ Epic 7 — Robustesse & Finalisation (mergée)
- [x] 7.1 Gestion globale des erreurs (handler HTTP propre)
- [x] 7.2 Validation des entrées avec messages d'erreur explicites
- [x] 7.3 Sécurisation des endpoints par rôle
- [x] 7.4 Données initiales au démarrage (admin par défaut)
- [x] 7.5 Tracer les actions sensibles dans le journal d'audit

### ✅ Epic 8 — Déploiement & Production (mergée)
- [x] 8.1 Sécuriser les credentials avec des variables d'environnement
- [x] 8.2 Créer un Dockerfile pour conteneuriser l'application
- [x] 8.3 Mettre à jour le docker-compose.yml pour lancer app + BDD ensemble
- [x] 8.4 Tester le démarrage complet en mode conteneur

## Démarrage rapide
```bash
git clone https://github.com/sloiseau44/critical-resource-api
cd critical-resource-api
docker-compose up --build
```
Swagger UI : http://localhost:8080/swagger-ui/index.html
Credentials par défaut : admin / admin123

## Ce qui pourrait être ajouté (backlog)
- Vérification de disponibilité d'une ressource avant réservation
- Workflow de confirmation des réservations (PENDING → CONFIRMED)
- Détection des chevauchements de dates
- Pipeline CI/CD GitHub Actions
- Monitoring avec Spring Actuator

## Structure du projet
\```
src/main/java/com/criticalresource/criticalresourceapi/
├── auth/
│   ├── AuthController.java
│   ├── AuthService.java
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── LoginRequest.java
├── config/
│   ├── DataInitializer.java
│   ├── ErrorResponse.java
│   ├── GlobalExceptionHandler.java
│   ├── OpenApiConfig.java
│   └── SecurityConfig.java
└── domain/
├── audit/
│   ├── AuditAction.java
│   ├── AuditLog.java
│   ├── AuditLogController.java
│   ├── AuditLogRepository.java
│   ├── AuditLogResponse.java
│   └── AuditLogService.java
├── reservation/
│   ├── Reservation.java
│   ├── ReservationController.java
│   ├── ReservationRepository.java
│   ├── ReservationRequest.java
│   ├── ReservationResponse.java
│   ├── ReservationService.java
│   └── ReservationStatus.java
├── resource/
│   ├── Resource.java
│   ├── ResourceCategory.java
│   ├── ResourceController.java
│   ├── ResourceRepository.java
│   ├── ResourceRequest.java
│   ├── ResourceResponse.java
│   ├── ResourceService.java
│   └── ResourceStatus.java
└── user/
├── Role.java
├── User.java
└── UserRepository.java

\```