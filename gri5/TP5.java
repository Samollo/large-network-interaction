public class TP5 {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Error: not enough argument.");
            return;
        }

        Graph graph = new Graph(args[0]);
//        System.out.println(graph.toString());
    }

}
