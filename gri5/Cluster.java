import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Cluster {
    private HashMap<Integer, Node> nodesList;
    private float internEdges;
    private float totalDegree;
    private int clusterId;

    public Cluster() {

        nodesList = new HashMap<>();
        internEdges = 0;
        totalDegree = -1;

    }

    public Cluster(Node n) {

        nodesList = new HashMap<>();
        internEdges = 0;
        totalDegree = -1;
        nodesList.put(n.getId(), n);
        clusterId = n.getId();

    }

    public void addNode(Node node) {

        nodesList.put(node.getId(), node);
        totalDegree += node.getDegree();

    }

    public void mergeCluster(Cluster cluster) {

        for (Node n : cluster.getNodesList().values()) {
            addNode(n);
        }

    }

    public HashMap<Integer, Node> getNodesList() {
        return nodesList;
    }

    public void setNodesList(HashMap<Integer, Node> nodesList) {
        this.nodesList = nodesList;
    }


    public float getInternEdges() {
        return internEdges;
    }

    public void setInternEdges(float internEdges) {
        this.internEdges = internEdges;
    }

    public float getTotalDegree() {
        if (totalDegree == -1) {
            totalDegree = 0;
            for (Node n : nodesList.values()) {
                totalDegree += n.getDegree();
            }
        }
        return totalDegree;
    }

    public void setTotalDegree(float totalDegree) {
        this.totalDegree = totalDegree;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
}
