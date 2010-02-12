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

   public static SpecimenCollectionGroup getSCG(CollectionProtocolRegistration cpr) {

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

   public static SpecimenCollectionGroup createSCGAndSetCPR(SpecimenCollectionGroup scg, CollectionProtocolRegistration cpr) {

      System.out.println("---------START ImportSCG.createSCGAndSetCPR()---------");
      SpecimenCollectionGroup nscg = new SpecimenCollectionGroup();
      Collection c = cpr.getSpecimenCollectionGroupCollection();
      Iterator<SpecimenCollectionGroup> scit = c.iterator();
      while (scit.hasNext()) {
         nscg = (SpecimenCollectionGroup) scit.next();
      }  
      nscg.setCollectionProtocolRegistration(cpr);
      try {
         Collection cpeCollection = cpr.getCollectionProtocol().getCollectionProtocolEventCollection();
         Iterator cpeIterator = cpeCollection.iterator();
         while(cpeIterator.hasNext()) {
            CollectionProtocolEvent cpe = (CollectionProtocolEvent)cpeIterator.next();
            nscg.setCollectionProtocolEvent(cpe);
         }
      } catch (Exception e) {
        System.out.println("Exception in createSCGAndSetCPR()");
        e.printStackTrace();
      }
      System.out.println("---------END ImportSCG.createSCGAndSetCPR()---------");
      return nscg;
   }

   public static SpecimenCollectionGroup addSCGProperties(SpecimenCollectionGroup scg, String excel[][]) {

      System.out.println("---------START ImportSCG.addSCGProperties()---------");
      Collection<SpecimenEventParameters> sepCollection = ImportSpecimenEventParameters.addSEP(scg, excel);
      scg.setSpecimenEventParametersCollection(sepCollection);
      System.out.println("---------END ImportSCG.addSCGProperties()---------");
      return scg;
   }  

   public static SpecimenCollectionGroup updateSCG(SpecimenCollectionGroup scg, CollectionProtocolRegistration cpr, String excel[][]) {

       String hospitalOR = excel[row][5];
       String spr = excel[row][8];
       String diagnosis = excel[row][9];

       System.out.println("---------START DataMigrationUtil.updateSCG()---------");
       scg.setId(new Long(16));
       scg.setName("Brain SPORE SCG_pt_35");
       scg.setCollectionStatus("Complete");
       scg.setClinicalStatus("Operative");
       scg.setClinicalDiagnosis("Anaplastic glioma of brain (disorder)");
       scg.setSurgicalPathologyNumber(spr);

       Site site = ImportSite.getSite(hospitalOR);
       scg.setSpecimenCollectionSite(site);
       scg.setConsentTierStatusCollectionFromCPR(cpr);
       scg.setActivityStatus("Active");
       System.out.println("---------END DataMigrationUtil.updateSCG()---------");
       return scg;
   }
} //end ImportSCG()
