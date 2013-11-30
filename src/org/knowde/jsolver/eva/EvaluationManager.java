/*
 * Copyright (C) 2013 msriti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.knowde.jsolver.eva;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.knowde.jsolver.core.Model;
import org.knowde.jsolver.core.Solver;
import org.knowde.jsolver.core.SolverFactory;
import org.knowde.jsolver.io.ProblemReader;
import org.knowde.jsolver.io.ModelWriter;
import org.knowde.jsolver.util.FileHandler;

/**
 *
 * @author msriti
 */
public class EvaluationManager {
   
    String mProblemsDir;
    
    String mModelsDir;
    
    private final static String PROBLEM_SHORT_DIR_NAME = "problems";
    private final static String MODEL_SHORT_DIR_NAME = "models";
    private final static String MODEL_FILE_EXT = "mod";
    
    public EvaluationManager(String sourceDir, String targetDir){
        setProblemsDir(sourceDir);
        setModelsDir(targetDir);
    }
    
    public void process() throws IOException {
            List<File> files = new FileHandler(mProblemsDir).listChildren(true);
            Iterator<File> it = files.iterator();
            while(it.hasNext()){
                ProblemReader pr = new ProblemReader((File)it.next());
                Solver solver = SolverFactory.getSolver(pr.getProblem());
                Model model = solver.solve();
                String outputFile = pr.getFilePath().replace(PROBLEM_SHORT_DIR_NAME, MODEL_SHORT_DIR_NAME)+"."+MODEL_FILE_EXT;
                ModelWriter sw = new ModelWriter(model, outputFile);
                sw.write();
                System.out.println("Time: "+ model.getTime());
            }
    }

    private void setProblemsDir(String sourceDir) {
       mProblemsDir = sourceDir;
    }

    private void setModelsDir(String targetDir) {
       mModelsDir = targetDir;
    }
}
