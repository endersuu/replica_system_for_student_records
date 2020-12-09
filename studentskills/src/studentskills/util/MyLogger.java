package studentskills.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Mylogger is imported from
 * http://www.cs.binghamton.edu/~mgovinda/courses/downloads/MyLogger.java
 * and modified
 */

public class MyLogger {

    public static enum DebugTags {
        READ_LINE("READ_LINE"), PARSE_LINE("PARSE_LINE"), INSERT_NODE("INSERT_NODE"), MODIFY_NODE("MODIFY_NODE");

        final private String tagName;

        DebugTags(String tagName) {
            this.tagName = tagName;
        }

        public static DebugTags fromString(String tagName) {
            for (DebugTags tag : DebugTags.values())
                if (tagName.equals(tag.tagName))
                    return tag;

            throw new IllegalArgumentException("tagName is not find in Enum DebugTags");
        }

    }

    private static Set<DebugTags> debugTags = new HashSet<>();

    public static void addDebugTag(DebugTags debugTag) {
        debugTags.add(debugTag);
    }

    public static void removeDebugTag(DebugTags debugTag) {
        if (debugTags.contains(debugTag)) debugTags.remove(debugTag);
    }

    public static void writeMessage(String message, DebugTags debugTag) {
        if (debugTags.contains(debugTag))
            System.out.println("DEBUG: " + debugTag + " : " + message);
    }

    public String toString() {
        return "The debug tags has been set to the following: " + debugTags;
    }
}
