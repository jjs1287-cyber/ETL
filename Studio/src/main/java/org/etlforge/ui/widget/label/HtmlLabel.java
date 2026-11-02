package org.etlforge.ui.widget.label;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import org.apache.commons.lang.StringEscapeUtils;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.PopupMenuRequestHandlers;
import org.etlforge.util.clipboard.ClipboardUtils;

public final class HtmlLabel extends JLabel {
   private final String message;
   private final Action copy;

   public HtmlLabel(String var1) {
      this(var1, 400);
   }

   public HtmlLabel(String var1, int var2) {
      super(toHtml(var1, var2));
      this.message = var1;
      this.copy = new HtmlLabel.CopyAction();
      this.setFocusable(true);
      ActionKeyBinding.install(this.copy, this, KeyUtils.COPY);
      PopupMenuRequestHandlers.install(this, this.copy);
   }

   private static String toHtml(String var0, int var1) {
      StringBuilder var2 = new StringBuilder("<html><table width=").append(var1).append("px><tr>");
      var2.append(StringEscapeUtils.escapeHtml(var0).replace("\n", "<br>"));
      var2.append("</tr></table></html>");
      return var2.toString();
   }

   private class CopyAction extends AbstractAction {
      public CopyAction() {
         super("Copy");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ClipboardUtils.copyToSystemClipboard(HtmlLabel.this.message);
      }
   }
}
