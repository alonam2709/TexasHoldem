import java.io.*;
import java.util.LinkedList;
import java.util.Vector;
public class HandLogList {
    // Maximum size of the log entries list
    private static final int MAX_SIZE = 10;
    // LinkedList to store LogEntry objects
    private LinkedList<LogEntry> logEntries;

    // Constructor initializes the logEntries LinkedList
    public HandLogList() {
        logEntries = new LinkedList<>();
    }

    // Adds a new LogEntry to the logEntries list
    public void addLogEntry(LogEntry logEntry) {
        // Check if the list has reached its maximum capacity
        if (logEntries.size() == MAX_SIZE) {
            // Remove the oldest entry to make space for the new entry
            logEntries.removeFirst();
        }
        // Add the new log entry to the end of the list
        logEntries.addLast(logEntry);
    }
    // Getter method to retrieve all log entries
    public LinkedList<LogEntry> getLogEntries() {
        return logEntries;
    }

    // Saves the current log entries to a file
    public void saveToFile(String fileName) {
        // Try-with-resources to ensure proper resource management
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (LogEntry logEntry : logEntries) {
                // Convert each LogEntry to a String
                String logEntryString = logEntry.toString();
                // Write the string representation to the file
                oos.writeObject(logEntryString);
            }
        } catch (IOException e) {
            // Handle potential IOExceptions
            e.printStackTrace();
        }
    }

    // Static method to load log entries from a file
    public static HandLogList loadFromFile(String fileName) {
        HandLogList loadedQueue = new HandLogList();

        // Try-with-resources for proper resource management
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    // Read the string representation of a log entry
                    String logEntryString = (String) ois.readObject();
                    // Convert the string back to a LogEntry object
                    LogEntry logEntry = parseLogEntryString(logEntryString);
                    // Add the log entry if it's valid
                    if (logEntry != null) {
                        loadedQueue.addLogEntry(logEntry);
                    }
                } catch (EOFException e) {
                    // Break the loop when end of file is reached
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Handle potential IOExceptions and ClassNotFoundExceptions
            e.printStackTrace();
            return null;
        }

        return loadedQueue;
    }

    // Converts a string representation of a log entry back into a LogEntry object
    private static LogEntry parseLogEntryString(String logEntryString) {
        try {
            // Split the string to separate hand description from probabilities
            String[] parts = logEntryString.split("\nProbabilities:\n");
            String handDescription = parts[0];

            // Further split to isolate individual probability lines
            String[] probabilityLines = parts[1].split("\n");

            // Create a Vector to store ProbabilityData objects
            Vector<ProbabilityData> probabilities = new Vector<>();

            for (String probabilityLine : probabilityLines) {
                // Split each line to separate the name and value
                String[] values = probabilityLine.split(": ");
                String name = values[0];
                double value = Double.parseDouble(values[1]);

                // Create ProbabilityData objects and add to the vector
                ProbabilityData probabilityData = new ProbabilityData(name, value);
                probabilities.add(probabilityData);
            }

            // Create and return a new LogEntry object using the parsed data
            return new LogEntry(handDescription, probabilities);
        } catch (Exception e) {
            // Handle any exceptions during the parsing process
            e.printStackTrace();
            return null;
        }
    }
}