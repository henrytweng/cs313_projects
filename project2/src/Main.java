import java.util.*;
import java.util.Scanner;

class Main{

   private static boolean isNum(char test) {
      if (test == '.' || (test >= '0' && test <= '9')) return true;
      return false;
   }

   private static boolean isOptr(char test) {
      if (test == '+' || test == '-' || test == '*' || test == '/') return true;
      return false;
   }


   static double evaluate(String expr){
      Stack<Double> valStack = new Stack<Double>();
      Stack<Character> opStack = new Stack<Character>();
      int leftParentheses = 0;

      for (int i = 0; i < expr.length(); i++) {
         char test = expr.charAt(i);
         int j = i;
         while (j < expr.length() && isNum(expr.charAt(j))) ++j;
         if (j > i) {
            String token = expr.substring(i, j);
            valStack.push(Double.parseDouble(token)); //Push number onto values stack
            i = j-1;
         }
         else if (isOptr(test)) opStack.push(test); //Push operator onto operators stack
         else if (test == '(') ++leftParentheses;
         else if (test == ')') {
            if (valStack.empty() || opStack.empty() || leftParentheses <= 0) return 0;

            double val = valStack.pop();
            char optr = opStack.pop();
            --leftParentheses;

            if(valStack.empty()) return 0;

            if (optr == '+') val = valStack.pop() + val;
            else if (optr == '-') val = valStack.pop() - val;
            else if (optr == '*') val = valStack.pop() * val;
            else if (optr == '/') val = valStack.pop() / val;
            valStack.push(val);
         }
      }
      if (!(opStack.empty()) || leftParentheses != 0) return 0;
      return valStack.pop();

   }
   /*
    * Sample main to run tests
    * *************************
    * Runs default tests if no
    * input is given.
    */
   public static void main(String[] args){
      //Variables
      Scanner scnr = new Scanner(System.in);
      //Custom test case
      if(scnr.hasNext()){
         String testInput = "";
         while(scnr.hasNext()){
            testInput = scnr.nextLine();
            System.out.println(testInput + " : " + evaluate(testInput));
         }
      }
      //Default test case
      else{
         String [] sampletests = {"(1+(2*3))", "(2*(4*(6/3)))","(3+(4*2))"};
         double [] sampleoutput = {7.0, 16.0, 11.0};
         for(int i = 0; i < sampletests.length; i++){
            if(evaluate(sampletests[i]) != sampleoutput[i]){
               System.out.println("Failed sample tests");
               return;
            }
         }
         System.out.println("Passed sample tests");
      }
   }
}