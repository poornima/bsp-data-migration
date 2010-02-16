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
import edu.wustl.catissuecore.domain.FluidSpecimen;
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
public class ImportFluidSpecimen extends CaTissueBaseTestCase {

   public static FluidSpecimen createFluidSpecimen (FluidSpecimen ps, String lineage, SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      String codeId = excel[rowNo][16];

      System.out.println("---------START ImportFluidSpecimen.createFluidSpecimen()---------");
      FluidSpecimen fs = null;
      try {
         fs = setFluidSpecimenAttributes(scg,excel,rowNo);
         fs.setParentSpecimen(ps);
         fs.setLineage(lineage);
         setLabelAndBarcode(lineage,fs,codeId,rowNo,0);
         fs.setSpecimenCollectionGroup(scg);
         fs.setCollectionStatus("Collected");
         fs.setIsAvailable(true);  
         fs.setActivityStatus("Active");
         //fs.setConsentTierStatusCollectionFromSCG(scg);
         fs = (FluidSpecimen) appService.createObject(fs);
         System.out.println("Object created successfully");
         System.out.println("Tissue specimen added successfully: Lineage " +fs.getLineage()+
                            " Label: "+fs.getLabel()+ " Id: "+fs.getObjectId());
      } catch(Exception e) {
         System.out.println("ImportFluidSpecimen.createFluidSpecimen()"+e.getMessage());
         e.printStackTrace();
      }
      System.out.println("---------End ImportFluidSpecimen.createFluidSpecimen()---------");
      return fs;
   }

   public static FluidSpecimen updateFluidSpecimen (FluidSpecimen fluidSpec, String codeId, int rowNo) {

      System.out.println("---------START ImportFluidSpecimen.updateFluidSpecimen()---------");
      FluidSpecimen fs = null; 

      fluidSpec.setApplyChangesTo("ApplyAll");

      Collection externalIdentifierCollection = new HashSet();
      ExternalIdentifier externalIdentifier = new ExternalIdentifier();
      externalIdentifier.setName("Code ID.");
      externalIdentifier.setValue(codeId);
      externalIdentifier.setSpecimen(fluidSpec);
      externalIdentifierCollection.add(externalIdentifier);

      fluidSpec.setExternalIdentifierCollection(externalIdentifierCollection);

      try {
         fs = (FluidSpecimen)appService.updateObject(fluidSpec); 
         System.out.println("Object updated successfully");
         System.out.println("Tissue specimen updated: collection status: " +fs.getCollectionStatus()+
                            " Label: "+fs.getLabel()+ " Av.Qty: "+fs.getAvailableQuantity()+ " Id: "+fs.getObjectId());
      } catch(Exception e) {
         System.out.println("ImportFluidSpecimen.updateFluidSpecimen()"+e.getMessage());
         e.printStackTrace();
      }
      System.out.println("---------End ImportFluidSpecimen.updateFluidSpecimen()---------");
      return fs;
   }

   public static FluidSpecimen setFluidSpecimenAttributes (SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      String opDate	  = excel[rowNo][10];
      String octs         = excel[rowNo][17];
      String pathStatus   = excel[rowNo][18];
      String lnvial	  = excel[rowNo][19];
      String tsite	  = excel[rowNo][20];
      String tside	  = excel[rowNo][21];
      String specType 	  = "";
      Date createdOn	  = null; 
      boolean octsVal = false;
      double quantity = 0;

      System.out.println("---------START ImportFluidSpecimen.setFluidSpecimenAttributes()---------");
      //System.out.println("opDate: "+createdOn+ " codeId: "+codeId+ " ocfs. "+ocfs. " specType: "+specType+
      //                   " ocfs.al: "+ocfs.al+ " lnvial: "+lnvial+ " qty: "+quantity);
      FluidSpecimen fs = new FluidSpecimen();

      fs.setSpecimenClass("Fluid");
      //octsVal = Boolean.parseBoolean(octs);
      //specType = octsVal ? "Fixed Tissue" : "Frozen Tissue";
      fs.setSpecimenType("Whole Blood");

      SpecimenCharacteristics sc = ImportSpecimen.setSpChar(tsite,tside,rowNo);
      fs.setSpecimenCharacteristics(sc);

      fs.setPathologicalStatus(pathStatus);
      try {
         createdOn = CommonUtilities.convertDateFromExcel(opDate);
         fs.setCreatedOn(createdOn);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +createdOn);
      }

      quantity = Double.parseDouble(lnvial);
      fs.setInitialQuantity(quantity);
      fs.setAvailableQuantity(quantity);

      //System.out.println("opDate: "+createdOn+ " codeId: "+codeId+ " ocfs. "+ocfs. " specType: "+specType+
      //                   " ocfs.al: "+ocfs.al+ " lnvial: "+lnvial+ " qty: "+quantity);

      CollectionEventParameters cep = ImportSpecimenEventParameters.addCEP(scg, excel, rowNo);
      cep.setSpecimen(fs);

      ReceivedEventParameters rep = ImportSpecimenEventParameters.addREP(scg, excel, rowNo);
      rep.setSpecimen(fs);

      Collection seCollection = new HashSet();
      seCollection.add(cep);
      seCollection.add(rep);
      fs.setSpecimenEventCollection(seCollection);

      System.out.println("---------END ImportFluidSpecimen.setFluidSpecimenAttributes()---------");
      return fs;
   } 

   public static void setLabelAndBarcode (String lineage, FluidSpecimen fs, String codeId, int rowNo, int currentAliquot) {

      System.out.println("---------START ImportFluidSpecimen.setLabelAndBarcode()---------");
      if ( (lineage.equals("New")) || (lineage.equals("Derived")) ) {
         fs.setLabel(codeId+"-"+lineage);
         fs.setBarcode(codeId+"-"+lineage);
      } else if (lineage.equals("Aliquot")) {
         System.out.println("currentAliquot = "+currentAliquot);
         if (currentAliquot < 10) {
            fs.setLabel(codeId+"-0"+currentAliquot);
            fs.setBarcode(codeId+"-0"+currentAliquot);
         } else {
            fs.setLabel(codeId+"-"+currentAliquot);
            fs.setBarcode(codeId+"-"+currentAliquot);
         }   
         //System.out.println("currentAliquot = "+currentAliquot+ " Label = "+codeId+currentAliquot);
      }
      System.out.println("---------END ImportFluidSpecimen.setLabelAndBarcode()---------");
   } 
}
 //end ImportFluidSpecimen()
