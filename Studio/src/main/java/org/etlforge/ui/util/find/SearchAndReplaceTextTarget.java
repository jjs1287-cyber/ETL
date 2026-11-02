package org.etlforge.ui.util.find;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class SearchAndReplaceTextTarget implements SearchAndReplaceTarget {
   private String string;
   private TextSearchMatch currentMatch;
   private int nextStartPosition = -1;

   public SearchAndReplaceTextTarget(String var1) {
      this.string = Preconditions.checkNotNull(var1, "string");
   }

   public TextSearchMatch nextMatch(TextSearchParameters var1) {
      int var2 = this.getStartPosition(var1);
      TextSearchMatch var3 = null;
      if (var2 < this.string.length()) {
         int var4 = this.getNextMatchPosition(var1, this.string, var2);
         if (var4 >= 0) {
            var3 = new TextSearchMatch(var4, var1.getSearchString().length());
         }
      }

      if (var3 != null) {
         this.currentMatch = var3;
      }

      return var3;
   }

   @Override
   public void replace(SearchTarget.Match var1, String var2) {
      Preconditions.checkArgument(var1 instanceof TextSearchMatch, "Invalid match: " + var1);
      TextSearchMatch var3 = (TextSearchMatch)var1;
      if (var3.start + var3.length <= this.string.length()) {
         String var4 = this.string.substring(0, var3.start);
         String var5 = this.string.substring(var3.start + var3.length);
         this.string = var4 + var2 + var5;
      }
   }

   @Override
   public List<TextSearchMatch> replaceAll(TextSearchParameters var1) {
      ArrayList var2 = Lists.newArrayList();
      String var3 = var1.getReplaceString();

      for (TextSearchMatch var4 = this.nextMatch(var1); var4 != null; var4 = this.nextMatch(var1)) {
         this.replace(var4, var3);
         var2.add(var4);
         this.updateNextSearchPositionAfterReplace(var1);
      }

      this.nextStartPosition = -1;
      return var2;
   }

   private void updateNextSearchPositionAfterReplace(TextSearchParameters var1) {
      switch (var1.getDirection()) {
         case UP:
            this.nextStartPosition = this.currentMatch.start - 1;
            break;
         case DOWN:
            int var2 = Math.max(var1.getReplaceString().length(), this.currentMatch.length);
            this.nextStartPosition = this.currentMatch.start + var2;
      }
   }

   public String getText() {
      return this.string;
   }

   private int getStartPosition(TextSearchParameters var1) {
      if (this.nextStartPosition >= 0) {
         return this.nextStartPosition;
      } else {
         boolean var2 = var1.getDirection() == SearchDirection.DOWN;
         if (this.currentMatch == null) {
            return var2 ? 0 : this.string.length() - 1;
         } else {
            return var2 ? this.currentMatch.start + this.currentMatch.length : this.currentMatch.start - 1;
         }
      }
   }

   private int getNextMatchPosition(TextSearchParameters var1, String var2, int var3) {
      String var4 = getEffectiveString(var2, var1);
      String var5 = getEffectiveString(var1.getSearchString(), var1);
      return var1.isWholeWordsOnly() ? this.findStartOfWholeWord(var1, var4, var5, var3) : this.findStartOfMatch(var1, var4, var5, var3);
   }

   private int findStartOfMatch(TextSearchParameters var1, String var2, String var3, int var4) {
      switch (var1.getDirection()) {
         case UP:
            return var2.lastIndexOf(var3, var4);
         case DOWN:
            return var2.indexOf(var3, var4);
         default:
            throw new AssertionError();
      }
   }

   private int findStartOfWholeWord(TextSearchParameters var1, String var2, String var3, int var4) {
      do {
         int var5 = this.findStartOfMatch(var1, var2, var3, var4);
         if (var5 == -1) {
            return -1;
         }

         if (this.isWholeWordMatch(var5, var3.length())) {
            return var5;
         }

         var4 = var5 + 1;
      } while (var4 < this.string.length());

      return -1;
   }

   private boolean isWholeWordMatch(int var1, int var2) {
      if (var1 == 0 && var2 == this.string.length()) {
         return true;
      } else {
         BreakIterator var3 = BreakIterator.getWordInstance();
         var3.setText(this.string);
         if (var3.isBoundary(var1)) {
            int var4 = var1 + var2;
            return var4 >= this.string.length() ? true : var3.isBoundary(var4);
         } else {
            return false;
         }
      }
   }

   private static String getEffectiveString(String var0, TextSearchParameters var1) {
      return var1.isCaseSensitive() ? var0 : var0.toLowerCase();
   }
}
