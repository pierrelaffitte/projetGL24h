package algorithmes;

public interface algoInterface {

	// TODO : evaluateMSE, evaluateAUC
	/**
	 * Importe le fichier CSV dont le chemin est en paramètre
	 * @param file : chemin du fichier CSV
	 * @return data : données du fichier
	 */
	public Object importer(String file); 
	
	/**
	 * Renvoie un modèle du fichier CSV de l'apprentissage sur la variable à expliquer  Y
	 * TODO mettre les variables explicatives en paramètre ?
	 * Utiliser la fonction importer pour les données d'apprentissage
	 * @param train : chemin du fichier CSV d'apprentissage
	 * @param y : nom de la variable à expliquer (attention à la casse)
	 * @return model
	 */
	public Object fit(String train, String y); 
	
	/**
	 * Affiche le taux d'erreur du modèle appliqué sur l'échantillon test
	 * Utiliser la fonction importer pour les données de test
	 * @param model : modèle d'apprentissage
	 * @param test : chemin du fichier CSV de test
	 * @param y : nom de la variable à expliquer
	 */
	public void evaluate(Object model, String test, String y);

}
