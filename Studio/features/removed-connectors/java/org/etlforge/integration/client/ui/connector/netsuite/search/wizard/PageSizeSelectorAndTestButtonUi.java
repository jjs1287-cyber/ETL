package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import javax.swing.Action;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class PageSizeSelectorAndTestButtonUi {
   private final PageSizeSelector pageSizeSelector;
   private final Action testAction;

   public PageSizeSelectorAndTestButtonUi(QueryBuilderModel var1, Action var2) {
      this.pageSizeSelector = createPageSizeSelector(var1);
      this.testAction = var2;
   }

   private static PageSizeSelector createPageSizeSelector(QueryBuilderModel var0) {
      PageSizeSelector var1 = new PageSizeSelector();
      Binding var2 = var1.bindWithModel(var0);
      var2.syncUi();
      return var1;
   }

   UiProvider layoutPageSizeSelectorAndTestButton() {
      if (this.testAction == null) {
         return this.pageSizeSelector;
      }

      KongaButton var1 = new KongaButton(this.testAction);
      WizardStyleSheet.MEDIUM_CAPTION_STYLE.makeOver(var1);
      return Horizontal.box().withBorder(Empty.border(0, 10, 0, 0)).add(InvisiblePanel.of(this.pageSizeSelector)).add(Horizontal.Align.east(var1));
   }
}
