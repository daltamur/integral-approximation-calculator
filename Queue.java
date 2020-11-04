public class Queue {

    // Instance variables
    ValueNode front;
    ValueNode back;

    // Constructor
    public Queue() {
        this.front = null;
        this.back = null;
    }

    // isEmpty -- check if queue has any eleemnts
    public boolean isEmpty() {
        return front == null && back == null;
    }

    // enqueue -- add item to the back of the queue
    public void enqueue(double value) {
        // Create the new node
        ValueNode sub = new ValueNode(value);
        // Check if queue is empty
        if (back == null) {
            // If so, add first element
            front = sub;
            back = sub;
        } else {
            // Otherwise, link to current back node
            back.next = sub;
            // Update back
            back = sub;
        }
    }


    // dequeue -- remove the node at front and return a reference
    public double dequeue() {
        if (back == null) {
            // If so, return null
            return 0;
        } else {
            ValueNode sub = front;
            front = front.next;
            if (front == null) {
                back = null;
            }
            return sub.value;
        }
    }


}
