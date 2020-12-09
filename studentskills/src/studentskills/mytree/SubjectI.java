package studentskills.mytree;

import java.util.Observer;
import java.util.Set;

public interface SubjectI {
    void registerObserver(ObserverI observer);

    void removeObserver(ObserverI observer);

    void notifyObservers(Operation operation, int bNumber, String firstName, String lastName, double gpa, String major, Set<String> skills);
    void notifyObservers(Operation operation, String origValue, String newValue);
}
