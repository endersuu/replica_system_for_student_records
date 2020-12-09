package studentskills.util;

import java.io.IOException;

public interface FileDisplayInterface {
    /**
     * write result to file.
     *
     * @param outPath path for results.
     */
    void writeToFile(String outPath) throws IOException;
}
