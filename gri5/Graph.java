import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph {
    private ArrayList<String> edgesList;
    private HashMap<Integer, Node> nodes;
    private HashMap<String, Integer> clustersEdgesMap;
    private HashMap<Integer, Cluster> clusters;
    private int clusterNumber = 1;
    private Double modularity = null;

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
    }

    private Node putNode(int idNode) {
        Node element = nodes.get(idNode);

        if (element == null) {
            element = new Node(idNode);
            element.setCluster(idNode);
            nodes.put(element.getId(), element);
            clusters.put(element.getId(), new Cluster(element));
            clusterNumber++;
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
//        mergeCluster();
        mijCalcul();
        System.out.println(clustersEdgesMap.toString());
        System.out.println(modularityCalcul());
        System.out.println(modularityCalcul());

    }

    /*
        private void mergeCluster() {
            HashSet<Integer> alreadyDone = new HashSet<>();
            HashMap<Float, Pair<Integer, Integer>> modularityMap;
            PriorityQueue<Float> modularityQueue;
            float bestModularity = -1;
            int step = 1;

            while (step < 10) {

                modularityMap = new HashMap<>();
                modularityQueue = new PriorityQueue<>();

                if (step == 1) {
                    modularityQueue.add(modularity(null));
                } else {
                    for (int i = 0; i < clusters.size(); i++) {
                        if (alreadyDone.contains(i)) {
                            continue;
                        }
                        for (int j = i + 1; j < clusters.size(); j++) {
                            if (alreadyDone.contains(i)) {
                                continue;
                            }
                            Pair<Integer, Integer> pair = new Pair<>(i, j);
                            modularityQueue.add(modularity(pair));
                            modularityMap.putIfAbsent(modularityQueue.peek(), pair);
                        }
                    }

                    Pair<Integer, Integer> bestPair = modularityMap.get(modularityQueue.peek());
                    System.out.println(bestPair.toString());
                    clusters.get(bestPair.fst).mergeCluster(clusters.get(bestPair.snd));
                    alreadyDone.add(bestPair.snd);
                }

                bestModularity = modularityQueue.peek() > bestModularity ? modularityQueue.peek() : bestModularity;

                System.out.println("étape=" + step + " nbClusters=" + clusterNumber +
                        " Q(P)=" + modularityQueue.peek() + " mémoire = " +
                        (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + "octets.");

                step++;

                if (modularityQueue.poll() == 0) {
                    break;
                }
            }

            System.out.println("Best modularity : " + bestModularity);
        }
    */
/*
    private float modularity(Pair<Integer, Integer> merge) {
        float result = 0;

        int compt = 0;
        for (Cluster cluster : clusters) {
            float eii;
            float aii;

            if (merge != null && compt == merge.fst) {
                Cluster copy = (Cluster) cluster.clone();
                copy.mergeCluster(clusters.get(merge.snd));

                eii = copy.getInternEdges() / edgesList.size();
                aii = (copy.getTotalDegree() * copy.getTotalDegree()) / (4 * (edgesList.size() * edgesList.size()));
            } else if (merge != null && compt == merge.snd) {
                compt++;
                continue;
            } else {
                eii = cluster.getInternEdges() / edgesList.size();
                aii = (cluster.getTotalDegree() * cluster.getTotalDegree()) / (4 * (edgesList.size() * edgesList.size()));
            }

            result += (eii - aii);
            compt++;
        }

        return result;
    }
*/
    private void mijCalcul() {
        clustersEdgesMap = new HashMap<>();
        for (String edge : edgesList) {
            String[] edgeSplitted = edge.split(";");
            int nodeOne = Integer.parseInt(edgeSplitted[0]);
            int nodeTwo = Integer.parseInt(edgeSplitted[1]);

            if (nodes.get(nodeOne).getCluster() != nodes.get(nodeTwo).getCluster()) {
                String clusterEdge = nodes.get(nodeOne).getCluster() + ";" + nodes.get(nodeTwo).getCluster();
                if (clustersEdgesMap.containsKey(clusterEdge)) {
                    clustersEdgesMap.replace(clusterEdge, clustersEdgesMap.get(clusterEdge) + 1);
                } else {
                    clustersEdgesMap.put(clusterEdge, 1);
                }

            }
        }
    }

    private int mijCalcul(int i, int j) {
        int mij = 0;
        for (Map.Entry<String, Integer> edge : clustersEdgesMap.entrySet()) {
            String[] edgeSplitted = edge.getKey().split(";");
            int nodeOne = Integer.parseInt(edgeSplitted[0]);
            int nodeTwo = Integer.parseInt(edgeSplitted[1]);

            if (nodeOne == i || nodeTwo == i || nodeOne == j || nodeTwo == j) {
                mij += edge.getValue();
            }
        }
        return mij;
    }

    private Double modularityCalcul() {
        if (modularity == null) {
            modularity = 0.0;

            for (Cluster c : clusters.values()) {
                modularity -= (Math.pow(c.getTotalDegree(), 2) / (4 * Math.pow(edgesList.size(), 2)));
            }

            return modularity;
        } else {
            double modularityDiff;

            for (Map.Entry<Integer, Cluster> map : clusters.entrySet()) {
                for (int i = map.getKey(); i < clusters.size(); i++) {
                    int mij = mijCalcul(map.getKey(), i);
                    System.out.println(map.getKey() + " " + i + " " + mij);

                    double one = ((double) mij) / ((double) edgesList.size());
                    double two = Math.pow((map.getValue().getTotalDegree() + clusters.get(i).getTotalDegree()), 2) / ((double)4 * Math.pow(edgesList.size(), 2));
                    double three = Math.pow(map.getValue().getTotalDegree(), 2) / ((double)4 * Math.pow(edgesList.size(), 2));
                    double four = Math.pow(clusters.get(i).getTotalDegree(), 2) / ((double)4 * Math.pow(edgesList.size(), 2));


                    modularityDiff = one - two + three + four;

                    System.out.println(modularityDiff + modularity);

                }
            }
        }
        return null;
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