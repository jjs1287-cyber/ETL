package org.etlforge.ui.util.find;

import javax.swing.Action;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class SearchTextDialog extends AbstractSearchDialog {
   private final TextSearchUi ui;

   public static SearchTextDialog sharedDialog(SearchTarget var0) {
      return new SearchTextDialog(TextSearchModel.SHARED, var0);
   }

   public SearchTextDialog(SearchTarget var1) {
      this(new TextSearchModel(), var1);
   }

   public SearchTextDialog(TextSearchModel var1, SearchTarget var2) {
      super(var1, var2, "Find");
      this.ui = TextSearchUi.searchOnly(var1);
      this.ui.setDefaultAction(this.getController().getFindAction());
   }

   @Override
   protected void focusUi() {
      this.ui.requestFocus();
   }

   @Override
   protected void layoutUi(BorderLayoutBuilder var1) {
      Action var2 = this.getController().getFindAction();
      var1.center(this.ui).east(var2);
   }

   @Override
   protected void disposeUi() {
      this.ui.dispose();
   }
}
