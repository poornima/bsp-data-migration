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
import edu.wustl.catissuecore.domain.DisposalEventParameters;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.global.Status;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportFluidSpecimen extends CaTissueBaseTestCase {

   public static void setLabelAndBarcode (String lineage, FluidSpecimen ts, String codeId, int rowno, int currentAliquot) {

      System.out.println("---------START ImportFluidSpecimen.setLabelAndBarcode()---------");
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
      System.out.println("---------END ImportFluidSpecimen.setLabelAndBarcode()---------");
   } 


   public static Specimen getAliquotByLabel(String label) {

      System.out.println("---------START ImportFluidSpecimen.getAliquotByLabel()---------");
      FluidSpecimen returnedspecimen = null; 
      FluidSpecimen specimen = new FluidSpecimen();
      specimen.setLabel(label);
      Logger.out.info(" searching domain object");
      try {
          List resultList = appService.search(FluidSpecimen.class, specimen);
          for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
             returnedspecimen = (FluidSpecimen) resultsIterator.next();
             System.out.println("Aliquot found is: " + returnedspecimen.getLabel() + " Id: " + returnedspecimen.getId());
             Logger.out.info(" Domain Object is successfully Found ---->  :: " + returnedspecimen.getLabel());
          }
          assertTrue("Specimen found", true);
      } catch (Exception e) {
          Logger.out.error(e.getMessage(), e);
          e.printStackTrace();
          assertFalse("Could not find Aliquot by label", true);
      }
      System.out.println("---------END ImportFluidSpecimen.getAliquotByLabel()---------");
      return returnedspecimen;
   }

   public static void createFluidSpecimen (SpecimenCollectionGroup scg, String excel[][], int rowno) throws Exception {

      String codeId             = excel[rowno][16];
      String accessdbdiagnosis = excel[rowno][22];

      Specimen specimenObj = initFluidSpecimen("New",excel,rowno,0);
      specimenObj.setSpecimenCollectionGroup(scg);
      specimenObj.setLabel(codeId);
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
      System.out.println("Before Creating Parent Fluid Specimen");
       try {
           specimenObj = (FluidSpecimen) appService.createObject(specimenObj);
           System.out.println("Spec:" + specimenObj.getLabel());
        } catch (Exception e)   {
                Logger.out.error(e.getMessage(), e);
                e.printStackTrace();
        }

       System.out.println(" Parent Fluid Domain Object is successfully added ---->    ID:: " + specimenObj.getId().toString());
       System.out.println(" Parent Fluid Domain Object is successfully added ---->    Name:: " + specimenObj.getLabel());

       String lnvial	    = excel[rowno][19];
       int  totalNoAliquots = Integer.parseInt(lnvial);
       System.out.println("total no aliquots to be created = "+totalNoAliquots); 
       for (int currentAliquot = 1; currentAliquot <= totalNoAliquots; currentAliquot++) {
        Specimen ts1 = initFluidSpecimen("Aliquot",excel,rowno,currentAliquot);
        ts1.setSpecimenCollectionGroup(scg);
        ts1.setLineage("Aliquot");
        ts1.setParentSpecimen(specimenObj);
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
        System.out.println("Befor creating Aliquot Fluid Specimen:"+currentAliquot);
        try {
                ts1 = (FluidSpecimen) appService.createObject(ts1);
                System.out.println("Spec:" + ts1.getLabel());
        } catch (Exception e)   {
                Logger.out.error(e.getMessage(), e);
                e.printStackTrace();
        }
       }
       System.out.println("After Creating Aliquots");
       DisposalEventParameters disposalEvent = new DisposalEventParameters();
       disposalEvent.setSpecimen(specimenObj);
       disposalEvent.setTimestamp(new Date(System.currentTimeMillis()));
       User user = new User();
       user.setId(new Long(1));//admin
       disposalEvent.setUser(user);
       disposalEvent.setReason("Disposing Specimen");
       disposalEvent.setComment("Dispose Event");
       disposalEvent.setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.toString());
       System.out.println("Before Creating DisposeEvent");
       try {
            disposalEvent = (DisposalEventParameters) appService.createObject(disposalEvent);
            System.out.println("Succesfully closed parent specimen "+specimenObj.getLabel());
       } catch (Exception e) {
            System.out.println("cannot create dispose event object");
            e.printStackTrace();
       }
       System.out.println("After Creating DisposeEvent");

   }
 
   //public static Specimen initFluidSpecimen(String lineage, String excel[][], int rowno, int currentAliquot) {
   public static Specimen initFluidSpecimen(String lineage, String excel[][], int rowno, int currentAliquot) throws Exception {

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
      String accessdbdiagnosis = excel[rowno][22];
      String specType 	  = "";
      Date createdOn	  = new Date(); 
      Date colDate        = new Date();
      Date rcvdDate       = new Date();
      boolean octsVal     = false;

       System.out.println("Inside fluidspecimen");
       FluidSpecimen ts= new FluidSpecimen();
       ts.setSpecimenClass("Fluid");
       if (lineage.equals("Aliquot")) { 
         if (currentAliquot < 10) {
           ts.setLabel(codeId+"-0"+currentAliquot);
           ts.setBarcode(codeId+"-0"+currentAliquot);
         } else {
           ts.setLabel(codeId+"-"+currentAliquot);
           ts.setBarcode(codeId+"-"+currentAliquot);
         }
       } else {
         ts.setLabel(codeId);
         ts.setBarcode(codeId);
       }
       ts.setActivityStatus("Active");
       ts.setCollectionStatus("Complete");

       //set specimen type 
       if (accessdbdiagnosis.contains("UNKNOWN BF") || accessdbdiagnosis.equals("U")) 
         ts.setSpecimenType("Bio Fluid");
       else if (accessdbdiagnosis.contains("CSF"))
         ts.setSpecimenType("Cerebrospinal Fluid");
       else if (accessdbdiagnosis.contains("CYST FLUID"))
         ts.setSpecimenType("Cyst Fluid");
       else if (accessdbdiagnosis.contains("PLASMA"))
         ts.setSpecimenType("Plasma");
       else if (accessdbdiagnosis.contains("BLOOD"))
         ts.setSpecimenType("Whole Blood");
       else if (accessdbdiagnosis.contains("SERUM"))
         ts.setSpecimenType("Serum");

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
       //try  {
         colDate = CommonUtilities.convertDateFromExcel(opDate);
         collectionEventParameters.setTimestamp(colDate);
       /*} catch (ParseException e1) {
         System.out.println("ERROR: could not parse date in String: " +colDate);
         e1.printStackTrace();
       }
       */
       collectionEventParameters.setContainer(colCont);
       collectionEventParameters.setCollectionProcedure(colProc);

       ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
       receivedEventParameters.setUser(user);
       //try {
         rcvdDate = CommonUtilities.convertDateFromExcel(accessionDate);
         receivedEventParameters.setTimestamp(rcvdDate);
       /*} catch (ParseException e) {
         System.out.println("ERROR: could not parse date in String: " +accessionDate);
         e.printStackTrace();
       }
       */
       receivedEventParameters.setReceivedQuality("Acceptable");
       receivedEventParameters.setSpecimen(ts);

       Collection specimenEventCollection = new HashSet();
       specimenEventCollection.add(collectionEventParameters);
       specimenEventCollection.add(receivedEventParameters);
       ts.setSpecimenEventCollection(specimenEventCollection);
       return ts;
    }

} //end ImportFluidSpecimen()
