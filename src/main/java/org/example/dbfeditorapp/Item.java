package org.example.dbfeditorapp;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;

public class Item {
    private final SimpleStringProperty name;
    private final SimpleBooleanProperty selected;

    public Item(String name) {
        this.name = new SimpleStringProperty(name);
        this.selected = new SimpleBooleanProperty(true);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }
}
