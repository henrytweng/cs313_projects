import java.util.*;

public class BSTSet<E extends Comparable<E>> extends AbstractBSTSet<E> {
    /* **************************************************
     *    Inherited from AbstractBSTSet:
     *    public Node<E> root;
     *    public void add(E e) {};
     *    public void remove(E e) {};
     * **************************************************/
    public BSTSet() {}

    public boolean contains(E e) {
        return contains(e, root);
    }

    private boolean contains(E e, Node<E> root) {
        if (root == null) return false;
        int cond = e.compareTo(root.data);
        if (cond < 0) return contains(e, root.left);
        else if (cond > 0) return contains(e, root.right);
        return true;
    }

    public E minimum(Node<E> root) {
        if (root == null) throw new NoSuchElementException();
        Node<E> n = root;
        while (n.left != null) n = n.left;
        return n.data;
    }

    public int height(Node<E> root) {
        if (root == null) return -1;
        return 1 + Integer.max(height(root.left), height(root.right));
    }

    // Returns true if the compared trees have the exact same structure
    public boolean sameTree(Node<E> r1, Node<E> r2) {
        if (r1 == null && r2 == null) return true;
        else if (r1 != null && r2 != null) {
            return (r1.data == r2.data && sameTree(r1.left, r2.left) && sameTree(r1.right, r2.right));
        }
        else return false;
    }

    public List<E> traverseInOrder() {
        List<E> inOrderList = new ArrayList<E>();
        traverseInOrder(inOrderList, root);
        return inOrderList;
    }

    private void traverseInOrder(List<E> list, Node<E> n) {
        if (n != null) {
            traverseInOrder(list, n.left);
            list.add(n.data);
            traverseInOrder(list, n.right);
        }
    }

    public boolean isBST(Node<E> root) {
        List<E> inOrderList = this.traverseInOrder();
        Iterator<E> itr = inOrderList.iterator();
        E data = itr.next();
        while (itr.hasNext()) {
            E next = itr.next();
            if (next.compareTo(data) < 0) return false;
            data = next;
        }
        return true;
    }
}