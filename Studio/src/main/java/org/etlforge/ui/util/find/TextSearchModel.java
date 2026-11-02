package org.etlforge.ui.util.find;

import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public final class TextSearchModel extends AbstractPropertyBasedModel {
   static final TextSearchModel SHARED = new TextSearchModel();
   public static final String SEARCH_STRING = "searchString";
   public static final String REPLACE_STRING = "replaceString";
   public static final String DIRECTION = "direction";
   public static final String CASE_SENSITIVE = "caseSensitive";
   public static final String WHOLE_WORDS = "wholeWords";
   public static final String WRAP_SEARCH = "wrap";
   private final Property<String> searchString = this.newProperty("searchString", "");
   private final Property<String> replaceString = this.newProperty("replaceString", "");
   private final Property<SearchDirection> direction = this.newProperty("direction", SearchDirection.DOWN);
   private final Property<Boolean> caseSensitive = this.newProperty("caseSensitive", false);
   private final Property<Boolean> wholeWords = this.newProperty("wholeWords", false);
   private final Property<Boolean> wrapSearch = this.newProperty("wrap", false);

   public TextSearchParameters getSearchParameters() {
      return new TextSearchParameters(
         this.searchString.get(), this.replaceString.get(), this.direction.get(), this.caseSensitive.get(), this.wholeWords.get(), this.wrapSearch.get()
      );
   }

   public void setSearchParameters(TextSearchParameters var1) {
      TextSearchParameters var2 = this.getSearchParameters();
      if (!var1.equals(var2)) {
         this.searchString.set(var1.getSearchString());
         this.replaceString.set(var1.getReplaceString());
         this.direction.set(var1.getDirection());
         this.caseSensitive.set(var1.isCaseSensitive());
         this.wholeWords.set(var1.isWholeWordsOnly());
         this.wrapSearch.set(var1.isWrap());
      }
   }

   public String getSearchString() {
      return this.searchString.get();
   }

   public void setSearchString(String var1) {
      this.searchString.set(var1);
   }

   public String getReplaceString() {
      return this.replaceString.get();
   }
}
