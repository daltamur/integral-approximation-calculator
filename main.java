import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.Math.PI;
import static java.lang.Math.pow;

public class main {
    public static void main (String[] args){
        Scanner scanner=new Scanner(System.in);
        beginRun(scanner);
    }

    private static void beginRun(Scanner scanner) {
        //Keeps user in a constant loop until they type in 'exit
        System.out.println("Choose an Approximation Option: Midpoint, Trapezoid, Simpson, or Exit");
        String methodChoice=scanner.next();
        if(methodChoice.equalsIgnoreCase("midpoint")){
            tableOrEquation(scanner, 'm');
        }else if(methodChoice.equalsIgnoreCase("trapezoid")){
            tableOrEquation(scanner, 't');
        }else if(methodChoice.equalsIgnoreCase("simpson")){
            tableOrEquation(scanner, 's');
        }else if(methodChoice.equalsIgnoreCase("exit")){
            System.out.println("Thank you for using this program.");
        } else{
            System.out.println("ERROR: Please enter a valid command");
            beginRun(scanner);
        }
    }

    private static void tableOrEquation(Scanner scanner, char method) {
        //They'll choose a table or equation format
        System.out.println("Using an equation or a table? TYPE: equation or table");
        String choice=scanner.next();
        if(choice.equalsIgnoreCase("equation")){
            equationIntegration(scanner, method);
        }else if(choice.equalsIgnoreCase("table")){
            tableIntegration(scanner, method);
        }else{
            System.out.println("ERROR: Please enter a valid command");
            tableOrEquation(scanner,method);
        }
    }

    private static void tableIntegration(Scanner scanner, char method) {
        //table gets input using regex and holds the x's and y's using a hashtable, a simple calculation is used to find change in x
        //I think we might have them input a file path and then it'll read an excel file or something, inputting it into the console is tedious
        System.out.println("Give your values in the following notation: ");
        System.out.println("(x1,y1):(x2,y2):(x3,y3):...(xn,yn)");
        String input=scanner.next();
        String[] inputs=input.split(":");
        Hashtable<Double, Double>pair=new Hashtable<>();
        double firstX=0.0;
        double lastX=0.0;
        for(int i=0; i<inputs.length;i++){
            String[] coordinates=inputs[i].split(",");
            String x_value=coordinates[0].substring(1);
            String y_value=coordinates[1].substring(0,coordinates[1].length()-1);
            double xVal=Double.parseDouble(x_value);
            if(i==0){
                firstX=xVal;
            }else if(i==inputs.length-1){
                lastX=xVal;
            }
            double yVal=Double.parseDouble(y_value);
            pair.put(xVal,yVal);
        }
        System.out.println("What is your n-value?");
        int nVal=scanner.nextInt();
        double changeInX=(lastX-firstX)/(nVal);
        double returned;
        if(method=='m'){
            returned=midPointTable(pair, changeInX, nVal, firstX, lastX);
            System.out.println("Evaluation using the midpoint rule: "+returned);
        }if(method=='t'){
            returned=trapezoidTable(pair, changeInX, nVal, firstX, lastX);
            System.out.println("Evaluation using the trapezoid rule: "+returned);
        }if(method=='s'){
            returned=simpsonTable(pair, changeInX, nVal, firstX, lastX);
            System.out.println("Evaluation using the simpson rule: "+returned);
        }
        beginRun(scanner);

    }

    private static double simpsonTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        //rist and last table values don't have an operation performed on them, so they just get added to returned
        double returned=0.0;
        double currentX=firstX;
        int i=1;
        returned+=pair.get(currentX);
        currentX+=changeInX;
        i++;
        while(!(currentX==lastX)){
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if(i%2==0){
                returned+=4*pair.get(currentX);
                currentX+=changeInX;
            }else {
                returned+=2*pair.get(currentX);
                currentX+=changeInX;
            }
            i++;
        }
        returned+=pair.get(currentX);
        returned=returned*(changeInX/3);
        return returned;
    }

    private static double trapezoidTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        double returned = 0.0;
        double currentX = firstX;
        int i = 1;
        returned += pair.get(currentX);
        currentX += changeInX;
        i++;
        while (!(currentX == lastX)) {
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if (i % 2 == 0) {
                returned += 2 * pair.get(currentX);
                currentX += changeInX;
            } else {
                returned += 2 * pair.get(currentX);
                currentX += changeInX;
            }
            i++;
        }
        returned += pair.get(currentX);
        returned = returned * (changeInX / 2);
        return returned;
    }

    private static double midPointTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        double returned = 0.0;
        //Start from the second X
        double currentX = firstX + changeInX ;
        //Instantiate another X for index-1
        double smolX = firstX;
        while (currentX <= lastX) {
            // find the midpoint of each index and add it to the sum
            returned += (pair.get(smolX)+pair.get(currentX))/2;
            //increment both current x and smol x
            currentX += changeInX;
            smolX += changeInX;
        }
        //multiply with delta x and return the value
        returned = returned * changeInX;
        return returned;
    }

    private static void equationIntegration(Scanner scanner, char method) {
        System.out.println("Write your equation, making sure to follow PEMDAS and using X as your variable. Wrap parentheses around negative numbers");
        String equation=scanner.next();
        System.out.println("What is your n-value?");
        double nValue=scanner.nextInt();
        System.out.println("What is your upperbound?");
        double lastVal=scanner.nextDouble();
        System.out.println("What is your lowerbound?");
        double firstVal=scanner.nextDouble();
        double changeInX=((lastVal-firstVal)/nValue);
        double returned;
        if (method == 's') {
            returned=simpsonEquation(equation,changeInX,nValue,firstVal,lastVal);
            System.out.println("Evaluation using the Simpson rule: "+returned);
        }else if(method=='t'){
            returned=trapezoidEquation(equation,changeInX,nValue,firstVal,lastVal);
            System.out.println("Evaluation using the trapezoid rule: "+returned);
        }else if(method=='m'){
            returned=midPointEquation(equation,changeInX,nValue,firstVal,lastVal);
            System.out.println("Evaluation using the midpoint rule: "+returned);
        }
//        equation=equation.replace("x","2");
//        returned=shuntingYard(equation, method);
        beginRun(scanner);
    }

    private static double midPointEquation(String equation, double changeInX,double nValue, double firstX, double lastX) {
        double returned = 0.0;
        //Start from the second X
        double currentX = firstX + changeInX ;
        //Instantiate another X for index-1
        double smolX = firstX;
        while (currentX <= lastX) {
            String e1 = equation.replace("x",String.valueOf(smolX));
            String e2 = equation.replace("x",String.valueOf(currentX));
            // find the midpoint of each index and add it to the sum
            returned += (shuntingYard(e1,'m')+shuntingYard(e2,'m'))/2;
            //increment both current x and smol x
            currentX += changeInX;
            smolX += changeInX;
        }
        //multiply with delta x and return the value
        returned = returned * changeInX;
        return returned;
    }

    private static double trapezoidEquation(String equation, double changeInX,double nValue, double firstX, double lastX) {
        double returned = 0.0;
        double currentX = firstX;
        int i = 1;
        returned += shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
        currentX += changeInX;
        i++;
        while (!(currentX == lastX)) {
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if (i % 2 == 0) {
                returned += 2 * shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
                currentX += changeInX;
            } else {
                returned += 2 * shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
                currentX += changeInX;
            }
            i++;
        }
        returned += shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
        returned = returned * (changeInX / 2);
        return returned;
    }

    private static double simpsonEquation(String equation, double changeInX,double nValue, double firstX, double lastX) {
        double returned=0.0;
        double currentX=firstX;
        int i=1;
        returned+=shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
        currentX+=changeInX;
        i++;
        while(!(currentX==lastX)){
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if(i%2==0){
                returned+=4*shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
                currentX+=changeInX;
            }else {
                returned+=2*shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
                currentX+=changeInX;
            }
            i++;
        }
        returned+=shuntingYard(equation.replace("x",String.valueOf(currentX)),'t');
        returned=returned*(changeInX/3);
        return returned;
    }

    private static double shuntingYard(String equation,char method){
        String trigBody="";
        queue postFixQueue=new queue();
        double enqueuedVal=0.0;
        stack workStack=new stack();
        String doubleVal="";
        String trigFunctionBody="";
        boolean isNegative=false;
        valueNodes temp=new valueNodes('(');
        try{
            for(int i=0;i<equation.length();i++){
                //if the char is a '(', push it with no question.
                if(equation.charAt(i)=='('){
                    temp=new valueNodes('(');
                    workStack.push(temp);
                    //if we have a closing parentheses, take everything off the stack until you find a open parentheses.
                }else if(equation.charAt(i)==')'){
                    temp=workStack.pop();
                    while(temp.operand!='('){
                        postFixQueue.enqueue(temp);
                        temp=workStack.pop();
                    }
                    //if there is a minus character right before an open parentheses, we know we have a negative
                }else if(equation.charAt(i)=='-'&&equation.charAt(i-1)=='('){
                    doubleVal+="-";
                    //if we have a digit or a decimal point, we're gonna keep building that number until the next character in the function is out
                }else if(Character.isDigit(equation.charAt(i))||equation.charAt(i)=='.'){
                    doubleVal+=equation.charAt(i);
                    //we're gonna cut off the string concatenation if the next character isn't a number, if it is
                    if(i!=equation.length()-1) {
                        if ((!Character.isDigit(equation.charAt(i + 1)))) {
                            if (equation.charAt(i + 1) != '.') {
                                System.out.println("gets put on stack");
                                enqueuedVal = Double.parseDouble(doubleVal);
                                System.out.println(enqueuedVal);
                                //throw it on the stack and reset the number counter
                                temp = new valueNodes(enqueuedVal);
                                postFixQueue.enqueue(temp);
                                doubleVal = "";
                            }
                        }
                    }
                    if(i==equation.length()-1){
                        System.out.println("gets put on stack");
                        enqueuedVal = Double.parseDouble(doubleVal);
                        System.out.println(enqueuedVal);
                        //throw it on the stack and reset the number counter
                        temp = new valueNodes(enqueuedVal);
                        postFixQueue.enqueue(temp);
                        doubleVal = "";
                    }
                }
                else if(equation.charAt(i)=='*'||equation.charAt(i)=='/'||equation.charAt(i)=='+'||equation.charAt(i)=='-'||equation.charAt(i)=='^'){

                    //uses pemdas to pop items onto the queue if the current top is an operator and has greater precedence
                    if(workStack.top==null||workStack.topOperandPeek()){
                        temp=new valueNodes(equation.charAt(i));
                        workStack.push(temp);
                    }else {
                        int operator=0;
                        if (equation.charAt(i) == '*') {
                            operator = 4;
                        }
                        if (equation.charAt(i) == '/') {
                            operator = 3;
                        }
                        if (equation.charAt(i) == '^') {
                            operator = 5;
                        }
                        if (equation.charAt(i) == '+') {
                            operator = 2;
                        }
                        if (equation.charAt(i) == '-') {
                            operator = 1;
                        }
                        valueNodes topVal = workStack.peek();
                        int stackOp=0;
                        if (topVal.operand == '*') {
                            stackOp = 4;
                        }
                        if (topVal.operand == '/') {
                            stackOp = 3;
                        }
                        if (topVal.operand == '^') {
                            stackOp =5;
                        }
                        if (topVal.operand == '+') {
                            stackOp = 2;
                        }
                        if (topVal.operand == '-') {
                            stackOp = 1;
                        }
                        if (stackOp>=operator) {
                            valueNodes popped = workStack.pop();
                            postFixQueue.enqueue(popped);
                            valueNodes pushed = new valueNodes(equation.charAt(i));
                            workStack.push(pushed);
                        } else {
                            valueNodes pushed = new valueNodes(equation.charAt(i));
                            workStack.push(pushed);
                        }
                    }
                }
                //if we have a trig function
                if(equation.charAt(i)=='s'||equation.charAt(i)=='c'||equation.charAt(i)=='t'||equation.charAt(i)=='a'){
                    //if the function is sin cos tan, csc, sec, or csc
                    if(equation.charAt(i)=='s'||equation.charAt(i)=='c'||equation.charAt(i)=='t'){
                        trigFunctionBody=equation.substring(i,i+3);
                        i=i+4;
                        //if the function is an arc function
                    }else if(equation.charAt(i)=='a'){
                        trigFunctionBody=equation.substring(i,i+4);
                        i=i+5;
                    }
                    //grow the equation inside the trig degree
                    while(equation.charAt(i)!=']'){
                        trigBody+=equation.charAt(i);
                        i++;
                    }
                    //get the numerical value of the equation inside the trig function
                    double returnedVal=shuntingYard(trigBody,method);
                    double returnedInRadians=returnedVal;
                    //if(!(trigFunctionBody.contains("a"))){
                    //    returnedInRadians = (returnedVal * (PI)) / 180;
                    //}
                    //perform the trig function matching the proper string
                    switch (trigFunctionBody) {
                        case "sin":
                            returnedInRadians = Math.sin(returnedInRadians);
                            break;
                        case "cos":
                            returnedInRadians = Math.cos(returnedInRadians);
                            break;
                        case "tan":
                            returnedInRadians = Math.tan(returnedInRadians);
                            break;
                        case "sec":
                            returnedInRadians = 1 / (Math.cos(returnedInRadians));
                            break;
                        case "csc":
                            returnedInRadians = 1 / (Math.sin(returnedInRadians));
                            break;
                        case "cot":
                            returnedInRadians = 1 / (Math.tan(returnedInRadians));
                            break;
                        case "asin":
                            returnedInRadians = Math.asin(returnedInRadians);
                            break;
                        case "acos":
                            returnedInRadians = Math.acos(returnedInRadians);
                            break;
                        case "atan":
                            returnedInRadians = Math.atan(returnedInRadians);
                            break;
                        case "asec":
                            returnedInRadians = Math.acos(1 / returnedInRadians);
                            break;
                        case "acsc":
                            returnedInRadians = Math.asin(1 / returnedInRadians);
                            break;
                        case "acot":
                            returnedInRadians = Math.atan(1 / returnedInRadians);
                            break;
                    }
                    valueNodes enqueued=new valueNodes(returnedInRadians);
                    System.out.println("returnedVal: "+returnedInRadians);
                    postFixQueue.enqueue(enqueued);
                    trigFunctionBody="";
                    trigBody="";
                }
                else if(equation.charAt(i)=='p'){
                    double queued=PI;
                    if(doubleVal.equals("-")){
                        queued=-queued;
                    }
                    valueNodes pi=new valueNodes(queued);
                    postFixQueue.enqueue(pi);
                    doubleVal="";
                    i++;
                }
            }
            if(workStack.peek()!=null){
                valueNodes popped=workStack.pop();
                postFixQueue.enqueue(popped);
            }
        }catch(Exception e){
            //just a catch for any mistypes so the program doesn't crash
            System.out.println("Something went wrong. Input your function again, making sure to follow PEMDAS and wrap negative numbers in parentheses");
            System.out.println("");
            Scanner scanner=new Scanner(System.in);
            equationIntegration(scanner,method);
        }
        return postFixStackEvaluator(postFixQueue);
    }


    public static Double postFixStackEvaluator(queue postFixQueue){
        // create stack
        Stack<Double> s = new Stack<Double>();

        // postFixQueue to postfix string
        StringBuilder postfix = new StringBuilder();
        while(!postFixQueue.isEmpty()){
            postfix.append(postFixQueue.dequeue().toString());
        }


        // string to character array
        char[] chars = postfix.toString().toCharArray();
        int charsLength = chars.length;

        for(int i = 0; i < charsLength; i++){
            // iterating through char Array
            char currentChar = chars[i];

            if(isOperation(currentChar)){
                switch(currentChar){
                    case '+' : s.push(s.pop() + s.pop()); break;
                    case '-' : s.push(-s.pop() + s.pop()); break;
                    case '*' : s.push(s.pop() * s.pop()); break;
                    case '/' : s.push(1 / s.pop() * s.pop()); break;
                    case '^' : s.push(pow(s.pop(),s.pop())); break;
                }
            } else if(Character.isDigit(currentChar)){
                //numbers get pushed to the stack
                s.push(0.0);
                while(Character.isDigit(chars[i])){
                    s.push(10.0 * s.pop() + (chars[i++] - '0'));
                }
            }
        }
        if(!s.isEmpty()){
            return s.pop();
        }else{
            return 0.0;
        }
    }
    private static boolean isOperation(char currentChar){
        return currentChar =='*' || currentChar == '/' || currentChar == '+' || currentChar =='-' || currentChar == '^';
    }

}

