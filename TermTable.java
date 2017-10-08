/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qm;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Mark_2
 */
public class TermTable {

    private List<Term> terms;
    private List<TermGroup> groups;
    public List<Function> functions = new ArrayList();
    public boolean pos;

    private int length;
    private int depth;

    public TermTable(int length, int depth) {
        terms = new ArrayList();
        groups = new ArrayList();

        this.length = length;
        this.depth = depth;

        for (int i = 0; i < length + 1 - depth; i++) {
            groups.add(new TermGroup());
        }
    }

    public TermTable() {
        terms = new ArrayList();
        groups = new ArrayList();

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number of variables: ");
        this.length = sc.nextInt();
//        this.length = 4;
        this.depth = 0;

        for (int i = 0; i < length + 1 - depth; i++) {
            groups.add(new TermGroup());
        }

        int numTerms = (int) Math.pow(2, length);
        for (int i = 0; i < numTerms; i++) {
            terms.add(new Term(length, i));
        }

        makeFunctions();
    }

    public boolean isPos() {
        return pos;
    }

    public void setPos(boolean pos) {
        this.pos = pos;
    }

    private void makeFunct(int function, List<Integer> values, List<Integer> donts, boolean pos) {
        List<Integer> usedValues;

        if (this.pos == pos) {
            usedValues = values;
        } else {
            usedValues = new ArrayList();
            for (int i = 0; i < Math.pow(2, length); i++) {
                boolean used = false;
                for (int value : values) {
                    if (i == value) {
                        used = true;
                    }
                }
                for (int dont : donts) {
                    if (i == dont) {
                        used = true;
                    }
                }
                if (!used) {
                    usedValues.add(i);
                }
            }
        }

        functions.add(new Function(usedValues));

        for (int value : usedValues) {
            terms.get(value).addFunction(new FunctionMember(function, false));
        }

        for (int dont : donts) {
            terms.get(dont).addFunction(new FunctionMember(function, true));
        }
    }

    public List<Function> getFunctions() {
        return functions;
    }

    public List<Term> getTerms() {
        return terms;
    }

    private void makeFunctions() {
        List<List<Integer>> dos = new ArrayList();
        List<List<Integer>> donts = new ArrayList();
        List<Boolean> posForFunctions = new ArrayList();
        Scanner sc = new Scanner(System.in);

        String makeFunct = "";
        do {
            dos.add(new ArrayList());
            donts.add(new ArrayList());
            System.out.println("Enter 'false' for SOP or 'true' for POS: ");
            posForFunctions.add(sc.nextBoolean());
            int addTerm = -1;
            do {
                System.out.println("Enter terms or -1 to move on to donts: ");
                addTerm = sc.nextInt();
                if (addTerm != -1) {
                    dos.get(dos.size() - 1).add(addTerm);
                }
            } while (addTerm != -1);

            do {
                System.out.println("Enter dont or -1 to exit: ");
                addTerm = sc.nextInt();
                if (addTerm != -1) {
                    donts.get(donts.size() - 1).add(addTerm);
                }
            } while (addTerm != -1);

            System.out.print("Make another function? Enter 'yes' or 'no': ");
            sc.nextLine();
            makeFunct = sc.nextLine();
        } while (makeFunct.equals("yes"));

        System.out.println("Enter 'true' to display output as POS or 'false' to display as SOP: ");
        pos = sc.nextBoolean();

        for (int i = 0; i < dos.size(); i++) {
            makeFunct(i, dos.get(i), donts.get(i), posForFunctions.get(i));
        }

//        List<Integer> f1do = new ArrayList();
//        List<Integer> f1dont = new ArrayList();
//
//        f1do.add(3);
//        f1do.add(4);
//        f1do.add(8);
//        f1do.add(9);
//        f1do.add(13);
//        f1do.add(14);
//
//        f1dont.add(12);
//        f1dont.add(15);
//
//        makeFunct(0, f1do, f1dont, false);
//
//        List<Integer> f2do = new ArrayList();
//        List<Integer> f2dont = new ArrayList();
//
//        f2do.add(5);
//        f2do.add(8);
//        f2do.add(9);
//        f2do.add(10);
//        f2do.add(13);
//        f2do.add(14);
//
//        f2dont.add(0);
//        f2dont.add(4);
//        f2dont.add(6);
//
//        makeFunct(1, f2do, f2dont, false);

//        List<Integer> f3do = new ArrayList();
//        List<Integer> f3dont = new ArrayList();
//        
//        f3do.add(2);
//        f3do.add(7);
//        f3do.add(8);
//        
//        f3dont.add(0);
//        f3dont.add(5);
//        f3dont.add(13);
//        
//        makeFunct(2, f3do, f3dont);
        
        for (Term term : terms) {
            if (term.isUsed()) {
                String str = "" + term.getBytes();
                int ones = 0;
                for (int i = 0; i < str.length(); i++) {
                    if (str.charAt(i) == '1') {
                        ones++;
                    }
                }
                groups.get(ones).addTerm(term);
            }
        }
    }

    public String toString() {
        String ret = "";

        for (TermGroup group : groups) {
            boolean empty = true;
            for (Term term : group.getTerms()) {
                ret += term + "\n";
                empty = false;
            }
            if (!empty) {
                for (int i = 0; i < (3 * length + 10); i++) {
                    ret += "-";
                }
                ret += "\n";
            }
        }
        return ret;
    }

    public void addTermToGroup(Term term, int group) {
        groups.get(group).addTerm(term);
    }

    public TermGroup getGroup(int index) {
        return groups.get(index);
    }

    private void simplifyGroup(TermTable newTable, int index) {
        TermGroup group1 = groups.get(index);
        TermGroup group2 = groups.get(index + 1);
        for (int x = 0; x < group1.getSize(); x++) {
            Term term1 = group1.getTerm(x);
            for (int y = 0; y < group2.getSize(); y++) {
                Term term2 = group2.getTerm(y);
                Term newTerm = compareBytes(term1, term2);
                if (newTerm != null) {
                    TermGroup newGroup = newTable.getGroup(index);
                    boolean exists = false;
                    for (Term t : newGroup.getTerms()) {
                        if (newTerm.compareTerms(t)) {
                            exists = true;
                        }
                    }
                    if (!exists) {
                        newGroup.addTerm(newTerm);
                    }
                }
            }
        }
    }

    public List<TermGroup> getGroups() {
        return groups;
    }

    private Term compareBytes(Term t1, Term t2) {
        Term ret = null;

        List<FunctionMember> sharedFunctions = compareFunctions(t1, t2);

        if (sharedFunctions.size() > 0) {

            //checks of function if all terms are taken
//            if (sharedFunctions.size() == t1.getFunctions().size()) {
//                boolean check = true;
//                for (int i = 0; i < sharedFunctions.size(); i++) {
//                    if (!t1.getFunction(i).equals(sharedFunctions.get(i))) {
//                        check = false;
//                    }
//                }
//                if (check) {
//                    t1.check();
//                }
//            }
            //checks if term can be make and makes term
            int changedNum = 0;
            String newBytes = "";
            for (int i = 0; i < length; i++) {
                if (t1.getBytes().charAt(i) == t2.getBytes().charAt(i)) {
                    newBytes += t1.getBytes().charAt(i);
                } else {
                    newBytes += "-";
                    changedNum++;
                }
            }

            if (changedNum == 1) {

                if (t1.compareFunctions(sharedFunctions)) {
                    t1.check();
                }

                if (t2.compareFunctions(sharedFunctions)) {
                    t2.check();
                }

                List<Integer> combined = new ArrayList(t1.getTerms());
                combined.addAll(t2.getTerms());
                ret = new Term(length, newBytes, sharedFunctions, combined);
            }
        }
        return ret;
    }

    private List<FunctionMember> compareFunctions(Term t1, Term t2) {
        List<FunctionMember> ret = new ArrayList();
        for (FunctionMember f1 : t1.getFunctions()) {
            for (FunctionMember f2 : t2.getFunctions()) {
                if (f1.getFunction() == f2.getFunction()) {
                    if (f1.isDont() && f2.isDont()) {
                        ret.add(new FunctionMember(f1.getFunction(), true));
                    } else {
                        ret.add(new FunctionMember(f1.getFunction(), false));
                    }
                }
            }
        }
        return ret;
    }

    public boolean isEmpty() {
        for (TermGroup t : groups) {
            if (t.getSize() != 0) {
                return false;
            }
        }
        return true;
    }

    public List<TermTable> simplify() {
        boolean complete = true;

        List<TermTable> ret = new ArrayList();

        ret.add(this);
        TermTable simplified = new TermTable(length, depth + 1);

        simplified.setPos(pos);

        for (int i = 0; i < groups.size() - 1; i++) {
            simplifyGroup(simplified, i);
        }

        ret.add(simplified);

        return ret;
    }
}
