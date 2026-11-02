package org.etlforge.ui.text.contentassist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.swing.text.Document;

public class PathCompletionContext implements CompletionContext {
   private final String separator;
   private final String prefix;
   private String[] tokens;

   public PathCompletionContext(Collection<String> var1, String var2, String var3) {
      this(var1.toArray(new String[var1.size()]), var2, var3);
   }

   public PathCompletionContext(String[] var1, String var2, String var3) {
      this.separator = var2;
      this.prefix = var3;
      this.tokens = this.buildTokenSet(var1);
   }

   public void refresh(Collection<String> var1) {
      this.refresh(var1.toArray(new String[var1.size()]));
   }

   public void refresh(String[] var1) {
      this.tokens = this.buildTokenSet(var1);
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      String var3 = Utils.getText(var1, 0, var2);
      return var3.length() > 0 ? this.getMatchingTokens(var3, 0, var2) : new CompletionToken[0];
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }

   private CompletionToken[] getMatchingTokens(String var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();

      for (String var8 : this.tokens) {
         if (var8.startsWith(var1) && !var8.equals(var1)) {
            var4.add(new DefaultCompletionToken(var8, var2, var3));
         }
      }

      return (CompletionToken[]) var4.toArray(new CompletionToken[var4.size()]);
   }

   private String[] buildTokenSet(String[] var1) {
      TreeSet var2 = new TreeSet();
      String var3 = Pattern.quote(this.separator);

      for (String var7 : var1) {
         if (var7.length() > 0) {
            this.addPathAtoms(var7, var3, var2);
         }
      }

      return (String[]) var2.toArray(new String[var2.size()]);
   }

   private void addPathAtoms(String var1, String var2, Set<String> var3) {
      new PathCompletionContext.PathAtomBuilder().build(var1, var2, var3);
   }

   private class PathAtomBuilder {
      private String toSplit;
      private boolean appendPrefix;
      private boolean stop;

      private PathAtomBuilder() {
      }

      public void build(String var1, String var2, Set<String> var3) {
         this.toSplit = var1;
         this.checkPrefix();
         if (!this.stop) {
            this.addAtoms(var2, var3);
         }
      }

      private void checkPrefix() {
         if (PathCompletionContext.this.prefix != null && this.toSplit.startsWith(PathCompletionContext.this.prefix)) {
            if (this.toSplit.length() == PathCompletionContext.this.prefix.length()) {
               this.stop = true;
               return;
            }

            this.toSplit = this.toSplit.substring(PathCompletionContext.this.prefix.length());
            this.appendPrefix = true;
         }
      }

      private void addAtoms(String var1, Set<String> var2) {
         StringBuilder var3 = new StringBuilder();
         if (this.appendPrefix) {
            var3.append(PathCompletionContext.this.prefix);
         }

         for (String var7 : this.toSplit.split(var1)) {
            var3.append(var7);
            if (var3.length() != this.toSplit.length()) {
               var3.append(PathCompletionContext.this.separator);
            }

            var2.add(var3.toString());
         }
      }
   }
}
