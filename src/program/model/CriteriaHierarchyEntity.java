package program.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class CriteriaHierarchyEntity extends BaseHierarchyEntity {

    public CriteriaHierarchyEntity(String name){
        super(name);
        this.group = new SimpleStringProperty(BaseHierarchyEntity.CRITERIA);
    }
}
