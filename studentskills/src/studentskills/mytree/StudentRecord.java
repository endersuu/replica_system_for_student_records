package studentskills.mytree;

import studentskills.util.MyLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class StudentRecord implements NodeI, SubjectI, ObserverI, Cloneable {
    // node of BST
    private NodeI left;
    private NodeI right;
    private int height;
    // data field
    private int bNumber;
    private String firstName;
    private String lastName;
    private double gpa;
    private String major;
    private Set<String> skills;
    // subject
    private ArrayList<ObserverI> observers;


    public StudentRecord(int bNumber, String firstName, String lastName, double gpa, String major, Set<String> skills) {
        // node of BST
        this.height = 1;
        // use b-num as key
        this.bNumber = bNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
        this.major = major;
        // deep copy set skills
        this.skills = new HashSet<>();
        this.skills.addAll(skills);
        // subject
        observers = new ArrayList<>();
    }

    // NodeI
    @Override
    public int getKey() {
        return getbNumber();
    }

    @Override
    public void setKey(int key) {
        setbNumber(key);
    }

    @Override
    public NodeI getLeft() {
        return left;
    }

    @Override
    public void setLeft(NodeI left) {
        this.left = left;
    }

    @Override
    public NodeI getRight() {
        return right;
    }

    @Override
    public void setRight(NodeI right) {
        this.right = right;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    // SubjectI
    @Override
    public void registerObserver(ObserverI observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ObserverI observer) {
        if (observers.contains(observer))
            observers.remove(observer);
        else
            throw new RuntimeException("subject tries to remove observer(" + observer + "), but the observer does not exist in ArrayList observers");
    }

    /**
     * functions notify all the observers
     * 2 functions are overload for INSERT and MODIFY
     */
    // INSERT
    @Override
    public void notifyObservers(Operation operation, int bNumber, String firstName, String lastName, double gpa, String major, Set<String> skills) {
        for (ObserverI observer : observers
        ) {
            MyLogger.writeMessage("Notify observer: " + observer, MyLogger.DebugTags.INSERT_NODE);
            observer.update(operation, bNumber, firstName, lastName, gpa, major, skills);
        }
    }

    // MODIFY
    @Override
    public void notifyObservers(Operation operation, String origValue, String newValue) {
        for (ObserverI observer : observers
        ) {
            MyLogger.writeMessage("Notify observer: " + observer, MyLogger.DebugTags.MODIFY_NODE);
            observer.update(operation, origValue, newValue);
        }
    }

    /**
     * functions update studentRecord itself
     * 2 functions are overload for INSERT and MODIFY
     */
    // INSERT
    @Override
    public void update(Operation operation, int bNumber, String firstName, String lastName, double gpa, String major, Set<String> skills) {
        if (operation != Operation.INSERT)
            throw new IllegalArgumentException("StudentRecord try to update a record base INSERT, but input Enum tag is not INSERT");

        this.bNumber = bNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gpa = gpa;
        this.major = major;
        this.skills.addAll(skills);
        MyLogger.writeMessage("updated successfully: " + this, MyLogger.DebugTags.INSERT_NODE);
    }

    // MODIFY
    @Override
    public void update(Operation operation, String origValue, String newValue) {
        if (operation != Operation.MODIFY)
            throw new IllegalArgumentException("StudentRecord try to update a record base MODIFY, but input Enum tag is not MODIFY");

        if (origValue.equals(firstName)) {
            firstName = newValue;
        } else if (origValue.equals(lastName)) {
            lastName = newValue;
        } else if (origValue.equals(major)) {
            major = newValue;
        } else if (skills.contains(origValue)) {
            skills.remove(origValue);
            skills.add(newValue);
        } else {
            throw new RuntimeException("StudentRecord try to update a record, but origValue \""+origValue+"\" can not be found in "+this);
        }
        MyLogger.writeMessage("updated successfully: " + this, MyLogger.DebugTags.MODIFY_NODE);
    }

    /**
     * functions update studentRecord itself and notify all the observers
     * 2 functions are overload for INSERT and MODIFY
     */
    // INSERT
    public void updateStudentRecord(Operation operation, int bNumber, String firstName, String lastName, double gpa, String major, Set<String> skills) {
        update(operation, bNumber, firstName, lastName, gpa, major, skills);
        notifyObservers(operation, bNumber, firstName, lastName, gpa, major, skills);
    }

    // MODIFY
    public void updateStudentRecord(Operation operation, String origValue, String newValue) {
        update(operation, origValue, newValue);
        notifyObservers(operation, origValue, newValue);
    }

    /**
     * @return a new StudentRecord replica whose data field is deep copied
     */
    @Override
    public StudentRecord clone() {
        // Type of this.skills is set of strings, deep copy it manually.
        Set<String> skillsClone = new HashSet<>(this.skills);
        return new StudentRecord(bNumber, firstName, lastName, gpa, major, skillsClone);
    }


    public int getbNumber() {
        return bNumber;
    }

    public void setbNumber(int bNumber) {
        this.bNumber = bNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    public void addSkills(Set<String> skills) {
        this.skills.addAll(skills);
    }

    public ArrayList<ObserverI> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<ObserverI> observers) {
        this.observers = observers;
    }


    @Override
    public String toString() {
        return "StudentRecord{" +
                bNumber +
                ',' + firstName +
                ',' + lastName +
                ',' + gpa +
                ',' + major +
                ',' + skills +
                '}';
    }
}
