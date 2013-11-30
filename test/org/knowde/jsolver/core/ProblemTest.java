/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.knowde.jsolver.core;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author msriti
 */
public class ProblemTest {
    
    public ProblemTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetVariableNumber() {
        System.out.println("getVariableNumber");
        Problem instance = new Problem();
        instance.setVariables(new int[]{1,2,3});
        int expResult = 3;
        int result = instance.getVariableNumber();
        assertEquals(expResult, result);       
    }

    @Test
    public void testGetConstraintNumber() {
        System.out.println("getConstraintNumber");
        Problem instance = new Problem();
        instance.addConstraint(new int[]{-1,-2,-3});
        instance.addConstraint(new int[]{-1,-2});
        int expResult = 2;
        int result = instance.getConstraintNumber();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetLiterals() {
        System.out.println("getLiterals");
        Problem instance = new Problem();
        int[] expResult = null;
        int[] result = instance.getLiterals();
        assertArrayEquals(expResult, result);
    }

    @Test
    public void testSetVariables() {
        System.out.println("setVariables");
        int[] vars = new int[]{1,2,3};
        Problem instance = new Problem();
        instance.setVariables(vars);
    }

    @Test
    public void testGetConstraints() {
        System.out.println("getConstraints");
        Problem instance = new Problem();
        instance.addConstraint(new int[]{-1,-2,-3});
        instance.addConstraint(new int[]{-1,-2});
        int expResult = 2;
        int result = instance.getConstraints().size();
        assertEquals(expResult, result);
    }

    @Test
    public void testAddConstraint() {
        System.out.println("addConstraint");
        Problem instance = new Problem();
        instance.addConstraint(new int[]{-1,-2,-3});
        int result = instance.getConstraintNumber();
        assertEquals(1, result);
    }
}
