package edu.wustl.catissuecore.bizlogic.test;

import java.util.Date;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
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

  public static CollectionProtocolRegistration registerParticipantToCP(Participant participant) {

     System.out.println("---------START ImportCPR.initCPR()---------");
     CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
     CollectionProtocol cp = ImportCP.getCP();
     cpr.setCollectionProtocol(cp);
     cpr.setParticipant(participant);
     cpr.setActivityStatus("Active");
     try{
        Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
        cpr.setRegistrationDate(timestamp);
     } catch (Exception e) {
        System.out.println("Exception in registerParticipantToCP" );
        e.printStackTrace();
     }
     System.out.println("Returned CPR is: "+cpr);
     System.out.println("---------END ImportCPR.registerParticipantToCPR()---------");
     return cpr;
  }
} //end ImportCPR()
