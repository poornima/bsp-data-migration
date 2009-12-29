package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import edu.wustl.catissuecore.bizlogic.test.UniqueKeyGeneratorUtil;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierResponse;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.SpecimenObjectFactory;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class DataMigrationUtil extends CaTissueBaseTestCase {

	private static int rowNo = 1; // Row number in the excel sheet.

        /*
         * This method initialize participant object  as given in the excel sheet
         * @param excel String double array
         */

    public void initParticipant(String excel[][]) {
	String lastName = excel[rowNo][2];
	String firstName = excel[rowNo][3];
	String middleName = excel[rowNo][4];
	String dOB = excel[rowNo][5];

        System.out.println("---------START DataMigrationUtil.initParticipant()---------");
        try {
        	Participant participant = new Participant();
        	participant.setLastName(lastName);
	        participant.setFirstName(firstName);
       		participant.setMiddleName(middleName);

                Date timestamp = EventsUtil.setTimeStamp(dOB,"15","45");
                participant.setBirthDate(timestamp);

        	participant.setGender("Unspecified");
        	participant.setEthnicity("Unknown");
        	participant.setSexGenotype("XX");

        	Collection<Race> raceCollection = new HashSet<Race>();
        	Race race = new Race();
        	race.setRaceName("White");
          	race.setParticipant(participant);
        	raceCollection.add(race);

        	race = new Race();
        	race.setRaceName("Unknown");
        	race.setParticipant(participant);
        	raceCollection.add(race);

        	participant.setRaceCollection(raceCollection);
        	participant.setActivityStatus("Active");
        	participant.setEthnicity("Hispanic or Latino");

                participant = (Participant) appService.createObject(participant);

          	System.out.println("Participant initiated successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
                System.out.println("Object created successfully");
                assertTrue("Object added successfully", true);
        } catch(Exception e){
                System.out.println("ParticipantTestCases.testAddParticipant()"+e.getMessage());
                e.printStackTrace();
                assertFalse("could not add object", true);
        }

    } //end initParticipant()

} //end DataMigrationUtil()
