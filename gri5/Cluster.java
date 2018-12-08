import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Cluster implements Cloneable {
    private HashMap<Integer, Node> nodesList;
    private float internEdges;
    private float totalDegree;
    private int clusterId;

    public Cluster() {

        nodesList = new HashMap<>();
        internEdges = 0;
        totalDegree = -1;

    }

    public Cluster(HashMap<Integer, Node> nl, float ie, float td) {

        nodesList = (HashMap<Integer, Node>) nl.clone();
        internEdges = ie;
        totalDegree = td;

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
        int i = 0;

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

    @Override
    protected Object clone() {
        return new Cluster(this.nodesList, this.internEdges, this.totalDegree);
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
