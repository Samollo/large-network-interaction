import java.util.ArrayList;
import java.util.HashSet;

public class Sommet implements Comparable<Sommet> {
    public int partialSum;
    public int distance;
    public int id;
    public boolean deleted;
    public int deletedVoisins;
    public int[] coordoonnees;
    private HashSet<Integer> idVoisinsSet;
    private ArrayList<Integer> idVoisins;

    public Sommet(int id, int ...xy) {
        this.deletedVoisins = 0;
        this.partialSum = 0;
        this.id = id;
        this.distance = 0;
        this.deleted = false;
        this.idVoisins = new ArrayList<>();
        this.idVoisinsSet = new HashSet<>();
        this.coordoonnees = new int[2];
        if (xy.length == 0) {
            this.coordoonnees = new int[2];
            coordoonnees[0] = id;
            coordoonnees[1] = id;
        } else {
            this.coordoonnees = xy;
        }

    }

    public int compareTo(Sommet o) {
        return Integer.compare(this.idVoisins.size(), o.getDegre());
    }

    public int getDegre() {
        return idVoisins.size();
    }

    public boolean addVoisin(int id) {
        if (idVoisins.indexOf(id) != -1) {
            return false;
        }
        idVoisinsSet.add(id);
        idVoisins.add(id);
        return true;
    }


    public int removeVoisin(int id) {
        if (idVoisins.indexOf(id) == -1) {
            return 0;
        }
        idVoisinsSet.remove(id);
        return idVoisins.remove(idVoisins.indexOf(id));
    }

    public ArrayList<Integer> getIdVoisins() {
        return idVoisins;
    }

    public HashSet<Integer> getIdVoisinsSet() {
        return idVoisinsSet;
    }

}