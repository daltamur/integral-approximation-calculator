public class queue {
    //instance vars
    valueNodes front;
    valueNodes back;

    public queue(){}


    //we'll use this to determine when to stop taking things off the queue in the postfix evaluator
    public boolean isEmpty(){
        return front==null &&back==null;
    }


    //enqueue just puts something on the queue

    public void enqueue(valueNodes inserted){
        if(back==null){
            back=inserted;
            front=inserted;
        }else{
            back.next=inserted;
            back=inserted;
        }
    }

    //takes nodes off the queue
    public valueNodes dequeue(){
        if(front!=null){
            valueNodes temp=front;
            front=front.next;
            if(front==null){
                back=null;
            }
            return temp;
        }else{
            return null;
        }

    }


}
