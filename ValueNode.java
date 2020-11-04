public class ValueNode {

    double value;
    Operator sign;
    ValueNode next;

    // Constructor
    public ValueNode(double value) {
        this.value = value;
        this.next = null;
    }

    public ValueNode(Operator sign){
        this.sign = sign;
        this.next = null;
    }

    public boolean isOperator(){
        return sign != null;
    }

    public boolean isDouble() {
        return !isOperator();
    }


}
