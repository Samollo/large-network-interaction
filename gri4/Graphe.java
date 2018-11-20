import java.util.*;

public class Graphe {
    private int maxDegre;
    private int minID;
    private int maxID;

    private ArrayList<String> listOfAretes;
    public String[] path;

    Map<Integer, Sommet> nodes;

    public Graphe() {
        nodes = new HashMap<>();
        maxDegre = 0;
        listOfAretes = new ArrayList<>();
        nbOfXs = new ArrayList<>();
    }

    /*****Not oriented*****/

    public boolean addLiaison(int idFirst, int idSecond) {
        if (idFirst == idSecond) {
            return false;
        }
        Sommet s1 = nodes.get(idFirst);
        Sommet s2 = nodes.get(idSecond);
        if (s1 == null) {
            s1 = new Sommet(idFirst);
            nodes.put(idFirst, s1);
        }
        if (s2 == null) {
            s2 = new Sommet(idSecond);
            nodes.put(idSecond, s2);
        }

        boolean resultProcess = s1.addVoisin(idSecond) && s2.addVoisin(idFirst);
        if (resultProcess) {
            updateDegre(s1.getDegre());
            updateDegre(s2.getDegre());
        } else {
        }
        if (idFirst > maxID) {
            maxID = idFirst;
        }
        if (idFirst < minID) {
            minID = idFirst;
        }

        if (idSecond > maxID) {
            maxID = idSecond;
        }
        if (idSecond < minID) {
            minID = idSecond;
        }
        return resultProcess;
    }

    public boolean updateDegre(int n) {
        if (n > maxDegre) {
            maxDegre = n;
            return true;
        }
        return false;
    }

    /********************Kleinberg************************/

    public void kleinberg(int c, int origine_x, int origine_y, int cible_x, int cible_y) {

    }

    /********************Watts-Strogatz************************/

    public void embranchement(int n, int k, double p) {
        for (int i = 0; i < n; i++) {
            // tire au hasard un sommet. Il ne doit pas etre deja voisin avec nodes[i]
            int r = (int) (Math.random() * (n - 1));
            while (nodes.get(i).getIdVoisins().contains(r)) {
                r = (int) (Math.random() * (n - 1));
            }
            // tire au hasard pour voir si on effectue le branchement entre i et r ou non
            if (Math.random() < p) {
                nodes.get(i).removeVoisin(k);
                addLiaison(i, r);
                continue;
            }
            listOfAretes.add(i + ";" + k);
        }
    }

    public void wattsStrogatz(int n, int k, double p, int origine, int cible) {
        System.out.println("[INFO] n = " + n);
        System.out.println("[INFO] k = " + k);
        System.out.println("[INFO] p = " + p);
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < k + 1; j++) {
                addLiaison(i, (i + j) % n);
            }
        }
        System.out.println("Avant rebranchement, Degre(max) = " + maxDegre);

        for (int i = 1; i < k + 1; i++) {
            embranchement(n, i, p);
        }
    }

    public int pathWS(int origine, int cible) {
        if (origine == cible) {
            return new String[]{0};
        }
        int current = origine;
        

    }
}