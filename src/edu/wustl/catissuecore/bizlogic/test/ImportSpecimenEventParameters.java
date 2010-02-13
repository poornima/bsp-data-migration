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

   private static int row = 1;

   public static void addSEP(SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      CollectionEventParameters cep = addCEP(scg, excel, rowNo);
      ReceivedEventParameters rep = addREP(scg, excel, rowNo);
      Collection<SpecimenEventParameters> sepCollection = new HashSet<SpecimenEventParameters>();
      sepCollection.add(cep);
      sepCollection.add(rep);
      scg.setSpecimenEventParametersCollection(sepCollection); 
   }  

   public static CollectionEventParameters addCEP(SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      int row = rowNo; 
      String opDate       = excel[row][10];
      String surgeon      = excel[row][11];
      String colProc      = excel[row][12];
      String colCont      = excel[row][13];
      Date   colDate = null;

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

   public static ReceivedEventParameters addREP(SpecimenCollectionGroup scg, String excel[][], int rowNo) {

      int row = rowNo;
      String accessionDate = excel[row][14];
      String rcvdQuality   = excel[row][15];
      Date   rcvdDate = null;

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
