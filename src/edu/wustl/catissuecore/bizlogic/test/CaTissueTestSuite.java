package edu.wustl.catissuecore.bizlogic.test;

import junit.framework.TestSuite;

public class CaTissueTestSuite
{
	
	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(CaTissueTestSuite.class);
	}
	
	public static junit.framework.Test suite() 
	{
		TestSuite suite = new TestSuite("Test for edu.wustl.catissuecore.bizlogic.test");
		//$JUnit-BEGIN$
		
		suite.addTestSuite(ExcelDataMigrationTestCase.class);

		//$JUnit-END$
		return suite;
	}
	
}
