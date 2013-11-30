/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.knowde.jsolver.io;

import java.io.File;
import java.io.IOException;
import org.knowde.jsolver.core.Problem;
import org.knowde.jsolver.core.ProblemFactory;
import org.knowde.jsolver.util.FileHandler;

/**
 *
 * @author msriti
 */
public class ProblemReader extends FileHandler {
    
    Problem mProblem;
    
    public ProblemReader(File problemFile) throws IOException{
        super(problemFile, true);
        setProblem(ProblemFactory.getProblem(getFileName()));
        parseProblemFile();
    }
    
    public ProblemReader(String filePath) throws IOException{
        this(new File(filePath));
    }

    private void setProblem(Problem problem) {
        mProblem = problem;
    }
    
    public Problem getProblem() {
        return mProblem;
    }

    private void parseProblemFile() throws IOException{
        System.out.println("ProblemReader.parseProblemFile(): processing file " + getFileName());
        String currentLine = null;
	while ((currentLine = nextLine()) != null ) {
		if (!currentLine.isEmpty() && !currentLine.trim().equals("") && !currentLine.trim().equals("\n")){
                    parseLine(currentLine);
                }
	}
	finalizeRead();
    }

    private void parseLine(String currentLine) {
        switch(currentLine.charAt(0)){
            case 'c':
                if ( currentLine.equals("c") || currentLine.equals("c preferences") ){
                    break; // ignore these two lines
                } else if (currentLine.startsWith("c S")){// c S 200 50
                    //readProblemVariables(currentLine.split(" "));
		} else { // here it remains only the list of pref line: "c 84<-29, 135<-29, -169<-185, 82<16, ..."
                    // readPreferences();
                }
		break;
            case 'p': // line.startsWith("p cnf"): p cnf 200 800 
                // nothing to do for the moment
                 readProblemVariables(currentLine.split(" "));
                break;
            case '-': case '1': case '2': case '3': case '4': 
            case '5': case '6':	case '7': case '8': case '9':
                readConstraint(currentLine.split(" "));	
		break;
            default: ;
        }
    }

    private void readProblemVariables(String[] splittedLine) {
        int variableNumber = Integer.parseInt(splittedLine[2]); 
        //int prefNumber = Integer.parseInt(splittedLine[3]); // ????
        if (variableNumber>0){
            int[] variables = new int[variableNumber];
            for (int i=0; i<variableNumber; i++){
                variables[i] = i+1;
            }
            mProblem.setVariables(variables);
        }
    }

    private void readConstraint(String[] splittedLine) {
        if (splittedLine.length > 1){
            int constraintLiterals[] = new int [splittedLine.length-1];
            for (int i=0; i<splittedLine.length-1; i++){
                constraintLiterals[i] = Integer.parseInt(splittedLine[i]);
            }
            mProblem.addConstraint(constraintLiterals);
        }
    }

    private void finalizeRead() throws IOException {
        close();
    }
}