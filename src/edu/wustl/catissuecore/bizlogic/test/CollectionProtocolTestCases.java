package edu.wustl.catissuecore.bizlogic.test;


import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import edu.wustl.catissuecore.domain.CellSpecimenRequirement;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

public class CollectionProtocolTestCases extends CaTissueBaseTestCase 
{
	AbstractDomainObject domainObject = null;
	
	public void testSearchCollectionProtocol()
	{
    	CollectionProtocol collectionProtocol = new CollectionProtocol();
        collectionProtocol.setId(new Long ("63"));
       	Logger.out.info(" searching domain object");
       	System.out.println(" searching domain object");
    	try {
        	// collectionProtocol = (CollectionProtocol) appService.createObject(collectionProtocol);
        	 List resultList = appService.search(CollectionProtocol.class,collectionProtocol);
        	 for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();)
        	 {
        		 CollectionProtocol returnedcollectionprotocol = (CollectionProtocol) resultsIterator.next();
        		 Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle());
                         System.out.println(" Domain Object is successfully Found ---->  :: " + returnedcollectionprotocol.getTitle()+"," +returnedcollectionprotocol.getId()+"," +returnedcollectionprotocol.getShortTitle());
             }
          } 
          catch (Exception e) {
        	Logger.out.error(e.getMessage(),e);
	 		e.printStackTrace();
	 		fail("Doesnot found collection protocol");
          }
	}
	
}
