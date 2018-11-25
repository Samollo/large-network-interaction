import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class TP4 {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: not enough argument.");
            return;
        }
        try {
            Graph graphe;
            String output;
            int n, k, origine, cible, c, origine_x, origine_y, cible_x, cible_y;
            double p;
            System.out.println("Command to execute: " + args[0]);
            args[0] = args[0].toLowerCase();
            switch (args[0]) {
                case "ws":
                    output = args[1];
                    n = Integer.parseInt(args[2]);
                    k = Integer.parseInt(args[3]);
                    p = Double.parseDouble(args[4]);
                    origine = Integer.parseInt(args[5]);
                    cible = Integer.parseInt(args[6]);
                    graphe = new Graph();
                    graphe.wattsStrogatz(n, k, p, origine, cible);
                    writeToTxt(graphe.getEdgesList(), output);
                    writeToDot(graphe.getEdgesList(), output);
                    break;
                case "k":
                    output = args[1];
                    c = Integer.parseInt(args[2]);
                    origine_x = Integer.parseInt(args[3]);
                    origine_y = Integer.parseInt(args[4]);
                    cible_x = Integer.parseInt(args[5]);
                    cible_y = Integer.parseInt(args[6]);
                    graphe = new Graph(c);
                    graphe.kleinberg(origine_x, origine_y, cible_x, cible_y);
                    System.out.println("degre moyen: "+graphe.degreMoyen());
                    writeToTxt(graphe.getEdgesList(), output);
                    writeKtoDot(graphe.getGrid(), graphe.getEdgesList(), output);
                    break;
            }
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    public static void writeToTxt(List<String> aretes, String output) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(output + ".txt", "UTF-8");
            writer.println("graph "+output+" {");
            for (int i = 0; i < aretes.size(); i++) {
                String[] toWrite = aretes.get(i).split(";");
                writer.println(toWrite[0] + "\t" + toWrite[1]);
            }
            writer.println("}");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void writeToDot(List<String> aretes, String output) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(output + ".dot", "UTF-8");
            writer.println("graph "+output+" {");
            for (int i = 0; i < aretes.size(); i++) {
                String[] toWrite = aretes.get(i).split(";");
                writer.println(toWrite[0] + " -- " + toWrite[1] + ";");
            }
            writer.println("}");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void writeKtoDot(Node[] grid, List<String> aretes, String output) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(output + ".dot", "UTF-8");
            writer.println("graph "+output+" {");
            for (int i = 0; i < grid.length; i++) {
                writer.println(grid[i].labelDotK());
            }
            for (int i = 0; i < aretes.size(); i++) {
                String[] toWrite = aretes.get(i).split(";");
                writer.println(toWrite[0] + " -- " + toWrite[1] + ";");
            }
            writer.println("}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}