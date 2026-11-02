package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.text.Document;
import org.etlforge.integration.salesforce.wsdl.Picklist;
import org.etlforge.integration.salesforce.wsdl.PicklistEntry;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.TokenDecorator;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;

final class PicklistCompletionContext implements CompletionContext {
   private final TokenListCompletionContext impl;

   public PicklistCompletionContext(Picklist var1) {
      ArrayList var2 = Lists.newArrayList();
      PicklistCompletionContext.InfoDecorator var3 = new PicklistCompletionContext.InfoDecorator();

      for (PicklistEntry var5 : var1) {
         var2.add(var5.getValue());
         var3.add(var5);
      }

      this.impl = new TokenListCompletionContext(var2);
      this.impl.setTokenDecorator(var3);
      this.impl.setDeleteSupported(false);
      this.impl.setCaseSensitive(false);
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      return this.impl.getAvailableTokens(var1, var2);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }

   private static class InfoDecorator implements TokenDecorator {
      private final Map<String, String> info = Maps.newHashMap();

      private InfoDecorator() {
      }

      public void add(PicklistEntry var1) {
         String var2 = var1.getValue();
         String var3 = var1.getLabel();
         if (!var2.equals(var3)) {
            this.info.put(var2, var3);
         }
      }

      @Override
      public String getDisplayName(String var1) {
         return var1;
      }

      @Override
      public Icon getIcon(String var1) {
         return null;
      }

      @Override
      public String getInfo(String var1) {
         return this.info.get(var1);
      }
   }
}
