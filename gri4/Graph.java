import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graalvm.compiler.graph.Node;

public class Graph {
    private int maxDegree;
    private int minID;
    private int maxID;

    private List<String> edgesList;
    private String[] path;

    private Map<Integer, Node> nodes;
    private Map<String, Node> knodes;
    private Node[] grid;

    public Graph() {
        nodes = new HashMap<>();
        knodes = new HashMap<>();
        edgesList = new ArrayList<>();
        maxDegree = 0;
    }

    public Graph(int c) {
        nodes = new HashMap<>();
        knodes = new HashMap<>();
        edgesList = new ArrayList<>();
        maxDegree = 0;
        grid = new Sommet[c*c];
        int idx = 0;
        for (int x = 0; i < c; i++) {
            for(int y = 0; j < c; j++) {
                grid[idx] = new Node(idx, x, y);
                knodes.put(x+";"+y, grid[idx]);
                nodes.put(idx, grid[idx]);
                idx++;
            }
        }
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

    public int eucliDist(Node a, Node b) {
        return a.coordinate;
    }

    public void kleinberg(int origine_x, int origine_y, int cible_x, int cible_y) {
        for (int x = 0; i < grid.length; i++) {
            for (int y = 0; j < grid.length; j++) {
                Node current = knodes.get(x+";"+y);
                if (x == 0 && y == 0) {
                    addEdge(current.id, knodes.get((x+1)+";"+y).id);
                    addEdge(current.id, knodes.get(x+";"+(y+1)).id);
                    continue;
                }
                if (x == 0 && y == grid.length - 1) {
                    addEdge(current.id, knodes.get((x+1)+";"+y).id);
                    addEdge(current.id, knodes.get(x+";"+(y-1)).id);
                    continue;
                }
                if (x == grid.length - 1 && y == 0) {
                    addEdge(current.id, knodes.get(x+";"+(y+1)).id);
                    addEdge(current.id, knodes.get((x-1)+";"+y).id);
                    continue;
                }
                if (x == grid.length - 1 && y == grid.length - 1) {
                    addEdge(current.id, knodes.get(x+";"+(y-1)).id);
                    addEdge(current.id, knodes.get((x-1)+";"+y).id);
                    continue;
                }
                if (x == 0 && y > 0 && y < grid.length) {
                    addEdge(current.id, knodes.get(x+";"+(y-1)).id);
                    addEdge(current.id, knodes.get(x+";"+(y+1)).id);
                    addEdge(current.id, knodes.get((x+1)+";"+y).id);
                    continue;
                }
                if (y == 0 && x > 0 && x < grid.length) {
                    addEdge(current.id, knodes.get((x-1)+";"+y).id);
                    addEdge(current.id, knodes.get(x+";"+(y+1)).id);
                    addEdge(current.id, knodes.get((x+1)+";"+y).id);
                    continue;
                }
                if (x == grid.length && y > 0 && y < grid.length) {
                    addEdge(current.id, knodes.get(x+";"+(y-1)).id);
                    addEdge(current.id, knodes.get(x+";"+(y+1)).id);
                    addEdge(current.id, knodes.get((x-1)+";"+y).id);
                    continue;
                }
                if (y == grid.length && x > 0 && x < grid.length) {
                    addEdge(current.id, knodes.get((x-1)+";"+y).id);
                    addEdge(current.id, knodes.get(x+";"+(y-1)).id);
                    addEdge(current.id, knodes.get((x+1)+";"+y).id);
                    continue;
                }
                addEdge(current.id, knodes.get(x+";"+(y+1)).id);
                addEdge(current.id, knodes.get(x+";"+(y-1)).id);
                addEdge(current.id, knodes.get((x-1)+";"+y).id);
                addEdge(current.id, knodes.get((x+1)+";"+y).id);
            }
        }
        
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