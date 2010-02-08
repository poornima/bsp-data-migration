package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
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

    private static int rowNo = 1; // Row number in the excel sheet

    public static Participant initParticipant(String excel[][]) {
        String    lastName = excel[rowNo][0];
        String    firstName = excel[rowNo][1];
        String    middleName = excel[rowNo][2];
        String    dob = excel[rowNo][3];
        String    genderFromAccess = excel[rowNo][4];
        String    medRecNo = excel[rowNo][5];
        String    raceFromAccess = excel[rowNo][6];
        String    gender = "";
        String    raceName = "";
        Date      date;

        System.out.println("---------START ImportParticipant.initParticipant()---------");
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

            Site site = ImportSite.getSite();
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

      try {
         Participant particpant = new Participant(p);
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

  public static Date convertDateFromExcel (String d) throws ParseException {

     // Courtesy: http://devpinoy.org/blogs/lamia/archive/2008/03/25/parsing-excel-date-with-poi-jxls.aspx

     Date actualDate=HSSFDateUtil.getJavaDate(Double.parseDouble(d), true);

     return actualDate;

  }

} //end ImportParticipant()