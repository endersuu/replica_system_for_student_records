package studentskills.mytree;


import studentskills.util.FileProcessor;
import studentskills.util.MyLogger;
import studentskills.util.Results;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeHelper {
    private final List<TreeI> trees;
    private FileProcessor fileProcessor;

    public TreeHelper(int treeAmount) {
        this.trees = new ArrayList<>();
        for (int i = 0; i < treeAmount; i++) {
            addTree(new AVLTree());
        }
    }

    private void addTree(TreeI tree) {
        trees.add(tree);
    }

    /**
     * @param studentRecord will be cloned
     *                      Replicas will be mutually registered as both observer and subject
     */
    public void insertStudentRecordToTreeReplica(StudentRecord studentRecord) {
        ArrayList<StudentRecord> studentRecordReplicas = new ArrayList<>();

        for (TreeI tree : trees
        ) {
            // notice clone() will discard information of tree and observer.
            StudentRecord studentRecordReplica = studentRecord.clone();
            MyLogger.writeMessage("Make Replica " + studentRecordReplica + " from " + studentRecord, MyLogger.DebugTags.INSERT_NODE);
            studentRecordReplicas.add(studentRecordReplica);
            MyLogger.writeMessage("Tree " + tree.getId() + " inserts " + studentRecordReplica, MyLogger.DebugTags.INSERT_NODE);
            tree.insert(studentRecordReplica);
        }

        mutuallyRegisterObserver(studentRecordReplicas);
    }

    /**
     * @param studentRecordReplicas Replicas will register each other as both observer and subject
     */
    protected void mutuallyRegisterObserver(ArrayList<StudentRecord> studentRecordReplicas) {
        for (StudentRecord subject : studentRecordReplicas)
            for (StudentRecord observer : studentRecordReplicas)
                if (subject != observer) {
                    MyLogger.writeMessage("observer " + observer.getbNumber() + " is registering on subject " + subject.getKey() + " with the same bNumber", MyLogger.DebugTags.INSERT_NODE);
                    subject.registerObserver(observer);
                }

    }


    /**
     * @param operation indicate path is for INSERT or MODIFY
     * @param path      file path
     */
    public void process(Operation operation, String path) throws InvalidPathException, SecurityException, FileNotFoundException, IOException {
        setFileProcessor(path);

        try {
            processLine(operation);
        } catch (NullPointerException e) {
            // boundary check: Input file is empty
            throw new RuntimeException("File is empty");
        }

        try {
            while (true) {
                processLine(operation);
            }
        } catch (NullPointerException e) {
            // use NullPointerException to jump out of while loop, don't exist
        } finally {
            fileProcessor.close();
        }
    }

    private void processLine(Operation operation) throws IOException {
        String line;
        try {
            line = getLine();
        } catch (IllegalArgumentException e) {
            System.err.println("WARNING: " + e.getMessage());
            System.err.println("Drop it and move next line");
            return;
        }

        MyLogger.writeMessage("Read line: " + operation + " : " + line, MyLogger.DebugTags.READ_LINE);
        switch (operation) {
            case INSERT: {
                executeInput(line);
                break;
            }
            case MODIFY: {
                executeModify(line);
                break;
            }
            default:
                throw new RuntimeException("Operation " + operation + " not support");
        }
    }

    public void executeInput(String line) {
        Pattern pattern = Pattern.compile("(\\d+):([^\\s,]+),([^\\s,]+),(\\d(\\.\\d*)?),([^\\s,]+),(([^\\s,]+,?)+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            int bNumber = Integer.parseInt(matcher.group(1));
            String firstName = matcher.group(2);
            String lastName = matcher.group(3);
            double gpa = Double.parseDouble(matcher.group(4));
            String major = matcher.group(6);
            String[] skillsArray = matcher.group(7).split(",");
            if (skillsArray.length > 10) {
                System.err.println("WARNING: Number of skills is more than 10 in this line: " + line);
                System.err.println("Process as usual");
            }
            Set<String> skills = new HashSet<>(Arrays.asList(skillsArray));
            MyLogger.writeMessage("Parse result: {" +
                    "bNumber=" + bNumber +
                    ',' + "firstName=" + firstName +
                    ',' + "lastName=" + lastName +
                    ',' + "gpa=" + gpa +
                    ',' + "major=" + major +
                    ',' + "skills=" + skills +
                    '}', MyLogger.DebugTags.PARSE_LINE);
            MyLogger.writeMessage("Try to insert studentRecord " + bNumber + " into tree 0", MyLogger.DebugTags.INSERT_NODE);
            try {
                NodeI node = getTreeFromId(0).search(bNumber);
                // INSERT based update, modify data field and add skills
                MyLogger.writeMessage("Node " + node.getKey() + " is duplicated, old node will be replaced.", MyLogger.DebugTags.INSERT_NODE);
                StudentRecord studentRecord = (StudentRecord) node;
                studentRecord.updateStudentRecord(Operation.INSERT, bNumber, firstName, lastName, gpa, major, skills);

            } catch (NullPointerException e) {
                insertStudentRecordToTreeReplica(new StudentRecord(bNumber, firstName, lastName, gpa, major, skills));
            } catch (RuntimeException e) {
                MyLogger.writeMessage("insert fail: " + e.getMessage(), MyLogger.DebugTags.INSERT_NODE);
                System.err.println("insert fail: " + e.getMessage());
                System.err.println("Drop it and move to next line");
            }


        } else {
            throw new RuntimeException("TreeHelper fail to parse a line(" + line + ") from input file");
        }
    }

    public void executeModify(String line) {
        Pattern pattern = Pattern.compile("(\\d+),(\\d+),([^\\s:]+):([^\\s:]*)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            int id = Integer.parseInt(matcher.group(1));
            int bNumber = Integer.parseInt(matcher.group(2));
            String origValue = matcher.group(3);
            String newValue = matcher.group(4);

            MyLogger.writeMessage("Parse result: {" +
                    "bNumber=" + bNumber +
                    ',' + "origValue=" + origValue +
                    ',' + "newValue=" + newValue +
                    '}', MyLogger.DebugTags.PARSE_LINE);
            MyLogger.writeMessage("Try to update studentRecord " + bNumber + " in tree " + id + " : change " + origValue + " to " + newValue, MyLogger.DebugTags.MODIFY_NODE);
            if (newValue.isEmpty()) {
                MyLogger.writeMessage("update fail: newValue is Empty in line: " + line, MyLogger.DebugTags.MODIFY_NODE);
                System.err.println("WARNING: newValue is Empty in line: " + line);
                System.err.println("Drop it and move next line");
                return;
            }

            NodeI node;

            try {
                node = getTreeFromId(id).search(bNumber);
                StudentRecord studentRecord = (StudentRecord) node;
                studentRecord.updateStudentRecord(Operation.MODIFY, origValue, newValue);
            } catch (NullPointerException e) {
                MyLogger.writeMessage("update fail: " + e.getMessage(), MyLogger.DebugTags.MODIFY_NODE);
                System.err.println("WARNING: StudentRecord in this line is not exist: " + line);
                System.err.println("Drop it and move next line");
            } catch (RuntimeException e) {
                MyLogger.writeMessage("update fail: " + e.getMessage(), MyLogger.DebugTags.MODIFY_NODE);
                System.err.println("WARNING: RuntimeException occur on this line: " + line + " " + e.getMessage());
                System.err.println("Drop it and move next line");
            }

        } else {
            throw new RuntimeException("TreeHelper fail to parse a line(" + line + ") from modify file");
        }
    }

    public Results printNodes(TreeI tree) {
        return new Results(tree.printNodes());
    }

    private TreeI getTreeFromId(int id) {
        for (TreeI tree : trees) {
            if (tree.getId() == id) {
                return tree;
            }
        }

        throw new RuntimeException("TreeHelper can not find this tree: " + id);
    }

    public List<TreeI> getTrees() {
        return trees;
    }

    public void setFileProcessor(String inputFilePath)
            throws InvalidPathException, SecurityException, FileNotFoundException, IOException {
        this.fileProcessor = new FileProcessor(inputFilePath);
    }

    public String getLine() throws IOException {
        String line = fileProcessor.poll();
        if (line.isEmpty())
            throw new IllegalArgumentException("Empty line is in input file");
        return line;
    }

}
