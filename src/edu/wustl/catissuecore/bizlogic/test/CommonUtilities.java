package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import java.util.Collection;
import java.util.Iterator;
import java.util.HashSet;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;


public class CommonUtilities {

    public static Date convertDateFromExcel (String s) throws ParseException {

       Date convertedDate = new Date();
       String pattern = "MM-dd-yyyy";
       SimpleDateFormat formatter = new SimpleDateFormat(pattern);
       convertedDate = formatter.parse(s);
       System.out.println("Date from excel is: "+s+" Converted Date is "+convertedDate);
        
      // Courtesy: http://devpinoy.org/blogs/lamia/archive/2008/03/25/parsing-excel-date-with-poi-jxls.aspx

      //Date actualDate=HSSFDateUtil.getJavaDate(Double.parseDouble(d), true);
      //return actualDate;
      return convertedDate;
    }

   public static Collection<ConsentTierStatus> setConsentTierStatus (CollectionProtocolRegistration cpr) {

      CollectionProtocol collectionProtocol = cpr.getCollectionProtocol();
      Collection consentTierCollection = collectionProtocol.getConsentTierCollection();
      Iterator consentTierItr = consentTierCollection.iterator();
      Collection consentTierStatusCollection = new HashSet();
      while(consentTierItr.hasNext()) {
         ConsentTier consentTier = (ConsentTier)consentTierItr.next();
         ConsentTierStatus consentStatus = new ConsentTierStatus();
         consentStatus.setConsentTier(consentTier);
         consentStatus.setStatus("Yes");
         consentTierStatusCollection.add(consentStatus);
      }
      return consentTierStatusCollection;
   }

}
