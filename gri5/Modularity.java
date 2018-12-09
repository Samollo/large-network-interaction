public class Modularity {
    private double value;
    private int left;
    private int right;
    private int leftSize;
    private int rightSize;

    public Modularity(double mod, int left, int leftSize, int right, int rightSize) {
        this.value = mod;
        this.left = left;
        this.right = right;
        this.leftSize = leftSize;
        this.rightSize = rightSize;
    }

    public double getValue() {
        return value;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getLeftSize() {
        return leftSize;
    }

    public int getRightSize() {
        return rightSize;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}

