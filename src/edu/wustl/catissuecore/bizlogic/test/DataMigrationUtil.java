package edu.wustl.catissuecore.bizlogic.test;

import java.util.List;
import java.util.Iterator;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class DataMigrationUtil extends CaTissueBaseTestCase {

  private static int rowNo = 1; // Row number in the excel sheet.

  public void writeToCaTissue(String excel[][], int rowCount) throws Exception {
 
      System.out.println("---------START DataMigrationUtil.writeToCaTissue()---------");
      System.out.println("Total no. of rows: "+rowCount);
      while (rowNo < excel.length -1) {
         System.out.println("----------START Processing for row number "+ rowNo + "---------------");
         

         //Initialize Participant 
         Participant ipart = ImportParticipant.initParticipant(excel,rowNo);

         //Register Participant to CP
         Participant rpart = ImportCPR.registerParticipantToCP(ipart,excel,rowNo);

         //Get the Participant's SCG 
         SpecimenCollectionGroup scg = ImportParticipant.getParticipantSCG(rpart);

         //Update SCG 
         SpecimenCollectionGroup uscg = ImportSCG.updateSCG(scg,excel,rowNo);
         //Note: Steps 3 and 4 are necessary for setting the following SCG attributes
           //SCG Collection Site, Surg Path no, Clinical Status, Collection Status
           //Collection and Received Event Parameters (Collection Procedure and Container, Received Quality)
        //The above attributes cannot be set while creating an SCG i.e., in ImportSCG.createSCG() method 
          
         System.out.println("----------END Processing for row number "+ rowNo + "---------------");
         rowNo++;
      } 
      System.out.println("---------END DataMigrationUtil.writeToCaTissue()---------");
  }

  public static Participant searchParticipant()  {

     Participant returnedParticipant = null;
     Participant participant = new Participant();
     Logger.out.info(" searching particpant");
     participant.setLastName(new String("Martin"));
     try {
        List resultList = appService.search(Participant.class,participant);
        for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
           returnedParticipant = (Participant) resultsIterator.next();
           Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedParticipant.getFirstName() +" "+returnedParticipant.getLastName());
           Logger.out.info(" Participant is successfully Found ---->  :: " + returnedParticipant.getFirstName() +" "+returnedParticipant.getLastName());
         }
     } catch (Exception e) {
       Logger.out.error(e.getMessage(),e);
       System.out.println("DataMigrationUtil.searchParticipant()"+e.getMessage());
       e.printStackTrace();
       assertFalse("Did not find particpant Domain Object", true);
    }
    return returnedParticipant;
  }


} //end DataMigrationUtil()
