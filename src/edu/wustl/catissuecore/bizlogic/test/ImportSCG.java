package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.Iterator;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportSCG extends CaTissueBaseTestCase {

   public static SpecimenCollectionGroup getSCG(CollectionProtocolRegistration cpr) {

       System.out.println("---------START ImportSCG.getSCG()---------");
       Collection scgCollection = cpr.getSpecimenCollectionGroupCollection();
       Iterator<SpecimenCollectionGroup> scgItr = scgCollection.iterator();
       System.out.println("scgCollection.size() " + scgCollection.size());
       while (scgItr.hasNext()) {
          SpecimenCollectionGroup scg = (SpecimenCollectionGroup) scgItr.next();
          System.out.println("SCG name: "+getName());
       }
       System.out.println("---------END ImportSCG.getSpecimenCollectionGroup()---------");
       return scg;
   }

   public static SpecimenCollectionGroup createSCGAndSetCPR(SpecimenCollectionGroup scg, CollectionProtocolRegistration cpr) {

   
   
   }

} //end ImportSCG()
