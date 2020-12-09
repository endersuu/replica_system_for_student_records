package studentskills.mytree;

import studentskills.util.MyLogger;

import java.util.LinkedList;
import java.util.Queue;

/**
 * AVL Tree implementation, refer to:
 * https://www.geeksforgeeks.org/avl-tree-set-1-insertion/
 * https://algorithms.tutorialhorizon.com/avl-tree-insertion/
 */
public final class AVLTree implements TreeI {
    private final int id;
    private NodeI root = null;

    public AVLTree() {
        this.id = UniqueID.getUniqueID();
    }

    @Override
    public NodeI getRoot() {
        return root;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getBalance(NodeI n) {
        if (n != null) {
            return (getHeight(n.getLeft()) - getHeight(n.getRight()));
        }
        return 0;
    }

    public int getHeight(NodeI n) {
        if (n != null) {
            return n.getHeight();
        }
        return 0;
    }

    private NodeI rightRotate(NodeI y) {
        NodeI x = y.getLeft();
        NodeI t2 = x.getRight();

        // Rotation
        x.setRight(y);
        y.setLeft(t2);

        // update their heights
        x.setHeight(Math.max(getHeight(x.getLeft()), getHeight(x.getRight())) + 1);
        y.setHeight(Math.max(getHeight(y.getLeft()), getHeight(y.getRight())) + 1);

        return x;
    }

    private NodeI leftRotate(NodeI x) {
        NodeI y = x.getRight();
        NodeI t2 = y.getLeft();

        // Rotation
        y.setLeft(x);
        x.setRight(t2);

        // update their heights
        x.setHeight(Math.max(getHeight(x.getLeft()), getHeight(x.getRight())) + 1);
        y.setHeight(Math.max(getHeight(y.getLeft()), getHeight(y.getRight())) + 1);

        return y;
    }

    @Override
    public void insert(NodeI node) {
        root = insertRecursive(root, node);
    }

    private NodeI insertRecursive(NodeI startNode, NodeI newNode) {
        if (startNode == null) {
            return newNode;
        }

        // insert as a BST
        if (startNode.getKey() > newNode.getKey()) {
            startNode.setLeft(insertRecursive(startNode.getLeft(), newNode));
        } else if (startNode.getKey() < newNode.getKey()) {
            startNode.setRight(insertRecursive(startNode.getRight(), newNode));
        } else {
            throw new IllegalArgumentException("This AVL tree does not support insert a node with same key "+newNode.getKey());
        }

        // update the node height
        startNode.setHeight(Math.max(getHeight(startNode.getLeft()), getHeight(startNode.getRight())) + 1);

        // rotation logic start
        int balDiff = getBalance(startNode);

        // Left-left case
        if (balDiff > 1 && newNode.getKey() < startNode.getLeft().getKey()) {
            return rightRotate(startNode);
        }

        // Right-right case
        if (balDiff < -1 && newNode.getKey() > startNode.getRight().getKey()) {
            return leftRotate(startNode);
        }

        // Left-right case
        if (balDiff > 1 && newNode.getKey() > startNode.getLeft().getKey()) {
            startNode.setLeft(leftRotate(startNode.getLeft()));
            return rightRotate(startNode);
        }

        // Right-left case
        if (balDiff < -1 && newNode.getKey() < startNode.getRight().getKey()) {
            startNode.setRight(rightRotate(startNode.getRight()));
            return leftRotate(startNode);
        }

        return startNode;
    }

    @Override
    public void inOrder(NodeI node) {
        if (node != null) {
            inOrder(node.getLeft());
            System.out.print(" " + node);
            inOrder(node.getRight());
        }
    }

    @Override
    public void preOrder(NodeI node) {
        if (node != null) {
            System.out.print(" " + node);
            preOrder(node.getLeft());
            preOrder(node.getRight());
        }
    }

    public Queue<String> printNodes() {
        Queue<String> orderedResults = new LinkedList<>();
        printNodeRecursive(root, orderedResults);
        return orderedResults;
    }

    public void printNodeRecursive(NodeI node, Queue<String> recordResult) {
        if (node != null) {
            printNodeRecursive(node.getLeft(), recordResult);
            StudentRecord studentRecord = (StudentRecord) node;
            recordResult.add(studentRecord.getbNumber() + ":" + studentRecord.getSkills());
            printNodeRecursive(node.getRight(), recordResult);
        }
    }

    @Override
    public NodeI search(int key) throws NullPointerException {
        return searchRecursive(root, key);
    }

    private NodeI searchRecursive(NodeI node, int key) throws NullPointerException {
        if (node.getKey() == key)
            return node;
        if (node.getKey() > key)
            return searchRecursive(node.getLeft(), key);
        else
            return searchRecursive(node.getRight(), key);
    }
}
