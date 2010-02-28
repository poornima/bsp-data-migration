package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Date;
import java.util.Iterator;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportParticipant extends CaTissueBaseTestCase {

    public static Participant initParticipant(String excel[][], int rowNo) {
        String    lastName = excel[rowNo][0];
        String    firstName = excel[rowNo][1];
        String    middleName = excel[rowNo][2];
        String    dob = excel[rowNo][3];
        String    genderFromAccess = excel[rowNo][4];
        String    hospitalOR = excel[rowNo][5];
        String    medRecNo = excel[rowNo][6];
        String    raceFromAccess = excel[rowNo][7];
        String    gender = "";
        String    raceName = "";
        Date      date;

        System.out.println("---------START ImportParticipant.initParticipant()---------");
        System.out.println("lastname=" +lastName+ " firstname=" +firstName+ " middlename=" +middleName+ " dob=" +dob+ " gender=" +genderFromAccess+ " hospitalOR=" +hospitalOR+ " medrecno="+medRecNo+ " race=" +raceFromAccess);

            Participant participant = new Participant();
            participant.setLastName(lastName);
            participant.setFirstName(firstName);
            participant.setMiddleName(middleName);

            try {
               date = CommonUtilities.convertDateFromExcel(dob);
               participant.setBirthDate(date);
            } catch (ParseException pe) {
               System.out.println("ERROR: could not parse date in String: " +dob);
            }
            participant.setVitalStatus("Unknown");
            gender = getGenderFromCaTissue(genderFromAccess);
            participant.setGender(gender);

            Collection participantMedicalIdentifierCollection = new HashSet();
            ParticipantMedicalIdentifier pmi = new ParticipantMedicalIdentifier();
            pmi.setParticipant(participant);
            Site site = ImportSite.getSite(hospitalOR);
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
         
            return participant;

    } //end initParticipant()

   public static Participant createParticipant(Participant p) {

      System.out.println("---------START ImportParticipant.createParticipant()---------");
      Participant participant = (Participant) p;
      try {
         participant = (Participant) appService.createObject(participant);
         System.out.println("Object created successfully");
         //assertTrue("Object added successfully", true);
         System.out.println("Participant Added successfully .. id is " + participant.getId()+
         "First Name is: "+participant.getFirstName()+" Last Name is: "+participant.getLastName());
      } catch(Exception e) {
         System.out.println("ImportParticipant.createParticipant()"+e.getMessage());
         e.printStackTrace();
         //assertFalse("could not add object", true);
      }
      System.out.println("---------START ImportParticipant.createParticipant()---------");
      return participant;
   }

   public static String getGenderFromCaTissue (String g) {

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

   public static String getRaceFromCaTissue (String r) {

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

  public static SpecimenCollectionGroup getParticipantSCG(Participant participant) {

       System.out.println("---------START ImportParticipant.getParticipantSCG()---------");
       SpecimenCollectionGroup scg = null;
       Collection cprCollection = participant.getCollectionProtocolRegistrationCollection();
       Iterator<CollectionProtocolRegistration> cprItr = cprCollection.iterator();
       while (cprItr.hasNext()) {
          CollectionProtocolRegistration cpr = (CollectionProtocolRegistration) cprItr.next();
          Collection scgCollection = cpr.getSpecimenCollectionGroupCollection();
          Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
          while (scgItr.hasNext()) {
             scg = (SpecimenCollectionGroup) scgItr.next();
          }
       }
       System.out.println("---------END ImportParticipant.getParticipantSCG()---------");
       return scg;
   }

   public static Participant searchParticipant(String excel[][], int rowno) {
     
      String    lastName = excel[rowno][0];
      String    firstName = excel[rowno][1];
      String    middleName = excel[rowno][2];
      String    medRecNo = excel[rowno][6];
      String    dob = excel[rowno][3];
      Date      date;

      Participant returnedParticipant = null;
      Participant participant = new Participant();
      participant.setLastName(lastName);
      participant.setFirstName(firstName);
      participant.setMiddleName(middleName);
      try {
         date = CommonUtilities.convertDateFromExcel(dob);
         participant.setBirthDate(date);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +dob);
      }
      Logger.out.info(" searching domain object");
      try {
         List resultList = appService.search(Participant.class,participant);
         for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
            returnedParticipant = (Participant) resultsIterator.next();
            System.out.println(" Domain Object is successfully Found ---->  :: " + returnedParticipant.getFirstName() +" "+returnedParticipant.getLastName());
         }
      } catch (Exception e) {
         Logger.out.error(e.getMessage(),e);
         System.out.println("ImportParticipant.getReturnParticipant()"+e.getMessage());
         e.printStackTrace();
         assertFalse("Did not find Domain Object", true);
      }
      return returnedParticipant;                 
   }      
} //end ImportParticipant()
