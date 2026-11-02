package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.migration;

import java.awt.Container;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import org.etlforge.ui.Empty;
import org.etlforge.ui.dialog.DialogPaneIcons;
import org.etlforge.ui.widget.text.KongaTextArea;

final class MigrationDialogUtils {
   private static final boolean DECORATE_WITH_ICONS = true;

   private MigrationDialogUtils() {
      throw new UnsupportedOperationException();
   }

   static void addIcon(Container var0, DialogPaneIcons.DialogIcon var1) {
      var1.doStandardDecoration(var0, Empty.border(10, 10, 0, 0));
   }

   static JComponent getReadOnlyTextDisplay(String var0) {
      KongaTextArea var1 = KongaTextArea.viewer(var0);
      var1.setFocusable(false);
      var1.setOpaque(false);
      return var1;
   }

   static JTextArea getExpressionArea(String var0, boolean var1) {
      KongaTextArea var2 = new KongaTextArea(var0, 6, 50).disableTabs();
      var2.setCaretPosition(var0.length());
      var2.setEditable(var1);
      return var2;
   }
}
