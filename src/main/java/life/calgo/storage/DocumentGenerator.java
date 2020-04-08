package life.calgo.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * An abstract class representing functionality for ReportGenerator and ExportGenerator.
 */
public abstract class DocumentGenerator {
    public static final int WIDTH_OF_DOCUMENT = 120;

    protected PrintWriter printWriter;
    protected File file;
    protected final Logger logger;

    public DocumentGenerator(String pathName, Logger logger) {

        this.file = new File(pathName);
        this.logger = logger;

        try {

            file.getParentFile().mkdirs();
            file.createNewFile();
            this.printWriter = new PrintWriter(file);

        } catch (FileNotFoundException e) {

            // happens when there is an error in opening or creating the file.
            logger.warning("Not able to generate document because file was unable to be created.");

        } catch (Exception e) {

            // other issues, usually due to the user's system.
            logger.warning("Check your system security settings and enable rights to create a new file.");

        }
    }

    /**
     * Writes a line for neatness in formatting.
     */
    public void printSeparator() {
        printWriter.println("--------------------------------------------------------------------------------"
                + "---------------------------------------");
    }

    /**
     * Wraps a String s into lines of n characters.
     *
     * @param s the String to be wrapped about.
     * @param n the number of characters allowed in a line.
     * @return the processed String after wrapping.
     */
    public String stringWrap(String s, int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 0 && i % (n - 1) == 0) {
                result.append("\n");
            }
            result.append(s.charAt(i));
        }

        return result.toString();
    }

    /**
     * A method that converts each String into a String array containing substrings that are split by "\n".
     * @param strings An ArrayList of strings.
     * @return An ArrayList of String arrays, which contain resulting substrings after split by "\n".
     */
    private ArrayList<String[]> splitNewLines(ArrayList<String> strings) {
        ArrayList<String[]> result = new ArrayList<>();
        for (String string : strings) {
            String[] curr = string.split("\n");
            result.add(curr);
        }
        return result;
    }

    /**
     * Combines columns to form a table.
     * @param strings An ArrayList of Strings, where each element is a column.
     * @param intervalWidth The distance between each column.
     * @return A stitched String with all columns combined together.
     */
    public String combineColumns(ArrayList<String> strings, int intervalWidth) {
        StringBuilder result = new StringBuilder();
        ArrayList<String[]> splitArrays = splitNewLines(strings);
        int numArrays = splitArrays.size();
        int maxNumLines = 0;

        for (String[] splitArray : splitArrays) {
            maxNumLines = Math.max(maxNumLines, splitArray.length);
        }

        for (int currLine = 0; currLine < maxNumLines; currLine++) {
            for (int s = 0; s < numArrays; s++) {
                String[] currArray = splitArrays.get(s);
                if (currLine < currArray.length) {
                    result.append(centraliseText(currArray[currLine], ReportGenerator.COLUMN_WIDTH));
                } else {
                    result.append(centraliseText("", ReportGenerator.COLUMN_WIDTH));
                }

                if (s == numArrays - 1) {
                    result.append("\n");
                } else {
                    result.append(" ".repeat(intervalWidth));
                }
            }
        }

        return result.toString();
    }

    /**
     * Writes an empty line.
     */
    public void printEmptyLine() {
        printWriter.println("");
    }

    /**
     * Centralises the specified String.
     *
     * @param text the String to be centralised.
     * @param width the width of the line whereby String should be centralised.
     * @return the processed String that has been centralised.
     */
    public String centraliseText(String text, int width) {

        int lengthOfText = text.length();
        int numWhitespace = (width - lengthOfText) / 2;

        return  " ".repeat(numWhitespace) +
                text +
                " ".repeat(numWhitespace);
    }

    /**
     * Writes the context/meta-information of the document.
     */
    public abstract void printHeader();

    /**
     * Writes the concluding remarks in the document.
     */
    public abstract void printFooter();
}
