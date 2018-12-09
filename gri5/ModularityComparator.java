import java.util.Comparator;

public class ModularityComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return -Double.compare(Double.parseDouble(o1.split(";")[0]), Double.parseDouble(o2.split(";")[0]));
    }
}
