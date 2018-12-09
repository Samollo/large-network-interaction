import java.util.HashMap;
import java.util.HashSet;

public class Cluster {
    private HashSet<Integer> nodesList;
    private HashMap<Integer, Node> nodesMap;
    private double internEdges;
    private double totalDegree;
    private int clusterId;

    public Cluster(int n, HashMap<Integer, Node> nodes) {

        nodesList = new HashSet<>();
        totalDegree = -1;
        nodesList.add(n);
        clusterId = n;
        nodesMap = nodes;

    }

    public void addNode(int node) {
        Node n = nodesMap.get(node);
        nodesList.add(node);
        totalDegree += n.getDegree();
        for (int neighbour : n.getIdNeighbours()) {
            if (nodesList.contains(neighbour)) {
                internEdges++;
            }
        }

    }

    public void mergeCluster(Cluster cluster) {

        for (int n : cluster.getNodesList()) {
            addNode(n);
        }

    }

    public HashSet<Integer> getNodesList() {
        return nodesList;
    }

    public double getInternEdges() {
        return internEdges;
    }

    public double getTotalDegree() {
        if (totalDegree == -1) {
            totalDegree = 0;
            for (int n : nodesList) {
                totalDegree += nodesMap.get(n).getDegree();
            }
        }
        return totalDegree;
    }

    public int getClusterId() {
        return clusterId;
    }

    @Override
    public String toString() {
        return nodesList.toString();
    }
}
