package studentskills.mytree;

import java.util.Set;

public interface ObserverI {
    void update(Operation operation, int bNumber, String firstName, String lastName, double gpa, String major, Set<String> skills);

    void update(Operation operation, String origValue, String newValue);
}
