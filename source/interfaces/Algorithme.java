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
	
	public ArrayList<Object> run(String nom_CSV, char delimiter, String y, String...otherArgs) throws IOException;
}

