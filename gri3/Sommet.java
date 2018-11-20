import java.util.ArrayList;
import java.util.HashSet;

public class Sommet implements Comparable<Sommet> {
    public int partialSum;
    public int distance;
    public int id;
    public boolean deleted;
    public int deletedVoisins;

    private HashSet<Integer> idVoisinsSet;
    private ArrayList<Integer> idVoisins;
    public ArrayList<Integer> entrants;
    public ArrayList<Integer> sortants;

    public Sommet(int id) {
        this.deletedVoisins = 0;
        this.partialSum = 0;
        this.id = id;
        this.distance = 0;
        this.deleted = false;
        this.idVoisins = new ArrayList<>();
        this.idVoisinsSet = new HashSet<>();
        this.entrants = new ArrayList<>();
        this.sortants = new ArrayList<>();

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

    public int removeSortant(int id) {
        if (sortants.indexOf(id) == -1) {
            return 0;
        }
        return sortants.remove(sortants.indexOf(id));
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

    public boolean addEntrant(int id) {
        if (entrants.indexOf(id) != -1) {
            return false;
        }
        entrants.add(id);
        return true;
    }

    public ArrayList<Integer> getEntrants() {
        return entrants;
    }

    public boolean addSortant(int id) {
        if (sortants.indexOf(id) != -1) {
            return false;
        }
        sortants.add(id);
        return true;
    }

    public ArrayList<Integer> getSortants() {
        return sortants;
    }

    public HashSet<Integer> getIdVoisinsSet() {
        return idVoisinsSet;
    }

}