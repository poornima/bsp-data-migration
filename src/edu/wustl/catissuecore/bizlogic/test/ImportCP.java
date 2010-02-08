package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;
import edu.wustl.catissuecore.domain.CollectionProtocol;
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


        CollectionProtocol cp = new CollectionProtocol();
        cp.setId(new Long ("63"));
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
} //end ImportCollectionProtocol()
