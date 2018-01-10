package algorithmes;

public interface algoInterface {

	/**
	 * Importe le fichier CSV dont le chemin est en paramètre
	 * @param file
	 * @return data
	 */
	public Object importer(String file); 
	
	/**
	 * Renvoie un modèle du fichier CSV de l'apprentissage
	 * Utiliser la fonction importer pour les données d'apprentissage
	 * @param train
	 * @return model
	 */
	public Object fit(String train); 
	
	/**
	 * Affiche le taux d'erreur du modèle appliqué sur l'échantillon test
	 * Utiliser la fonction importer pour les données de test
	 * @param model
	 * @param test
	 */
	public void evaluate(Object model, String test);

}
