package edu.wustl.catissuecore.bizlogic.test;

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

   public static void addSEP(SpecimenCollectionGroup scg, String excel[][]) {

      CollectionEventParameters cep = addCEP(scg, excel);
      ReceivedEventParameters rep = addREP(scg, excel);
      Collection<SpecimenEventParameters> sepCollection = new HashSet<SpecimenEventParameters>();
      sepCollection.add(cep);
      sepCollection.add(rep);
      scg.setSpecimenEventParametersCollection(sepCollection); 
   }  

   public static CollectionEventParameters addCEP(SpecimenCollectionGroup scg, String excel[][]) {

      String opDate       = excel[row][10];
      String surgeon      = excel[row][11];
      String colProc      = excel[row][12];
      String colCont      = excel[row][13];

      //CP Collection Events
      System.out.println("---------START ImportSpecimenEventParameters.addCEP()---------");
      CollectionEventParameters cep = new CollectionEventParameters();
      cep.setComment("Surgeon: "+surgeon);
      cep.setCollectionProcedure(colProc);
      cep.setContainer(colCont);
      Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
      cep.setTimestamp(timestamp);
      //cep.setTimestamp(opDate);
      User user = getUser(CaTissueBaseTestCase.USER_NAME);
      cep.setUser(user);
      cep.setSpecimenCollectionGroup(scg);
      System.out.println("---------END ImportSpecimenEventParameters.addCEP()---------");
      return cep;
   }

   public static ReceivedEventParameters addREP(SpecimenCollectionGroup scg, String excel[][]) {

      String accessionDate = excel[row][14];

      //CP Received Events
      System.out.println("---------START ImportSpecimenEventParameters.addREP()---------");
      ReceivedEventParameters rep = new ReceivedEventParameters();
      rep.setComment("");
      User receivedUser = getUser(CaTissueBaseTestCase.USER_NAME);
      rep.setUser(receivedUser);
      rep.setReceivedQuality("Not Specified");
      Date timestamp = EventsUtil.setTimeStamp("08-15-1975","15","45");
      rep.setTimestamp(timestamp);
      //rep.setTimestamp(accessionDate);
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
