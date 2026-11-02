package org.etlforge.integration.salesforce;

import com.google.common.collect.Sets;
import java.util.Set;
import org.etlforge.xml.ExpandedName;
import org.etlforge.xml.XmlNameFactory;

public final class SimpleTypes {
   private static final Set<String> simpleSalesforceTypes = Sets.newHashSet(
      "ID", "QueryLocator", "StatusCode", "fieldType", "soapType", "layoutComponentType", "EmailPriority", "DebugLevel"
   );
   private static final String SFDC_NS = "urn:enterprise.soap.sforce.com";
   public static final ExpandedName STRING = XmlNameFactory.conforming().newExpandedName("http://www.w3.org/2001/XMLSchema", "string");

   public static boolean isSimple(ExpandedName var0) {
      String var1 = var0.getNamespace();
      if ("http://www.w3.org/2001/XMLSchema".equals(var1)) {
         return true;
      } else if ("urn:enterprise.soap.sforce.com".equals(var1)) {
         String var2 = var0.getLocalName();
         return simpleSalesforceTypes.contains(var2);
      } else {
         return false;
      }
   }

   private SimpleTypes() {
   }
}
