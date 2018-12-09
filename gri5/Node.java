import java.util.LinkedList;

public class Node {
    private int id;
    private LinkedList<Integer> idNeighbours;

    public Node(int id) {
        this.id = id;
        this.idNeighbours = new LinkedList<>();
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
        }
    }
}