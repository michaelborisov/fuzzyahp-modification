package fuzzy;

/**
 * Created by michaelborisov on 29.03.17.
 */
public class Alternative {

    String name;

    public Double[] getInterval() {
        return interval;
    }

    public String getName() {
        return name;
    }

    Double [] interval;

    public Alternative(String name, Double[] interval){
        this.name = name;
        this.interval = interval;
    }

}
