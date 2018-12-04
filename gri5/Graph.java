import com.sun.tools.javac.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Graph {
    private ArrayList<String> edgesList;
    private HashMap<Integer, Node> nodes;
    private int[][] clusters;

    public Graph() {
        nodes = new HashMap<>();
        edgesList = new ArrayList<>();
    }

    public Graph(String path) {
        nodes = new HashMap<>();
        edgesList = new ArrayList<>();
        graphFileParsing(path);
        clustering();
    }

    private boolean addEdge(int idFirst, int idSecond) {
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

        edgesList.add(idFirst + " " + idSecond);

        return s1.addNeighbour(idSecond) && s2.addNeighbour(idFirst);
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
        clusters = new int[nodes.size()][1];

        int i = 0;
        for (Node n : nodes.values()) {
            clusters[i] = new int[]{n.getId()};
            i++;
        }

        modularity();
    }

    private void modularity() {
        float result = 0;


        for (int[] cluster : clusters) {

            HashSet<Integer> alreadyDone = new HashSet<>();

            float eii = 0;
            float aii = 0;

            for (int i = 0; i < cluster.length; i++) {

                for (int j = i; j < cluster.length; j++) {

                    for (int neighbour : nodes.get(cluster[i]).getIdNeighbours()) {
                        if (!alreadyDone.contains(neighbour)) {
                            alreadyDone.add(neighbour);
                            eii++;
                        }
                    }

                }

                aii += nodes.get(cluster[i]).getDegree();

            }

            eii = eii / edgesList.size();
            aii = aii / 2 * edgesList.size();
            result += (eii - aii);
        }
        System.out.println(result);

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