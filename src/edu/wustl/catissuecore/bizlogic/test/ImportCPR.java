package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportCPR extends CaTissueBaseTestCase {


  public static Participant registerParticipantToCP(Participant participant, String[][] excel, int rowNo) {

     System.out.println("---------START ImportCPR.registerParticipantToCP()---------");
     CollectionProtocolRegistration cpr = initCPR(participant, excel, rowNo);
     Collection cprCollection = new HashSet();
     cprCollection.add(cpr);
     participant.setCollectionProtocolRegistrationCollection(cprCollection);
     try {
         participant = (Participant) appService.createObject(participant);
         System.out.println("Object created successfully");
         assertTrue("Object added successfully", true);
         System.out.println("Participant Added successfully .. id is " + participant.getId()+
         "First Name is: "+participant.getFirstName()+" Last Name is: "+participant.getLastName());
      } catch(Exception e) {
         System.out.println("ImportParticipant.createParticipant()"+e.getMessage());
         e.printStackTrace();
         assertFalse("could not add object", true);
      }
     System.out.println("---------START ImportCPR.registerParticipantToCP()---------");
     return participant;
  }

  public static CollectionProtocolRegistration initCPR(Participant participant, String[][] excel, int rowNo) {

     int row = rowNo;
     String medRecNo = excel[row][6];  
     String opDate   = excel[row][10];
     Date   regDate;

     System.out.println("---------START ImportCPR.initCPR()---------");
     CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
     CollectionProtocol cp = ImportCP.searchCP();
     System.out.println("CP is " +cp.getTitle()); 
     cpr.setCollectionProtocol(cp);
     cpr.setParticipant(participant);
     cpr.setProtocolParticipantIdentifier(medRecNo); //placeholder
     cpr.setActivityStatus("Active"); //placeholder
     try {
       regDate = CommonUtilities.convertDateFromExcel(opDate);
       cpr.setRegistrationDate(regDate); //placeholder
      } catch (ParseException pe) {
       System.out.println("ERROR: could not parse date in String: " +opDate);
      }
     Collection consentTierResponseCollection = new HashSet();
     Collection consentTierCollection = cp.getConsentTierCollection();
     if (consentTierCollection != null) {
        System.out.println("********Consent Tier Collection is not null");
        Iterator consentTierItr = consentTierCollection.iterator();
        while (consentTierItr.hasNext()) {
           ConsentTier consentTier = (ConsentTier)consentTierItr.next();

           ConsentTierResponse consentResponse = new ConsentTierResponse();
           consentResponse.setConsentTier(consentTier); //placeholder
           consentResponse.setResponse("Yes"); //placeholder
           consentTierResponseCollection.add(consentResponse);
        }
     } else {
        System.out.println("********Consent Tier Collection is null");
     } 
     cpr.setConsentTierResponseCollection(consentTierResponseCollection); 
     ImportSCG.createSCG(cpr);
     Collection<SpecimenCollectionGroup> scgCollection = new HashSet<SpecimenCollectionGroup>();
     cpr.setSpecimenCollectionGroupCollection(scgCollection); 
     System.out.println("Returned CPR is: "+cpr);
     System.out.println("---------END ImportCPR.initCPR()---------");
     return cpr;
  }
} //end ImportCPR()
