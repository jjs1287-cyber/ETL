package org.etlforge.integration.client.ui.interchange.location.panel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.etlforge.application.ui.widget.AutoCompletionPreference;
import org.etlforge.integration.client.ui.interchange.PredefinedVariable;
import org.etlforge.integration.client.ui.interchange.location.TargetFileNameVariables;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;

final class TargetFileNameCompletionContext implements CompletionContext {
   private static final Map<String, PredefinedVariable> VARIABLES = Maps.newTreeMap();

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      ArrayList var3 = Lists.newArrayList();
      String var4 = this.getContext(var1, var2);
      int var5 = var4.lastIndexOf(91, var2);
      if (var5 >= 0) {
         int var6 = var4.lastIndexOf(93, var2);
         if (var6 <= var5) {
            this.addMatchingTokens(var4, var5, var2, var3);
         }
      }

      if (var3.isEmpty() && AutoCompletionPreference.isCodeEditorStyle()) {
         this.addAllExistingTokens(var3, var2, var2);
      }

      return (CompletionToken[]) var3.toArray(new CompletionToken[var3.size()]);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }

   private void addMatchingTokens(String var1, int var2, int var3, List<CompletionToken> var4) {
      String var5 = var1.substring(var2).toLowerCase();

      for (Entry var7 : VARIABLES.entrySet()) {
         if (((String)var7.getKey()).startsWith(var5)) {
            var4.add(((PredefinedVariable)var7.getValue()).asCompletionToken(var2, var3));
         }
      }
   }

   private void addAllExistingTokens(List<CompletionToken> var1, int var2, int var3) {
      for (PredefinedVariable var5 : VARIABLES.values()) {
         var1.add(var5.asCompletionToken(var2, var3));
      }
   }

   private String getContext(Document var1, int var2) {
      try {
         return var1.getText(0, var2);
      } catch (BadLocationException var4) {
         return "";
      }
   }

   static {
      for (PredefinedVariable var1 : TargetFileNameVariables.getVariables()) {
         VARIABLES.put(var1.getName().toLowerCase(), var1);
      }
   }
}
