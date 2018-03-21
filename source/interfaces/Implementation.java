package interfaces;

/**
 * Interface pour les implémentations des 3 librairies 
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public interface Implementation {

	/**
	 * Importe le fichier CSV dont le chemin est en paramètre
	 * @param file chemin du fichier CSV
	 * @return données du fichier
	 */
	public Object importer(String file); 
	
	/**
	 * Renvoie un modèle d'apprentissage du fichier CSV sur la variable à expliquer  Y
	 * @param train chemin du fichier CSV d'apprentissage
	 * @param y nom de la variable à expliquer (attention à la casse)
	 * @param otherArgs autres arguments relatifs à l'algo utilisé
	 * @return modèle d'apprentissage
	 */
	public Object fit(String train, String y, String... otherArgs); 
	
	/**
	 * Renvoie le taux d'erreur du modèle appliqué sur l'échantillon test
	 * @see #importer(String)
	 * @see #fit(String, String, String...)
	 * @param train chemin du fichier CSV d'apprentissage
	 * @param test chemin du fichier CSV de test
	 * @param y nom de la variable à expliquer
	 * @param otherArgs autres arguments relatifs à l'algo utilisé
	 * @return taux d'erreur
	 */
	public Object evaluate(String train, String test, String y, String... otherArgs);

}
