public class Stack {

    // Instance variables
    ValueNode top;

    // Constructor
    public Stack () {
        this.top = null;
    }

    // push -- push an item to the top
    public void push (Operator other) {
            ValueNode sub = new ValueNode(other);
            sub.next = top;
            top = sub;
    }


    // pop -- remove top element
    public Operator pop () {
        if(top!= null) {
            ValueNode sub = top;
            top = top.next;
            return sub.sign;
        } else {
            return null;
        }
    }

    // peek -- examine the top element, but don't pop it
    public Operator peek() {
        if (top!= null) {
            return top.sign;
        } else {
            return null;
        }
    }

    // isEmpty -- determine is stack is empty
    public boolean isEmpty () {
       return top==null;
    }


}
