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
public class ImportTissueSpecimen extends CaTissueBaseTestCase {

   public static TissueSpecimen createTissueSpecimen (TissueSpecimen ps, String lineage, SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      String codeId = excel[rowNo][16];

      System.out.println("---------START ImportTissueSpecimen.createTissueSpecimen()---------");
      TissueSpecimen ts = null;
      try {
         ts = setTissueSpecimenAttributes(scg,excel,rowNo);
         ts.setParentSpecimen(ps);
         ts.setLineage(lineage);
         setLabelAndBarcode(lineage,ts,codeId,rowNo,0);
         ts.setSpecimenCollectionGroup(scg);
         ts.setCollectionStatus("Collected");
         ts.setIsAvailable(true);  
         ts.setActivityStatus("Active");
         //ts.setConsentTierStatusCollectionFromSCG(scg);
         ts = (TissueSpecimen) appService.createObject(ts);
         System.out.println("Object created successfully");
         System.out.println("Tissue specimen added successfully: Lineage " +ts.getLineage()+
                            " Label: "+ts.getLabel()+ " Id: "+ts.getObjectId());
      } catch(Exception e) {
         System.out.println("ImportTissueSpecimen.createTissueSpecimen()"+e.getMessage());
         e.printStackTrace();
      }
      System.out.println("---------End ImportTissueSpecimen.createTissueSpecimen()---------");
      return ts;
   }

   public static TissueSpecimen createAliquotTissueSpecimen (TissueSpecimen ps, String lineage, SpecimenCollectionGroup scg,
                                                             String excel[][], int rowNo, int currentAliquot) {

      String codeId = excel[rowNo][16];
      String lnvial = excel[rowNo][19];

      Double qtyPerAliquot = 1.0; 

      System.out.println("---------START ImportTissueSpecimen.createAliquotTissueSpecimen()---------");
      TissueSpecimen ts = null;
      try {
         ts = setTissueSpecimenAttributes(scg,excel,rowNo);
         ts.setParentSpecimen(ps);
         ts.setLineage(lineage);
         setLabelAndBarcode(lineage,ts,codeId,rowNo,currentAliquot);
         ts.setSpecimenCollectionGroup(scg);
         ts.setInitialQuantity(qtyPerAliquot);
         ts.setAvailableQuantity(qtyPerAliquot);
         ts.setCollectionStatus("Collected");
         ts.setIsAvailable(true);
         ts.setActivityStatus("Active");

         Collection externalIdentifierCollection = new HashSet();
         ExternalIdentifier externalIdentifier = new ExternalIdentifier();
         externalIdentifier.setName("Code ID.");
         if (currentAliquot < 10) {
            externalIdentifier.setValue(codeId+"-0"+currentAliquot);
         } else {
            externalIdentifier.setValue(codeId+"-"+currentAliquot);
         }
         externalIdentifier.setSpecimen(ts);
         externalIdentifierCollection.add(externalIdentifier);

         ts.setExternalIdentifierCollection(externalIdentifierCollection);

         ts = (TissueSpecimen) appService.createObject(ts);
         System.out.println("Object created successfully");
         System.out.println("Tissue specimen added successfully: Lineage " +ts.getLineage()+
                            " Label: "+ts.getLabel()+ " Id: "+ts.getObjectId());
      } catch(Exception e) {
         System.out.println("ImportTissueSpecimen.createAliquotTissueSpecimen()"+e.getMessage());
         e.printStackTrace();
      }
      System.out.println("---------End ImportTissueSpecimen.createTissueSpecimen()---------");
      return ts;
   }

   public static TissueSpecimen updateTissueSpecimen (TissueSpecimen tissueSpec, String codeId, int rowNo) {

      System.out.println("---------START ImportTissueSpecimen.updateTissueSpecimen()---------");
      TissueSpecimen ts = null; 

      tissueSpec.setApplyChangesTo("ApplyAll");

      Collection externalIdentifierCollection = new HashSet();
      ExternalIdentifier externalIdentifier = new ExternalIdentifier();
      externalIdentifier.setName("Code ID.");
      externalIdentifier.setValue(codeId);
      externalIdentifier.setSpecimen(tissueSpec);
      externalIdentifierCollection.add(externalIdentifier);

      tissueSpec.setExternalIdentifierCollection(externalIdentifierCollection);

      try {
         ts = (TissueSpecimen)appService.updateObject(tissueSpec); 
         System.out.println("Object updated successfully");
         System.out.println("Tissue specimen updated: collection status: " +ts.getCollectionStatus()+
                            " Label: "+ts.getLabel()+ " Av.Qty: "+ts.getAvailableQuantity()+ " Id: "+ts.getObjectId());
      } catch(Exception e) {
         System.out.println("ImportTissueSpecimen.updateTissueSpecimen()"+e.getMessage());
         e.printStackTrace();
      }
      System.out.println("---------End ImportTissueSpecimen.updateTissueSpecimen()---------");
      return ts;
   }

   public static TissueSpecimen updateAliquotTissueSpecimen (TissueSpecimen tissueSpec, String codeId, int rowNo, int currentAliquot) {

      System.out.println("---------START ImportTissueSpecimen.updateAliquotTissueSpecimen()---------");
      TissueSpecimen ts = null;

      tissueSpec.setApplyChangesTo("ApplyAll");

      Collection externalIdentifierCollection = new HashSet();
      ExternalIdentifier externalIdentifier = new ExternalIdentifier();
      externalIdentifier.setName("Code ID.");
      if (currentAliquot < 10) {
         externalIdentifier.setValue(codeId+"-0"+currentAliquot);
      } else {
         externalIdentifier.setValue(codeId+"-"+currentAliquot);
      }
      externalIdentifier.setSpecimen(tissueSpec);
      externalIdentifierCollection.add(externalIdentifier);

      tissueSpec.setExternalIdentifierCollection(externalIdentifierCollection);

      try {
         ts = (TissueSpecimen)appService.updateObject(tissueSpec);
         System.out.println("Object updated successfully");
         System.out.println("Tissue specimen updated: collection status: " +ts.getCollectionStatus()+
                            " Label: "+ts.getLabel()+ " Av.Qty: "+ts.getAvailableQuantity()+ " Id: "+ts.getObjectId());
      } catch(Exception e) {
         System.out.println("ImportTissueSpecimen.updateTissueSpecimen()"+e.getMessage());
         e.printStackTrace();
      }
      System.out.println("---------End ImportTissueSpecimen.updateAliquotTissueSpecimen()---------");
      return ts;
   }


   public static TissueSpecimen setTissueSpecimenAttributes (SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      String opDate	  = excel[rowNo][10];
      String octs	  = excel[rowNo][17];
      String pathStatus   = excel[rowNo][18];
      String lnvial	  = excel[rowNo][19];
      String tsite	  = excel[rowNo][20];
      String tside	  = excel[rowNo][21];
      String specType 	  = "";
      Date createdOn	  = null; 
      boolean octsVal = false;
      double quantity = 0;

      System.out.println("---------START ImportTissueSpecimen.setTissueSpecimenAttributes()---------");
      //System.out.println("opDate: "+createdOn+ " codeId: "+codeId+ " octs: "+octs+ " specType: "+specType+
      //                   " octsVal: "+octsVal+ " lnvial: "+lnvial+ " qty: "+quantity);
      TissueSpecimen ts = new TissueSpecimen();

      ts.setSpecimenClass("Tissue");
      octsVal = Boolean.parseBoolean(octs);
      specType = octsVal ? "Fixed Tissue" : "Frozen Tissue";
      ts.setSpecimenType(specType);

      SpecimenCharacteristics sc = ImportSpecimen.setSpChar(tsite,tside,rowNo);
      ts.setSpecimenCharacteristics(sc);

      ts.setPathologicalStatus(pathStatus);
      try {
         createdOn = CommonUtilities.convertDateFromExcel(opDate);
         ts.setCreatedOn(createdOn);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +createdOn);
      }

      quantity = Double.parseDouble(lnvial);
      ts.setInitialQuantity(quantity);
      ts.setAvailableQuantity(quantity);

      //System.out.println("opDate: "+createdOn+ " codeId: "+codeId+ " octs: "+octs+ " specType: "+specType+
      //                   " octsVal: "+octsVal+ " lnvial: "+lnvial+ " qty: "+quantity);

      CollectionEventParameters cep = ImportSpecimenEventParameters.addCEP(scg, excel, rowNo);
      cep.setSpecimen(ts);

      ReceivedEventParameters rep = ImportSpecimenEventParameters.addREP(scg, excel, rowNo);
      rep.setSpecimen(ts);

      Collection seCollection = new HashSet();
      seCollection.add(cep);
      seCollection.add(rep);
      ts.setSpecimenEventCollection(seCollection);

      System.out.println("---------END ImportTissueSpecimen.setTissueSpecimenAttributes()---------");
      return ts;
   } 

   public static void setLabelAndBarcode (String lineage, TissueSpecimen ts, String codeId, int rowNo, int currentAliquot) {

      System.out.println("---------START ImportTissueSpecimen.setLabelAndBarcode()---------");
      if ( (lineage.equals("New")) || (lineage.equals("Derived")) ) {
         ts.setLabel(codeId+UniqueKeyGeneratorUtil.getUniqueKey()+lineage);
         ts.setBarcode(codeId+UniqueKeyGeneratorUtil.getUniqueKey()+lineage);
         //ts.setLabel(codeId+"-"+lineage);
         //ts.setBarcode(codeId+"-"+lineage);
      } else if (lineage.equals("Aliquot")) {
         System.out.println("currentAliquot = "+currentAliquot);
         ts.setLabel(codeId+UniqueKeyGeneratorUtil.getUniqueKey()+lineage);
         ts.setBarcode(codeId+UniqueKeyGeneratorUtil.getUniqueKey()+lineage);
/*
         if (currentAliquot < 10) {
            ts.setLabel(codeId+"-0"+currentAliquot);
            ts.setBarcode(codeId+"-0"+currentAliquot);
         } else {
            ts.setLabel(codeId+"-"+currentAliquot);
            ts.setBarcode(codeId+"-"+currentAliquot);
         }   
*/
         //System.out.println("currentAliquot = "+currentAliquot+ " Label = "+codeId+currentAliquot);
      }
      System.out.println("---------END ImportTissueSpecimen.setLabelAndBarcode()---------");
   } 

   public static TissueSpecimen getAliquotByLabel(String label) {

      System.out.println("---------START ImportTissueSpecimen.getAliquotByLabel()---------");
      TissueSpecimen returnedspecimen = null; 
      TissueSpecimen specimen = new TissueSpecimen();
      specimen.setLabel(label);
      Logger.out.info(" searching domain object");
      try {
          List resultList = appService.search(TissueSpecimen.class, specimen);
          for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
             returnedspecimen = (TissueSpecimen) resultsIterator.next();
             System.out.println("Aliquot found is: " + returnedspecimen.getLabel() + " Id: " + returnedspecimen.getId());
             Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
          }
          assertTrue("Specimen found", true);
      } catch (Exception e) {
          Logger.out.error(e.getMessage(), e);
          e.printStackTrace();
          assertFalse("Could not find Aliquot by label", true);
      }
      System.out.println("---------END ImportTissueSpecimen.getAliquotByLabel()---------");
      return returnedspecimen;
   }
} //end ImportTissueSpecimen()
