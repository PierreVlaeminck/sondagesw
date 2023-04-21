## Projet Service Web de gestion de Sondages

### 1 - Description du projet
  
Création d'un web service pour un service de création de sondage, lié a une base de donnée,
il est possible de créer, modifier ou supprimer les sondages présents dans le web service.

### 2 - Pages du site :
Ce projet ne contient que le web service et aucune page HTML, il s'agit juste de la partie Web Service,
qui permet aux données d'être utilisées par un site web.
Afin d'avoir la partie client un second Repositories sera disponible sur mon GitHub, ayant pour nom :
sondagecs (Sondage client service)


### 3 - Outils de réalisation :
Code réalisé avec : Intellij  
Outils collaboratifs : GitHub  
Framework utilisé : Spring Boot
Base de donnée : PhpMyAdmin

#### Bibliothèques utilisées :
- Spring Boot DevTools  
- Spring Web  
- Mysql driver  
- Spring Data JPA  
- Spring Validation  
- Sping DOC  

### 4 - Points d'entrée de l'API

- GET /rest/votes/ : Récupère la liste de tous les sondages dont la date de cloture est dans le futur
- GET /rest/votes/{id} : Lecture du sondage {id} et retour au format JSON
- POST /rest/votes/ : Création d'un nouveau sondage puis réponse HTTP 200
- PUT /rest/votes/{id} : Modification du sondage {id} puis réponse HTTP 200
- DELETE /rest/votes/{id} : Suppression du sondage {id} puis réponse HTTP 200

### 5 - Installation du projet :
Logiciel requis : Intellij, Google Chrome(Nécessaire si vous voulez réaliser les tests de validation)  
Une base de donnée en local avec PhpMyAdmin est nécessaire pour tester le bon fonctionnement.
Vous pouvez réaliser des tests de validaton à l'adresse suivante une fois le serveur lancé :  
http://localhost:8080/swagger-ui/index.html#/

#### Lancement du projet :
Attention si lors du lancement du web service cela vous affiche une erreur pour le port utilisé,
vous pouvez alors venir le modifier dans le fichier "application.properties", il suffit pour cela d'ajouter :
server.port="numéro de port"

### 6 - Equipes :
Projet réalisé seul.

Merci d'avoir pris le temp de lire le ReadMe.