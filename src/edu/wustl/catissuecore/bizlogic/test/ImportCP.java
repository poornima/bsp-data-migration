package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.List;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportCP extends CaTissueBaseTestCase {

  private static CollectionProtocol returnedCollectionProtocol = null;

  public static CollectionProtocol getCP() {

     CollectionProtocol scp = searchCP();
     CollectionProtocol ucp = setCP(scp);         
     return ucp;
  }

  public static CollectionProtocol searchCP() {

      CollectionProtocol cp = new CollectionProtocol();
      cp.setShortTitle(new String ("import-bsp"));
      //cp.setId(new Long (63));
      System.out.println("Searching Domain Object........");
      try {
            List resultList = appService.search(CollectionProtocol.class,cp);
            for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
               returnedCollectionProtocol = (CollectionProtocol) resultsIterator.next();
               System.out.println("Domain Object found is ---->  :: " + returnedCollectionProtocol.getTitle()+"," +returnedCollectionProtocol.getId()+"," +returnedCollectionProtocol.getShortTitle());
            }
      } catch (Exception e) {
            System.out.println("ImportCP.getCP()"+e.getMessage());
            e.printStackTrace();
            assertFalse("Did not find Domain Object", true);
      }
      return returnedCollectionProtocol;
  }

  public static CollectionProtocol setCP(CollectionProtocol cp) {

      System.out.println("---------START ImportCP.updateCP()---------");
      Collection consentTierColl = new LinkedHashSet();
      ConsentTier c = new ConsentTier();
      c.setId(new Long(1));
      c.setStatement("Consent for Brain Research");
      consentTierColl.add(c);
      cp.setConsentTierCollection(consentTierColl);
      cp.setDescriptionURL("");
      cp.setActivityStatus("Active");
      System.out.println("---------END ImportCP.updateCP()---------");
      return cp;
  }
} //end ImportCollectionProtocol()
