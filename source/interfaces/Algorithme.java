package interfaces;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Interface des différents algorithmes
 * @author Laura Dupuis, Pierre Laffitte, Flavien Lévêque, Charlène Noé
 *
 */
public interface Algorithme {

	/**
	 * Affiche les taux d'erreur pour les trois librairies selon l'algo choisi
	 * @param nom_CSV nom du fichier CSV à tester
	 * @param delimiter delimiteur du CSV
	 * @param y nom de la variable à expliquer
	 * @param otherArgs autres arguments relatifs à l'algo choisi
	 * @throws IOException exception de lecture
	 */
	public void getAccuracy(String nom_CSV, char delimiter, String y, String...otherArgs) throws IOException;
	
	/**
	 * Retourne les accuracy des trois librairies sous forme de liste
	 * 0 : SparkML, 1 : Weka, 2 : Renjin
	 * @param nom_CSV nom du fichier csv
	 * @param delimiter delimiteur du fichier
	 * @param y variable d'intérêt
 	 * @param otherArgs autres arguments
	 * @return liste des accuracy
	 * @throws IOException exception de lecture
	 */
	public ArrayList<Object> run(String nom_CSV, char delimiter, String y, String...otherArgs) throws IOException;
}

