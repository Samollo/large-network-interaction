import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Graph {
    private int edge;
    private HashMap<Integer, Node> nodes;
    private HashMap<String, Double> clustersEdgesMap;
    private HashMap<Integer, Cluster> clusters;
    private PriorityQueue<Modularity> modularities;
    private double bestModularity = -1.0;
    private int step = 1;

    public Graph(String path) {
        nodes = new HashMap<>();
        edge = 0;
        clusters = new HashMap<>();
        clustersEdgesMap = new HashMap<>();
        graphFileParsing(path);
        clustering();
    }

    private void addEdge(int idFirst, int idSecond) {
        if (idFirst == idSecond) {
            return;
        }

        Node n1 = putNode(idFirst);
        n1.addNeighbour(idSecond);

        Node n2 = putNode(idSecond);
        n2.addNeighbour(idFirst);

        edge++;
        clustersEdgesMap.put(idFirst + ";" + idSecond, 1.0);
        clustersEdgesMap.put(idSecond + ";" + idFirst, 1.0);

    }

    private Node putNode(int idNode) {
        Node element = nodes.get(idNode);

        if (element == null) {
            element = new Node(idNode);
            nodes.put(element.getId(), element);
            clusters.put(element.getId(), new Cluster(idNode, nodes));
        }
        return element;
    }

    private void graphFileParsing(String path) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.charAt(0) != '#')
                    addEdge(Integer.parseInt(line.split("\\s")[0]), Integer.parseInt(line.split("\\s")[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
        } catch (IOException e) {
            System.out.println("Error: read null line.");
        }
    }

    private void clustering() {
        modularities = new PriorityQueue<>(new ModularityComparator());
        int leftClus = -1;
        int rightClus = -1;
        double modularity = 1.0;

        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(10000);


        while (clusters.size() > 1) {
            if (step == 1) {
                modularity = firstStepModularity();
            } else {
                if (leftClus == -1) {
                    for (Cluster c1 : clusters.values()) {
                        calculIncrementForAll(c1);
                    }
                } else {
                    Cluster c1 = clusters.get(leftClus);
                    calculIncrementForAll(c1);
                }

                Modularity bestCouple = depop();
                leftClus = bestCouple.getLeft();
                rightClus = bestCouple.getRight();

                modularity = bestCouple.getValue() + modularity;
                updateClusterEdges(leftClus, rightClus);
            }


            if (roundAvoid(modularity, 5) == 0.0)
                modularity = 0.0;

            bestModularity = bestModularity < modularity ? modularity : bestModularity;


            System.out.println("étape=" + step + " nbClusters=" + clusters.size() + " Q(P)=" + modularity + " mémoire : " +
                    (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "octets");
            String clustersString = clusters.values().toString();
            clustersString = clustersString.replaceAll(",", "");
            clustersString = clustersString.substring(1, clustersString.length() - 1);
            System.out.println(clustersString);
            step++;
        }

        System.out.println("Meilleure modularité : " + bestModularity);
    }

    private Modularity depop() {
        boolean isOK;
        Modularity bestCouple;

        do {
            isOK = true;

            bestCouple = modularities.poll();
            int leftClus = bestCouple.getLeft();
            int rightClus = bestCouple.getRight();

            if (!clusters.containsKey(leftClus)) {
                isOK = false;
                continue;
            }
            if (!clusters.containsKey(rightClus)) {
                isOK = false;
                continue;
            }
            if (clusters.get(leftClus).getNodesList().size() != bestCouple.getLeftSize()) {
                isOK = false;
                continue;
            }
            if (clusters.get(rightClus).getNodesList().size() != bestCouple.getRightSize()) {
                isOK = false;
            }
        } while (!isOK);

        return bestCouple;
    }


    private void calculIncrementForAll(Cluster c1) {
        Modularity m;
        double mCalcul = -2;
        int cl = 0;
        int cr = 0;
        for (Cluster c2 : clusters.values()) {
            if (c1.getClusterId() == c2.getClusterId()) {
                continue;
            }
            double tmp = modularityCalcul(c1, c2);
            if (tmp > mCalcul) {
                mCalcul = tmp;
                cl = c1.getClusterId();
                cr = c2.getClusterId();
            }
        }
        m = new Modularity(mCalcul, cl, clusters.get(cl).getNodesList().size(), cr, clusters.get(cr).getNodesList().size());
        modularities.add(m);

    }

    public double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }

    private void updateClusterEdges(int i, int j) {
        HashMap<String, Double> hashMap = new HashMap<>();

        clustersEdgesMap.remove(i + ";" + j);
        clustersEdgesMap.remove(j + ";" + i);

        for (String edge : clustersEdgesMap.keySet()) {
            String[] edgeSplitted = edge.split(";");
            int nodeOne = Integer.parseInt(edgeSplitted[0]);
            int nodeTwo = Integer.parseInt(edgeSplitted[1]);

            if (nodeOne == j) {
                replaceOldClustersEdges(i, hashMap, nodeTwo);
            } else if (nodeTwo == j) {
                replaceOldClustersEdges(nodeOne, hashMap, i);
            } else {
                if (hashMap.containsKey(edge)) {
                    hashMap.replace(edge, clustersEdgesMap.get(edge) + 1.0);
                } else {
                    hashMap.put(edge, clustersEdgesMap.get(edge));
                }
            }
        }
        clustersEdgesMap = hashMap;
        if (i != j) {
            clusters.get(i).mergeCluster(clusters.get(j));
            clusters.remove(j);
        }
    }

    private void replaceOldClustersEdges(int i, HashMap<String, Double> hashMap, int nodeTwo) {
        if (hashMap.containsKey(i + ";" + nodeTwo)) {
            hashMap.replace(i + ";" + nodeTwo, hashMap.get(i + ";" + nodeTwo) + 1);
        } else {
            hashMap.put(i + ";" + nodeTwo, 1.0);
        }
    }

    public double firstStepModularity() {
        double firstStep = 0.0;

        for (Cluster c : clusters.values()) {
            firstStep -= (Math.pow(c.getTotalDegree(), 2) / (4 * Math.pow((double) edge, 2)));
        }

        return firstStep;
    }

    public Double modularityCalcul(Cluster i, Cluster j) {
        double mij = getClustersEdges(i.getClusterId(), j.getClusterId());

        double one = mij / ((double) edge);
        double two = Math.pow((clusters.get(i.getClusterId()).getTotalDegree() + clusters.get(j.getClusterId()).getTotalDegree()), 2) / (4 * Math.pow((double) edge, 2));
        double three = Math.pow(clusters.get(i.getClusterId()).getTotalDegree(), 2) / (4 * Math.pow((double) edge, 2));
        double four = Math.pow(clusters.get(j.getClusterId()).getTotalDegree(), 2) / (4 * Math.pow((double) edge, 2));

        return one - two + three + four;
    }

    private Double getClustersEdges(int i, int j) {
        return clustersEdgesMap.get(i + ";" + j) == null ? 0.0 : clustersEdgesMap.get(i + ";" + j);
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Node : ");

        for (Node n : nodes.values()) {
            res.append(n.getId()).append(" ");
        }
        res.append("\n").append("Edges :\n");

        return res.toString();
    }
}