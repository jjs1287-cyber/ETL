package com.etlforge.integration.client.ui.sfdc.wizard.soql;

import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.BooleanBinding;
import org.etlforge.ui.property.TextBinding;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.text.KongaTextArea;

final class SoqlArea implements UiProvider {
   private final KongaTextArea soqlArea = new KongaTextArea(Size.rows(3), Size.columns(40));
   private final KongaCheckBox queryAllChoice;

   public SoqlArea(QueryBuilderModel var1) {
      TextBinding.bind(var1, "soql", this.soqlArea);
      this.soqlArea.setLabel("&Query string");
      this.soqlArea.setTabSize(4);
      this.queryAllChoice = new KongaCheckBox("Include &deleted rows");
      BooleanBinding.bind(var1, "queryAll", this.queryAllChoice);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = Horizontal.box().add(this.soqlArea.getLabel()).glue().add(this.queryAllChoice);
      new BorderLayoutBuilder(0, 2).north(var2).center(this.soqlArea).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.soqlArea.requestFocusInWindow();
   }

   public void disableQueryAll(String var1) {
      this.queryAllChoice.setSelected(false);
      this.queryAllChoice.setEnabled(false);
      this.queryAllChoice.setToolTipText(var1);
   }
}
