import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node> {
    public double partialSum;
    public int distance;
    public int id;
    public int x, y;    
    private List<Integer> idNeighbours;
    private static int idK = 0;

    public Node(int id, int... xy) {
        this.partialSum = 0;
        this.id = id;
        this.distance = 0;
        this.idNeighbours = new ArrayList<>();

        if (xy.length == 0) {
            x = id;
            y = id;
        } else {
            x = xy[0];
            y = xy[1];
        }
    }
    
    public int compareTo(Node o) {
        return Integer.compare(this.idNeighbours.size(), o.getDegree());
    }

    public int getDegree() {
        return idNeighbours.size();
    }

    public String stringifyXY() {
        return x+";"+y;
    }

    public String labelDotK() {
        return id + " [ label=\"("+x+","+y+")\" ];";
    }


    public String dot() {
        return id + " [ label =\"("+x+","+y+")\" ];";
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