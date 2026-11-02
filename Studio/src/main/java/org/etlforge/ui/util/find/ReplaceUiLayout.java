package org.etlforge.ui.util.find;

import javax.swing.JComponent;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class ReplaceUiLayout implements UiProvider {
   private final TextSearchUi replaceUi;
   private final JComponent layout;

   public ReplaceUiLayout(TextSearchUi var1, TextSearchController var2) {
      this.replaceUi = var1;
      this.layout = this.doLayout(var2);
   }

   private JComponent doLayout(TextSearchController var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(10, 0);
      var2.center(this.replaceUi);
      Grid var3 = Grid.oneColumn(5);
      var3.addAll(var1.getFindAction(), var1.getReplaceAction(), var1.getReplaceAndFindAction(), var1.getReplaceAllAction());
      var2.east(InvisiblePanel.of(var3));
      var2.withBorder(Empty.border(5));
      return var2.container();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.layout);
   }

   @Override
   public void requestFocus() {
      this.replaceUi.requestFocus();
   }
}
