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

      String codeId	  = excel[rowNo][16];

      System.out.println("---------START ImportTissueSpecimen.createTissueSpecimen()---------");
      TissueSpecimen ts = null;
      try {
         ts = setTissueSpecimenAttributes(scg, excel, rowNo);
         ts.setParentSpecimen(ps);
         ts.setLineage(lineage);
         ts.setLabel(codeId+"-"+UniqueKeyGeneratorUtil.getUniqueKey()+"-"+lineage);
         ts.setBarcode(codeId+"-"+UniqueKeyGeneratorUtil.getUniqueKey()+"-"+lineage);
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

   public static TissueSpecimen updateTissueSpecimen (TissueSpecimen tissueSpec, String excel[][], int rowNo) {

      String codeId	  = excel[rowNo][16];

      System.out.println("---------START ImportTissueSpecimen.updateTissueSpecimen()---------");
      TissueSpecimen ts = null; 

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

   public static TissueSpecimen setTissueSpecimenAttributes (SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      String opDate	  = excel[rowNo][10];
      String codeId	  = excel[rowNo][16];
      String octs	  = excel[rowNo][17];
      String pathStatus   = excel[rowNo][18];
      String lnvial	  = excel[rowNo][19];
      String specType 	  = "";
      Date createdOn	  = null; 
      boolean octsVal = false;
      double quantity = 0;

      System.out.println("---------START ImportTissueSpecimen.setTissueSpecimenAttributes()---------");
      System.out.println("opDate: "+createdOn+ " codeId: "+codeId+ " octs: "+octs+ " specType: "+specType+
                         " octsVal: "+octsVal+ " lnvial: "+lnvial+ " qty: "+quantity);
      TissueSpecimen ts = new TissueSpecimen();

      //ts.setLabel(codeId);
      //ts.setBarcode(codeId);

      ts.setSpecimenClass("Tissue");
      octsVal = Boolean.parseBoolean(octs);
      specType = octsVal ? "Fixed Tissue" : "Frozen Tissue";
      ts.setSpecimenType(specType);

      SpecimenCharacteristics sc = setSpChar(excel, rowNo);
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

      System.out.println("opDate: "+createdOn+ " codeId: "+codeId+ " octs: "+octs+ " specType: "+specType+
                         " octsVal: "+octsVal+ " lnvial: "+lnvial+ " qty: "+quantity);

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

   public static SpecimenCharacteristics setSpChar (String excel[][], int rowNo) {

      String tsite = excel[rowNo][20];
      String tside = excel[rowNo][21];

      System.out.println("---------START ImportTissueSpecimen.setSpChar()---------");
      SpecimenCharacteristics sc =  new SpecimenCharacteristics();
      sc.setTissueSite(tsite);
      sc.setTissueSide(tside);
      System.out.println("---------END ImportTissueSpecimen.setSpChar()---------");
      return sc;   
   }   

   public static Collection setExternalIdentifier(String excel[][], int rowNo) {

      String codeId	  = excel[rowNo][16];
     
      System.out.println("---------START ImportTissueSpecimen.setExternalIdentifier()---------");
      Collection externalIdentifierCollection = new HashSet();
      ExternalIdentifier externalIdentifier = new ExternalIdentifier();
      externalIdentifier.setName("Code ID.");
      externalIdentifier.setValue(codeId);
      externalIdentifierCollection.add(externalIdentifier);
      System.out.println("---------END ImportTissueSpecimen.setExternalIdentifier()---------");
      return externalIdentifierCollection;
   }
} //end ImportTissueSpecimen()
