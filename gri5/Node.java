import java.util.HashSet;
import java.util.LinkedList;

public class Node {
    private int id;
    private int cluster;
    private LinkedList<Integer> idNeighbours;
    private HashSet<Integer> neighboursSet;

    public Node(int id) {
        this.id = id;
        this.cluster = -1;
        this.idNeighbours = new LinkedList<>();
        this.neighboursSet = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public int getDegree() {
        return idNeighbours.size();
    }

    public LinkedList<Integer> getIdNeighbours() {
        return idNeighbours;
    }

    public void addNeighbour(int id) {
        if (idNeighbours.indexOf(id) == -1) {
            idNeighbours.add(id);
            neighboursSet.add(id);
        }
    }

    public int getCluster() {
        return cluster;
    }

    public void setCluster(int cluster) {
        this.cluster = cluster;
    }

    public HashSet<Integer> getNeighboursSet() {
        return neighboursSet;
    }

    public void setNeighboursSet(HashSet<Integer> neighboursSet) {
        this.neighboursSet = neighboursSet;
    }
}