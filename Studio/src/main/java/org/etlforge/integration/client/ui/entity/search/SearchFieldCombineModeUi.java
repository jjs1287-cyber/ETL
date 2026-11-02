package org.etlforge.integration.client.ui.entity.search;

import java.awt.event.ActionListener;
import javax.swing.JRadioButton;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;

final class SearchFieldCombineModeUi implements UiProvider {
   private final JRadioButton and = new KongaRadioButton("&AND", true);
   private final JRadioButton or = new KongaRadioButton("&OR", false);

   public SearchFieldCombineModeUi() {
      ButtonUtils.makeMutuallyExclusive(this.and, this.or);
   }

   public SearchFieldCombineMode getMode() {
      return this.or.isSelected() ? SearchFieldCombineMode.OR : SearchFieldCombineMode.AND;
   }

   public void addActionListener(ActionListener var1) {
      this.and.addActionListener(var1);
      this.or.addActionListener(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, "Combine fields using ", this.and, this.or);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.and.requestFocusInWindow();
   }
}
