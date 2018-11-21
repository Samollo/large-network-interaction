import java.util.ArrayList;
import java.util.HashSet;

public class Node implements Comparable<Node> {
    public int partialSum;
    public int distance;
    public int id;
    public boolean deleted;
    public int deletedNeighbour;
    public int[] coordinate;
    private ArrayList<Integer> idNeighbours;

    public Node(int id, int... xy) {
        this.deletedNeighbour = 0;
        this.partialSum = 0;
        this.id = id;
        this.distance = 0;
        this.deleted = false;
        this.idNeighbours = new ArrayList<>();
        this.coordinate = new int[2];

        if (xy.length == 0) {
            this.coordinate = new int[2];
            coordinate[0] = id;
            coordinate[1] = id;
        } else {
            this.coordinate = xy;
        }
    }

    public int compareTo(Node o) {
        return Integer.compare(this.idNeighbours.size(), o.getDegre());
    }

    public int getDegre() {
        return idNeighbours.size();
    }

    public boolean addVoisin(int id) {
        if (idNeighbours.indexOf(id) != -1) {
            return false;
        }
        idNeighboursSet.add(id);
        idNeighbours.add(id);
        return true;
    }

    public int removeVoisin(int id) {
        if (idNeighbours.indexOf(id) == -1) {
            return 0;
        }
        idNeighboursSet.remove(id);
        return idNeighbours.remove(idNeighbours.indexOf(id));
    }

    public ArrayList<Integer> getidNeighbours() {
        return idNeighbours;
    }

    public HashSet<Integer> getidNeighboursSet() {
        return idNeighboursSet;
    }

}