/*
 */
package org.schemaanalyst.util.csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;

/**
 * A simple writer for saving CSVResult objects to files.
 * 
 * @author Chris J. Wright
 */
public class CSVWriter {
    /**
     * The path to write the report to.
     */
    private String path;
    /**
     * The separator to use between writing values.
     */
    private final String separator;

    /**
     * Constructor for creating a writer that saves CSVResults to a file at the 
     * given path and the default separator.
     * 
     * @param path The path to the file.
     */
    public CSVWriter(String path) {
        this(path, ", ");
    }

    /**
     * Constructor for creating a writer that saves CSVResults to a file at the 
     * given path, using the given separator.
     * 
     * @param path The path to the file.
     * @param separator The CSV separator.
     */
    public CSVWriter(String path, String separator) {
        this.path = path;
        this.separator = separator;
    }
    
    /**
     * Write a CSVResult object to a CSV file. If the file does not already 
     * exist, a header row is added before the first row is written.
     * 
     * @param result The content to write.
     */
    public synchronized void write(CSVResult result) {
        boolean writeHeader = writeHeader();
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            if (writeHeader) {
                writer.println(CSVUtilities.convertIterableToCSV(result.getValues().keySet(), separator));
            }
            writer.println(CSVUtilities.convertIterableToCSV(result.getValues().values(), separator));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /**
     * Determine whether the header row needs to be written.
     * 
     * @return Whether the header row needs to be written.
     */
    private boolean writeHeader() {
        return (!Files.exists(FileSystems.getDefault().getPath(path)));
    }
}
