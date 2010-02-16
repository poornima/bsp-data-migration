package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
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

   public static void addSpecimens(SpecimenCollectionGroup scg, String excel[][], int rowNo) {
       
       String codeId   = excel[rowNo][16];
       String lnvial   = excel[rowNo][19];
       int totalNoAliquots = Integer.parseInt(lnvial);
       
       System.out.println("---------START ImportSpecimen.addSpecimens()---------");
       if (codeId.startsWith("BF")) {
         //Create Fluid Specimens

         //Parent Fluid Specimen
         FluidSpecimen pfs = ImportFluidSpecimen.createFluidSpecimen (null,"New",scg,excel,rowNo);

         //Derived Fluid Specimen
         FluidSpecimen dfs = ImportFluidSpecimen.createFluidSpecimen (pfs,"Derived",scg,excel,rowNo);

         //Update Parent and Child Specimens
         FluidSpecimen upfs = ImportFluidSpecimen.updateFluidSpecimen (pfs,codeId,rowNo);
         FluidSpecimen udfs = ImportFluidSpecimen.updateFluidSpecimen (dfs,codeId,rowNo);
       } else {
         //Create Tissue Specimens

         //Parent Tissue Specimen
         TissueSpecimen pts = ImportTissueSpecimen.createTissueSpecimen (null,"New",scg,excel,rowNo);

         //Derived Tissue Specimen
         TissueSpecimen dts = ImportTissueSpecimen.createTissueSpecimen (pts,"Derived",scg,excel,rowNo);

         //Aliquot Tissue Specimens
         TissueSpecimen ats = null;
         for (int currentAliquot = 1; currentAliquot <= totalNoAliquots; currentAliquot++) {
         //for (int currentAliquot = 1; currentAliquot <= 2; currentAliquot++) {
            ats = ImportTissueSpecimen.createAliquotTissueSpecimen (dts,"Aliquot",scg,excel,rowNo,currentAliquot);
         }

         //Update Parent and Child Specimens
         TissueSpecimen upts = ImportTissueSpecimen.updateTissueSpecimen (pts,codeId,rowNo);
         TissueSpecimen udts = ImportTissueSpecimen.updateTissueSpecimen (dts,codeId,rowNo);
         //TissueSpecimen uas = ImportTissueSpecimen.updateAliquotTissueSpecimen (as,codeId,rowNo,totalNoAliquots);
       }
       System.out.println("---------END ImportSpecimen.addSpecimens()---------");
   } 

   public static SpecimenCharacteristics setSpChar (String tsite, String tside, int rowNo) {

      System.out.println("---------START ImportSpecimen.setSpChar()---------");
      SpecimenCharacteristics sc =  new SpecimenCharacteristics();
      sc.setTissueSite(tsite);
      sc.setTissueSide(tside);
      System.out.println("---------END ImportSpecimen.setSpChar()---------");
      return sc;   
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
