package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.io.*;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;

public class ExcelTestCaseUtility extends CaTissueBaseTestCase {



	/**
	 * This method will read the source data from the excel sheet with
	 * pre-decided format and will try to create participants
	 * as specified in the data source
	 *
	 * @throws Exception
	 */
	public static void registerParticipants() throws Exception {
		try {
			System.out.println("---------START ExcelTestCaseUtility.registerParticipants----------");
			System.out.println("user.dir  " + System.getProperty("user.dir"));
                        String[][] excel = new String[1][25];
                        File file = new File("/home/catissue/bsp-import-data/tsite-correction-2010-03-01/p0018");
                        BufferedReader bufRdr = new BufferedReader(new FileReader(file));
                        String line = null;
                        int row = 0;
                        int col = 0;
                        //read each line of text file
                        while((line = bufRdr.readLine()) != null) {
                             StringTokenizer st = new StringTokenizer(line,"\t");
                             while (st.hasMoreTokens()) {
                                //get next token and store it in the array
                                excel[row][col] = st.nextToken();
                                System.out.println("data is: "+excel[row][col]);
                                col++;
                             }
                             row++;
                         }
                         //close the file
                         bufRdr.close();
                         new DataMigrationUtil().writeToCaTissue(excel, 1);
			System.out.println("---------END ExcelTestCaseUtility.registerParticipant-----------");
		} catch (Exception e) {
			System.out.println("Exception in registerParticipants");
			e.printStackTrace();
			throw e;
		}
	}

}
