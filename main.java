import java.util.Hashtable;
import java.util.Scanner;

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
        System.out.println("Giver your values in the following notation: ");
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
            double xVal=Double.valueOf(x_value);
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
            }else if(!(i%2==0)){
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
        System.out.println("Come back later!");
        return 0.0;
    }

    private static double midPointTable(Hashtable<Double, Double> pair, double changeInX, int nVal, double firstX, double lastX) {
        System.out.println("Come back later!");
        return 0.0;
    }

    private static void equationIntegration(Scanner scanner, char method) {
        System.out.println("Come back later!");
        beginRun(scanner);
    }


}
