package studentskills.driver;

import studentskills.mytree.TreeHelper;
import studentskills.mytree.Operation;
import studentskills.util.MyLogger;
import studentskills.util.Results;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.InvalidPathException;

public class Driver {
    private static final int REQUIRED_NUMBER_OF_CMDLINE_ARGS = 7;

    public static void main(String[] args) {

        if ((args.length != REQUIRED_NUMBER_OF_CMDLINE_ARGS) || (args[0].equals("${input}")) || (args[1].equals("${modify}")) ||
                (args[2].equals("${out1}")) || (args[3].equals("${out2}")) || (args[4].equals("${out3}")) ||
                (args[5].equals("${error}")) || (args[6].equals("${debug}"))) {
            System.err.printf("Error: Incorrect number of arguments. Program accepts %d arguments.", REQUIRED_NUMBER_OF_CMDLINE_ARGS);
            System.exit(0);
        }

        String input = args[0];
        String modify = args[1];
        String out1 = args[2];
        String out2 = args[3];
        String out3 = args[4];
        String error = args[5];
        String debug = args[6];

        for(String tagName:debug.replaceAll("\\s","").split(","))
            MyLogger.addDebugTag(MyLogger.DebugTags.fromString(tagName));

        TreeHelper treeHelper = new TreeHelper(3);

        try {
            PrintStream out = new PrintStream(new FileOutputStream(error));
            System.setErr(out);

            treeHelper.process(Operation.INSERT, input);
            treeHelper.process(Operation.MODIFY, modify);

            Results results1 = treeHelper.printNodes(treeHelper.getTrees().get(0));
            Results results2 = treeHelper.printNodes(treeHelper.getTrees().get(1));
            Results results3 = treeHelper.printNodes(treeHelper.getTrees().get(2));

            results1.writeToFile(out1);
            results2.writeToFile(out2);
            results3.writeToFile(out3);

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.err.println("ERROR: "+e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.err.println("ERROR: "+e.getMessage());
            System.exit(2);
        } catch (InvalidPathException e) {
            System.out.println(e.getMessage());
            System.err.println("ERROR: "+e.getMessage());
            System.exit(3);
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
            System.err.println("ERROR: "+e.getMessage());
            System.exit(4);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.err.println("ERROR: "+e.getMessage());
            System.exit(5);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.err.println("ERROR: "+e.getMessage());
            System.exit(6);
        }

    }

}
