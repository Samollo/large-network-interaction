import java.io.*;
import java.util.ArrayList;

public class TP4 {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: not enough argument.");
            return;
        }
        try {
            Graphe graphe;
            String output;
            int n, k, origine, cible, c, origine_x, origine_y, cible_x, cible_y;
            double p;
            System.out.println("Command to execute: " + args[0]);
            switch (args[0]) {
                case "WS":
                    output = args[1];
                    n = Integer.parseInt(args[2]);
                    k = Integer.parseInt(args[3]);
                    p = Double.parseDouble(args[4]);
                    origine = Integer.parseInt(args[5]);
                    cible = Integer.parseInt(args[6]);
                    graphe = new Graphe();
                    graphe.wattsStrogatz(n, k, p, origine, cible);
                    writeToTxt(graphe.edgesList, output);
                    writeToDot(graphe.edgesList, output);
                    break;
                case "K":
                    output = args[1];
                    c = Integer.parseInt(args[2]);
                    origine_x = Integer.parseInt(args[3]);
                    origine_y = Integer.parseInt(args[4]);
                    cible_x = Integer.parseInt(args[5]);
                    cible_y = Integer.parseInt(args[6]);
                    graphe = new Graphe();
                    graphe.kleinberg(c, origine_x, origine_y, cible_x, cible_y);
                    writeToTxt(graphe.edgesList, output);
                    writeToDot(graphe.edgesList, output);
                    break;
            }
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

    }

    public static void writeToTxt(ArrayList<String> aretes, String output) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(output + ".txt", "UTF-8");
            writer.println("graph exemple {");
            for (int i = 0; i < aretes.size(); i++) {
                String[] toWrite = aretes.get(i).split(";");
                writer.println(toWrite[0] + "\t" + toWrite[1]);
            }
            writer.println("}");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void writeToDot(ArrayList<String> aretes, String output) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(output + ".dot", "UTF-8");
            writer.println("graph exemple {");
            for (int i = 0; i < aretes.size(); i++) {
                String[] toWrite = aretes.get(i).split(";");
                writer.println(toWrite[0] + " -- " + toWrite[1] + ";");
            }
            writer.println("}");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}