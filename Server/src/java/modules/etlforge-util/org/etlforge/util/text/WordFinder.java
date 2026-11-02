package org.etlforge.util.text;

import com.google.common.base.Preconditions;
import java.text.BreakIterator;
import java.util.Locale;

public final class WordFinder {
   private final String text;
   private final BreakIterator iterator;

   public WordFinder(String var1) {
      this.text = var1;
      this.iterator = BreakIterator.getWordInstance(Locale.US);
      this.iterator.setText(var1);
   }

   public int getStartOfNextMatch(String var1, int var2) {
      this.checkStartPosition(var2);
      this.iterator.setText(this.text);
      int var3 = this.prepareIteratorForForwardSearch(var2);

      for (int var4 = this.iterator.next(); var4 != -1; var4 = this.iterator.next()) {
         if (var1.equals(this.text.substring(var3, var4))) {
            return var3;
         }

         var3 = var4;
      }

      return -1;
   }

   private int prepareIteratorForForwardSearch(int var1) {
      if (this.iterator.isBoundary(var1)) {
         return var1 == 0 ? this.iterator.first() : this.iterator.following(var1 - 1);
      } else {
         return this.iterator.following(var1);
      }
   }

   private void checkStartPosition(int var1) {
      Preconditions.checkArgument(var1 >= 0 && var1 <= this.text.length(), "startPosition must be >= 0 and < text.length");
   }

   public int getStartOfPreviousMatch(String var1, int var2) {
      this.checkStartPosition(var2);
      this.iterator.setText(this.text);
      int var3 = this.prepareIteratorForBackwardSearch(var2);

      for (int var4 = this.iterator.previous(); var4 != -1; var4 = this.iterator.previous()) {
         if (var1.equals(this.text.substring(var4, var3))) {
            return var4;
         }

         var3 = var4;
      }

      return -1;
   }

   private int prepareIteratorForBackwardSearch(int var1) {
      if (this.iterator.isBoundary(var1)) {
         return var1 == this.text.length() ? this.iterator.last() : this.iterator.preceding(var1 + 1);
      } else {
         return this.iterator.preceding(var1);
      }
   }
}
