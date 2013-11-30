/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver;

import java.io.IOException;
import org.knowde.jsolver.eva.EvaluationManager;

/**
 *
 * @author msriti
 */
public class EvaluationManagerTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        EvaluationManager em = new EvaluationManager("D:\\test\\jSolver\\small sat\\problems", "D:\\test\\jSolver\\small sat\\solutions");
        em.process();
    }
}
