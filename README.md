# Projet Back2Back testing

### Laura Dupuis, Pierre Laffitte, Flavien Lévêque et Charlène Noé

L'objectif de ce projet est de comparer les performances de diverses librairies de Java sur des algorithmes de Machine Learning. Les algorithmes mis en oeuvre dans notre projet sont les arbres de classification et les random forest. Trois librairies ont été testées: Weka, SparkML et Renjin. 

Pour de plus amples informations sur le sujet, merci de consulter notre rapport et le sujet défini par les tuteurs sur le site http://hackmd.diverse-team.fr/CYBg7GAsCmCGIFoTAKyMgTgBwCYECNhCFhpoBGFcnYcgNnIGYg==?view#

Le dossier ressources contient tous les jeux de données importés ainsi que leurs séparations en échantillons test et échantillons train. 

Le package classificationTree contient l'implémentation de la classificationTree pour les diverses librairies. De même, le package randomForest implémente les randomforest.  

Le package interfaces détient nos deux interfaces Algorithme et Implementation. 

Le package utilitaire est l'interface avec l'utilisateur. C'est ce package qui va permettre à l'utilisateur d'importer ses propres données et de les tester sur nos différents algorithmes de Machine Learning. Il pourra également voir les différences entre les trois librairies à l'aide de l'accuracy affichée. 
