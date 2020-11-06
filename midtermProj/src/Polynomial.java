public class Polynomial extends PolynomialInterface {

    public Polynomial() { super(); }

    public Polynomial(String s) throws Exception {
        s = s.trim().replaceAll("\\s", "").toUpperCase();
        String[] terms = s.split("(?=\\b[\\+\\-])");
        Term[] temp = new Term[terms.length];

        int maxDegree = 0, index = -1;
        for (String input: terms) {
            if (isValidTerm(input)) {
                String coeffStr = null;
                double coeff = 1.0;
                int degree = 0;
                if (!(input.contains("X"))) coeffStr = input;
                else {
                    for (int i = 0; i < input.length(); i++) {
                        if (input.charAt(i) == 'X') {
                            if (i == 0) coeffStr = "+";
                            else coeffStr = input.substring(0, i);
                            if (i == input.length() - 1) degree = 1;
                        }
                        else if (i >= 2 && input.charAt(i - 1) == '^') {
                            try { degree = Integer.parseInt(input.substring(i)); }
                            catch (NumberFormatException e) {
                                System.out.println(input + "has invalid degree.");
                                degree = 1;
                            }
                        }
                    }
                }
                if (coeffStr.equals("-")) coeff = -1.0;
                else if (coeffStr.equals("+"));
                else try {
                    coeff = Double.parseDouble(coeffStr);
                }
                catch (NumberFormatException e) { System.out.println(input + "has invalid coeff."); }
                if (degree > maxDegree) maxDegree = degree;
                temp[++index] = new Term(coeff, degree);
            }
        }
        mergeSort(temp, 0, index); // sorted by degree, descending
        // Initialize the Polynomial DList
        int t = 0;
        for (int deg = maxDegree; deg >= 0; --deg)
            if (t <= index && temp[t].getDegree() == deg) {
                data.addLast(temp[t]); ++t;
            }
    }

    private static void mergeSort(Term[] arr, int i, int k) {
        int j = 0;
        if (i < k) {
            j = (i + k) / 2;
            mergeSort(arr, i, j);
            mergeSort(arr, j + 1, k);
            merge(arr, i, j, k);
        }
    }

    private static void merge(Term[] arr, int i, int j, int k) {
        Term[] temp = new Term[k - i + 1];
        int m = 0, l = i, r = j + 1;
        while (l <= j && r <= k) {
            if (arr[l].getDegree() >= arr[r].getDegree()) temp[m] = arr[l++];
            else temp[m] = arr[r++];
            ++m;
        }
        // Add remaining terms, if any
        while (l <= j) temp[m++] = arr[l++];
        while (r <= k) temp[m++] = arr[r++];
        for (m = 0; m < temp.length; ++m) arr[i + m] = temp[m];
    }

    private static void clean(PolynomialInterface p) {
        try {
            DNode<Term> ptr = p.data.getFirst();
            while (ptr != null) {
                if (ptr.getData().getCoefficient() == 0) p.data.remove(ptr);
                ptr = ptr.getNext();
            }
        }
        catch (Exception ignored) {};
    }

    public PolynomialInterface add(PolynomialInterface p) {
        PolynomialInterface ans = new Polynomial();
        try {
            DNode<Term> leftPtr = this.data.getFirst(), rightPtr = p.data.getFirst();
            Term l,r;
            boolean leftIsDone = false, rightIsDone = false;

            while (leftPtr != null && rightPtr != null) {
                l = leftPtr.getData();
                r = rightPtr.getData();
                if (l.getDegree() > r.getDegree()) {
                    ans.data.addLast(new Term(l.getCoefficient(), l.getDegree()));
                    if (leftPtr == this.data.getLast()) {
                        leftIsDone = true; break;
                    }
                    leftPtr = leftPtr.getNext();
                } else if (l.getDegree() == r.getDegree()) {
                    ans.data.addLast(new Term(l.getCoefficient() + r.getCoefficient(), l.getDegree()));
                    if (leftPtr == this.data.getLast()) leftIsDone = true;
                    else leftPtr = leftPtr.getNext();
                    if (rightPtr == p.data.getLast()) rightIsDone = true;
                    else rightPtr = rightPtr.getNext();
                    if (leftIsDone || rightIsDone) break;
                } else {
                    ans.data.addLast(new Term(r.getCoefficient(), r.getDegree()));
                    if (rightPtr == p.data.getLast()) {
                        rightIsDone = true; break;
                    }
                    rightPtr = rightPtr.getNext();
                }
            }
            if (rightIsDone && !leftIsDone)
                while (leftPtr != null) {
                    l = leftPtr.getData();
                    ans.data.addLast(new Term(l.getCoefficient(), l.getDegree()));
                    if (leftPtr == this.data.getLast()) {
                        leftIsDone = true; break;
                    }
                    leftPtr = leftPtr.getNext();
                }
            else if (leftIsDone && !rightIsDone)
                while (rightPtr != null) {
                    r = rightPtr.getData();
                    ans.data.addLast(new Term(r.getCoefficient(), r.getDegree()));
                    if (rightPtr == p.data.getLast()) {
                        rightIsDone = true; break;
                    }
                    rightPtr = rightPtr.getNext();
                }
        }
        catch (Exception ignored) {}
        clean(ans);
        return ans;
    }

    public PolynomialInterface subtract(PolynomialInterface p) {
        PolynomialInterface ans = new Polynomial();
        try {
            DNode<Term> leftPtr = this.data.getFirst();
            DNode<Term> rightPtr = p.data.getFirst();
            Term l,r;
            boolean leftIsDone = false, rightIsDone = false;

            while (leftPtr != null && rightPtr != null) {
                l = leftPtr.getData();
                r = rightPtr.getData();
                if (l.getDegree() > r.getDegree()) {
                    ans.data.addLast(new Term(l.getCoefficient(), l.getDegree()));
                    if (leftPtr == this.data.getLast()) {
                        leftIsDone = true;
                        break;
                    }
                    leftPtr = leftPtr.getNext();
                } else if (l.getDegree() == r.getDegree()) {
                    ans.data.addLast(new Term(l.getCoefficient() - r.getCoefficient(), l.getDegree()));
                    if (leftPtr == this.data.getLast()) leftIsDone = true;
                    else leftPtr = leftPtr.getNext();
                    if (rightPtr == p.data.getLast()) rightIsDone = true;
                    else rightPtr = rightPtr.getNext();
                    if (leftIsDone || rightIsDone) break;

                } else {
                    ans.data.addLast(new Term(r.getCoefficient() * -1.0, r.getDegree()));
                    if (rightPtr == p.data.getLast()) {
                        rightIsDone = true;
                        break;
                    }
                    rightPtr = rightPtr.getNext();
                }
            }
            if (rightIsDone && !leftIsDone)
                while (leftPtr != null) {
                    l = leftPtr.getData();
                    ans.data.addLast(new Term(l.getCoefficient(), l.getDegree()));
                    if (leftPtr == this.data.getLast()) {
                        leftIsDone = true;
                        break;
                    }
                    leftPtr = leftPtr.getNext();
                }
            else if (leftIsDone && !rightIsDone)
                while (rightPtr != null) {
                    r = rightPtr.getData();
                    ans.data.addLast(new Term(r.getCoefficient() * -1.0, r.getDegree()));
                    if (rightPtr == p.data.getLast()) {
                        rightIsDone = true;
                        break;
                    }
                    rightPtr = rightPtr.getNext();
                }
        }
        catch (Exception ignored) {}
        clean(ans);
        return ans;
    }

    public PolynomialInterface multiply(PolynomialInterface p) {
        PolynomialInterface ans = new Polynomial();
        try {
            DNode<Term> leftPtr = this.data.getFirst(), rightPtr = p.data.getFirst();
            Term l = leftPtr.getData(), r = rightPtr.getData();

            Term[] prod = new Term[l.getDegree() + r.getDegree() + 1]; //array indices correspond to term degree
            while (rightPtr != null) {
                r = rightPtr.getData();
                if (r.getCoefficient() != 0) {
                    while (leftPtr != null) {
                        l = leftPtr.getData();
                        if (l.getCoefficient() != 0) {
                            double coeff = l.getCoefficient() * r.getCoefficient();
                            int deg = l.getDegree() + r.getDegree();
                            if (prod[deg] == null) prod[deg] = new Term(coeff, deg);
                            else prod[deg].setCoefficient(prod[deg].getCoefficient() + coeff);
                        }
                        if (leftPtr == this.data.getLast()) break;
                        leftPtr = leftPtr.getNext();
                    }
                }
                leftPtr = this.data.getFirst();
                if (rightPtr == p.data.getLast()) break;
                rightPtr = rightPtr.getNext();
            }
            for (int i = prod.length - 1; i >= 0; --i)
                if (prod[i] != null && prod[i].getCoefficient() != 0) ans.data.addLast(prod[i]);
        }
        catch (Exception ignored) {  }
        clean(ans);
        return ans;
    }

    public PolynomialInterface divide(PolynomialInterface p) throws Exception {
        PolynomialInterface ans = new Polynomial();
            if (equalsZero(p)) throw new ArithmeticException();
            PolynomialInterface r = copy(this);
            DNode<Term> numPtr = r.data.getFirst(), denPtr = p.data.getFirst();

            // based on the Euclidean algorithm : n = dq + r
            while (!equalsZero(r) && numPtr.getData().getDegree() >= denPtr.getData().getDegree()) {
                double coeff = numPtr.getData().getCoefficient() / denPtr.getData().getCoefficient();
                int deg = numPtr.getData().getDegree() - denPtr.getData().getDegree();
                Term q = new Term(coeff, deg);
                ans.data.addLast(q);
                PolynomialInterface t = new Polynomial();
                t.data.addLast(q);
                PolynomialInterface td = p.multiply(t); // r = r - (t * d)
                r = r.subtract( td );
                clean(r);
                if (r.data.isEmpty()) break;
                else numPtr = r.data.getFirst();
            }
            clean(ans);
            return ans;
    }

    public PolynomialInterface remainder(PolynomialInterface p) throws Exception {
        PolynomialInterface ans = new Polynomial();
        try {
            if (equalsZero(p)) throw new ArithmeticException();
            PolynomialInterface r = copy(this);
            DNode<Term> numPtr = r.data.getFirst(), denPtr = p.data.getFirst();

            while (!(equalsZero(r)) && numPtr.getData().getDegree() >= denPtr.getData().getDegree()) {
                double coeff = numPtr.getData().getCoefficient() / denPtr.getData().getCoefficient();
                int deg = numPtr.getData().getDegree() - denPtr.getData().getDegree();
                Term q = new Term(coeff, deg);
                PolynomialInterface t = new Polynomial();
                t.data.addLast(q);
                PolynomialInterface td = p.multiply(t); // r = r - (t * d)
                r = r.subtract( td );
                clean(r);
                numPtr = r.data.getFirst();
            }
            ans = r;
        }
        catch (ArithmeticException e) { System.out.println("Cannot divide by zero!"); }
        catch (Exception e) { System.out.println("WARNING: Error in remainder operation."); }
        return ans;
    }

    private static boolean isValidTerm(String s) {
        String pattern = "(.*)[\\D&&[^-+*/^X]](.*)";
        return !(s.matches(pattern));
    }

    private static boolean equalsZero(PolynomialInterface p) throws Exception {
        if (p.data.isEmpty()) return true;
        DNode<Term> ptr = p.data.getFirst();
        while (ptr != null && ptr.getData().getCoefficient() == 0) {
            if (ptr == p.data.getLast()) return true;
            ptr = ptr.getNext();
        }
        return false;
    }

    private static PolynomialInterface copy(PolynomialInterface p) throws Exception {
        PolynomialInterface copy = new Polynomial();
        DNode<Term> ptr = p.data.getFirst();
        while (ptr != null) {
            copy.data.addLast(ptr.getData());
            if (ptr == p.data.getLast()) break;
            ptr = ptr.getNext();
        }
        return copy;
    }

    /****************
     * MAIN FUNCTION
     ****************/
    public static void main(String args[]) throws Exception {
        PolynomialInterface p, q;
        p = new Polynomial ("x^3 - x^2 + x + 4");
        q = new Polynomial("x^3 + x^2 + x - 6");
        System.out.println("Default Input\n***************************");
        Utility.run(p, q);
    }
}