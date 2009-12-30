package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.common.util.HQLCriteria;


/**
 * This class is to test the functionality where system will try to
 * read an excel sheet with domain data and enter it into caTissue database
 *
 * @author vishvesh_mulay
 *
 */
public class ExcelDataMigrationTestCase extends CaTissueBaseTestCase {

	AbstractDomainObject domainObject = null;


	/**
	 * This test will try to register a participant
	 * as specified in the source data.
	 */
	public void testRegisterPart(){
		try{
			ExcelTestCaseUtility.registerParticipants();
			//we can check here whether the participants are properly registered or not.
			assertTrue("Successed", true);
		 }
		 catch(Exception e){
			 e.printStackTrace();
			 assertFalse("Falied", true);
		 }
	}


}
