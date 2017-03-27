package fuzzy;

/**
 * Created by michaelborisov on 26.03.17.
 */
public class TypeOneMF {

    public double getLowerBound() {
        return lowerBound;
    }

    public double getMiddle() {
        return middle;
    }

    public double getUpperBound() {
        return upperBound;
    }

    private double lowerBound;
    private double middle;
    private double upperBound;

    public TypeOneMF(double lowerBound, double middle, double upperBound){
        this.lowerBound = lowerBound;
        this.middle = middle;
        this.upperBound = upperBound;
    }
}
