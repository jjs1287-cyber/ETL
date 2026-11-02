package org.etlforge.ui.text.contentassist;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.text.Document;
import org.etlforge.ui.text.DocumentUtils;

public class TokenListCompletionContext implements CompletionContext {
   private List<String> tokens;
   private List<String> tokensWithCase;
   private boolean useWordBoundaries;
   private boolean caseSensitive;
   private boolean deleteSupported;
   private TokenDecorator decorator;
   private Set<String> ignoredWordBoundaries;
   private Comparator<CompletionToken> displayOrder;
   private final Object lock = new Object();

   private TokenListCompletionContext() {
      this.caseSensitive = true;
      this.deleteSupported = true;
      this.displayOrder = DisplayValueOrder.CASE_INSENSITIVE;
   }

   public TokenListCompletionContext(String[] var1) {
      this();
      this.tokens = createTokensFromArray(var1);
      this.applyCaseSensitiveSetting();
   }

   public TokenListCompletionContext(Iterable<String> var1) {
      this();
      this.tokens = Lists.newArrayList(var1);
      this.applyCaseSensitiveSetting();
   }

   private static List<String> createTokensFromArray(String[] var0) {
      ArrayList var1 = Lists.newArrayList();

      for (String var5 : var0) {
         if (!var5.isEmpty()) {
            var1.add(var5);
         }
      }

      return var1;
   }

   private void applyCaseSensitiveSetting() {
      if (this.caseSensitive) {
         synchronized (this.lock) {
            this.tokensWithCase = this.tokens;
         }
      } else {
         this.createLowerCaseTokens();
      }
   }

   private void createLowerCaseTokens() {
      synchronized (this.lock) {
         this.tokensWithCase = Lists.newArrayList();

         for (String var3 : this.tokens) {
            this.tokensWithCase.add(var3.toLowerCase());
         }
      }
   }

   public final void setCaseSensitive(boolean var1) {
      if (var1 != this.caseSensitive) {
         this.caseSensitive = var1;
         this.applyCaseSensitiveSetting();
      }
   }

   public final void setTokenDecorator(TokenDecorator var1) {
      this.decorator = var1;
   }

   public final void setTokens(String[] var1) {
      synchronized (this.lock) {
         this.tokens = createTokensFromArray(var1);
         this.applyCaseSensitiveSetting();
      }
   }

   public final void setTokens(Collection<String> var1) {
      this.setTokens(var1.toArray(new String[var1.size()]));
   }

   public final void setUseWordBoundaries(boolean var1) {
      this.useWordBoundaries = var1;
   }

   public final void setIgnoredWordBoundaries(String... var1) {
      this.ignoredWordBoundaries = Sets.newHashSet(var1);
   }

   public final void setDisplayOrder(Comparator<CompletionToken> var1) {
      this.displayOrder = var1;
   }

   public final void setDeleteSupported(boolean var1) {
      this.deleteSupported = var1;
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return this.deleteSupported ? this.removeToken(var1) : false;
   }

   private boolean removeToken(CompletionToken var1) {
      String var2 = var1.getDisplayValue();
      synchronized (this.lock) {
         return this.caseSensitive ? this.tokens.remove(var2) : this.tokens.remove(var2) && this.tokensWithCase.remove(var2.toLowerCase());
      }
   }

   @Override
   public final CompletionToken[] getAvailableTokens(Document var1, int var2) {
      int var3 = this.useWordBoundaries ? DocumentUtils.getWordStart(var1, var2) : 0;
      String var4 = this.getPreAmble(var1, var3, var2);
      if (this.useWordBoundaries && var3 > 0 && this.ignoredWordBoundaries != null) {
         String var5 = DocumentUtils.getText(var1, var3 - 1, 1);
         if (this.ignoredWordBoundaries.contains(var5)) {
            var4 = var5 + var4;
            var3--;
         }
      }

      if (var4.length() == 0) {
         var3 = var2;
      }

      return this.getMatchingTokens(var4, var3, var2);
   }

   private String getPreAmble(Document var1, int var2, int var3) {
      String var4 = Utils.getText(var1, var2, var3 - var2);
      if (!this.caseSensitive) {
         var4 = var4.toLowerCase();
      }

      return var4;
   }

   private CompletionToken[] getMatchingTokens(String var1, int var2, int var3) {
      SortedSet var4 = this.collectAllMatches(var1, var2, var3);
      this.removeSingleTokenIfIdenticalToPreamble(var1, var4);
      return (CompletionToken[]) var4.toArray(new CompletionToken[var4.size()]);
   }

   private SortedSet<DefaultCompletionToken> collectAllMatches(String var1, int var2, int var3) {
      TreeSet var4 = Sets.newTreeSet(this.displayOrder);
      synchronized (this.lock) {
         int var6 = 0;

         for (int var7 = this.tokensWithCase.size(); var6 < var7; var6++) {
            String var8 = this.tokensWithCase.get(var6);
            if (var8.startsWith(var1)) {
               DefaultCompletionToken var9 = this.createCompletionToken(this.tokens.get(var6), var2, var3);
               var4.add(var9);
            }
         }

         return var4;
      }
   }

   private DefaultCompletionToken createCompletionToken(String var1, int var2, int var3) {
      return this.decorator != null ? this.createDecoratedToken(var1, var2, var3) : new DefaultCompletionToken(var1, var2, var3);
   }

   private DefaultCompletionToken createDecoratedToken(String var1, int var2, int var3) {
      String var4 = this.decorator.getDisplayName(var1);
      DefaultCompletionToken var5 = new DefaultCompletionToken(var1, var2, var3, var4);
      var5.setIcon(this.decorator.getIcon(var1));
      var5.setInfo(this.decorator.getInfo(var1));
      return var5;
   }

   private void removeSingleTokenIfIdenticalToPreamble(String var1, SortedSet<DefaultCompletionToken> var2) {
      if (var2.size() == 1) {
         DefaultCompletionToken var3 = (DefaultCompletionToken)var2.iterator().next();
         String var4 = var3.getValue();
         boolean var5 = this.caseSensitive ? var4.equals(var1) : var4.toLowerCase().equals(var1.toLowerCase());
         if (var5) {
            var2.remove(var3);
         }
      }
   }
}
