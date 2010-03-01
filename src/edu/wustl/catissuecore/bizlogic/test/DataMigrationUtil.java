package edu.wustl.catissuecore.bizlogic.test;

import java.util.List;
import java.util.Iterator;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class DataMigrationUtil extends CaTissueBaseTestCase {

  private static int rowno = 0; // Row number in the excel sheet.
  private static long id = 5;

  public void writeToCaTissue(String excel[][], int rowCount) throws Exception {
 
      String lastName	  = excel[rowno][0];
      String firstName    = excel[rowno][1];
      String middleName   = excel[rowno][2];
      String dob          = excel[rowno][3];
      String genderFromAccess = excel[rowno][4];
      String hospitalOR   = excel[rowno][5];
      String medRecNo     = excel[rowno][6];
      String raceFromAccess = excel[rowno][7];
      String surgpathno   = excel[rowno][8];
      String dignosis     = excel[rowno][9];
      String opDate       = excel[rowno][10];
      String surgeon      = excel[rowno][11];
      String colProc      = excel[rowno][12];
      String colCont      = excel[rowno][13];
      String accessionDate= excel[rowno][14];
      String rcvdQuality  = excel[rowno][15];
      String codeId       = excel[rowno][16];
      String octs         = excel[rowno][17];
      String pathStatus   = excel[rowno][18];
      String lnvial       = excel[rowno][19];
      String tsite        = excel[rowno][20];
      String tside        = excel[rowno][21];
      String accessdbdiagnosis = excel[rowno][22];

     System.out.println("lastname = "+lastName+" firstname = "+firstName+ " middlename = "+middleName+
                        " dob = "+dob+" gender = "+genderFromAccess+" hospitalOR = "+hospitalOR+
                        " medrecno = "+medRecNo+" race = "+raceFromAccess+" surgpathno = "+surgpathno+
                        " diagnosis = "+dignosis+" opdate = "+opDate+" surgeon = "+surgeon+
                        " colProc = "+colProc+ " colcont = "+colCont+" accession = "+accessionDate+
                        " rcvqlty = "+rcvdQuality+ " codeId = "+codeId+" octs = "+octs+
                        " pathStatus = "+pathStatus+" lnvial = "+lnvial+" tsite = "+tsite+
                        " tside = "+tside+" accessdd diag = "+accessdbdiagnosis);  

      //String lnvial   = excel[rowno][19];

      System.out.println("---------START DataMigrationUtil.writeToCaTissue()---------");

         Participant ptObj = ImportParticipant.searchParticipant(excel,rowno);         
         System.out.println("participant = "+ptObj);
         if (ptObj == null) { 
           System.out.println(" Pt Obj is null block: participant = "+ptObj);
           //Initialize Participant 
           Participant ipart = ImportParticipant.initParticipant(excel,rowno);

           //Register Participant to CP
           Participant rpart = ImportCPR.registerParticipantToCP(ipart,excel,rowno);

           //Get the Participant's SCG 
           SpecimenCollectionGroup scg = ImportParticipant.getParticipantSCG(rpart);

           //Update SCG 
           SpecimenCollectionGroup uscg = ImportSCG.updateSCG(scg,excel,rowno);
           //Note: Steps 3 and 4 are necessary for setting the following SCG attributes
             //SCG Collection Site, Surg Path no, Clinical Status, Collection Status
             //Collection and Received Event Parameters (Collection Procedure and Container, Received Quality)
             //The above attributes cannot be set while creating an SCG i.e., in ImportSCG.createSCG() method 

             //NOTE: For testing only, get SCG by Id, otherwise, use 'uscg' above
             //SpecimenCollectionGroup scg = ImportSCG.getSCGById(id); 
           if ( lnvial.equals("") || lnvial.equals(null) ) {
             System.out.println("specimen qty is null. specimen should be available for distribution while adding");
             System.out.println("patient associated with above null qty specimen is "+rpart.getId()+","+rpart.getLastName()+","+rpart.getFirstName()+
                                ","+rpart.getMiddleName()+","+rpart.getBirthDate());
           } else {
             ImportSpecimen.addSpecimens(uscg,excel,rowno);
           }
         } else {
           System.out.println(" Pt Obj is not null block: participant = "+ptObj);
           SpecimenCollectionGroup anotherVisitSCG = ImportSCG.createAnotherVisitSCG(ptObj, excel, rowno);
           if ( lnvial.equals("") || lnvial.equals(null) ) {
             System.out.println("specimen qty is null. specimen should be available for distribution while adding");
             System.out.println("patient associated with above null qty specimen is "+ptObj.getId()+","+ptObj.getLastName()+","+ptObj.getFirstName()+
                                ","+ptObj.getMiddleName()+","+ptObj.getBirthDate());
           } else {
             ImportSpecimen.addSpecimens(anotherVisitSCG,excel,rowno);
           }
        }
         //Add Storage Container and Positions
         //ImportSpecimen.setSP(excel,rowno);
    
      System.out.println("---------END DataMigrationUtil.writeToCaTissue()---------");
  }
} //end DataMigrationUtil()
