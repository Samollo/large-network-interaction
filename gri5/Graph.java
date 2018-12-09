import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Graph {
    private ArrayList<String> edgesList;
    private HashMap<Integer, Node> nodes;
    private HashMap<String, Integer> clustersEdgesMap;
    private HashMap<Integer, Cluster> clusters;
    private Double modularity = null;
    private int step = 1;

    public Graph(String path) {
        nodes = new HashMap<>();
        edgesList = new ArrayList<>();
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

        edgesList.add(idFirst + ";" + idSecond);
        clustersEdgesMap.put(idFirst + ";" + idSecond, 1);
    }

    private Node putNode(int idNode) {
        Node element = nodes.get(idNode);

        if (element == null) {
            element = new Node(idNode);
            element.setCluster(idNode);
            nodes.put(element.getId(), element);
            clusters.put(element.getId(), new Cluster(element));
        }
        return element;
    }

    private void graphFileParsing(String path) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                addEdge(Integer.parseInt(line.split("\\s")[0]), Integer.parseInt(line.split("\\s")[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found.");
        } catch (IOException e) {
            System.out.println("Error: read null line.");
        }
    }

    private void clustering() {
        Double calculatedModularity;
        int leftClus = -1;
        int rightClus = -1;
        Double bestM;

        while (clusters.size() > 0) {
            if (step == 1) {
                modularity = firstStepModularity();
            } else {
                bestM = (double) -10000;
                for (Cluster c1 : clusters.values()) {
                    for (Cluster c2 : clusters.values()) {
                        calculatedModularity = modularityCalcul(c1, c2);
                        if (calculatedModularity != null) {
                            if (bestM <= calculatedModularity) {
                                bestM = calculatedModularity;
                                leftClus = c1.getClusterId();
                                rightClus = c2.getClusterId();
                            }
                        }
                    }
                }
                modularity = bestM + modularity;
                updateClusterEdges(leftClus, rightClus);
            }

            System.out.println("étape=" + step + " nbClusters=" + clusters.size() + " Q(P)=" + modularity);
            step++;
        }
    }


    private void updateClusterEdges(int i, int j) {
        HashMap<String, Integer> hashMap = new HashMap<>();

        clustersEdgesMap.remove(i + ";" + j);
        clustersEdgesMap.remove(j + ";" + i);


        for (String edge : clustersEdgesMap.keySet()) {
            String[] edgeSplitted = edge.split(";");
            int nodeOne = Integer.parseInt(edgeSplitted[0]);
            int nodeTwo = Integer.parseInt(edgeSplitted[1]);

            if (nodeOne == j) {
                if (hashMap.containsKey(i + ";" + nodeTwo)) {
                    hashMap.replace(i + ";" + nodeTwo, hashMap.get(i + ";" + nodeTwo) + 1);
                } else {
                    hashMap.put(i + ";" + nodeTwo, 1);
                }
            } else if (nodeTwo == j) {
                if (hashMap.containsKey(nodeOne + ";" + i)) {
                    hashMap.replace(nodeOne + ";" + i, hashMap.get(nodeOne + ";" + i) + 1);
                } else {
                    hashMap.put(nodeOne + ";" + i, 1);
                }
            } else {
                if (hashMap.containsKey(edge)) {
                    hashMap.replace(edge, clustersEdgesMap.get(edge) + 1);
                } else {
                    hashMap.put(edge, clustersEdgesMap.get(edge));
                }
            }
        }
        clustersEdgesMap = hashMap;
        System.out.println(i + " " + j + " " + clusters.size());
        if (i != j) {
            clusters.get(i).mergeCluster(clusters.get(j));
            clusters.remove(j);
        }
    }

    public double firstStepModularity() {
        double firstStep = 0.0;

        for (Cluster c : clusters.values()) {
            firstStep -= (Math.pow(c.getTotalDegree(), 2) / (4 * Math.pow((double) edgesList.size(), 2)));
        }

        return firstStep;
    }

    public Double modularityCalcul(Cluster i, Cluster j) {

        if (i.getClusterId() == j.getClusterId()) {
            return null;
        }
        Integer mij = getClustersEdges(i.getClusterId(), j.getClusterId());

        double one = mij / ((double) edgesList.size());
        double two = Math.pow((clusters.get(i.getClusterId()).getTotalDegree() + clusters.get(j.getClusterId()).getTotalDegree()), 2) / (4 * Math.pow((double) edgesList.size(), 2));
        double three = Math.pow(clusters.get(i.getClusterId()).getTotalDegree(), 2) / (4 * Math.pow((double) edgesList.size(), 2));
        double four = Math.pow(clusters.get(j.getClusterId()).getTotalDegree(), 2) / (4 * Math.pow((double) edgesList.size(), 2));

        return one - two + three + four;
    }

    private Integer getClustersEdges(int i, int j) {
        return clustersEdgesMap.get(i + ";" + j) == null ? 0 : clustersEdgesMap.get(i + ";" + j);
    }


    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Node : ");

        for (Node n : nodes.values()) {
            res.append(n.getId()).append(" ");
        }
        res.append("\n").append("Edges :\n");

        for (String s : edgesList) {
            res.append(s).append("\n");
        }
        return res.toString();
    }
}