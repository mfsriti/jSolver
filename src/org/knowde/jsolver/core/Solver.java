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
    
    int mUnselectedVariablesNumber;
   
    Variable [] mUnselectedVariables;
    
    Solution mCurrentSolution;
    
    List<Constraint> mSelectedConstraints;
    
    public static Solver getInstance(){
        return sSolver;
    }
    
    protected Solver(Problem p){
        sSolver = this;
        mProblem = p;
        mStack = new Stack();
        mCurrentSolution = null;
        mSelectedConstraints = new ArrayList<>();
        setRemainingVariables(mProblem.getVariables());
    }
  
    private void setRemainingVariables(Variable [] rvars){
      mUnselectedVariablesNumber = rvars.length;
      mUnselectedVariables = new Variable[mUnselectedVariablesNumber];
      //mRemainingVariables = rvars.values().toArray(new Variable[mRemainingVariablesNumber]);
      System.arraycopy(rvars, 0, mUnselectedVariables, 0, mUnselectedVariablesNumber);
    }

    public Solution solve(){
        Literal currentPath = selectPath(); int counter = 0; int solCounter = 0;
        //System.out.println("Normal: "+currentPath);
        while (!mStack.empty()){ // same as: while (currentPath != null)
            //System.err.println(++counter+" "+currentPath);
            List<Constraint> list = currentPath.satisfyConstraints();
            if (list!=null){
                for (Constraint con: list){
                    mSelectedConstraints.add(con);
                }
            }
            if (mProblem.solved()){
               saveCurrentSolution(++solCounter, true, false);
               currentPath = backtrack(false);// to find the next solution
               //System.out.println("Backtrack: "+currentPath);
            } else {
                boolean wellDecremented = currentPath.decrementInverseSatisfiedContraints();
                if (wellDecremented) {
                    currentPath = selectPath(); // we select another path for the next loop iteration
                    //System.out.println("Normal: "+currentPath);
                } else { 
                    currentPath = backtrack(false); // we don't to increment because in this case the decrement function do the job
                    //System.out.println("Backtrack: "+currentPath);
                }
            }
        }
        
        if (solCounter==0)
            alertProblemWithoutSolution();
        
        return mCurrentSolution;
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
            literal = selectLiteral(node);
            if (literal==null) {// means that the node is explored 
                unselectVariable(node);
                literal = backtrack(true);
            } 
            if (literal!=null && (mStack.empty() || !literal.equals(mStack.peek()))){
                mStack.push(literal);
            }
        }
        return literal;
    }
    
    protected Literal selectPath(){
        Constraint con = selectConstraint();
        Literal literal = selectPath(con.getRemainingLiteral());
        
        //System.out.println("con="+con+", len:"+con.getLength());
        literal.select();
        if (con.isProcessable()){ // node processing deduction
            literal.getVariable().selectLiterals();
        } 
        return literal;
    }
    
    private Literal selectPath(Literal literal) {
        Variable node = literal.getVariable();
        node = selectVariable(node);
        if (node==null){
            literal = backtrack(true);
        } else {
            mStack.push(literal);
        }
        return literal;   
    }

    /**
     *  Select a random variable and decrement the number of the remaining variables.
       * @return 
     */
    protected Variable selectVariable(){
       Variable selected = null;
       if (mUnselectedVariablesNumber!=0)
           selected = mUnselectedVariables[--mUnselectedVariablesNumber];
       return selected;
    }
    
    protected Variable selectVariable(Variable variable){
        switch (mUnselectedVariablesNumber){
            case 0: variable = null; break;
            case 1: --mUnselectedVariablesNumber; break;
            default: 
                for (int i=0; i<mUnselectedVariablesNumber; i++){
                    if (mUnselectedVariables[i].getValue() == variable.getValue()){
                        mUnselectedVariables[i] = mUnselectedVariables[--mUnselectedVariablesNumber];
                    }
                }
        }
        return variable;
    }
    
    protected void unselectVariable(Variable var){
        var.unselectLiterals();
        mUnselectedVariables[mUnselectedVariablesNumber++] = var;
    }

    protected Literal selectLiteral(Variable node){
        Literal selected = null;
        if (!node.explored()) {
            if (node.isPositiveLiteralSelected())  {
                node.selectNegativeLiteral();
                selected = node.getNegativeLiteral();
            } else {
                node.selectPositiveLiteral();
                selected = node.getPositiveLiteral();
            }
        }
        
        return selected;
    }
  
    private void saveCurrentSolution(int solCounter, boolean print, boolean check){
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
        Solution sol = new Solution(mProblem.getName(), litsSol);
        
        //@TODO: compare the solution, may be implemented int PrefSolver
        mCurrentSolution = sol;
        
        if (print){
            System.out.print("Problem \""+mProblem.getName()+"\": solution "+solCounter+" => ");
            System.out.println(sol);
        }
        
        if (check){
            //check the satisfied constraint only, because we are supposed to find them all satisfied
            boolean checked = true;
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
                System.out.println("-- Invalid solution");
            } else {
                System.out.println("-- Valid solution");
            }
        }
    }
    
    private void alertProblemWithoutSolution(){
        System.out.println("Problem \""+mProblem.getName()+"\": No solution found.");
    }

    private Constraint selectConstraint() {
        return mProblem.getSmallestConstraint();
    }
    
}
