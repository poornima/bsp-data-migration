package edu.wustl.catissuecore.bizlogic.test;

import java.util.Iterator;
import java.util.List;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportSite extends CaTissueBaseTestCase {

  public static Site returnedSite = null;

  public static Site getSite() {

        Site site = new Site();
        site.setName(new String ("UABH"));
        System.out.println("Searching Domain Object........");
        try {
             List resultList = appService.search(Site.class,site);
             for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
                returnedSite = (Site) resultsIterator.next();
                System.out.println(" Domain Object is successfully Found ---->  :: "+returnedSite.getName()+" "
                +returnedSite.getId()+" "+returnedSite.getType());
             }
        } catch (Exception e) {
             System.out.println("ImportSite.getSite()"+e.getMessage());
             e.printStackTrace();
             assertFalse("Did not find Domain Object", true);
        }
        return returnedSite;
    }
} //end ImportSite()
