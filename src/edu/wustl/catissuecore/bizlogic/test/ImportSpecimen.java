package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportSpecimen extends CaTissueBaseTestCase {

   public static void addSpecimens(SpecimenCollectionGroup scg, String excel[][], int rowNo) throws Exception {
       
       String codeId   = excel[rowNo][16];
       String lnvial   = excel[rowNo][19].trim();
       String icd0     = excel[rowNo][23];

       System.out.println("---------START ImportSpecimen.addSpecimens()---------");
       System.out.println("lnvial = "+lnvial);
       if (codeId.startsWith("BF")) {
         //Create Fluid Specimens
         ImportFluidSpecimen.createFluidSpecimen (scg,excel,rowNo);
       } else {
         //Create Tissue Specimens
         ImportTissueSpecimen.createTissueSpecimen (scg,excel,rowNo);
       }
       System.out.println("---------END ImportSpecimen.addSpecimens()---------");
   } 

  public static SpecimenPosition setSP (String excel[][], int rowNo) {

     String codeId  = excel[rowNo][0];
     String rackbox = excel[rowNo][1];
     String seat    = excel[rowNo][2];   		 
     String trow     = excel[rowNo][3];   		 
     String tcolumn  = excel[rowNo][4];   		 

     System.out.println("---------START ImportSpecimen.setSP()---------");

     TissueSpecimen aliquot = ImportTissueSpecimen.getAliquotByLabel(codeId);

     StorageContainer sc = new StorageContainer();
     sc.setName(rackbox);
     SpecimenPosition position = new SpecimenPosition();
     position.setStorageContainer(sc);

     int row = Integer.parseInt(trow);
     int column = Integer.parseInt(tcolumn);
     System.out.println("row: " +row+" column: "+column);
 
     position.setPositionDimensionOne(row);
     position.setPositionDimensionTwo(column);
     aliquot.setSpecimenPosition(position);
     System.out.println("Storage container is: "+sc.getName()+" row: "+position.getPositionDimensionOne()+
                        " column: "+position.getPositionDimensionTwo()+"aliquot position: "+aliquot.getSpecimenPosition()); 
     System.out.println("---------END ImportSpecimen.setSP()---------");
     return position;
  }
} //end ImportSpecimen()
