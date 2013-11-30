package org.knowde.jsolver.core;

/**
 *
 * @author msriti
 */
public class Constraint extends Object{
    
    int mLength; 
    
    Literal mSatisfiedBy;
    
    Literal [] mLiterals;
    
    Problem mProblem;
    
    public final static String CONSTRAINT_PREFIX = "con_";
    
    public Constraint(Problem problem, int[] literals){
        mLength = 0;
        mProblem = problem;
        mSatisfiedBy = null;
        setLiterals(literals);
    }
    
    public int getLength(){
        return mLength;
    }
    
    /**
     * Indicates that if the constraint is not satisfied by any selected literal
     * @return true if the length of the constraint is equal to zero
     */
    public boolean isUnderloaded(){
        return (mLength == 0);
    }
    
    /**
     * Indicates that if it is possible to perform a node processing
     * @return true if the lenght of the constraint is equal to zero 
     */
    public boolean isProcessable(){
        return (mLength == 1);
    }
    
    /**
     * Decrements the length of the constraint logically.
     */
    public void decrement(){
        mProblem.removeConstraint(this);
        --mLength;
        mProblem.addConstraint(this);
       
    }
    
    public void increment(){
        mProblem.removeConstraint(this);
        ++mLength;
        mProblem.addConstraint(this);
    }
    
    private void setLiterals(int[] literals){
      mLiterals = new Literal[literals.length];
      for (int i=0; i<literals.length; i++){
          int number = literals[i];
          Literal lit = mProblem.findLiteral(number);
          lit.addSatisfiedConstraint(this);
          mLiterals[i] = lit;
          lit = mProblem.findLiteral(number*-1);
          lit.addInverseSatisfiedConstraint(this);
          mLength++;
      }
    }

    public String toString() {
        String str="";
        for (Literal literal : mLiterals) {
            str += literal + " ";
        }
        return str;
    }
    
    public boolean equals(Constraint o){
        return toString().equals(o.toString());
    }
    
    private void setSatisfiedBy(Literal lit){
        mSatisfiedBy = lit;
    }
    
    public Literal getSatisfiedBy(){
        return mSatisfiedBy;
    }
    
    public boolean isSatisfied(){
        return (mSatisfiedBy != null);
    }
    
    public void satisfy(Literal lit){
        setSatisfiedBy(lit);
        mProblem.removeConstraint(this);
    }
    
    public void unsatisfy(){
        setSatisfiedBy(null);
        mProblem.addConstraint(this);
    }

    /**
     * Used for node processing
     * @return the last literal that is not selected yet, if contraint's length is 1
     */
    public Literal getRemainingLiteral() {
        Literal result = null;
        for (Literal l: mLiterals){
            if(!l.isInverseLiteralSelected()){
                result = l;
                break;
            }
        }
        return result;
    }
    
    public boolean contains(int value){
        for(Literal l: mLiterals){
            if(l.getValue()==value)
                return true;
        }
        return false;
    }
    
    public Literal[] getLiterals(){
        return mLiterals;
    }

}