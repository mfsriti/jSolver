/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver;

import java.io.IOException;
import org.knowde.jsolver.core.Problem;
import org.knowde.jsolver.core.ProblemFactory;
import org.knowde.jsolver.core.Model;
import org.knowde.jsolver.core.Solver;
import org.knowde.jsolver.core.SolverFactory;
import org.knowde.jsolver.io.ProblemReader;

/**
 *
 * @author msriti
 */
public class JSolver {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Problem problem;
        Solver solver;
        
        problem = ProblemFactory.getProblem("Problem1.txt");
        problem.setVariables(new int[]{1,2,3});
        problem.addConstraint(new int[]{-1,-2,-3});
        problem.addConstraint(new int[]{-1,-2});
        solver = SolverFactory.getSolver(problem);
        solver.solve();        
        
        problem = ProblemFactory.getProblem("Problem2.txt");
        problem.setVariables(new int[]{1,2});
        problem.addConstraint(new int[]{1,2});
        problem.addConstraint(new int[]{-1,-2});
        problem.addConstraint(new int[]{-1,2});
        problem.addConstraint(new int[]{1,-2});
        solver =  SolverFactory.getSolver(problem);
        solver.solve();
        
        problem =  ProblemFactory.getProblem("Problem3.txt");
        problem.setVariables(new int[]{1,2,3});
        problem.addConstraint(new int[]{1,2,3});
        problem.addConstraint(new int[]{-1,2,3});
        problem.addConstraint(new int[]{1,-2,3});
        problem.addConstraint(new int[]{1,2,-3});
        problem.addConstraint(new int[]{-1,-2,3});
        problem.addConstraint(new int[]{1,-2,-3});
        problem.addConstraint(new int[]{-1,2,-3});
        //problem.addConstraint(new int[]{-1,-2,-3});
        solver = SolverFactory.getSolver(problem);
        solver.solve();
        
        //ProblemReader pr = new ProblemReader("D:\\test\\jSolver\\problems\\Random_Benchmarks\\Transitive_reduction\\Rnd-200var-0-800-cl_TR_all\\gen-200var-0-800-cl-0.cnf_graphs-v50-d0.03912-n10_1_tr.dat.noptsat");
       // ProblemReader pr = new ProblemReader("D:\\Hachemi_CSP_ImamU\\data\\Structured_Benchmarks\\MAXSAT\\BMC\\barrel3.dimacs.satpref");
        //ProblemReader pr = new ProblemReader("D:\\Hachemi_CSP_ImamU\\data\\Structured_Benchmarks\\MINONE\\2bitcomp_5.cnf.satpref");
        ProblemReader pr = new ProblemReader("C:\\users\\msriti\\Dropbox\\research\\hachemi\\gen-200var-0-800-cl-0.cnf_graphs-v50-d0.03912-n10_1_tc.dat.noptsat");
        
        solver = SolverFactory.getSolver(pr.getProblem());
        long startTime = System.currentTimeMillis();
        Model s = solver.solve();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Model: "+ s);
        System.out.println("Time: "+ elapsedTime);
    }
}
