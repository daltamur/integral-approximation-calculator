public class stack {
    //Instance vars
    valueNodes top;


    //constructor
    public  stack (){}


    //push--puts an element on the top of the stack
    public void push(valueNodes pushed){
        if(top==null){
            top=pushed;
        }else{
            pushed.next=top;
            top=pushed;
        }
    }


    //remove top element
    public valueNodes pop(){
        if(top!=null) {
            valueNodes temp = top;
            top=top.next;
            return temp;
        }else{
            return null;
        }
    }

    public boolean topOperandPeek(){
        boolean returned = false;
        if(top!=null){
            //check if we have an opened parentheses operand on the top or a double.
            valueNodes temp=top;
            //this is how we know to stop putting stuff in the queue.
            if(top.isOperand&&top.operand=='('){
                returned= true;
            }else{
                returned= false;
            }

        }
        return returned;
    }

    public valueNodes peek(){
        return top;
    }
}
