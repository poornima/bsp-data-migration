package edu.wustl.catissuecore.bizlogic.test;

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
      while (rowNo < excel.length -1) {
         System.out.println("----------START Processing for row number "+ rowNo + "---------------");

         //Initialize Participant 
         Participant ipart = ImportParticipant.initParticipant(excel);

         //Register Participant to CP
         Participant rpart = ImportCPR.registerParticipantToCP(ipart,excel);

/*
         //Get the CPR SCG
         SpecimenCollectionGroup scg = ImportSCG.getSCG(rpart); 

         //Create SCG and set CPR
         SpecimenCollectionGroup cscg = ImportSCG.createSCGAndSetCPR(scg,cpr);

         //Add SCG Properties
         SpecimenCollectionGroup ascg = ImportSCG.addSCGProperties(cscg,excel);

         //Update SCG 
         SpecimenCollectionGroup uscg = ImportSCG.updateSCG(ascg,cpr,excel);

*/
         System.out.println("----------END Processing for row number "+ rowNo + "---------------");
         rowNo++;
      } 
      System.out.println("---------END DataMigrationUtil.writeToCaTissue()---------");
  }
} //end DataMigrationUtil()
