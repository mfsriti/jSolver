/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver.core;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author msriti
 */
public class Literal extends Object {
    
    // The value of the literal
    private int mValue;
    
    // The variable on which the literal is related
    private Variable mVariable;
    
    // Indicates if the literal was treated or not (SOLVING)
    boolean mSelected;
    
    // The list of the constraints where the literal appears
    List<Constraint> mSatisfiedContraints;
    //Map<String,Constraint> mSatisfiedContraints;
    
    // The list of the constraints where the literal appears in inverse sign
    List<Constraint> mInverseSatisfiedContraints;
    //Map<String,Constraint> mInverseSatisfiedContraints;
    
    int mCounter;
    
    public Literal(Variable v, int value){
        setVariable(v);
        setValue(value);
        setSelected(false);
        resetCounter();
        mSatisfiedContraints = new ArrayList<>();
        mInverseSatisfiedContraints = new ArrayList<>();
    }

    /**
     * @return the mValue
     */
    public int getValue() {
        return mValue;
    }

    /**
     * @param mValue the mValue to set
     */
    private void setValue(int mValue) {
        this.mValue = mValue;
    }
    
    public void addSatisfiedConstraint(Constraint constraint){
        mSatisfiedContraints.add(constraint);
    }
    
    public void addInverseSatisfiedConstraint(Constraint constraint){
        mInverseSatisfiedContraints.add(constraint);
    }
    
    public List satisfyConstraints(){
        List<Constraint> list = null;
        if (!mSatisfiedContraints.isEmpty()){
            list = new ArrayList<>();
            for (Constraint value : mSatisfiedContraints) {    
                if (!value.isSatisfied()) {
                    value.satisfy(this);
                    list.add(value);
                }
            }
        }
        return list;
    }
    
    /**
     *
     * @return
     */
    public List unsatisfyConstraints(){
        List<Constraint> list = null;
        if (!mSatisfiedContraints.isEmpty()){
            list = new ArrayList<>();
            for (Constraint value : mSatisfiedContraints) {
                if (value.getSatisfiedBy().equals(this)){
                    value.unsatisfy();
                    list.add(value);
                }
            }
        }
        return list;
    }
    
    public boolean equals(Literal o){
        return (o!=null && getValue() == o.getValue());
    }
    
    public Variable getVariable(){
        return mVariable;
    }
    
    private void setVariable(Variable v){
        mVariable = v;
    }
    
    /**
     * Decrement all the constraints length where the literal appears in inverse
     * value (sign)
     * @return - null if the decrementation is well done,
     *         - a constraint of length zero if the decrementation was cancelled
     *           and the returned constraint caused the cancellation
     *         - a constraint of length one if the decrementation is well done 
     *           and the returned constraint could be used for an eventual node 
     *           processing 
     */
    public boolean decrementInverseSatisfiedContraints(){
        boolean underloaded = false;
        boolean result = true;
        ListIterator<Constraint> it = mInverseSatisfiedContraints.listIterator();
        
        while (it.hasNext()){
            Constraint c = it.next();
            if (!c.isSatisfied()){
                c.decrement();
                if (c.isUnderloaded()){
                    underloaded = true;
                    result = false;
                    break;
                } 
            }
        }
        
        if (underloaded){
            //it.previous();
            while (it.hasPrevious()){
                Constraint c = it.previous();
                if (!c.isSatisfied()){
                    c.increment();
                }
            }
        }
        
        return result;
    }
    
    public void incrementInverseSatisfiedContraints(){
        for (Constraint c : mInverseSatisfiedContraints) {
            if (!c.isSatisfied()){
                c.increment();
            }
        }
    }
    
    @Override
    public String toString (){
        return Integer.toString(getValue());
    }
    
    /**
     * Check whether the constraints list related to the literal is empty or not
     * @return 
     */
    public boolean gotConstraintsToSatisfy(){
        return (! mSatisfiedContraints.isEmpty());
    }
    
    /*public boolean isAllConstraintsSatisfied(){
        for (Constraint value : mSatisfiedContraints) {
            if (!value.isSatisfied()) {
                return false;
            }
        }
        return true;
    }
    
     public boolean hasSatisfiedConstraint(){
        for (Constraint value : mSatisfiedContraints) {
            if (value.isSatisfied() && value.getSatisfiedBy().equals(this)) {
                return true;
            }
        }
        return false;
    }
    */
    private void setSelected(boolean sel){
        mSelected = sel;
    }
    
    public void select(){
        setSelected(true);
    }
    
    public void unselect(){
        setSelected(false);
    }
    
    public boolean isSelected(){
        return mSelected;
    }

    public boolean isInverseLiteralSelected() {
       return getInverseLiteral().isSelected();
    }
    
    public Literal getInverseLiteral(){
        return (mVariable.getNegativeLiteral().equals(this) ? 
                mVariable.getPositiveLiteral() : 
                mVariable.getNegativeLiteral());
    }

    public void resetCounter() {
        mCounter = 0;
    }
    
    public void incrementCounter(){
        mCounter++;
    }

    public int getCounter() {
        return mCounter;
    }
    
    public List<Constraint> getInverseSatisfiedContraints(){
        return mSatisfiedContraints;
    }
}
