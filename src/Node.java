class Node {
    private HandLogEntry data;
    private Node next;

    public Node(HandLogEntry data) {
        this.data = data;
        this.next = null;
    }

    public HandLogEntry getData() {
        return data;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return next;
    }
}
