package org.etlforge.integration.structure.crom;

import java.io.Writer;
import org.etlforge.integration.structure.mapping.StringIndexMap;
import org.xml.sax.Attributes;

public class CROM_Ldap extends CROM_ext {
   public static final String ELEMENT = "Ldap";

   public CROM_Ldap() {
   }

   public CROM_Ldap(Attributes var1) {
   }

   public String toString(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var1);
      var2.append('<').append("Ldap");
      var2.append("/>\n");
      return var2.toString();
   }

   @Override
   public void writeXml(Writer var1, StringIndexMap var2) {
   }

   public CROM_DB clone() {
      return (CROM_DB)super.clone();
   }
}
