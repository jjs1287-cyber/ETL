package org.etlforge.ui.dialog;

import java.awt.Container;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.border.Border;
import org.etlforge.ui.layout.Vertical;

public final class DialogPaneIcons {
   public static final DialogPaneIcons.DialogIcon QUESTION = DialogPaneIcons.DialogIcon.QUESTION;
   public static final DialogPaneIcons.DialogIcon INFORMATION = DialogPaneIcons.DialogIcon.INFORMATION;
   public static final DialogPaneIcons.DialogIcon WARNING = DialogPaneIcons.DialogIcon.WARNING;
   public static final DialogPaneIcons.DialogIcon ERROR = DialogPaneIcons.DialogIcon.ERROR;

   private DialogPaneIcons() {
   }

   public enum DialogIcon {
      QUESTION(UIManager.getIcon("OptionPane.questionIcon")),
      INFORMATION(UIManager.getIcon("OptionPane.informationIcon")),
      WARNING(UIManager.getIcon("OptionPane.warningIcon")),
      ERROR(UIManager.getIcon("OptionPane.errorIcon"));

      private final Icon icon;

      DialogIcon(Icon var3) {
         this.icon = var3;
      }

      public Icon getIcon() {
         return this.icon;
      }

      public void doStandardDecoration(Container var1, Border var2) {
         if (this.icon != null) {
            Vertical.box().add(this.icon).glue().withBorder(var2).deeplyNonOpaque().addToParent(var1, "West");
         }
      }
   }
}
