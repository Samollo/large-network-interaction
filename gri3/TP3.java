import java.io.*;

public class TP3 {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Error: not enough argument.");
            return;
        }
        try {
            Graphe graphe;
            String output;
            int n, m, k, n1;
            double p, y;
            System.out.println("Command to execute: " + args[0]);
            switch (args[0]) {
                case "gnp":
                    output = args[1];
                    n = Integer.parseInt(args[2]);
                    p = Double.parseDouble(args[3]);
                    graphe = new Graphe();
                    graphe.generateGNP(n, p);
                    writeToDot(graphe.aretes, output);
                    writeToTxt(graphe.aretes, output);
                    System.out.println("Nombres d'aretes obtenues: " + graphe.aretes.length);
                    break;

                case "gnm":
                    output = args[1];
                    n = Integer.parseInt(args[2]);
                    m = Integer.parseInt(args[3]);
                    graphe = new Graphe();
                    graphe.generateGNM(n, m);
                    writeToDot(graphe.aretes, output);
                    writeToTxt(graphe.aretes, output);
                    System.out.println("Nombres d'aretes obtenues: " + graphe.aretes.length);
                    break;
                case "ws":
                    output = args[1];
                    n = Integer.parseInt(args[2]);
                    k = Integer.parseInt(args[3]);
                    p = Double.parseDouble(args[4]);
                    graphe = new Graphe();
                    graphe.wattsStrogatz(n, k, p);
                    writeToTxt(graphe.aretes, output);
                    writeToDot(graphe.aretes, output);
                    break;
                case "Power":
                    output = args[1];
                    n = Integer.parseInt(args[2]);
                    y = Double.parseDouble(args[3]);
                    n1 = Integer.parseInt(args[4]);
                    graphe = new Graphe();
                    graphe.powerLaw(n, y, n1);
                    writeToTxt(graphe.aretes, output);
                    writeToDot(graphe.aretes, output);
                    break;
            }
        } catch (NullPointerException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

    }

    public static void writeToTxt(String[] aretes, String output) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(output + ".txt", "UTF-8");
            writer.println("graph exemple {");
            for (int i = 0; i < aretes.length; i++) {
                String[] toWrite = aretes[i].split(";");
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

    public static void writeToDot(String[] aretes, String output) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(output + ".dot", "UTF-8");
            writer.println("graph exemple {");
            for (int i = 0; i < aretes.length; i++) {
                String[] toWrite = aretes[i].split(";");
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