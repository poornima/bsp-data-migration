package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
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
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
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
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class DataMigrationUtil extends CaTissueBaseTestCase {

  private static int rowNo = 1; // Row number in the excel sheet.

  public static Site returnedSite = null;

  public static Site getSite() {

        Site site = new Site();
        site.setName(new String ("UABH"));
        System.out.println("Searching Domain Object........");
        try {
             List resultList = appService.search(Site.class,site);
             for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
                returnedSite = (Site) resultsIterator.next();
                System.out.println(" Domain Object is successfully Found ---->  :: "+returnedSite.getName()+" "+returnedSite.getId()+" "+returnedSite.getType());
             }
        } catch (Exception e) {
             System.out.println("DataMigrationUtil.getSite()"+e.getMessage());
             e.printStackTrace();
             assertFalse("Did not find Domain Object", true);
        }
        return returnedSite;
    }

  public void writeToCaTissue(String excel[][], int rowCount) throws Exception {
 
      Participant participant = null;  
      SpecimenCollectionGroup scg = null;
      Collection<Specimen> specList = null;
      
      System.out.println("---------START DataMigrationUtil.writeToCaTissue()---------");

      while (rowNo < excel.length -1) {

         System.out.println("----------START Processing for row number "+ rowNo + "---------------");
         
         //Register Participant to CPR 
         participant = createAndRegisterParticipantToCollectionProtocol(excel);
/*
         //Search for the Participant SCG
         scg = getSpecimenCollectionGroup(participant);

         //Search for the CPR SCG
         CollectionProtocolRegistration cpr = scg.getCollectionProtocolRegistration();

         //Copy the scg properties into a newSCG object
         //Set the newSCG CPR reference to the CPR of the above Participant 
         SpecimenCollectionGroup newSCG = createSCGAndSetCPR(scg, cpr);

         //update newSCG 

         //updateSCG method adds the following from the BSP Access db-
         // 1. Specimen Event Parameters
         // 2. Clinical Diagnosis
         // 3. Surgical Pathology Number 
         SpecimenCollectionGroup updatedSCG = updateSCG(newSCG, excel);
*/     
         System.out.println("----------END Processing for row number "+ rowNo + "---------------");

         rowNo++;
      } 

      System.out.println("---------END DataMigrationUtil.writeToCaTissue()---------");
  }

  public Participant createAndRegisterParticipantToCollectionProtocol(String excel[][]) throws Exception {

     System.out.println("---------START DataMigrationUtil.createAndRegisterParticipantToCollectionProtocol()---------");

     Participant participant = initParticipant(excel);

     CollectionProtocolRegistration collectionProtocolRegistration = initCollectionProtocolRegistration(participant);
 
     System.out.println("Returned CPReg is: "+collectionProtocolRegistration);

     Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection = new HashSet<CollectionProtocolRegistration>();
     collectionProtocolRegistrationCollection.add(collectionProtocolRegistration);
     participant.setCollectionProtocolRegistrationCollection(collectionProtocolRegistrationCollection);

     try {
         participant = (Participant) appService.createObject(participant);
         System.out.println("Participant Object created successfully");
         System.out.println("Participant Added successfully::" + participant.getId()+","+participant.getFirstName()+","+participant.getLastName());
         assertTrue("Participant Object added successfully", true);
     } catch (Exception e) {
         System.out.println("DataMigrationUtil.createAndRegisterParticipantToCollectionProtocol()"+e.getMessage());
         e.printStackTrace();
         assertFalse("could not add object Participant", true);
     } 
     System.out.println("---------END DataMigrationUtil.createAndRegisterParticipantToCollectionProtocol()---------");
     return participant;
  }
    /*
     * This method initialize participant object  as given in the excel sheet
     * @param excel String double array
     */

    public Participant initParticipant(String excel[][]) {
        String     lastName = "";
        String     firstName = "";
        String     middleName = "";
        String     dob = "";
        String     genderFromAccess = "";
        String     medRecNo = "";
        String     raceFromAccess = "";
        String     gender = "";
        String     raceName = "";
        Date       date;

        System.out.println("---------START DataMigrationUtil.initParticipant()---------");
            lastName = excel[rowNo][0];
            firstName = excel[rowNo][1];
            middleName = excel[rowNo][2];
            dob = excel[rowNo][3];
            genderFromAccess = excel[rowNo][4];
            medRecNo = excel[rowNo][5];
            raceFromAccess = excel[rowNo][6];
            System.out.println("lastname=" +lastName+ "firstname=" +firstName+ "middlename=" +middleName+ "dob=" +dob+ "gender=" +genderFromAccess+ "medrecno="+medRecNo+ "race=" +raceFromAccess);

            Participant participant = new Participant();
            participant.setLastName(lastName);
            participant.setFirstName(firstName);
            participant.setMiddleName(middleName);

            try {
               date = convertDateFromExcel(dob);
               participant.setBirthDate(date);
            } catch (ParseException pe) {
               System.out.println("ERROR: could not parse date in String: " +dob);
            }

            gender = getGenderFromCaTissue(genderFromAccess);
            participant.setGender(gender);

            Collection participantMedicalIdentifierCollection = new HashSet();
            ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();

            Site site = (Site) getSite();
            pmi.setSite(site);
            pmi.setMedicalRecordNumber(medRecNo);
            pmi.setParticipant(participant);
            participantMedicalIdentifierCollection.add(pmi);
            participant.setParticipantMedicalIdentifierCollection(participantMedicalIdentifierCollection);

            Collection<Race> raceCollection = new HashSet<Race>();
            Race race = new Race();

            raceName = getRaceFromCaTissue(raceFromAccess);
            race.setRaceName(raceName);
            race.setParticipant(participant);
            raceCollection.add(race);
            participant.setRaceCollection(raceCollection);

            if (raceFromAccess.equals("Hispanic"))
               participant.setEthnicity("Hispanic or Latino");
            else
               participant.setEthnicity("Unknown");

            participant.setActivityStatus("Active");
            System.out.println("Participant initiated successfully -->Name:"+participant.getFirstName()+" "+participant.getLastName());
            return participant;

    } //end initParticipant()

  public CollectionProtocolRegistration initCollectionProtocolRegistration(Participant participant) {

     System.out.println("---------START DataMigrationUtil.initCollectionProtocolRegistration()---------");

     CollectionProtocolRegistration collectionProtocolRegistration = new CollectionProtocolRegistration();

     CollectionProtocol cp = getCollectionProtocol();

     collectionProtocolRegistration.setCollectionProtocol(cp);
     collectionProtocolRegistration.setParticipant(participant);
     collectionProtocolRegistration.setActivityStatus("Active");
     try{
        //collectionProtocolRegistration.setRegistrationDate(Utility.parseDate(colldate.replace('/', '-'), "M-d-yyyy"));
        /*collDate is commented while inegrating with nightly bcz it should be in the format MM-dd-yyyy  **/
        //Date timestamp = EventsUtil.setTimeStamp(colldate,"15","45");
        Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
        collectionProtocolRegistration.setRegistrationDate(timestamp);
     } catch (Exception e) {
        System.out.println("Exception in initCollectionProtocolRegistration" );
        System.err.println("Exception in initCollectionProtocolRegistration" );
        e.printStackTrace();
     }

     System.out.println("Returned CPReg is: "+collectionProtocolRegistration);
     System.out.println("---------END DataMigrationUtil.initCollectionProtocolRegistration()---------");
     return collectionProtocolRegistration;
  }

  public CollectionProtocol getCollectionProtocol() {

      CollectionProtocol returnedCollectionProtocol = null;

        CollectionProtocol cp = new CollectionProtocol();
        cp.setId(new Long ("63"));
        System.out.println("Searching Domain Object........");
        try {
             List resultList = appService.search(CollectionProtocol.class,cp);
             for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
                returnedCollectionProtocol = (CollectionProtocol) resultsIterator.next();
                System.out.println(" Domain Object is successfully Found ---->  :: " + returnedCollectionProtocol.getTitle()+"," +returnedCollectionProtocol.getId()+"," +returnedCollectionProtocol.getShortTitle());
             }
        } catch (Exception e) {
             System.out.println("TestParticipant.getCollectionProtocol()"+e.getMessage());
             e.printStackTrace();
             assertFalse("Did not find Domain Object", true);
        }
        return returnedCollectionProtocol;
   }

   /*
    * This method searches specimen collection group in the cpr of the participant
    * @param studyLabel study calender event label/point given in the excel
    */
   public SpecimenCollectionGroup getSpecimenCollectionGroup(Participant participant) {

       System.out.println("---------START DataMigrationUtil.getSpecimenCollectionGroup()---------");
       SpecimenCollectionGroup scg = null;
       Collection cprColl = participant.getCollectionProtocolRegistrationCollection();
       System.out.println("No of cpr retreived from participant " + cprColl.size());
       Iterator<CollectionProtocolRegistration> cprItr = cprColl.iterator();
       while (cprItr.hasNext()) {
          CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) cprItr.next();
          Collection scgCollection = cpr.getSpecimenCollectionGroupCollection();

          Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
          System.out.println("scgCollection.size() " + scgCollection.size());
          while (scgItr.hasNext()) {
             scg = (SpecimenCollectionGroup) scgItr.next();
             System.out.println("CollectionPointLabel() "+ scg.getCollectionProtocolEvent().getCollectionPointLabel());
          }
       }
       System.out.println("---------END DataMigrationUtil.getSpecimenCollectionGroup()---------");
       return scg;
   }

   public String getGenderFromCaTissue (String g) {

      String gender = "";

      if (g.equals("M"))
        gender = "Male Gender";
      else if (g.equals("F"))
        gender = "Female Gender";
      else if (g.equals("UNKNOWN"))
        gender = "Unknown";
      else
        gender = "Unspecified";

      return gender;

   }

   public String getRaceFromCaTissue (String r) {

     String race = "";

     if (r.equals("Caucasian"))
       race = "White";
     else if (r.equals("Black"))
       race = "Black or African American";
     else if (r.equals("Oriental"))
       race = "Asian";
     else if (r.equals("Hispanic") || r.equals("U"))
       race = "Unknown";

     return race;
  }

  public Date convertDateFromExcel (String d) throws ParseException {

     // Courtesy: http://devpinoy.org/blogs/lamia/archive/2008/03/25/parsing-excel-date-with-poi-jxls.aspx

     Date actualDate=HSSFDateUtil.getJavaDate(Double.parseDouble(d), true);

     return actualDate;

  }

} //end DataMigrationUtil()
