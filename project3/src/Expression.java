import java.util.ArrayDeque;
import java.util.ArrayList;

public class Expression extends ExpressionTree {
   private ArrayDeque<Character> opStack;

   public Expression(String s) {
      super();
      try {
         String postFix = inToPost(s);
         String[] tokens = postFix.split(",");

         ArrayDeque<BNode<String>> nodeStack = new ArrayDeque<>();

         for (String tok : tokens) {
            if (isOptr(tok.charAt(0))) {
               BNode<String> r = nodeStack.pop();
               BNode<String> l = nodeStack.pop();
               BNode<String> newNode = new BNode<String>(tok, null, l, r);
               nodeStack.push(newNode);
            }
            else {
               BNode<String> newNode = new BNode<String>(tok, null, null, null);
               nodeStack.push(newNode);
            }
         }
         root = nodeStack.pop();
      }
      catch (Exception e) { System.out.println("Error in constructor."); }
   }

   private String inToPost(String s) {
      s = s.replace(" ", "");
      opStack = new ArrayDeque<>();
      StringBuilder output = new StringBuilder();
      for(int i = 0; i < s.length(); i++) {
         char tok = s.charAt(i);
         if (tok == '(') opStack.push(tok);
         else if (isOptr(tok)) {
            while (!opStack.isEmpty()) {
               char top = opStack.peek();
               if (top == '(' || prec(top) < prec(tok)) break;
               else output.append(opStack.pop()).append(',');
            }
            opStack.push(tok);
         }
         else if (tok == ')') {
            while (!opStack.isEmpty()) {
               char top = opStack.pop();
               if (top == '(') break;
               else output.append(top).append(',');
            }
         }
         else {
            output.append(tok);
            if (i + 1 < s.length()) {
               char next = s.charAt(i + 1);
               if (isOptr(next) || next == ')') output.append(',');
            }
            else output.append(',');
         }
      }
      while (!opStack.isEmpty())
         output.append(opStack.pop()).append(',');
      return output.toString();
   }

   private int prec(char op) {
      if (op == '+' || op == '-') return 1;
      if (op == '*' || op == '/') return 2;
      return -1;
   }

   public String fullyParenthesised() {
      StringBuilder ans = new StringBuilder();
      ArrayList<String> list = new ArrayList<>();
      inFix((BNode<String>)root, list);
      for (String s: list) ans.append(s);
      return ans.toString();
   }

   private void inFix(BNode<String> n, ArrayList<String> list) {
      if (isLeaf(n)) list.add(n.data);
      else {
         list.add("(");
         inFix(n.left, list);
         list.add(n.data);
         inFix(n.right, list);
         list.add(")");
      }
   }

   public double evaluate() {
      String expr = this.fullyParenthesised();
      ArrayDeque<Double> valStack = new ArrayDeque<>();
      opStack = new ArrayDeque<>();
      int leftParentheses = 0;

      for (int i = 0; i < expr.length(); i++) {
         char test = expr.charAt(i);
         int j = i;
         while (j < expr.length() && isNum(expr.charAt(j))) ++j;
         if (j > i) {
            String token = expr.substring(i, j);
            valStack.push(Double.parseDouble(token)); // Push number onto values stack
            i = j-1;
         }
         else if (isOptr(test)) opStack.push(test); // Push operator onto operators stack
         else if (test == '(') ++leftParentheses;
         else if (test == ')') {
            if (valStack.isEmpty() || opStack.isEmpty() || leftParentheses <= 0) return 0;
            double val = valStack.pop();
            char optr = opStack.pop();
            --leftParentheses;

            if(valStack.isEmpty()) return 0;

            if (optr == '+') val = valStack.pop() + val;
            else if (optr == '-') val = valStack.pop() - val;
            else if (optr == '*') val = valStack.pop() * val;
            else if (optr == '/') val = valStack.pop() / val;
            valStack.push(val);
         }
      }
      if (!(opStack.isEmpty()) || leftParentheses != 0) return 0;
      return valStack.pop();
   }

   private boolean isNum(char test) {
      return test == '.' || (test >= '0' && test <= '9');
   }

   private boolean isOptr(char test) {
      return test == '+' || test == '-' || test == '*' || test == '/';
   }
}
