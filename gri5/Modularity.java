import com.sun.tools.javac.util.Pair;

public class Modularity implements Comparable<Modularity> {
    private double value;
    private int left;
    private int right;

    public Modularity(double mod, int left, int right) {
        this.value = mod;
        this.left = left;
        this.right = right;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public int compareTo(Modularity o) {
        if (value > o.getValue())
            return -1;
        if (value < o.getValue())
            return 1;
        else
            return 0;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}

