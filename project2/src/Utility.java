import java.util.*;
class Utility {
    public static void main(String args[]){
        Counter<String, String> x = new WordSet<String, String>();
        Utility.run(x);
     }
   public static void run(Counter<String, String> words){
      Counter<String, String> sortedWords = new WordSet<String, String>();
      Scanner scnr = new Scanner(System.in);
      String[] testInput = "A user interface is like a joke. If you have to explain it, it is not that good. What is a UX designer's favourite book? A macbook.".split("[\\s\t\n]");
          if(scnr.hasNext()){
    	  System.out.println("User Input\n***************************");
    	  while (scnr.hasNext()) {
    		String word = scnr.next().replaceAll("[^a-zA-Z\\d ]+$", "");
    	  	String keyWord = Utility.dropS(word);
    	  	words.put(keyWord, word);
    	  }
      }
      //Default test case
      else {
    	  System.out.println("Default Input\n***************************");
    	  for(String word : testInput) {
			  String keyWord = Utility.dropS(word);
			  words.put(keyWord, word.replaceAll("[^a-zA-Z\\d ]+$", ""));
    	  }
      }
      
      ArrayList<String> sortedKeys = new ArrayList<String>(words.keySet()); 
          
      Collections.sort(sortedKeys);  
  
      // Display the TreeMap which is naturally sorted 
      for (String w : sortedKeys){
         if (words.getCount(w) >= 3){
            String [] vals = words.get(w).split(",");
            ArrayList<String> valarr = new ArrayList<String>();
            for(int i = 1; i < vals.length; i ++)
               valarr.add(vals[i]);
            vals = valarr.toArray(new String[valarr.size()]);
            Arrays.sort(vals);
            System.out.print(w +":\t" + words.get(w).split(",")[0] + ", ");
            for(int v = 0; v < vals.length; v++){
               System.out.print(vals[v]);
               if(v < vals.length -1)
                   System.out.print(", ");
            }
            System.out.println();
         }
      }

   }
   
   static String dropS(String word) {
      String ans = word.replaceAll("[^a-zA-Z ]+$", "").toLowerCase();
      if (ans.endsWith("s"))
         return ans.substring(0, ans.length() - 1);
      else if (ans.endsWith("ed"))
         return ans.substring(0, ans.length() - 2);
      else if (ans.endsWith("ing"))
         return ans.substring(0, ans.length() - 3);
      return ans;
   }
}