import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private int maxDegree;
    private int minID;
    private int maxID;

    private List<String> edgesList;
    private String[] path;

    private Map<Integer, Node> nodes;

    public Graph() {
        nodes = new HashMap<>();
        edgesList = new ArrayList<>();
        maxDegree = 0;
    }

    public List<String> getEdgesList() {
        return edgesList;
    }

    /***** Not oriented *****/

    public boolean addEdge(int idFirst, int idSecond) {
        if (idFirst == idSecond) {
            return false;
        }
        Node s1 = nodes.get(idFirst);
        Node s2 = nodes.get(idSecond);
        if (s1 == null) {
            s1 = new Node(idFirst);
            nodes.put(idFirst, s1);
        }
        if (s2 == null) {
            s2 = new Node(idSecond);
            nodes.put(idSecond, s2);
        }

        boolean resultProcess = s1.addNeighbour(idSecond) && s2.addNeighbour(idFirst);
        if (resultProcess) {
            updateDegree(s1.getDegree());
            updateDegree(s2.getDegree());
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

    public boolean updateDegree(int n) {
        if (n > maxDegree) {
            maxDegree = n;
            return true;
        }
        return false;
    }

    /******************** Kleinberg ************************/

    public void kleinberg(int c, int origine_x, int origine_y, int cible_x, int cible_y) {

    }

    /******************** Watts-Strogatz ************************/

    public void fork(int n, int k, double p) {
        for (int i = 0; i < n; i++) {
            // tire au hasard un Node. Il ne doit pas etre deja voisin avec nodes[i]
            int r;
            do {
                r = (int) (Math.random() * (n - 1));
            } while (nodes.get(i).getIdNeighbours().contains(r));

            // tire au hasard pour voir si on effectue le branchement entre i et r ou non
            if (Math.random() < p) {
                nodes.get(i).removeNeighbour(k);
                addEdge(i, r);
            }
        }
    }

    public void wattsStrogatz(int n, int k, double p, int origine, int cible) {
        for (int i = 0; i < n; i++) {
            for (int j = 1; j < k + 1; j++) {
                addEdge(i, (i + j) % n);
            }
        }

        for (int i = 1; i < k + 1; i++) {
            fork(n, i, p);
        }

        pathWS(origine, cible, n);
    }

    public int distWS(int x, int y, int n) {
        int hi = Math.max(x, y);
        int lo = Math.min(x, y);
        return Math.min(hi - lo, Math.abs(hi - n - lo));
    }

    public void pathWS(int origine, int cible, int n) {
        int current = origine;
        int dist = 0;
        while (current != cible) {
            System.out.print(current + " ");
            int shortestDist = n;
            int nearestNode = -1;
            for (int i = 0; i < nodes.get(current).getIdNeighbours().size(); i++) {
                int d = distWS(nodes.get(current).getIdNeighbours().get(i), cible, n);
                if (d < shortestDist) {
                    shortestDist = d;
                    nearestNode = nodes.get(current).getIdNeighbours().get(i);
                }
            }
            if (distWS(current, cible, n) < shortestDist) {
                System.out.println("Nous sommes dans une impasse. dist(current, target) < dist(voisins, target)");
                return;
            }
            edgesList.add(current + ";" + nearestNode);
            current = nearestNode;
            dist += 1;
        }
        System.out.print(current + " ");
        System.out.println("longueur du chemin: " + dist);
    }
}