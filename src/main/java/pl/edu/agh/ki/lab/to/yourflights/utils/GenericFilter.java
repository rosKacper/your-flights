package pl.edu.agh.ki.lab.to.yourflights.utils;

import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import pl.edu.agh.ki.lab.to.yourflights.model.Flight;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Generyczna klasa służaca do filtrowania rekordów w tabeli
 * @param <Type> - typ obsługiwanego modelu
 */
public class GenericFilter<Type extends RecursiveTreeObject<Type>> {

    /**
     * Tabela zawierająca rekordy modelu
     */
    private final JFXTreeTableView<Type> filteredTable;
    /**
     * Lista predykatów dla danego modelu
     */
    private final List<Predicate<Type>> predicateList = new LinkedList<>();

    public GenericFilter(JFXTreeTableView<Type> filteredTable ) {
        this.filteredTable = filteredTable;
    }

    public void addPredicate(Predicate<Type> predicate) {
        predicateList.add(predicate);
    }

    /**
     * Funkcja która do każdego pola dodaje obserwator, odpowiedzialny za sprawdzenie predykatów
     */
    public <K> void setListener(Property<K> property) {
        property.addListener(new ChangeListener<K>() {
            @Override
            public void changed(ObservableValue<? extends K> observable, K oldValue, K newValue) {
                // sprawdzenie predykatów
                filteredTable.setPredicate(typeTreeItem -> predicateList.stream()
                        .allMatch(predicate -> predicate.test(typeTreeItem.getValue())));
            }
        });
    }

}
