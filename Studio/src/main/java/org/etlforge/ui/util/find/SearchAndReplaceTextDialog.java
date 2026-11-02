package org.etlforge.ui.util.find;

import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class SearchAndReplaceTextDialog extends AbstractSearchDialog {
   private final TextSearchAndReplaceUi ui;

   public static SearchAndReplaceTextDialog sharedDialog(SearchAndReplaceTarget var0) {
      return new SearchAndReplaceTextDialog(TextSearchModel.SHARED, var0);
   }

   public SearchAndReplaceTextDialog(SearchAndReplaceTarget var1) {
      this(new TextSearchModel(), var1);
   }

   public SearchAndReplaceTextDialog(TextSearchModel var1, SearchAndReplaceTarget var2) {
      super(var1, var2, "Find/Replace");
      this.ui = new TextSearchAndReplaceUi(var1, this.getController());
      this.ui.setDefaultAction(this.getController().getFindAction());
   }

   public void showSearchUi() {
      this.ui.showSearchUi();
   }

   public void showReplaceUi() {
      this.ui.showReplaceUi();
   }

   @Override
   protected void focusUi() {
      this.ui.requestFocus();
   }

   @Override
   protected void layoutUi(BorderLayoutBuilder var1) {
      var1.center(this.ui);
   }

   @Override
   protected void disposeUi() {
      this.ui.dispose();
   }
}
