import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        LinkedList<Atome> elementList = new LinkedList<Atome>();

        try { // saisir les atomes dans le tableau periodique a l'aide de bufferedreader
            BufferedReader inFile = new BufferedReader(new FileReader("algo.txt")); // lire le file algo.txt
            String line = "";
            while ((line = inFile.readLine()) != null){
                line = line.trim();
                if (line.length() > 0) {
                    elementList.add(new Atome(0, Integer.parseInt(line.split(" ")[0]), line.split(" ")[4], Integer.parseInt(line.split(" ")[1]), line.split(" ")[5]));
                }
            }
        } catch(Exception ef) {
            ef.printStackTrace(); // pour savoir s'il y a l'exception dans la partie try
        }

        FenetreMolecules fenetreMolecules1 = new FenetreMolecules(elementList);
    }



}

