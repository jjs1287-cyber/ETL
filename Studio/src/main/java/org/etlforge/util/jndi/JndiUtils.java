package org.etlforge.util.jndi;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

public final class JndiUtils {
   public static void close(Context var0) {
      if (var0 != null) {
         try {
            var0.close();
         } catch (NamingException var2) {
            var2.printStackTrace();
         }
      }
   }

   public static String getSingleStringAttributeValue(Attributes var0, String var1, String var2) throws NamingException {
      Attribute var3 = var0.get(var1);
      if (var3 != null) {
         Object var4 = var3.get();
         if (var4 instanceof String) {
            return (String)var4;
         }
      }

      return var2;
   }

   public static void reportAttributesToConsole(Attributes var0) throws NamingException {
      StringBuilder var1 = new StringBuilder();
      NamingEnumeration var2 = var0.getAll();

      while (var2.hasMore()) {
         Attribute var3 = (Attribute)var2.next();
         var1.append(var3.getID() + ":").append("\n");
         int var4 = 0;

         for (int var5 = var3.size(); var4 < var5; var4++) {
            var1.append("   ").append(var3.get(var4)).append("\n");
         }

         var1.append("\n");
      }

      System.out.println(var1);
   }

   private JndiUtils() {
   }
}
