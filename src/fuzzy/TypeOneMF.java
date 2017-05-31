package fuzzy;

import type1.sets.T1MF_Triangular;

/**
 * Created by michaelborisov on 26.03.17.
 */
public class TypeOneMF implements AbstractMF<TypeOneMF>{

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

    public TypeOneMF getReciprocal(){
        return new TypeOneMF(1.0 / upperBound, 1.0 / middle, 1.0 / lowerBound);
    }

    public TypeOneMF(double lowerBound, double middle, double upperBound){
        this.lowerBound = lowerBound;
        this.middle = middle;
        this.upperBound = upperBound;
    }

    public double getDefuzzifiedValue(){
        T1MF_Triangular v = new T1MF_Triangular("Anonymous", lowerBound, middle, upperBound);
        return v.getDefuzzifiedCentroid(100);
    }

    public static double calculateHeightOfIntersection(TypeOneMF first, TypeOneMF second){
        if(first.getMiddle() > second.getMiddle()){
            return 1.0;
        }
        double firstValue = second.getLowerBound() - first.getUpperBound();
        double secondValue = (first.getMiddle() - first.getUpperBound()) -
                (second.getMiddle() - second.getLowerBound());

        double res = firstValue/secondValue;
        if(res < 0){
            return 0;
        }
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TypeOneMF)){
            return false;
        }
        return (this.lowerBound == ((TypeOneMF) obj).lowerBound &&
                this.middle == ((TypeOneMF) obj).middle &&
                this.upperBound == ((TypeOneMF) obj).upperBound);
    }
}
