package studentskills.util;

import java.io.IOException;

import java.util.LinkedList;
import java.util.Queue;
import java.io.Writer;
import java.io.FileWriter;


public class Results implements FileDisplayInterface, StdoutDisplayInterface {

    private Queue<String> resultsQueue; // record processed words

    public Results() {
        this.resultsQueue = new LinkedList<>();
    }

    public Results(Queue<String> resultsQueue) {
        this.resultsQueue = resultsQueue;
    }

    public void recordResult(String result) {
        if (result.isEmpty()) throw new IllegalArgumentException("empty string is not allowed to add to results");

        resultsQueue.add(result);
    }

    /**
     * write result to file.
     *
     * @param outputPath path of output file.
     */
    @Override
    public void writeToFile(String outputPath) throws IOException {
        //file will be created, if file does not exist
        Writer outWriter = new FileWriter(outputPath);

        // write rotated strings to buff
        for (String result : resultsQueue)
            outWriter.write(result + '\n');

        // OS flush buff automatically when file close
        outWriter.close();
    }

    /**
     * write result to stdout.
     */
    @Override
    public void writeToStdOut() {
        // write rotated strings to stdout buff one by one
        for (String result : resultsQueue)
            System.out.print(result + "\n");

        // OS flush buff automatically when process over
        System.out.flush();
    }

    @Override
    public String toString() {
        return "Results{" +
                "results=" + resultsQueue +
                '}';
    }
}


