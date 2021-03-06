package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.HashSet;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.applicationservice.ApplicationServiceProvider;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportSCG extends CaTissueBaseTestCase {

   public static SpecimenCollectionGroup getCPRSCG(CollectionProtocolRegistration cpr) {

       SpecimenCollectionGroup scg = null;
       System.out.println("---------START ImportSCG.getSCG()---------");
       Collection scgCollection = cpr.getSpecimenCollectionGroupCollection();
       Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
       System.out.println("scgCollection.size() " + scgCollection.size());
       while (scgItr.hasNext()) {
          scg = (SpecimenCollectionGroup) scgItr.next();
          System.out.println("SCG name: "+scg.getName());
       }
       System.out.println("---------END ImportSCG.getSpecimenCollectionGroup()---------");
       return scg;
   }

   public static SpecimenCollectionGroup createSCG(CollectionProtocolRegistration cpr) {

      System.out.println("---------START ImportSCG.createSCG()---------");
      SpecimenCollectionGroup scg = null;
      try {
         Collection<ConsentTierStatus> ctsCollection = CommonUtilities.setConsentTierStatus(cpr);
         Collection cpeCollection = cpr.getCollectionProtocol().getCollectionProtocolEventCollection();
         Iterator cpeIterator = cpeCollection.iterator();
         while(cpeIterator.hasNext()) {
            CollectionProtocolEvent cpe = (CollectionProtocolEvent)cpeIterator.next();
            scg = new SpecimenCollectionGroup(cpe);
            scg.setCollectionProtocolRegistration(cpr);
            scg.setConsentTierStatusCollection(ctsCollection);
         }
      } catch (Exception e) {
        System.out.println("Exception in createSCG()");
        e.printStackTrace();
      }
      System.out.println("---------END ImportSCG.createSCG()---------");
      return scg;
   }

   public static SpecimenCollectionGroup updateSCG(SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      String hospitalOR = excel[rowNo][5];
      String sprNum = excel[rowNo][8];
      String diagnosis = excel[rowNo][9];
      String accessdbdiagnosis = excel[rowNo][22];

      System.out.println("---------START ImportSCG.updateSCG()---------");
      SpecimenCollectionGroup uscg = null;
      try {
         scg.setActivityStatus("Active");
         scg.setClinicalDiagnosis(diagnosis);

         Site site = ImportSite.getSite(hospitalOR);
         scg.setSpecimenCollectionSite(site);
         scg.setSurgicalPathologyNumber(sprNum);
         if (accessdbdiagnosis.contains("PRIMARY"))
           scg.setClinicalStatus("Primary");
         else if (accessdbdiagnosis.contains("RECURRRENT"))
           scg.setClinicalStatus("Recurrent");
         else (accessdbdiagnosis.contains("RECURRRENT"))
           scg.setClinicalStatus("Not Specified");
         scg.setCollectionStatus("Complete");

         ImportSpecimenEventParameters.addSEP(scg, excel, rowNo);

         uscg = (SpecimenCollectionGroup)appService.updateObject(scg);

         System.out.println("Specimen Collection Status: "+uscg.getCollectionStatus().equals("Complete"));
         if (uscg.getCollectionStatus().equals("Complete")) {
            assertTrue("Specimen Collected ---->", true);
         } else {
            assertFalse("Anticipatory Specimen", true);
         }
      } catch (Exception e) {
         System.out.println("Exception in updateSCG()");
         e.printStackTrace();
      }
      System.out.println("---------END ImportSCG.updateSCG()---------");
      return uscg;
   }

   public static SpecimenCollectionGroup createAnotherVisitSCG(Participant p, String[][] excel, int rowno) {

      String hospitalOR   = excel[rowno][5];
      String sprNum       = excel[rowno][8];
      String diagnosis    = excel[rowno][9];
      String opDate       = excel[rowno][10];
    
      System.out.println("---------START ImportSCG.createAnotherVisitSCG()---------");
      SpecimenCollectionGroup newSCG = null;
  
      Collection cprCollection = p.getCollectionProtocolRegistrationCollection();  
      Iterator cprItr = cprCollection.iterator();
      while (cprItr.hasNext()) {
           CollectionProtocolRegistration cpr = (CollectionProtocolRegistration)cprItr.next();
           SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
           scg = createSCG(cpr);

           scg.setName("Visit: "+opDate);
           scg.setActivityStatus("Active");
           scg.setClinicalDiagnosis(diagnosis);

           Site site = ImportSite.getSite(hospitalOR);
           scg.setSpecimenCollectionSite(site);
           scg.setSurgicalPathologyNumber(sprNum);
           scg.setClinicalStatus("Operative");
           scg.setCollectionStatus("Complete");

           ImportSpecimenEventParameters.addSEP(scg, excel, rowno);
           try {
              newSCG = (SpecimenCollectionGroup) appService.createObject(scg);
              System.out.println("new scg created = "+newSCG.getName()+ " collection status = "+newSCG.getCollectionStatus());            
           } catch (Exception e) {
              System.out.println("Exception in creating new SCG createAnotherVisitSCG()");
              e.printStackTrace();
           }
      }
      System.out.println("---------END ImportSCG.createAnotherVisitSCG()---------");
      return newSCG;
   }

   public static SpecimenCollectionGroup getSCGById(long id) {

       SpecimenCollectionGroup returnedSCG = null;
       SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
       scg.setId(new Long (id));
       Logger.out.info(" searching domain object");
       try {
          List resultList = appService.search(SpecimenCollectionGroup.class, scg);
          for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
             returnedSCG = (SpecimenCollectionGroup) resultsIterator.next();
             System.out.println("SCG by id is: " + returnedSCG.getName() +" Id: "+returnedSCG.getId());
             Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedSCG.getName());
          }
          assertTrue("SCG found", true);
       } catch (Exception e) {
         Logger.out.error(e.getMessage(),e);
         System.out.println("ImportSCG.getSCGById()"+e.getMessage());
         e.printStackTrace();
         assertFalse("Couldnot found Specimen", true);
      }
      return returnedSCG;
   }
} //end ImportSCG()
