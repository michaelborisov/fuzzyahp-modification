package fuzzy;

import intervalType2.sets.IntervalT2MF_Triangular;

/**
 * Created by michaelborisov on 26.03.17.
 */
public class IntervalTypeTwoMF implements AbstractMF<IntervalTypeTwoMF>{

    public IntervalTypeTwoMF(TypeOneMF lowerMF, TypeOneMF upperMF){
        this.lowerMF = lowerMF;
        this.upperMF = upperMF;
    }

    public TypeOneMF getLowerMF() {
        return lowerMF;
    }

    public TypeOneMF getUpperMF() {
        return upperMF;
    }

    @Override
    public IntervalTypeTwoMF getReciprocal(){
        return new IntervalTypeTwoMF(lowerMF.getReciprocal(), upperMF.getReciprocal());
    }

    @Override
    public String toString() {
        if (lowerMF.getMiddle() == upperMF.getMiddle()) {
            return String.format(
                    "({%s}, {%s}); {%s}; ({%s}, {%s})",
                    upperMF.getLowerBound(),
                    lowerMF.getLowerBound(),
                    lowerMF.getMiddle(),
                    lowerMF.getUpperBound(),
                    upperMF.getUpperBound());
        }else{
            return "Incorrect values, check your input";
        }
    }

    private TypeOneMF lowerMF;
    private TypeOneMF upperMF;


}
