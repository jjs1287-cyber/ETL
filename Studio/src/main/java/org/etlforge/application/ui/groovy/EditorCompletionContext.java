package org.etlforge.application.ui.groovy;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;

final class EditorCompletionContext implements CompletionContext {
   private final String[] packages;

   public EditorCompletionContext(String[] var1) {
      this.packages = var1;
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      String var3 = this.getFragment(var1, var2);
      if (var3 != null) {
         List var4 = this.getMatchingPackages(var3);
         return this.createMatchingTokens(var4, var2 - var3.length(), var2);
      } else {
         return new CompletionToken[0];
      }
   }

   String getFragment(Document var1, int var2) {
      int var3 = DocumentUtils.getRowNumber(var1, var2);
      int var4 = DocumentUtils.getRowStart(var1, var3);
      if (var2 > var4) {
         String var5 = DocumentUtils.getText(var1, var4, var2 - var4);
         if (var5.startsWith("import ")) {
            return StringUtils.substringAfter(var5, "import ");
         }
      }

      return null;
   }

   List<String> getMatchingPackages(String var1) {
      if (var1.isEmpty()) {
         return Arrays.asList(this.packages);
      }

      ArrayList var2 = Lists.newArrayList();
      char var3 = var1.charAt(var1.length() - 1);
      if (var3 == '.' || Character.isJavaIdentifierPart(var3)) {
         String var4 = var1.trim();

         for (String var8 : this.packages) {
            if (var8.startsWith(var4)) {
               var2.add(var8);
            }
         }
      }

      return var2;
   }

   private CompletionToken[] createMatchingTokens(List<String> var1, int var2, int var3) {
      ArrayList var4 = Lists.newArrayList();

      for (String var6 : var1) {
         var4.add(new DefaultCompletionToken(var6, var2, var3));
      }

      return (CompletionToken[]) var4.toArray(new CompletionToken[var4.size()]);
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }
}
