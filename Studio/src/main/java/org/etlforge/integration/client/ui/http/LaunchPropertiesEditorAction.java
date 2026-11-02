package org.etlforge.integration.client.ui.http;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import org.etlforge.ui.widget.WidgetUtils;
import org.etlforge.ui.widget.overlay.OverlayContainer;

public class LaunchPropertiesEditorAction extends AbstractAction {
   private final HttpPropertiesEditor editor;
   private JComponent trigger;

   public LaunchPropertiesEditorAction(HttpPropertiesEditor var1) {
      this(var1, Strings.get("LaunchPropertiesEditorAction.Name"), null);
   }

   public LaunchPropertiesEditorAction(HttpPropertiesEditor var1, String var2, Icon var3) {
      super(var2, var3);
      this.editor = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LaunchPropertiesEditorAction.this.editor.setOverlayContainer(LaunchPropertiesEditorAction.this.getOverlayContainer());
            LaunchPropertiesEditorAction.this.editor.display();
         }
      });
   }

   public void setTrigger(JComponent var1) {
      this.trigger = var1;
   }

   private OverlayContainer getOverlayContainer() {
      return this.trigger != null ? WidgetUtils.getOverlayContainerAncestor(this.trigger) : null;
   }
}
