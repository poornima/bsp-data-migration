/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.test;


import edu.wustl.common.test.BaseTestCase;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import gov.nih.nci.system.comm.client.ClientSession;

/**
 * @author ganesh_naikwade
 *
 */
public class CaTissueBaseTestCase extends BaseTestCase{

	/**
	 * @throws java.lang.Exception
	 */
        // these two vars copied from ~/src/BSP_Data_Migration

        public static final String USER_NAME = "ppreddy@uab.edu";
        public static final String PASSWORD = "hard2Guess";


	static ApplicationService appService = null;
	public CaTissueBaseTestCase(){
		super();
	}
	/**
	 * 
	 */
	public void setUp(){
		
		//Logger.configure("");
		//System.setProperty("javax.net.ssl.trustStore", "D://jboss_18080//server//default//conf//chap8.keystore");
		appService = ApplicationServiceProvider.getApplicationService();
		ClientSession cs = ClientSession.getInstance();
		try
		{ 
			cs.startSession("ppreddy@uab.edu", "hard2Guess");
		} 	
					
		catch (Exception ex) 
		{ 
			System.out.println(ex.getMessage()); 
			ex.printStackTrace();
			fail();
			System.exit(1);
		}		
	}

}
