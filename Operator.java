public enum Operator {
   LPARENTHESIS(0),RPARENTHESIS(1),PLUS(2),MINUS(3),MULTIPLY(4),DIVIDE(5),TIMES(6);
   final int precedence;
    Operator(int precedence){
      this.precedence=precedence;
   }
}
