package org.etlforge.integration.client.ui.connector.mscrm.query.wizard;

import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class TextQueryEditor implements QueryEditor {
   private final KongaTextArea queryEditor;

   public TextQueryEditor(QuerySelectionModel var1) {
      this.queryEditor = this.createEditor(var1);
   }

   private KongaTextArea createEditor(QuerySelectionModel var1) {
      KongaTextArea var2 = new KongaTextArea(Size.rows(20), Size.columns(80));
      var2.setLabel("fetch&Xml:");
      var2.setLineWrap(false);
      TextStyles.DefaultMonoSpaced.makeOver(var2);
      TextBinding.bindAndSyncUi(var1, "xml", var2);
      return var2;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.queryEditor.asUiProvider().decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.queryEditor.requestFocusInWindow();
   }

   @Override
   public String getFetchXml() {
      return this.queryEditor.getText().trim();
   }

   @Override
   public boolean canDoForwardNavigation() {
      return true;
   }
}
