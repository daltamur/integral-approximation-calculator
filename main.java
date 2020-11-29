import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;

import static java.lang.Math.*;

public class main {
    public static void main (String[] args) throws IOException {
        Scanner scanner=new Scanner(System.in);
        beginRun(scanner);
    }

    private static void beginRun(Scanner scanner) throws IOException {
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

    private static void tableOrEquation(Scanner scanner, char method) throws IOException {
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

    private static void tableIntegration(Scanner scanner, char method) throws IOException {
        //table gets input using regex and holds the x's and y's using a hashtable, a simple calculation is used to find change in x
        //I think we might have them input a file path and then it'll read an excel file or something, inputting it into the console is tedious
        System.out.println("Give the file path of your excel file, it should be formatted like /Users/You/outputCSV/output.csv");
        System.out.println("NOTE: Most accurate when decimal goes to the hundredths place at the most.");
        String input=scanner.next();
        BufferedReader br=getBR(input);
        String line="";
        Hashtable<Double, Double>pair=getHashTable(br);
        BufferedReader br2=getBR(input);
        String[]firstx=br2.readLine().split(",");
        double firstX=Double.parseDouble(firstx[0]);
        double lastX=0.0;
        while((line=br2.readLine())!=null) {
            String[]xs=line.split(",");
            lastX = Double.parseDouble(xs[0]);
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

    private static Hashtable<Double, Double> getHashTable(BufferedReader br) throws IOException {
        Hashtable<Double,Double>coordinates=new Hashtable<>();
        String line="";
        while((line=br.readLine())!=null){
            String[]xAndY=line.split(",");
            double x=Double.parseDouble(xAndY[0]);
            double y=Double.parseDouble(xAndY[1]);
            coordinates.put(x,y);
        }
        return coordinates;
    }

    private static BufferedReader getBR(String input) throws FileNotFoundException {
        BufferedReader br=new BufferedReader(new FileReader(input));
        return br;
    }

    private static double simpsonTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        //rist and last table values don't have an operation performed on them, so they just get added to returned
        double returned=0.0;
        double currentX=firstX;
        int i=1;
        returned+=pair.get(currentX);
        currentX+=floor(changeInX*100)/100;
        i++;
        while(currentX<=lastX-changeInX){
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if(i%2==0){
                returned+=4.0*pair.get(currentX);
                currentX+=changeInX;
            }else {
                returned+=2.0*pair.get(currentX);
                currentX+=changeInX;
            }
            currentX=floor(currentX*100)/100;
            i++;
        }
        currentX=floor(currentX*100)/100;
        returned+=pair.get(currentX);
        returned=returned*(changeInX/3);
        return returned;
    }

    private static double trapezoidTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        double returned = 0.0;
        double currentX = firstX;
        int i = 1;
        returned += pair.get(currentX);
        currentX+=floor(changeInX*100)/100;
        i++;
        while ((currentX <= lastX-changeInX)) {
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if (i % 2 == 0) {
                returned += 2 * pair.get(currentX);
                currentX += changeInX;
            } else {
                returned += 2 * pair.get(currentX);
                currentX += changeInX;
            }
            currentX=floor(currentX*100)/100;
            i++;
        }
            currentX=floor(currentX*100)/100;
            returned += pair.get(currentX);
            returned = returned * (changeInX / 2);
            return returned;
    }

    private static double midPointTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        double returned = 0.0;
        //Start from the second X
       double currentX=floor((changeInX+firstX)*100)/100;
        //Instantiate another X for index-1
        double smolX = firstX;
        smolX=floor((firstX)*100)/100;
        while (currentX <= lastX) {
            // find the midpoint of each index and add it to the sum
            returned += (pair.get(floor(((smolX+currentX)/2)*100))/100);
            //increment both current x and smol x
            currentX += changeInX;
            smolX += changeInX;
            currentX=floor(currentX*100)/100;
            smolX=floor(smolX*100)/100;
        }
        //multiply with delta x and return the value
        returned = returned * changeInX;
        return returned;
    }

    private static void equationIntegration(Scanner scanner, char method) throws IOException {
        System.out.println("Write your equation, making sure to follow PEMDAS and using X as your variable. Wrap parentheses around negative numbers");
        System.out.println("Put make sure logs have a base 10 or are a natural log.");
        String equation=scanner.next();
        System.out.println("What is your n-value?");
        System.out.println("NOTE: Make the n-value an even number if you are using Simpson's Rule.");
        double nValue=scanner.nextInt();
        System.out.println("What is your upperbound?");
        double lastVal=scanner.nextDouble();
        System.out.println("What is your lowerbound?");
        double firstVal=scanner.nextDouble();
        double changeInX=((lastVal-firstVal)/nValue);
        double returned=0.0;
        if (method == 's') {
            returned=simpsonEquation(equation,changeInX,firstVal,lastVal);
        }else if(method=='t'){
            returned=trapezoidEquation(equation,changeInX,firstVal,lastVal);
        }else if(method=='m'){
            returned=midPointEquation(equation,changeInX,firstVal,lastVal);
        }
        System.out.println("The value is: "+returned);
        errorEquation(equation,lastVal,firstVal,changeInX,returned);
        beginRun(scanner);
    }

    private static void errorEquation(String equation,double upperBound,double lowerBound,double changeInX,double returned) throws IOException {
        double upperErrorBound=0.0;
        double lowerErrorBound=0.0;
        double firstX=lowerBound;
        for(double i=firstX;.0001*floor(i*10000)<=.0001*floor((upperBound-changeInX)*10000);i+=changeInX){
            double currentMax=shuntingYard(equation.replace("x","("+i+")"));
            double currentMin=shuntingYard(equation.replace("x","("+i+")"));
            double equationVal=0.0;
            for(double w = i; .001*floor(w*1000)<=.001*floor((i+changeInX)*1000); w+=.001){
                String xSubbedEquation=equation.replace("x","("+w+")");
                equationVal = shuntingYard(xSubbedEquation);
                currentMax=Math.max(currentMax,equationVal);
                currentMin=Math.min(currentMin,equationVal);
                //System.out.println(w);
            }
            double maxRect=currentMax;
            double minRect=currentMin;
            upperErrorBound+=maxRect;
            lowerErrorBound+=minRect;
        }
        upperErrorBound=upperErrorBound*changeInX;
        double upperError=upperErrorBound-returned;
        lowerErrorBound=lowerErrorBound*changeInX;
        double lowerError=returned-lowerErrorBound;
        System.out.println("The error is +"+upperError+" and -"+lowerError+".");
    }

    private static double midPointEquation(String equation, double changeInX,double firstX, double lastX) throws IOException {
        double returned = 0.0;
        //Start from the second X
        double currentX = firstX + changeInX ;
        //Instantiate another X for index-1
        double smolX = firstX;
        double totalX=0.0;
        while (.0001*floor(currentX*10000)<=.0001*floor((lastX-changeInX)*10000)) {
            totalX=(currentX+smolX)/2.0;
            String e1 = equation.replace("x","("+String.valueOf(totalX)+")");
            // find the midpoint of each index and add it to the sum
            double val=shuntingYard(e1);
            returned+=val;
            //increment both current x and smol x
            currentX += changeInX;
            smolX += changeInX;
        }
        totalX=(currentX+smolX)/2.0;
        String e1 = equation.replace("x","("+String.valueOf(totalX)+")");
        double val=(shuntingYard(e1));
        returned+=val;
        //multiply with delta x and return the value
        returned = returned * changeInX;
        return returned;
    }

    private static double trapezoidEquation(String equation, double changeInX,double firstX, double lastX) throws IOException {
        double returned = 0.0;
        double currentX = firstX;
        int i = 1;
        returned += shuntingYard(equation.replace("x","("+String.valueOf(currentX))+")");
        currentX += changeInX;
        i++;
        while ((.0001*floor(currentX*10000)) <= (.0001*floor((lastX-changeInX)*10000))) {
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if (i % 2 == 0) {
                returned += 2 * shuntingYard(equation.replace("x","("+String.valueOf(currentX))+")");
                currentX += changeInX;
            } else {
                returned += 2 * shuntingYard(equation.replace("x","("+String.valueOf(currentX))+")");
                currentX += changeInX;
            }
            i++;
        }
        returned += shuntingYard(equation.replace("x","("+String.valueOf(currentX))+")");
        returned = returned * (changeInX / 2);
        return returned;
    }

    private static double simpsonEquation(String equation,double changeInX, double firstX, double lastX) throws IOException {
        Double returned=0.0;
        double currentX=firstX;
        int i=1;
        returned+=shuntingYard(equation.replace("x","("+currentX+")"));
        currentX+=changeInX;
        i++;
        while((.0001*floor(currentX*10000))<=(.0001*floor((lastX-changeInX)*10000))){
            //if index is even, multiply by 2 then add to sum. if odd, multiply by 4 then add to sum
            if(i%2==0){
                returned+=4.0*shuntingYard(equation.replace("x","("+currentX+")"));
                currentX+=changeInX;
            }else {
                returned+=2.0*shuntingYard(equation.replace("x","("+currentX+")"));
                currentX+=changeInX;
            }
            i++;
        }
        returned+=shuntingYard(equation.replace("x","("+currentX+")"));
        returned=returned*(changeInX/3);
        return returned;
    }

    private static double shuntingYard(String equation) throws IOException {
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
                            //System.out.println("gets put on stack");
                            enqueuedVal = Double.parseDouble(doubleVal);
                            //System.out.println(enqueuedVal);
                            //throw it on the stack and reset the number counter
                            temp = new valueNodes(enqueuedVal);
                            postFixQueue.enqueue(temp);
                            doubleVal = "";
                        }
                    }
                }
                if(i==equation.length()-1){
                    enqueuedVal = Double.parseDouble(doubleVal);
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
                double returnedVal=shuntingYard(trigBody);
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
                //System.out.println("returnedVal: "+returnedInRadians);
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
            if(equation.charAt(i)=='e'){
                double val=Math.exp(1);
                valueNodes e=new valueNodes(val);
                postFixQueue.enqueue(e);
            }
            if(equation.charAt(i)=='l'){
                if(equation.charAt(i+1)=='n'){
                    //ln
                    i=i+3;
                    String lnBody="";
                    while(equation.charAt(i)!=']'){
                        lnBody+=equation.charAt(i);
                        i++;
                    }
                    double inserted=Math.log(shuntingYard(lnBody));
                    valueNodes lnVal=new valueNodes(inserted);
                    postFixQueue.enqueue(lnVal);
                }else if(equation.charAt(i+1)=='o'){
                    //ln
                    i=i+3;
                    String logBody="";
                    while(equation.charAt(i)!=']'){
                        logBody+=equation.charAt(i);
                        i++;
                    }
                    double inserted=Math.log10(shuntingYard(logBody));
                    valueNodes lnVal=new valueNodes(inserted);
                    postFixQueue.enqueue(lnVal);
                }
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
            beginRun(scanner);
        }
        return postFixStackEvaluator(postFixQueue);
    }


    private static double postFixStackEvaluator(queue postFixQueue){
        Stack<Double> s = new Stack<Double>();
        StringBuilder totalString= new StringBuilder();
        valueNodes popped=postFixQueue.dequeue();
        double firstVal=0.0;
        double secondVal=0.0;
        while(popped!=null){
            if(popped.isDouble){
                double dVal=popped.doubleValue;
                s.push(dVal);
            }
            if(popped.isOperand&&popped.operand!='('){
                char operand=popped.operand;
                switch(operand){
                    case '+' : s.push(s.pop() + s.pop()); break;
                    case '-' : s.push(-s.pop() + s.pop()); break;
                    case '*' : s.push(s.pop() * s.pop()); break;
                    case '/' : s.push(1 / s.pop() * s.pop()); break;
                    case '^' : firstVal=s.pop(); secondVal=s.pop(); s.push(Math.pow(secondVal,firstVal)); break;
                }
            }
            popped=postFixQueue.dequeue();
        }
        double finalVal=s.pop();
        //System.out.println(finalVal);
        return finalVal;
    }


}
