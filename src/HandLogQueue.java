/*
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.File;

class HandLogQueue implements Iterable<HandLogEntry>, Serializable {
    private Node head;
    private Node tail;
    private int size;
    private static final int MAX_SIZE = 10;

    public HandLogQueue() {
        head = null;
        tail = null;
        size = 0;
    }

    // Add a new log entry to the queue
    public void add(HandLogEntry entry) {
        Node newNode = new Node(entry);
        if (size == 0) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;

        // If the queue exceeds the maximum size, remove the oldest entry
        if (size > MAX_SIZE) {
            head = head.getNext();
            size--;
        }
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the queue from a file

    public static HandLogQueue loadFromFile(String fileName) {
        HandLogQueue loadedQueue = null;
        File file = new File(fileName);

        if (file.exists()) {
            try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
                loadedQueue = (HandLogQueue) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return loadedQueue;
    }

    @Override
    public Iterator<HandLogEntry> iterator() {
        return new Iterator<HandLogEntry>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public HandLogEntry next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                HandLogEntry data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

}
*/
import java.io.*;
import java.util.Vector;

public class HandLogQueue {
    private static final int MAX_SIZE = 10;
    private Vector<LogEntry> logEntries;

    public HandLogQueue() {
        logEntries = new Vector<>(MAX_SIZE);
    }

    public void addLogEntry(LogEntry logEntry) {
        if (logEntries.size() == MAX_SIZE) {
            // If the queue is full, remove the oldest entry from the front
            logEntries.remove(0);
        }

        logEntries.add(logEntry);
    }

    public Iterable<LogEntry> getLogEntries() {
        return logEntries;
    }

    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(logEntries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HandLogQueue loadFromFile(String fileName) {
        HandLogQueue loadedQueue = new HandLogQueue();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            loadedQueue.logEntries = (Vector<LogEntry>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return loadedQueue;
    }
}