/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mark_2
 */
public class QM {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TermTable t = new TermTable();
        List<TermTable> termTables = new ArrayList();
        termTables.add(t);
        printTables(termTables);
        simplify(termTables);

    }

    public static void simplify(List<TermTable> t) {
        List<TermTable> newTerms = t.get(t.size() - 1).simplify();
        if (!newTerms.get(1).isEmpty()) {
            t.remove(t.size() - 1);
            t.addAll(newTerms);
            printTables(t);
            simplify(t);
        } else {
            List<TermGroup> implicants = new ArrayList();
            List<Function> functions = new ArrayList();
            for (int i = t.size() - 1; i >= 0; i--) {
                TermGroup group = new TermGroup();
                TermTable table = t.get(i);

                for (TermGroup termGroup : table.getGroups()) {
                    for(Term term: termGroup.getTerms()){
                        if(!term.isChecked())
                            group.addTerm(term);
                    }
                }

                implicants.add(group);
                functions = table.getFunctions();
            }
            ImplicantTable implicantTable = new ImplicantTable(functions, implicants, t.get(0).pos);
            implicantTable.printTable();
            implicantTable.simplifyTable();

            implicantTable.printFinalFunctions();
        }
    }

    public static void printTables(List<TermTable> tables) {
        int counter = 1;
        for (TermTable t : tables) {
            System.out.println("Table" + counter);
            System.out.println(t);
            counter++;
        }
        System.out.println("/////////////////////////////////////////////////////////////////\n");
    }

}
