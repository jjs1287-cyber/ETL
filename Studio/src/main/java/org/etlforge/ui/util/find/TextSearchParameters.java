package org.etlforge.ui.util.find;

import com.google.common.base.Preconditions;

public final class TextSearchParameters {
   private final String searchString;
   private final String replaceString;
   private final SearchDirection direction;
   private final boolean caseSensitive;
   private final boolean wholeWordsOnly;
   private final boolean wrap;
   private Integer hashCode;

   public TextSearchParameters(String var1, SearchDirection var2, boolean var3, boolean var4) {
      this(var1, null, var2, var3, var4);
   }

   public TextSearchParameters(String var1, String var2, SearchDirection var3, boolean var4, boolean var5) {
      this(var1, var2, var3, var4, var5, false);
   }

   public TextSearchParameters(String var1, String var2, SearchDirection var3, boolean var4, boolean var5, boolean var6) {
      Preconditions.checkNotNull(var1, "searchString");
      Preconditions.checkNotNull(var3, "direction");
      this.searchString = var1;
      this.replaceString = var2 == null ? "" : var2;
      this.direction = var3;
      this.caseSensitive = var4;
      this.wholeWordsOnly = var5;
      this.wrap = var6;
   }

   public String getSearchString() {
      return this.searchString;
   }

   public String getReplaceString() {
      return this.replaceString;
   }

   public SearchDirection getDirection() {
      return this.direction;
   }

   public boolean isCaseSensitive() {
      return this.caseSensitive;
   }

   public boolean isWholeWordsOnly() {
      return this.wholeWordsOnly;
   }

   public boolean isWrap() {
      return this.wrap;
   }

   @Override
   public String toString() {
      StringBuilder var1 = new StringBuilder("[TextSearchParameters: ");
      var1.append(this.searchString);
      if (!this.replaceString.isEmpty()) {
         var1.append(", Replace: ").append(this.replaceString);
      }

      var1.append(", Match Case: ").append(this.caseSensitive);
      var1.append(", Whole Words: ").append(this.wholeWordsOnly);
      var1.append(", Direction: ").append(this.direction);
      var1.append(", Wrap: ").append(this.wrap);
      var1.append("]");
      return var1.toString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof TextSearchParameters ? this.equals((TextSearchParameters)var1) : false;
      }
   }

   private boolean equals(TextSearchParameters var1) {
      if (this.caseSensitive != var1.caseSensitive) {
         return false;
      } else if (this.wholeWordsOnly != var1.wholeWordsOnly) {
         return false;
      } else if (this.direction != var1.direction) {
         return false;
      } else if (this.wrap != var1.wrap) {
         return false;
      } else if (!this.replaceString.equals(var1.replaceString)) {
         return false;
      } else {
         return this.caseSensitive ? this.searchString.equals(var1.searchString) : this.searchString.equalsIgnoreCase(var1.searchString);
      }
   }

   @Override
   public int hashCode() {
      if (this.hashCode == null) {
         this.hashCode = this.calculateHashCode();
      }

      return this.hashCode;
   }

   private Integer calculateHashCode() {
      int var1 = 17;
      var1 = 31 * var1 + this.replaceString.hashCode();
      var1 = 31 * var1 + (this.caseSensitive ? 0 : 1);
      var1 = 31 * var1 + (this.wholeWordsOnly ? 0 : 1);
      var1 = 31 * var1 + (this.wrap ? 0 : 1);
      var1 = 31 * var1 + this.direction.hashCode();
      int var2 = this.caseSensitive ? this.searchString.hashCode() : this.searchString.toLowerCase().hashCode();
      var1 = 31 * var1 + var2;
      return var1;
   }

   public TextSearchParameters newSearchString(String var1) {
      return var1.equals(this.searchString)
         ? this
         : new TextSearchParameters(var1, this.replaceString, this.direction, this.caseSensitive, this.wholeWordsOnly, this.wrap);
   }

   public TextSearchParameters newReplaceString(String var1) {
      return var1.equals(this.replaceString)
         ? this
         : new TextSearchParameters(this.searchString, var1, this.direction, this.caseSensitive, this.wholeWordsOnly, this.wrap);
   }

   public TextSearchParameters newDirection(SearchDirection var1) {
      return var1 == this.direction
         ? this
         : new TextSearchParameters(this.searchString, this.replaceString, var1, this.caseSensitive, this.wholeWordsOnly, this.wrap);
   }

   public TextSearchParameters newCaseSensitivity(boolean var1) {
      return var1 == this.caseSensitive
         ? this
         : new TextSearchParameters(this.searchString, this.replaceString, this.direction, var1, this.wholeWordsOnly, this.wrap);
   }

   public TextSearchParameters newWholeWordsOnly(boolean var1) {
      return var1 == this.wholeWordsOnly
         ? this
         : new TextSearchParameters(this.searchString, this.replaceString, this.direction, this.caseSensitive, var1, this.wrap);
   }

   public TextSearchParameters newWrap(boolean var1) {
      return var1 == this.wrap
         ? this
         : new TextSearchParameters(this.searchString, this.replaceString, this.direction, this.caseSensitive, this.wholeWordsOnly, var1);
   }
}
