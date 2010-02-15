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
public class ImportSpecimen extends CaTissueBaseTestCase {

   public static void addSpecimens(SpecimenCollectionGroup scg, String excel[][], int rowNo) {
       
       String codeId   = excel[rowNo][16];
       String lnvial   = excel[rowNo][19];
       int totalNoAliquots = Integer.parseInt(lnvial);
       
       System.out.println("---------START ImportSpecimen.addSpecimens()---------");
       if (codeId.startsWith("BF")) {
         //Create Fluid Specimen
         //FluidSpecimen fs = ImportFliuidSpecimen.createFluidSpecimen (null,new,scg,excel,rowNo);
       } else {
         //Create Tissue Specimens

         //Parent Tissue Specimen
         TissueSpecimen ps = ImportTissueSpecimen.createTissueSpecimen (null,"New",scg,excel,rowNo);

         //Derived Tissue Specimen
         TissueSpecimen ds = ImportTissueSpecimen.createTissueSpecimen (ps,"Derived",scg,excel,rowNo);

         //Aliquot Tissue Specimens
         TissueSpecimen as = null;
         //for (int currentAliquot = 1; currentAliquot <= totalNoAliquots; currentAliquot++) {
         for (int currentAliquot = 1; currentAliquot <= 2; currentAliquot++) {
            as = ImportTissueSpecimen.createAliquotTissueSpecimen (ds,"Aliquot",scg,excel,rowNo,currentAliquot);
         }

         //Update Parent and Child Specimens
         TissueSpecimen ups     = ImportTissueSpecimen.updateTissueSpecimen (ps,codeId,rowNo);
         TissueSpecimen uds = ImportTissueSpecimen.updateTissueSpecimen (ds,codeId,rowNo);
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
} //end ImportSpecimen()
