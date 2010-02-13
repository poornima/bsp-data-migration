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

   private static int row = 1;

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
         Collection<ConsentTierStatus> ctsCollection = setConsentTierStatus(cpr);
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

   public static Collection<ConsentTierStatus> setConsentTierStatus (CollectionProtocolRegistration cpr) {

      CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
      Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
      Iterator consentTierItr = consentTierCollection.iterator();
      Collection consentTierStatusCollection = new HashSet();
      while(consentTierItr.hasNext()) {
         ConsentTier consentTier = (ConsentTier)consentTierItr.next();
         ConsentTierStatus consentStatus = new ConsentTierStatus();
         consentStatus.setConsentTier(consentTier);
         consentStatus.setStatus("Yes");
         consentTierStatusCollection.add(consentStatus);
      }
      return consentTierStatusCollection;
   }

   public static SpecimenCollectionGroup updateSCG(SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      int row = rowNo;
      String hospitalOR = excel[row][5];
      String sprNum = excel[row][8];
      String diagnosis = excel[row][9];

      System.out.println("---------START ImportSCG.updateSCG()---------");
      SpecimenCollectionGroup uscg = null;
      try {
         scg.setActivityStatus("Active");
         scg.setClinicalDiagnosis(diagnosis);

         Site site = ImportSite.getSite(hospitalOR);
         scg.setSpecimenCollectionSite(site);
         scg.setSurgicalPathologyNumber(sprNum);
         scg.setClinicalStatus("Operative");
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
} //end ImportSCG()
