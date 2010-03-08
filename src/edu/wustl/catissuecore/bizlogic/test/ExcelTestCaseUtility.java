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
                String filename = "";
		try {
			System.out.println("---------START ExcelTestCaseUtility.registerParticipants----------");
			System.out.println("user.dir  " + System.getProperty("user.dir"));
                        for (int i = 1670; i < 1874; i++) {
                          if  (i < 10) {
                            filename = "p000" + i;
                            System.out.println("---------Adding Patient "+filename+"----------------"); 
                            readTsvAndWriteToMigrationUtil(filename);  
                            System.out.println("---------Done Adding Patient "+filename+"----------------"); 
                          } else if ( (i >= 10) && (i < 100)) {
                            filename = "p00" + i;  
                            System.out.println("---------Adding Patient "+filename+"----------------"); 
                            readTsvAndWriteToMigrationUtil(filename);  
                            System.out.println("---------Done Adding Patient "+filename+"----------------"); 
                          } else if ( (i >= 100) && (i < 1000)) {
                            filename = "p0" + i;
                            System.out.println("---------Adding Patient "+filename+"----------------"); 
                            readTsvAndWriteToMigrationUtil(filename);  
                            System.out.println("---------Done Adding Patient "+filename+"----------------"); 
                          } else {
                            filename = "p" + i;
                            System.out.println("---------Adding Patient "+filename+"----------------"); 
                            readTsvAndWriteToMigrationUtil(filename);  
                            System.out.println("---------Done Adding Patient "+filename+"----------------"); 
                          }
                        } // close for loop 
			System.out.println("---------END ExcelTestCaseUtility.registerParticipant-----------");
		} catch (Exception e) {
			System.out.println("Exception in ExcelTestCaseUtility.registerParticipants()");
			e.printStackTrace();
			throw e;
		}
	}

        public static void readTsvAndWriteToMigrationUtil (String filename) throws Exception {

           String[][] excel = new String[1][25];
           String line = null;
           String formattedstring = null;
           int row = 0;
           int col = 0;
           String f = "/home/catissue/bsp-import-data/date-correction-2010-03-01/"+filename;
           try {
              File file = new File(f);
              BufferedReader bufRdr = new BufferedReader(new FileReader(file));
              //read each line of text file
              while ( (line = bufRdr.readLine()) != null) {
                   StringTokenizer st = new StringTokenizer(line,"\t");
                   while (st.hasMoreTokens()) {
                       //get next token and store it in the array
                       formattedstring = st.nextToken();
                       excel[row][col] = formattedstring.replaceAll("[\"]", ""); 
                       System.out.println("data is: "+excel[row][col]);
                       col++;
                   }
                   row++;
              }
              //close the file
              bufRdr.close();
              new DataMigrationUtil().writeToCaTissue(excel, 1);
           } catch (Exception e) {
              System.out.println("Exception in ExcelTestCaseUtility.readTsvAndWriteToMigrationUtil()");
              e.printStackTrace();
              throw e;
           }
        }  

}
