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

    public Map<Integer, Node> nodes;
    private Map<String, Node> knodes;
    private Node[] grid;
    private int sizeSide;

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
        sizeSide = c;
        grid = new Node[c*c];
        int idx = 0;
        for (int x = 0; x < c; x++) {
            for(int y = 0; y < c; y++) {
                grid[idx] = new Node(idx, x, y);
                knodes.put(x+";"+y, grid[idx]);
                nodes.put(idx, grid[idx]);
                idx++;
            }
        }
    }

    public Node[] getGrid() {
        return grid;
    }

    public Map<String, Node> getKnodes() {
        return knodes;
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
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

    public void kleinberg(int origine_x, int origine_y, int cible_x, int cible_y) {
        kleinbergGraph();
        pathK(origine_x, origine_y, cible_x, cible_y);
    }

    private void kleinbergGraph(){
        for (int x = 0; x < sizeSide; x++) {
            for (int y = 0; y < sizeSide; y++) {
                Node current = knodes.get(x+";"+y);
                int b = lotery(current.id);

                //Additional link
                // we check if the lotery went fine
                // and if the edge can be added to the list
                if(b != -1 && addEdge(current.id, b)) {
                    addToEdgesList(current, nodes.get(b));
                }
                //Top spots
                if (x == 0 && y == 0) {
                    // If addEdge is a success, we add the edge to the list
                    // It's a success only if the edge does not already exist
                    // or if we're not trying to create a loop
                    if(addEdge(current.id, knodes.get((x+1)+";"+y).id)) addToEdgesList(current, knodes.get((x+1)+";"+y));
                    if(addEdge(current.id, knodes.get(x+";"+(y+1)).id)) addToEdgesList(current, knodes.get(x+";"+(y+1)));
                    continue;
                }
                if (x == 0 && y == sizeSide - 1) {
                    if(addEdge(current.id, knodes.get((x+1)+";"+y).id)) addToEdgesList(current, knodes.get((x+1)+";"+y));
                    if(addEdge(current.id, knodes.get(x+";"+(y-1)).id)) addToEdgesList(current, knodes.get(x+";"+(y-1)));
                    continue;
                }
                if (x == sizeSide - 1 && y == 0) {
                    if(addEdge(current.id, knodes.get(x+";"+(y+1)).id)) addToEdgesList(current, knodes.get(x+";"+(y+1)));
                    if(addEdge(current.id, knodes.get((x-1)+";"+y).id)) addToEdgesList(current, knodes.get((x-1)+";"+y));
                    continue;
                }
                if (x == sizeSide - 1 && y == sizeSide - 1) {
                    if(addEdge(current.id, knodes.get(x+";"+(y-1)).id)) addToEdgesList(current, knodes.get(x+";"+(y-1)));
                    if(addEdge(current.id, knodes.get((x-1)+";"+y).id)) addToEdgesList(current, knodes.get((x-1)+";"+y));
                    continue;
                }
                //Sides
                if (x == 0 && y > 0 && y < sizeSide - 1) {
                    if(addEdge(current.id, knodes.get(x+";"+(y-1)).id)) addToEdgesList(current, knodes.get(x+";"+(y-1)));
                    if(addEdge(current.id, knodes.get(x+";"+(y+1)).id)) addToEdgesList(current, knodes.get(x+";"+(y+1)));
                    if(addEdge(current.id, knodes.get((x+1)+";"+y).id)) addToEdgesList(current, knodes.get((x+1)+";"+y));
                    continue;
                }
                if (y == 0 && x > 0 && x < sizeSide - 1) {
                    if(addEdge(current.id, knodes.get((x-1)+";"+y).id)) addToEdgesList(current, knodes.get((x-1)+";"+y));
                    if(addEdge(current.id, knodes.get(x+";"+(y+1)).id)) addToEdgesList(current, knodes.get(x+";"+(y+1)));
                    if(addEdge(current.id, knodes.get((x+1)+";"+y).id)) addToEdgesList(current, knodes.get((x+1)+";"+y));
                    continue;
                }
                if (x == sizeSide - 1 && y > 0 && y < sizeSide - 1) {
                    if(addEdge(current.id, knodes.get(x+";"+(y-1)).id)) addToEdgesList(current, knodes.get(x+";"+(y-1)));
                    if(addEdge(current.id, knodes.get(x+";"+(y+1)).id)) addToEdgesList(current, knodes.get(x+";"+(y+1)));
                    if(addEdge(current.id, knodes.get((x-1)+";"+y).id)) addToEdgesList(current, knodes.get((x-1)+";"+y));
                    continue;
                }
                if (y == sizeSide - 1 && x > 0 && x < sizeSide - 1) {
                    if(addEdge(current.id, knodes.get((x-1)+";"+y).id)) addToEdgesList(current, knodes.get((x-1)+";"+y));
                    if(addEdge(current.id, knodes.get(x+";"+(y-1)).id)) addToEdgesList(current, knodes.get(x+";"+(y-1)));
                    if(addEdge(current.id, knodes.get((x+1)+";"+y).id)) addToEdgesList(current, knodes.get((x+1)+";"+y));
                    continue;
                }
                //Inside nodes
                if(addEdge(current.id, knodes.get(x+";"+(y+1)).id)) addToEdgesList(current, knodes.get(x+";"+(y+1)));
                if(addEdge(current.id, knodes.get(x+";"+(y-1)).id)) addToEdgesList(current, knodes.get(x+";"+(y-1)));
                if(addEdge(current.id, knodes.get((x-1)+";"+y).id)) addToEdgesList(current, knodes.get((x-1)+";"+y));
                if(addEdge(current.id, knodes.get((x+1)+";"+y).id)) addToEdgesList(current, knodes.get((x+1)+";"+y));
            }
        }
    }

    private double eucliDist(Node a, Node b) {
        return Math.sqrt((Math.pow(a.x-b.x, 2)) + (Math.pow(a.y-b.y, 2)));
    }

    private double sumOfKleinberg(int idA) {
        double sum = 0;
        for (int i = 0; i < grid.length; i++) {
            if (i == idA) continue;
            sum += 1/Math.pow(eucliDist(nodes.get(idA), nodes.get(i)), 2);
        }
        return sum;
    }

    private double chanceToLink(double sum, int idA, int idB) {
        if (idA == idB) return 0;
        return 1/(sum*Math.pow(eucliDist(nodes.get(idA), nodes.get(idB)), 2));
    }

    private double[] partialSum(int idA) {
        double s = sumOfKleinberg(idA);
        double[] partials = new double[grid.length];
        partials[0] = chanceToLink(s, idA, 0);
        for (int i = 1; i < partials.length; i++) {
            partials[i] = partials[i-1] + chanceToLink(s, idA, i);
        }
        return partials;
    }

    private int lotery(int idA) {
        double[] partials = partialSum(idA);
        double p = Math.random();
        for(int i = 1; i < grid.length; i++) {
            if (i == idA) continue;
            if (partials[i-1] < p && p <= partials[i]) return i;
        }
        return -1;

    }

    private boolean addToEdgesList(Node a, Node b) {
        return edgesList.add(a.id+";"+b.id);
    }

    public double degreMoyen() {
        double sum = 0;
        for (int i = 0; i < grid.length; i++) {
            sum += nodes.get(i).getDegree();
        }
        return sum/grid.length;
    }

    private void pathK(int origine_x, int origine_y, int cible_x, int cible_y) {
        Node current = knodes.get(origine_x+";"+origine_y);
        Node target = knodes.get(cible_x+";"+cible_y);
        int dist = 0;

        while (current.id != target.id) {
            System.out.print(current.id + " " + "("+current.x+","+current.y+") ; ");
            double shortestDist = grid.length;
            int nearestNode = -1;
            List<Integer> neighbour = current.getIdNeighbours();
            for (int i = 0; i < neighbour.size(); i++) {
                double d = eucliDist(target, nodes.get(neighbour.get(i)));
                if (d < shortestDist) {
                    shortestDist = d;
                    nearestNode = neighbour.get(i);
                }
            }
            if (eucliDist(current, target) < shortestDist) {
                System.out.println("Nous sommes dans une impasse. dist(current, target) < dist(voisins, target)");
                return;
            }
            current = nodes.get(nearestNode);
            dist += 1;
        }

        System.out.print(current.id + " ");
        System.out.println("longueur du chemin: " + dist);
    }
    /******************** Watts-Strogatz ************************/

    private void fork(int n, int k, double p) {
        for (int i = 0; i < n; i++) {
            // tire au hasard un Node. Il ne doit pas etre deja voisin avec nodes[i]
            int r;
            do {
                r = (int) (Math.random() * (n - 1));
            } while (nodes.get(i).getIdNeighbours().contains(r));

            // tire au hasard pour voir si on effectue le branchement entre i et r ou non
            if (Math.random() < p) {
                nodes.get(i).getIdNeighbours().remove(k);
                addEdge(i, r); 
                edgesList.add(i+";"+r);
            }
            edgesList.add(i+";"+nodes.get(i).getIdNeighbours().get(k));
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

    private int distWS(int x, int y, int n) {
        int hi = Math.max(x, y);
        int lo = Math.min(x, y);
        return Math.min(hi - lo, Math.abs(hi - n - lo));
    }

    private void pathWS(int origine, int cible, int n) {
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
            current = nearestNode;
            dist += 1;
        }
        System.out.print(current + " ");
        System.out.println("longueur du chemin: " + dist);
    }
}