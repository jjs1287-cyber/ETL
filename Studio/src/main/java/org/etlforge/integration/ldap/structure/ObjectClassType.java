package org.etlforge.integration.ldap.structure;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import org.etlforge.util.jndi.JndiUtils;

public enum ObjectClassType {
   ABSTRACT,
   STRUCTURAL,
   AUXILIARY;

   public static ObjectClassType fromAttributes(Attributes var0) throws NamingException {
      for (ObjectClassType var4 : values()) {
         String var5 = JndiUtils.getSingleStringAttributeValue(var0, var4.name(), "false");
         if ("true".equals(var5)) {
            return var4;
         }
      }

      return STRUCTURAL;
   }
}
