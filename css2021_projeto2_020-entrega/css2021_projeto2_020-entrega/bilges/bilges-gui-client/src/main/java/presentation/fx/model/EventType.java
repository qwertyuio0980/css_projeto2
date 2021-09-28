package presentation.fx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EventType {
    private final StringProperty name = new SimpleStringProperty();

    public EventType(String name) {
        setName(name);
    }
    
    public final StringProperty nameProperty() {
        return this.name;
    }

    public final String getName() {
        return this.nameProperty().get();
    }

    public final void setName(final String name) {
        this.nameProperty().set(name);
    }
    
    @Override
    public String toString(){
    	return getName();
    }
}
