import java.util.Comparator;

public class ModularityComparator implements Comparator<Modularity> {
    @Override
    public int compare(Modularity o1, Modularity o2) {
        return -Double.compare(o1.getValue(), o2.getValue());
    }
}
