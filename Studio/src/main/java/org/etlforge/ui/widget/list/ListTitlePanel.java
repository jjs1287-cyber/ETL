package org.etlforge.ui.widget.list;

import javax.swing.Icon;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.label.KongaLabel;

final class ListTitlePanel implements UiProvider {
   private final OneRowPanel panel = new OneRowPanel();
   private final KongaLabel label;

   public ListTitlePanel(String var1) {
      this(var1, null);
   }

   public ListTitlePanel(String var1, Icon var2) {
      this.panel.setAnchor(OneRowPanel.WEST);
      this.panel.setFillOut();
      this.label = KongaLabel.withMnemonic(var1);
      this.label.setIcon(var2);
      this.label.setHorizontalAlignment(10);
      this.panel.add(this.label);
   }

   public void decorateTitle(LabelDecorator var1) {
      var1.decorate(this.label);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.panel.decorate(var1);
   }

   @Override
   public void requestFocus() {
   }

   public void bindLabel(KongaList var1) {
      new KongaListLabel(var1, this.label);
   }
}
