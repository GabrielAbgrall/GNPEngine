Utilisation des différents éléments :


MAZE

new Maze(X)

X étant la taille du labyrinthe (la taille doit être un nombre impair pour fonctionner correctement, autrement une erreur est générée dans le constructeur.



DISPLAY ENGINE

Création : new DisplayEngine(X)

X étant la taille de la surface de rendu (généralement la même taille que la taille du labyrinthe)

Utilisation :
- displayEngine.getDisplayableList().add(X), X étant un objet implémentant IDisplayable (pour le moment aucune possibilité de test car il n'y a aucun autre IDisplayable utilisable)
- displayEngine.display() affiche les éléments de la DisplayableList dans la console via le système de Surface




Les autres éléments sont pour le moment en cours de réalisation et ne sont donc ni fonctionnels ni testables.

Un document plus complet sera rédigé au cours de l'avancement du projet. Il décrira l'utilisation et les algorithmes mis en place dans le code.
