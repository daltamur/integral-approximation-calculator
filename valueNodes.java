public class valueNodes {
    boolean isOperand;
    boolean isDouble;
    double doubleValue;
    char operand;
    valueNodes next;

    public valueNodes(char operand){
        isOperand=true;
        this.operand=operand;
        next=null;
    }

    public valueNodes(double doubleValue){
        isDouble=true;
        this.doubleValue=doubleValue;
        next=null;
    }

}
