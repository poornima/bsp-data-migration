package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportSpecimenEventParameters extends CaTissueBaseTestCase {

   public static Collection<SpecimenEventParameters> addSEP(SpecimenCollectionGroup scg, String excel[][]) {

      CollectionEventParameters cep = ImportCPEvent.addCEP(scg, excel);
      ReceivedEventParameters rep = ImportCPEvent.addREP(scg, excel);
      Collection<SpecimenEventParameters> sepCollection = new HashSet<SpecimenEventParameters>();
      sepCollection.add(cep);
      sepCollection.add(rep);
      return sepCollection;
   }  
} //end ImportSpecimenEventParameters()
