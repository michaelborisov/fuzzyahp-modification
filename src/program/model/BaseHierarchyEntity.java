package program.model;

import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;

/**
 * Created by michaelborisov on 06.05.17.
 */
public abstract class BaseHierarchyEntity implements Serializable {

    public static final String ALTERNATIVE = "Альтернативы";
    public static final String CRITERIA = "Критерии";

    private SimpleStringProperty name;
    protected SimpleStringProperty group;

    public BaseHierarchyEntity(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String firstName) {
        name.set(firstName);
    }

    public String getGroup() {
        return group.get();
    }

    @Override
    public String toString() {
        return name.get();
    }
}
