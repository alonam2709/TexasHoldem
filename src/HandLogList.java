import java.io.*;
import java.util.LinkedList;
import java.util.Vector;
public class HandLogList {
    private static final int MAX_SIZE = 10;
    private LinkedList<LogEntry> logEntries;

    public HandLogList() {
        logEntries = new LinkedList<>();
    }

    public void addLogEntry(LogEntry logEntry) {
        if (logEntries.size() == MAX_SIZE) {
            // If the queue is full, remove the oldest entry from the front
            logEntries.removeFirst();
        }

        logEntries.addLast(logEntry);
    }

    public LinkedList<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            for (LogEntry logEntry : logEntries) {
                String logEntryString = logEntry.toString(); // Get the string representation
                oos.writeObject(logEntryString); // Save the string representation
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HandLogList loadFromFile(String fileName) {
        HandLogList loadedQueue = new HandLogList();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            while (true) {
                try {
                    String logEntryString = (String) ois.readObject();
                    // Parse the string representation to create a LogEntry
                    LogEntry logEntry = parseLogEntryString(logEntryString);
                    if (logEntry != null) {
                        loadedQueue.addLogEntry(logEntry);
                    }
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return loadedQueue;
    }

    private static LogEntry parseLogEntryString(String logEntryString) {
        try {
            // Split the logEntryString into handDescription and probabilities
            String[] parts = logEntryString.split("\nProbabilities:\n");
            String handDescription = parts[0];

            // Split the probabilities part into individual lines
            String[] probabilityLines = parts[1].split("\n");

            // Create a Vector to store ProbabilityData objects
            Vector<ProbabilityData> probabilities = new Vector<>();

            for (String probabilityLine : probabilityLines) {
                // Split each line into values (you may need to adjust this based on your format)
                String[] values = probabilityLine.split(": ");

                // Extract the values for ProbabilityData
                String name = values[0];
                double value = Double.parseDouble(values[1]);

                // Create a new ProbabilityData object and add it to the Vector
                ProbabilityData probabilityData = new ProbabilityData(name, value);
                probabilities.add(probabilityData);
            }

            // Create and return a LogEntry with extracted data
            return new LogEntry(handDescription, probabilities);
        } catch (Exception e) {
            // If parsing fails, return null or handle the error as needed
            e.printStackTrace();
            return null;
        }
    }
}