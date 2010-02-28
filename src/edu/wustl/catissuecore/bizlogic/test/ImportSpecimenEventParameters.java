package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.CollectionEventParameters;
import edu.wustl.catissuecore.domain.ReceivedEventParameters;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * This class contains the functionality of migrating the domain data
 * from an excel sheet into the caTissue database.
 * @author vishvesh_mulay
 *
 */
public class ImportSpecimenEventParameters extends CaTissueBaseTestCase {

   public static void addSEP(SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      CollectionEventParameters cep = addCEP(scg, excel, rowNo);
      ReceivedEventParameters rep = addREP(scg, excel, rowNo);
      Collection<SpecimenEventParameters> sepCollection = new HashSet<SpecimenEventParameters>();
      sepCollection.add(cep);
      sepCollection.add(rep);
      scg.setSpecimenEventParametersCollection(sepCollection); 
   } 

   public static CollectionEventParameters addCEP(SpecimenCollectionGroup scg, String excel[][], int row) {

      String opDate       = excel[row][10];
      String surgeon      = excel[row][11];
      String colProc      = excel[row][12];
      String colCont      = excel[row][13];
      Date   colDate = new Date();

      //CP Collection Events
      System.out.println("---------START ImportSpecimenEventParameters.addCEP()---------");
      CollectionEventParameters cep = new CollectionEventParameters();
      cep.setComment("Surgeon: "+surgeon);
      cep.setCollectionProcedure(colProc);
      cep.setContainer(colCont);
      
      try {
         colDate = CommonUtilities.convertDateFromExcel(opDate);
         cep.setTimestamp(colDate);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +colDate);
      }
      User user = getUser(CaTissueBaseTestCase.USER_NAME);
      cep.setUser(user);
      cep.setSpecimenCollectionGroup(scg);
      System.out.println("---------END ImportSpecimenEventParameters.addCEP()---------");
      return cep;
   }

   public static ReceivedEventParameters addREP(SpecimenCollectionGroup scg, String excel[][], int row) {

      String accessionDate = excel[row][14];
      String rcvdQuality   = excel[row][15];
      Date   rcvdDate = new Date();

      //CP Received Events
      System.out.println("---------START ImportSpecimenEventParameters.addREP()---------");
      ReceivedEventParameters rep = new ReceivedEventParameters();
      rep.setComment("");
      User receivedUser = getUser(CaTissueBaseTestCase.USER_NAME);
      rep.setUser(receivedUser);
      rep.setReceivedQuality(rcvdQuality);
      try {
         rcvdDate = CommonUtilities.convertDateFromExcel(accessionDate);
         rep.setTimestamp(rcvdDate);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +accessionDate);
      }
      rep.setSpecimenCollectionGroup(scg);
      System.out.println("---------END ImportSpecimenEventParameters.addREP()---------");
      return rep;
   }
  
   public static CollectionEventParameters insertCEP(TissueSpecimen ts, SpecimenCollectionGroup scg, String excel[][], int row) {

      String opDate       = excel[row][10];
      String surgeon      = excel[row][11];
      String colProc      = excel[row][12];
      String colCont      = excel[row][13];
      Date   colDate = new Date();

      //CP Collection Events
      System.out.println("---------START ImportSpecimenEventParameters.insertCEP()---------");
      CollectionEventParameters cep = new CollectionEventParameters();
      cep.setComment("Surgeon: "+surgeon);
      cep.setCollectionProcedure(colProc);
      cep.setContainer(colCont);

      try {
         colDate = CommonUtilities.convertDateFromExcel(opDate);
         cep.setTimestamp(colDate);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +colDate);
      }
      User user = getUser(CaTissueBaseTestCase.USER_NAME);
      cep.setUser(user);
      cep.setSpecimenCollectionGroup(scg);
      cep.setSpecimen(ts);
      try {
         cep = (CollectionEventParameters) appService.createObject(cep);
         System.out.println("CollectionEventParameters obj created successfully");
         System.out.println("CEP id = "+cep.getId()+" specimen = "+cep.getSpecimen()+" class = "+cep.getClass());
      } catch (Exception e) {
        System.out.println("Cannot create collection event parameter object in ImportSpecimenEventParameters.insertCEP()"+e.getMessage());
        e.printStackTrace();
      }
      System.out.println("---------END ImportSpecimenEventParameters.insertCEP()---------");
      return cep;
   }

  public static ReceivedEventParameters insertREP(TissueSpecimen ts, SpecimenCollectionGroup scg, String excel[][], int row) {

      String accessionDate = excel[row][14];
      String rcvdQuality   = excel[row][15];
      Date   rcvdDate = new Date();

      //CP Received Events
      System.out.println("---------START ImportSpecimenEventParameters.addREP()---------");
      ReceivedEventParameters rep = new ReceivedEventParameters();
      rep.setComment("");
      User receivedUser = getUser(CaTissueBaseTestCase.USER_NAME);
      rep.setUser(receivedUser);
      rep.setReceivedQuality(rcvdQuality);
      try {
         rcvdDate = CommonUtilities.convertDateFromExcel(accessionDate);
         rep.setTimestamp(rcvdDate);
      } catch (ParseException pe) {
         System.out.println("ERROR: could not parse date in String: " +accessionDate);
      }

      rep.setSpecimenCollectionGroup(scg);
      rep.setSpecimen(ts);
      try {
         rep = (ReceivedEventParameters) appService.createObject(rep);
         System.out.println("ReceivedEventParameters obj created successfully");
         System.out.println("REP id = "+rep.getId()+" specimen = "+rep.getSpecimen()+" class = "+rep.getClass());
      } catch (Exception e) {
        System.out.println("Cannot create collection event parameter object in ImportSpecimenEventParameters.insertREP()"+e.getMessage());
        e.printStackTrace();
      }
      System.out.println("---------END ImportSpecimenEventParameters.insertREP()---------");
      return rep;
   }
 
   private static User getUser(String loginName) {

      User user = new User();
      user.setLoginName(loginName);
      try {
          List resultList = appService.search(User.class, user);
          if (resultList != null && resultList.size() > 0) {
            user = (User) resultList.get(0);
          }
       } catch (Exception e) {
          System.out.println("Exception in getUser");
          e.printStackTrace();
       }
       return user;
   }

} //end ImportSpecimenEventParameters()
