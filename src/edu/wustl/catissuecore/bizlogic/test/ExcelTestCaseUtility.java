package edu.wustl.catissuecore.bizlogic.test;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;

public class ExcelTestCaseUtility extends CaTissueBaseTestCase {



	/**
	 * This method will read the source data from the excel sheet with
	 * pre-decided format and will try to create participants with their
	 * corresponding specimens and aliquotes as specified in the data source
	 *
	 * @throws Exception
	 */
	public static void registerParticipantsWithSpecimen() throws Exception {
		try {
			System.out
					.println("---------START ExcelTestCaseUtility.registerParticipantsWithSpecimen-----------");
//			System.out.println("user.dir  " + System.getProperty("user.dir"));
			String excelFilePath = System.getProperty("user.dir")
			+ "/excelFiles/Data_Source.xls";
			ExcelFileReader EX_CP = new ExcelFileReader();
			String allexcel[][] = EX_CP.setInfo(excelFilePath);
			new DataMigrationUtil().registerAndCollectSCG(allexcel);
			System.out
					.println("---------END ExcelTestCaseUtility.registerPart-----------");
		} catch (Exception e) {
			System.out.println("Exception in registerPart");
			e.printStackTrace();
			throw e;
		}
	}
//
//	public static void createStorageContainer()
//	{
//		try{
//			StorageContainer storageContainer= BaseTestCaseUtility.initStorageContainer();
//			storageContainer.setName("test_contaner");
//			System.out.println(storageContainer);
//
//			Collection holdsStorageTypeCollection = new HashSet();
//			StorageType sttype = new StorageType();
//			sttype.setId(3L);
//			holdsStorageTypeCollection.add(sttype);
//			//storageContainer.setHoldsStorageTypeCollection(holdsStorageTypeCollection);*/
//			storageContainer.setStorageType(sttype);
//			Site site = new Site();
//			site.setId(2L);
//			storageContainer.setSite(site);
//
//			CollectionProtocol collectionProtocol = new CollectionProtocol();
//			collectionProtocol.setId(3L);
//			Collection collectionProtocolCollection = new HashSet();
//			collectionProtocolCollection.add(collectionProtocol);
//			storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);
//
//
//			storageContainer = (StorageContainer) appService.createObject(storageContainer);
//			System.out.println("container name "+ storageContainer.getName());
//			System.out.println("Object created successfully");
//			assertTrue("Object added successfully", true);
//		 }
//		 catch(Exception e){
//			 e.printStackTrace();
//			 assertFalse("could not add object", true);
//		 }
//	}

}
