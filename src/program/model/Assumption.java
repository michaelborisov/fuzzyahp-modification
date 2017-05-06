package program.model;

import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;

import java.io.Serializable;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class Assumption implements Serializable {

    public Assumption(TypeOneMF confidence, TypeOneMF expectation) {
        this.confidence = confidence;
        this.expectation = expectation;
    }

    private void convertT1MFtoIT2MF(){
        this.assumption = new IntervalTypeTwoMF(confidence, expectation);
    }
    TypeOneMF expectation;
    TypeOneMF confidence;
    IntervalTypeTwoMF assumption;
}
