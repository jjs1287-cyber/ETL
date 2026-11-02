package org.etlforge.integration.data.connector;

import com.google.common.base.Preconditions;
import org.etlforge.system.ArgCheck;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.StartElementHandler;
import org.etlforge.xml.sax.StartOfElement;

public class FetchXmlValidator {
   public static FetchXmlValidator.Result validate(String var0) {
      String var1 = var0.trim();
      if (var1.isEmpty()) {
         return FetchXmlValidator.Result.invalid("The query XML cannot be empty.");
      }

      FetchXmlValidator.EntityNameHandler var2 = new FetchXmlValidator.EntityNameHandler();
      KongaSaxParser var3 = new KongaSaxParser(var2);

      try {
         var3.parseString(var1);
      } catch (Exception var5) {
         return FetchXmlValidator.Result.invalid("Invalid query XML: " + var5.getMessage());
      }

      return var2.validate();
   }

   private FetchXmlValidator() {
   }

   private static class EntityNameHandler {
      private boolean entityIsPresent;
      private String name;

      private EntityNameHandler() {
      }

      @StartElementHandler("fetch/entity")
      public void entity(StartOfElement var1) {
         this.entityIsPresent = true;
         this.name = var1.getAttribute("name");
      }

      public FetchXmlValidator.Result validate() {
         if (!this.entityIsPresent) {
            return FetchXmlValidator.Result.invalid("The <entity> element is missing.");
         }

         if (this.name == null) {
            return FetchXmlValidator.Result.invalid("The <entity> name-attribute is missing.");
         }

         String var1 = this.name.trim();
         return var1.isEmpty() ? FetchXmlValidator.Result.invalid("The <entity> name-attribute is empty.") : FetchXmlValidator.Result.valid(var1);
      }
   }

   public static final class Result {
      private final String error;
      private final String object;

      public static FetchXmlValidator.Result invalid(String var0) {
         return new FetchXmlValidator.Result(null, ArgCheck.checkNotEmpty(var0, "message"));
      }

      public static FetchXmlValidator.Result valid(String var0) {
         return new FetchXmlValidator.Result(ArgCheck.checkNotEmpty(var0, "objectName"), null);
      }

      private Result(String var1, String var2) {
         assert var1 == null && var2 != null || var1 != null && var2 == null;
         this.object = var1;
         this.error = var2;
      }

      public boolean isValid() {
         return this.error == null;
      }

      public String getError() {
         Preconditions.checkState(this.error != null);
         return this.error;
      }

      public String getObjectName() {
         Preconditions.checkState(this.object != null);
         return this.object;
      }

      @Override
      public String toString() {
         return this.isValid() ? "Valid: " + this.object : "Invalid: " + this.error;
      }
   }
}
