import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

public class WordSet<K extends Comparable<K>, V> implements Counter<K, V> {
      private HashMap<K, Entry <K,V>> table;

      public WordSet() {
          table = new HashMap<>();
    }

      public String get(K word) {
         if (table.containsKey(word)) {
             StringBuilder ans = new StringBuilder();
             ArrayList<V> arr = new ArrayList<V>();
             ans.append("total count: ").append(getCount(word)).append(",");
             for (Entry <K,V> i = table.get(word); i != null; i = i.next) {
                 if (!(arr.contains(i.value) || i.value == null)) {
                     ans.append(i.value).append(",");
                     arr.add(i.value);
                 }
             }
             return ans.toString();
         }
	      return "";
      }

     public int getCount(K word) {
         if (table.containsKey(word)) {
             int count = 0;
             for (Entry<K,V> i = table.get(word); i != null; i = i.next) {
                 ++count;
             }
             return count;
         }
         return 0;
     }

      public Set<K> keySet() {
          return table.keySet();
      }

      public void put(K keyWord, V word) {
          if (table.containsKey(keyWord)) {
              Entry<K,V> i = table.get(keyWord);
              while (i.next != null) i = i.next;
              i.next = new Entry<> (keyWord, word, null);
          }
          else {
              table.putIfAbsent(keyWord, new Entry<>(keyWord, word, null));
          }
      }

      private static class Entry<K,V> {
          K key;
          V value;
          Entry <K,V> next;
          Entry (K k, V v, Entry<K,V> n) {
              key = k;
              value = v;
              next = n;
          }
      }
}