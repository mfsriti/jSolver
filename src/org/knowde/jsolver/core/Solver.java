/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
/**
 *
 * @author msriti
 */
public class Solver {
    
    static Solver sSolver;
    
    Problem mProblem;
    
    /**
     * Contains the selected variables.
     */
    Stack<Literal> mStack;
    
    //int mUnselectedVariablesNumber;
   
    //List<Variable> mUnselectedVariables;
    
    Model mCurrentModel;
    
    List<Constraint> mSelectedConstraints;
    
    public static Solver getInstance(){
        return sSolver;
    }
    
    protected Solver(Problem p){
        sSolver = this;
        mProblem = p;
        mStack = new Stack();
        mCurrentModel = null;
        mSelectedConstraints = new ArrayList<>();
       // mUnselectedVariables = Arrays.asList(mProblem.getVariables());
    }
    
    public Model solve(){
        long startTime = System.currentTimeMillis();
        Literal currentLiteral = selectLiteral(); int counter = 0; int solCounter = 0;
        //System.out.println("Normal: "+currentPath);
        while (!mStack.empty()){ // same as: while (currentPath != null)
            //System.err.println(++counter+" "+currentPath);
            List<Constraint> list = currentLiteral.satisfyConstraints();
            if (list!=null){
                for (Constraint con: list){
                    mSelectedConstraints.add(con);
                }
            }
            if (mProblem.solved()){
               long stopTime = System.currentTimeMillis();
               saveCurrentModel(++solCounter, stopTime-startTime, false, false);
               currentLiteral = backtrack(false);// to find the next solution
               //System.out.println("Backtrack: "+currentPath);
               break;
            } else {
                boolean wellDecremented = currentLiteral.decrementInverseSatisfiedContraints();
                if (wellDecremented) {
                    currentLiteral = selectLiteral(); // we select another path for the next loop iteration
                    //System.out.println("Normal: "+currentPath);
                } else { 
                    currentLiteral = backtrack(false); // we don't to increment because in this case the decrement function do the job
                    //System.out.println("Backtrack: "+currentPath);
                }
            }
        }
        
        if (solCounter==0)
            alertProblemWithoutModel();
        
        return mCurrentModel;
     }

    protected Literal backtrack(boolean withIncrement){
        //System.out.println("backtrack");
        Literal literal = null;
        if (!mStack.empty()){
            literal = mStack.pop();
            List<Constraint> list = literal.unsatisfyConstraints();
            if (list!=null){
                for (Constraint con: list){
                    mSelectedConstraints.remove(con);
                }
            }
            if (withIncrement){
                literal.incrementInverseSatisfiedContraints();
            }
            Variable node = literal.getVariable();
            literal = (literal.isInverseLiteralSelected() ? null: literal.getInverseLiteral());
            if (literal==null) {// means that the node is explored 
                node.unselectLiterals();
                literal = backtrack(true);
            } 
            if (literal!=null && (mStack.empty() || !literal.equals(mStack.peek()))){
                mStack.push(literal);
            }
        }
        return literal;
    }
    
    protected Literal selectLiteral(){
        Literal literal = mProblem.selectLiteral();
        literal.select();
        
        List<Constraint> list = literal.getInverseSatisfiedContraints();
        for (Constraint con: list){
            if (con.isProcessable()){ // node processing deduction
                literal.getVariable().selectLiterals();
                break;
            }    
        }
        
        mStack.push(literal);
           
        return literal;
    }
  
    private void saveCurrentModel(int solCounter, long time, boolean print, boolean check){
        int []lits = new int[mStack.size()];
        int litsSolCounter = 0;
        for (int i=0; i<lits.length; i++){
            Literal lit = mStack.get(i);
           // if (lit.gotConstraintsToSatisfy() && lit.hasSatisfiedConstraint())
                lits[litsSolCounter++] = lit.getValue();
        }
        int []litsSol = new int[litsSolCounter];
        System.arraycopy(lits, 0, litsSol, 0, litsSolCounter);
        Arrays.sort(litsSol);
        Model model = new Model(mProblem.getName(), litsSol);
        model.setTime(time);
        //@TODO: compare the solution, may be implemented int PrefSolver
        mCurrentModel = model;
        
        if (print){
            System.out.print("Problem \""+mProblem.getName()+"\": model "+solCounter+" => ");
            System.out.println(model);
        }
        
        if (check){
            //check the satisfied constraint only, because we are supposed to find them all satisfied
            boolean checked = true;
            System.out.println("Constraints size: " + mSelectedConstraints.size());
            for (Constraint c: mSelectedConstraints){
                boolean b = false;
                for (int v: litsSol){
                    if (c.contains(v)){
                        b = true;
                        break;
                    }
                }
                checked = checked && b;
                if (!checked)
                    break;
            }
            if (!checked){
                System.out.println("-- Invalid model");
            } else {
                System.out.println("-- Valid model");
            }
        }
    }
    
    private void alertProblemWithoutModel(){
        System.out.println("Problem \""+mProblem.getName()+"\": No model found.");
    }
    
}
