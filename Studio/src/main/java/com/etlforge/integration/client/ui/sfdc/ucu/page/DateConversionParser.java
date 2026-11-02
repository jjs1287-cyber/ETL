package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import java.util.Iterator;
import java.util.List;
import org.etlforge.integration.data.entity.sfdc.DateConversionMapping;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;

final class DateConversionParser {
   private String sourceDe;

   public DateConversionParser(SObjectSchema.Field.SoapType var1, String var2, Optional<DateConversionParams> var3) {
      List var4 = this.createExpectedTokenList(var1, var3);
      List var5 = this.createActualTokenList(var2);
      if (var5 == null) {
         this.sourceDe = null;
      } else {
         this.sourceDe = this.compareExpectedAndActualTokenLists(var4, var5);
      }
   }

   private List<Transform.Token> createExpectedTokenList(SObjectSchema.Field.SoapType var1, Optional<DateConversionParams> var2) {
      String var3 = "X";
      String var4 = var1 == SObjectSchema.Field.SoapType.DATE ? DateConversionMapping.forDate(var3, var2) : DateConversionMapping.forDateTime(var3, var2);
      List var5 = this.parseExpression(var4);
      if (var5 == null) {
         throw new RuntimeException("The automatically generated date conversion mapping cannot be parsed.");
      }

      this.removeWhitespaceAndCommentsAndSuch(var5);
      return var5;
   }

   private List<Transform.Token> parseExpression(String var1) {
      Transform var2 = new Transform();
      return var2.tokenize(var1);
   }

   private void removeWhitespaceAndCommentsAndSuch(List<Transform.Token> var1) {
      Iterator var2 = var1.iterator();

      while (var2.hasNext()) {
         if (this.isIgnorable((Transform.Token)var2.next())) {
            var2.remove();
         }
      }
   }

   private boolean isIgnorable(Transform.Token var1) {
      return var1.m_id == 1100 || var1.m_id == 1101;
   }

   private List<Transform.Token> createActualTokenList(String var1) {
      List var2 = this.parseExpression(var1);
      if (var2 != null) {
         this.removeWhitespaceAndCommentsAndSuch(var2);
      }

      return var2;
   }

   private String compareExpectedAndActualTokenLists(List<Transform.Token> var1, List<Transform.Token> var2) {
      String var3 = null;
      boolean var4 = false;
      boolean var5 = false;
      Iterator var6 = var1.iterator();
      Iterator var7 = var2.iterator();

      while (var6.hasNext() && var7.hasNext()) {
         Transform.Token var8 = (Transform.Token)var6.next();
         Transform.Token var9 = (Transform.Token)var7.next();
         if (var8.m_id != var9.m_id && var9.m_id == 310 && var4 && var7.hasNext()) {
            var4 = false;
            var9 = (Transform.Token)var7.next();
         }

         if (this.isDe(var8) && this.isDe(var9)) {
            var3 = var9.getStr();
            var4 = !var5;
         } else {
            if (!this.areEquivalent(var8, var9)) {
               return null;
            }

            if (var9.m_id == 101) {
               var5 = true;
            } else if (var5) {
               var4 = var9.m_id == 1000;
            }
         }
      }

      return !var6.hasNext() && !var7.hasNext() ? var3 : null;
   }

   private boolean isDe(Transform.Token var1) {
      switch (var1.m_id) {
         case 405:
         case 406:
         case 408:
            return true;
         case 407:
         default:
            return false;
      }
   }

   private boolean areEquivalent(Transform.Token var1, Transform.Token var2) {
      if (var1.m_id != var2.m_id) {
         return false;
      } else {
         String var3 = var1.getStr();
         String var4 = var2.getStr();
         if (var3 == null) {
            return var4 == null;
         } else {
            return var4 == null ? false : var3.equalsIgnoreCase(var4);
         }
      }
   }

   public boolean isUserEdited() {
      return this.sourceDe == null;
   }

   public String getSourceDataElement() {
      return this.sourceDe;
   }
}
