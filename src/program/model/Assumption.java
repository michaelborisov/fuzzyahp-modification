package program.model;

import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;

import java.io.Serializable;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class Assumption implements Serializable {

    public Assumption(TypeOneMF expectation, TypeOneMF confidence) {
        this.confidence = confidence;
        this.expectation = expectation;
    }

    public IntervalTypeTwoMF convertT1MFtoIT2MF(TypeOneMF midConf){
        if(expectation == null){
            expectation = new TypeOneMF(1, 1, 1);
            confidence = new TypeOneMF(1, 1, 1);
            this.assumption = new IntervalTypeTwoMF(expectation, confidence);
            return this.assumption;
        }
        double multiplicator = midConf.getDefuzzifiedValue() / confidence.getDefuzzifiedValue();

        double loweBound = expectation.getLowerBound();
        double upperBound = expectation.getUpperBound();
        double shift = 0.03 * multiplicator / 2.0;
        TypeOneMF lower = new TypeOneMF(loweBound + shift, expectation.getMiddle(), upperBound - shift);
        TypeOneMF upper = new TypeOneMF(loweBound - shift, expectation.getMiddle(), upperBound + shift);
        this.assumption = new IntervalTypeTwoMF(lower, upper);
        return this.assumption;
    }

    public TypeOneMF getExpectation() {
        return expectation;
    }

    public TypeOneMF getConfidence() {
        return confidence;
    }

    public void setExpectation(TypeOneMF expectation) {
        this.expectation = expectation;
    }

    public void setConfidence(TypeOneMF confidence) {
        this.confidence = confidence;
    }

    TypeOneMF expectation;
    TypeOneMF confidence;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    String label;
    IntervalTypeTwoMF assumption;

    AhpProject mProject;
}
