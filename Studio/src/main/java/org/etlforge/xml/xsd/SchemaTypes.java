package org.etlforge.xml.xsd;

import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class SchemaTypes {
   private static final XmlNameFactory nameFactory = XmlNameFactory.conforming();
   public static final ExpandedName DATE = expName("date");
   public static final ExpandedName DATE_TIME = expName("dateTime");

   private static final ExpandedName expName(String var0) {
      return nameFactory.newExpandedName("http://www.w3.org/2001/XMLSchema", var0);
   }

   private SchemaTypes() {
   }
}
