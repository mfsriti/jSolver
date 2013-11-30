/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.knowde.jsolver.io;

import java.io.IOException;
import org.knowde.jsolver.core.Solution;
import org.knowde.jsolver.util.FileHandler;

/**
 *
 * @author msriti
 */
public class SolutionWriter extends FileHandler{
    
    private Solution mSolution;

    public SolutionWriter(Solution solution, String outputFilePath) throws IOException{
       super(outputFilePath, false);
       setSolution(solution);
    }

    public void write() throws IOException{
        writeLine(mSolution.toString());
        finalizeWrite();
        System.out.println("SolutionWriter.write(): writing file " + getFileName());
    }

    private void setSolution(Solution solution) {
        mSolution = solution;
    }

    private void finalizeWrite() throws IOException {
      close();
    }
    
}
