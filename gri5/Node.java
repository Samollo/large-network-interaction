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

    public void setId(int id) {
        this.id = id;
    }

    public int getDegree() {
        return idNeighbours.size();
    }

    public LinkedList<Integer> getIdNeighbours() {
        return idNeighbours;
    }

    public void setIdNeighbours(LinkedList<Integer> idNeighbours) {
        this.idNeighbours = idNeighbours;
    }

    public boolean addNeighbour(int id) {
        if (idNeighbours.indexOf(id) != -1) {
            return false;
        }
        return idNeighbours.add(id);
    }

    public int removeNeighbour(int id) {
        if (idNeighbours.indexOf(id) == -1) {
            return -1;
        }
        return idNeighbours.remove(idNeighbours.indexOf(id));
    }
}