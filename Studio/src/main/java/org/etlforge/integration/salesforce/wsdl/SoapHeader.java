package org.etlforge.integration.salesforce.wsdl;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.util.name.Nameable;

public abstract class SoapHeader implements Nameable {
   protected static final String HEADER_ROOT = "root$transaction.header$";
   private final String name;

   protected SoapHeader(String var1) {
      this.name = var1;
   }

   @Override
   public final String getName() {
      return this.name;
   }

   public final List<String> getMapList() {
      ArrayList var1 = Lists.newArrayList();
      this.collectMappings(var1);
      if (var1.isEmpty()) {
         return Collections.emptyList();
      }

      String var2 = "root$transaction.header$" + this.name + "$";
      ArrayList var3 = Lists.newArrayList();

      for (Object __etlItem34 : var1) {
         SoapHeader.Mapping var5 = (SoapHeader.Mapping) __etlItem34;
         String var6 = var2 + var5.element;
         var3.add(MapStringFormat.format(var6, var5.expression));
      }

      return var3;
   }

   protected abstract void collectMappings(List<SoapHeader.Mapping> var1);

   public abstract String getStringRepresentation();

   protected static final class Mapping {
      public final String element;
      public final String expression;

      public static SoapHeader.Mapping forBoolean(String var0, boolean var1) {
         return new SoapHeader.Mapping(var0, var1 ? "true" : "false");
      }

      public static SoapHeader.Mapping forInt(String var0, int var1) {
         return new SoapHeader.Mapping(var0, "" + var1);
      }

      public static SoapHeader.Mapping forVariable(String var0, String var1) {
         return new SoapHeader.Mapping(var0, GlobalDE.forScript(var1));
      }

      public static SoapHeader.Mapping forVariableUsingGet(String var0, String var1) {
         return new SoapHeader.Mapping(var0, "Get(\"" + var1 + "\")");
      }

      public static SoapHeader.Mapping forString(String var0, String var1) {
         return new SoapHeader.Mapping(var0, "\"" + var1 + "\"");
      }

      private Mapping(String var1, String var2) {
         this.element = var1;
         this.expression = buildExpression(var2);
      }

      private static String buildExpression(String var0) {
         String var1 = "// This mapping is part of a Salesforce wizard.\n// Modifying it may cause the wizard to malfunction.";
         return String.format("<trans>\n%s\n%s\n</trans>", var1, var0);
      }
   }
}
