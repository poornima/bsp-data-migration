package edu.wustl.catissuecore.bizlogic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;

public class CommonUtilities {

    public static Date convertDateFromExcel (String d) throws ParseException {

    // Courtesy: http://devpinoy.org/blogs/lamia/archive/2008/03/25/parsing-excel-date-with-poi-jxls.aspx

    Date actualDate=HSSFDateUtil.getJavaDate(Double.parseDouble(d), true);

    return actualDate;

    }
}
