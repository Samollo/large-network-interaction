import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    public int partialSum;
    public int distance;
    public int id;
    public int[] coordinate;
    private List<Integer> idNeighbours;
    private static int idK = 0;

    public Node(int id, int... xy) {
        this.partialSum = 0;
        this.id = id;
        this.distance = 0;
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
        return Integer.compare(this.idNeighbours.size(), o.getDegree());
    }

    public int getDegree() {
        return idNeighbours.size();
    }

    public boolean addNeighbour(int id) {
        if (idNeighbours.indexOf(id) != -1) {
            return false;
        }
        idNeighbours.add(id);
        return true;
    }

    public int removeNeighbour(int id) {
        if (idNeighbours.indexOf(id) == -1) {
            return 0;
        }
        return idNeighbours.remove(idNeighbours.indexOf(id));
    }

    public List<Integer> getIdNeighbours() {
        return idNeighbours;
    }

}