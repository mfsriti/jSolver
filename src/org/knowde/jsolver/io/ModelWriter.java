/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.knowde.jsolver.io;

import java.io.IOException;
import org.knowde.jsolver.core.Model;
import org.knowde.jsolver.util.FileHandler;

/**
 *
 * @author msriti
 */
public class ModelWriter extends FileHandler{
    
    private Model mModel;

    public ModelWriter(Model model, String outputFilePath) throws IOException{
       super(outputFilePath, false);
       setModel(model);
    }

    public void write() throws IOException{
        writeLine(mModel.toString());
        finalizeWrite();
        System.out.println("ModelWriter.write(): writing file " + getFileName());
    }

    private void setModel(Model model) {
        mModel = model;
    }

    private void finalizeWrite() throws IOException {
      close();
    }
    
}
