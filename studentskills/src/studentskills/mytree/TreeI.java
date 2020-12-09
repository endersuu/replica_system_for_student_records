package studentskills.mytree;

import java.util.Queue;

public interface TreeI {
    NodeI getRoot();
    int getId();
    void insert(NodeI node);
    void inOrder(NodeI node);
    void preOrder(NodeI node);
    Queue<String> printNodes();
    NodeI search(int key) throws NullPointerException;
}
