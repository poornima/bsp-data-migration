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
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportTissueSpecimen extends CaTissueBaseTestCase {

   public static void setLabelAndBarcode (String lineage, TissueSpecimen ts, String codeId, int rowno, int currentAliquot) {

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

   public static void createTissueSpecimen (SpecimenCollectionGroup scg, String excel[][], int rowno) {

      String codeId             = excel[rowno][16];
      String accessdbdiagnosis = excel[rowno][22];

      Specimen specimenObj = initTissueSpecimen("New",excel,rowno,0);
      specimenObj.setSpecimenCollectionGroup(scg);
      specimenObj.setLabel(codeId+"-New");
      specimenObj.setIsAvailable(Boolean.TRUE);
      specimenObj.setCollectionStatus("Collected");
      specimenObj.setActivityStatus("Active");
      Collection eidc1 = new HashSet();
      ExternalIdentifier eid1 = new ExternalIdentifier();
      eid1.setName("Code ID.");
      eid1.setValue(codeId);
      eid1.setSpecimen(specimenObj);
      eidc1.add(eid1);
      ExternalIdentifier eid2 = new ExternalIdentifier();
      eid2.setName("Diagnosis1");
      eid2.setValue(accessdbdiagnosis);
      eid2.setSpecimen(specimenObj);
      eidc1.add(eid2);
      specimenObj.setExternalIdentifierCollection(eidc1);
      System.out.println("Before Creating Parent Tissue Specimen");
       try {
           specimenObj = (TissueSpecimen) appService.createObject(specimenObj);
           System.out.println("Spec:" + specimenObj.getLabel());
        } catch (Exception e)   {
                Logger.out.error(e.getMessage(), e);
                e.printStackTrace();
        }

       System.out.println(" Parent Cell Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
       System.out.println(" Parent Cell Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());

       Specimen deriveSp = initTissueSpecimen("Derived",excel,rowno,0);
       deriveSp.setLineage("Derived");
       deriveSp.setLabel(codeId+"-Derived");
       deriveSp.setSpecimenCollectionGroup(scg);
       deriveSp.setParentSpecimen(specimenObj);
       deriveSp.setIsAvailable(Boolean.TRUE);
       deriveSp.setCollectionStatus("Collected");
       deriveSp.setActivityStatus("Active");
       Collection eidc2 = new HashSet();
       ExternalIdentifier eid3 = new ExternalIdentifier();
       eid3.setName("Code ID.");
       eid3.setValue(codeId);
       eid3.setSpecimen(deriveSp);
       eidc2.add(eid3);
       ExternalIdentifier eid4 = new ExternalIdentifier();
       eid4.setName("Diagnosis1");
       eid4.setValue(accessdbdiagnosis);
       eid4.setSpecimen(deriveSp);
       eidc2.add(eid4);
       deriveSp.setExternalIdentifierCollection(eidc2);
       System.out.println("Before Creating Derived Tissue Specimen");
       try {
          deriveSp = (TissueSpecimen) appService.createObject(deriveSp);
          System.out.println("Spec:" + deriveSp.getLabel());
        } catch (Exception e)   {
                Logger.out.error(e.getMessage(), e);
                e.printStackTrace();
        }

       System.out.println(" Derived Domain Object is successfully added ---->    ID:: " + deriveSp.getId().toString());
       System.out.println(" Derived Domain Object is successfully added ---->    Name:: " + deriveSp.getLabel());

       String lnvial	    = excel[rowno][19];
       int  totalNoAliquots = Integer.parseInt(lnvial);
       System.out.println("total no aliquots to be created = "+totalNoAliquots); 
       for (int currentAliquot = 1; currentAliquot <= totalNoAliquots; currentAliquot++) {
        Specimen ts1 = initTissueSpecimen("Aliquot",excel,rowno,currentAliquot);
        ts1.setSpecimenCollectionGroup(scg);
        ts1.setLineage("Aliquot");
        ts1.setParentSpecimen(deriveSp);
        if (currentAliquot < 10) {
           ts1.setLabel(codeId+"-0"+currentAliquot);
        } else {
           ts1.setLabel(codeId+"-"+currentAliquot);
        }
        ts1.setIsAvailable(Boolean.TRUE);
        ts1.setCollectionStatus("Collected");
        ts1.setActivityStatus("Active");
        Collection eidc3 = new HashSet();
        ExternalIdentifier eid5 = new ExternalIdentifier();
        eid5.setName("Code ID.");
        eid5.setValue(codeId);
        eid5.setSpecimen(ts1);
        eidc3.add(eid5);
        ExternalIdentifier eid6 = new ExternalIdentifier();
        eid6.setName("Diagnosis1");
        eid6.setValue(accessdbdiagnosis);
        eid6.setSpecimen(ts1);
        eidc3.add(eid6);
        ts1.setExternalIdentifierCollection(eidc3);
        System.out.println("Befor creating Aliquot Cell Specimen:"+currentAliquot);
        try {
                ts1 = (TissueSpecimen) appService.createObject(ts1);
                System.out.println("Spec:" + ts1.getLabel());
        } catch (Exception e)   {
                Logger.out.error(e.getMessage(), e);
                e.printStackTrace();
        }
       }
   }
 
   public static Specimen initTissueSpecimen(String lineage, String excel[][], int rowno, int currentAliquot) {

      String opDate	  = excel[rowno][10];
      String surgeon      = excel[rowno][11];
      String colProc      = excel[rowno][12];
      String colCont      = excel[rowno][13];
      String accessionDate= excel[rowno][14];
      String rcvdQuality  = excel[rowno][15];
      String codeId       = excel[rowno][16];
      String octs	  = excel[rowno][17];
      String pathStatus   = excel[rowno][18];
      String lnvial	  = excel[rowno][19];
      String tsite	  = excel[rowno][20];
      String tside	  = excel[rowno][21];
      String specType 	  = "";
      Date createdOn	  = new Date(); 
      Date colDate        = new Date();
      Date rcvdDate       = new Date();
      boolean octsVal     = false;

       System.out.println("Inside tissuespecimen");
       TissueSpecimen ts= new TissueSpecimen();
       ts.setSpecimenClass("Tissue");
       if (lineage.equals("Aliquot")) { 
         if (currentAliquot < 10) {
           ts.setLabel(codeId+"-0"+currentAliquot);
           ts.setBarcode(codeId+"-0"+currentAliquot);
         } else {
           ts.setLabel(codeId+"-"+currentAliquot);
           ts.setBarcode(codeId+"-"+currentAliquot);
         }
       } else {
         ts.setLabel(codeId+"-"+lineage);
         ts.setBarcode(codeId+"-"+lineage);
       }
       ts.setActivityStatus("Active");
       ts.setCollectionStatus("Complete");
       ts.setSpecimenType("Frozen Tissue");
       ts.setPathologicalStatus(pathStatus);

       SpecimenCharacteristics specimenCharacteristics =  new SpecimenCharacteristics();
       specimenCharacteristics.setTissueSite(tsite);
       specimenCharacteristics.setTissueSide(tside);
       ts.setSpecimenCharacteristics(specimenCharacteristics);

       if (lineage.equals("Aliquot")) {
        Double qty = 1.0;
        ts.setInitialQuantity(qty);
        ts.setAvailableQuantity(qty);
      } else {
        Double quantity = Double.parseDouble(lnvial);
        ts.setInitialQuantity(quantity);
        ts.setAvailableQuantity(quantity);
      }
       ts.setIsAvailable(new Boolean(true));

       System.out.println("Setting parameters");

       CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
       collectionEventParameters.setComment("Surgeon: "+surgeon);
       collectionEventParameters.setSpecimen(ts);
       User user = new User();
       user.setId(new Long(1));
       collectionEventParameters.setUser(user);
       try  {
         colDate = CommonUtilities.convertDateFromExcel(opDate);
         collectionEventParameters.setTimestamp(colDate);
       } catch (ParseException e1) {
         System.out.println("ERROR: could not parse date in String: " +colDate);
         e1.printStackTrace();
       }
       collectionEventParameters.setContainer(colCont);
       collectionEventParameters.setCollectionProcedure(colProc);

       ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
       receivedEventParameters.setUser(user);
       try {
         rcvdDate = CommonUtilities.convertDateFromExcel(accessionDate);
         receivedEventParameters.setTimestamp(rcvdDate);
       } catch (ParseException e) {
         System.out.println("ERROR: could not parse date in String: " +accessionDate);
         e.printStackTrace();
       }
       receivedEventParameters.setReceivedQuality("Acceptable");
       receivedEventParameters.setSpecimen(ts);

       Collection specimenEventCollection = new HashSet();
       specimenEventCollection.add(collectionEventParameters);
       specimenEventCollection.add(receivedEventParameters);
       ts.setSpecimenEventCollection(specimenEventCollection);
       return ts;
    }

} //end ImportTissueSpecimen()
