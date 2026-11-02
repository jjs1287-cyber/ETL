package org.etlforge.integration.client.ui.script.text;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import javax.swing.text.Document;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElementsListener;
import org.etlforge.integration.dataelement.GlobalDE;
import org.etlforge.lang.Arrays2;
import org.etlforge.system.Disposable;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.TokenListCompletionContext;

public final class VariableCompletionContext implements CompletionContext, Disposable {
   private final TokenListCompletionContext impl = new TokenListCompletionContext(new String[0]);
   private GlobalDataElements globalVariables;
   private VariableCompletionContext.Listener listener;
   private CompletionContext sourceVariables;

   public VariableCompletionContext() {
      this.impl.setCaseSensitive(false);
      this.impl.setUseWordBoundaries(true);
      this.impl.setIgnoredWordBoundaries("$", ".", "_");
      this.impl.setDisplayOrder(new VariableCompletionContext.TokenOrder());
   }

   @Override
   public void dispose() {
      if (this.globalVariables != null) {
         this.globalVariables.removeListener(this.listener);
      }
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      CompletionToken[] var3 = this.impl.getAvailableTokens(var1, var2);
      if (this.sourceVariables == null) {
         return var3;
      }

      CompletionToken[] var4 = this.sourceVariables.getAvailableTokens(var1, var2);
      return var4.length == 0 ? var3 : Arrays2.append(var3, var4);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }

   public void setGlobalDataElements(GlobalDataElements var1) {
      if (var1 != this.globalVariables) {
         if (this.globalVariables != null) {
            this.globalVariables.removeListener(this.listener);
         }

         if (var1 != null) {
            if (this.listener == null) {
               this.listener = new VariableCompletionContext.Listener();
            }

            var1.addListener(this.listener);
         }

         this.globalVariables = var1;
         this.updateTokens();
      }
   }

   private void updateTokens() {
      Collection var1 = this.getTokens();
      this.impl.setTokens(var1);
   }

   private Collection<String> getTokens() {
      ArrayList var1 = Lists.newArrayList();
      if (this.globalVariables != null) {
         var1.addAll(this.globalVariables.getAllDataElementsWithPrefix());
      }

      return var1;
   }

   public void setSourceDataElements(CompletionContext var1) {
      this.sourceVariables = var1;
   }

   private class Listener implements GlobalDataElementsListener {
      private Listener() {
      }

      @Override
      public void dataElementStoreChanged() {
         VariableCompletionContext.this.updateTokens();
      }

      @Override
      public void dataElementStoreRebuilt() {
         VariableCompletionContext.this.updateTokens();
      }

      @Override
      public void processFilterChanged(ProcessFilter var1) {
      }
   }

   private static class TokenOrder implements Comparator<CompletionToken>, Serializable {
      private TokenOrder() {
      }

      public int compare(CompletionToken var1, CompletionToken var2) {
         String var3 = var1.getDisplayValue();
         String var4 = var2.getDisplayValue();
         boolean var5 = GlobalDE.isGlobalDE(var3);
         boolean var6 = GlobalDE.isGlobalDE(var4);
         if (var5) {
            if (!var6) {
               return 1;
            }
         } else if (var6) {
            return -1;
         }

         return var3.compareToIgnoreCase(var4);
      }
   }
}
