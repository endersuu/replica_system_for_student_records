package studentskills.mytree;

public interface NodeI {
    int getKey();

    void setKey(int key);

    NodeI getLeft();

    void setLeft(NodeI left);

    NodeI getRight();

    void setRight(NodeI right);

    int getHeight();

    void setHeight(int height);
}
