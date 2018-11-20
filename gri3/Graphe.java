import java.util.*;

public class Graphe {
    private int maxDegre;
    private int minID;
    private int maxID;

    private ArrayList<String> listOfAretes;
    private ArrayList<Integer> nbOfXs;

    public String[] aretes;

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

    /********************GNM Erdos-Renyi************************/

    public void generateGNM(int n, int m) {
        aretes = new String[m];
        ArrayList<String> listOfAretes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                listOfAretes.add(i + ";" + j);
            }
        }

        Collections.shuffle(listOfAretes);
        aretes = listOfAretes.toArray(new String[0]);
    }

    /********************GNMPErdos-Renyi************************/

    public void generateGNP(int n, double p) {
        ArrayList<String> listOfAretes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (p > Math.random()) {
                    continue;
                }
                listOfAretes.add(i + ";" + j);
            }
        }
        aretes = listOfAretes.toArray(new String[0]);
    }

    /********************Watts-Strogatz************************/

    public void wattsStrogatz(int n, int k, double p) {
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
        aretes = new String[listOfAretes.size()];
        for (int i = 0; i < aretes.length; i++) {
            aretes[i] = listOfAretes.get(i);
        }
    }

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
                listOfAretes.add(i + ";" + r);
                continue;
            }
            listOfAretes.add(i + ";" + k);
        }
    }

    /********************PowerLaw************************/

    public void powerLaw(int n, double y, int n1) {
        // On applique la loi de puissance qui nous renvoi le nombre de sommets n' totaux
        int nPrime = power(n, y, n1);
        System.out.println("n\' = " + nPrime);
        int[] degres = new int[n];
        int index;

        // On peuple notre tableau representant la distribution des degres a partir
        // de Nb(0) = n - n'
        // et de la liste nbOfXs ou nbOfXs(i) = Nb(i) le nb de sommet de degre i

        for (index = 0; index < n - nPrime; index++) {
            degres[index] = 0;
        }

        for (int i = 0; i < nbOfXs.size(); i++) {
            for (int j = 0; j < nbOfXs.get(i); j++) {
                degres[index + j] = i + 1;
            }
            index += nbOfXs.get(i);
        }

        // On permute aleatoirement le tableau representant la distribution des degres
        randomPermutation(degres);

        // On recuperer une liste de sommet generee par l'algorithme de molloyReed
        ArrayList<Integer> nodesList = molloyReed(degres);
        // On genere les aretes en renvoyant une liste de sommets qui n'ont pas pu etre ajoutes
        ArrayList<Integer> failed = createEdges(nodesList);
        if (failed.size() > 0) {
            System.out.println("Le programme n'a pas pu lier " + failed.size() + " sommets");
            // On tente de creer les aretes a partir de la list recue
            ArrayList<Integer> failedBis = createEdges(failed);
            System.out.println("Le programme n'a pas pu lier " + failedBis.size() + " sommets");
        }
        aretes = listOfAretes.toArray(new String[0]);
    }

    public ArrayList<Integer> createEdges(ArrayList<Integer> nList) {
        ArrayList<Integer> failed = new ArrayList<>();
        // Si le nombre de sommets est impair, on ignore le dernier
        int len;
        if (nList.size() % 2 == 1) {
            len = nList.size() - 1;
        }
        else {
            len = nList.size();
        }

        for (int i = 0; i < len; i += 2) {
            if (addLiaison(nList.get(i), nList.get(i + 1))) listOfAretes.add(nList.get(i) + ";" + nList.get(i + 1));
            else {
                failed.add(nList.get(i));
                failed.add(nList.get(i + 1));
            }
        }
        Collections.shuffle(failed);
        return failed;
    }

    public void randomPermutation(int[] array) {
        for (int i = 0; i < array.length; i++) {
            int r = (int) (Math.random() * (array.length - 1));
            int tmp = array[i];
            array[i] = array[r];
            array[r] = tmp;
        }
    }

    public ArrayList<Integer> molloyReed(int[] array) {
        ArrayList<Integer> mReedList = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i]; j++) {
                mReedList.add(i);
            }
        }
        Collections.shuffle(mReedList);
        return mReedList;
    }

    public int power(int n, double y, int n1) {
        int nPrime = 0;
        int x = 1;
        int nbOfX;
        while (nPrime < n) {
            nbOfX = Math.toIntExact(Math.round(n1 * Math.pow(x, -y)));
            if (nbOfX == 0) {
                break; // Si nous sommes a la fin de notre suite, on quitte.
            }
            nPrime += nbOfX;
            nbOfXs.add(nbOfX);
            System.out.println("Nb(" + x + ") = " + nbOfX);
            x++;
        }
        return nPrime;
    }
}