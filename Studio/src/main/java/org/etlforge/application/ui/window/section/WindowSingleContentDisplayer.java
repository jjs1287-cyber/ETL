package org.etlforge.application.ui.window.section;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.focus.FocusUtils;

final class WindowSingleContentDisplayer implements FocusListener {
   private final SingleContentWindowSection windowSection;
   private final JComponent stage;
   private boolean hasFocus = false;

   public WindowSingleContentDisplayer(SingleContentWindowSection var1) {
      this.windowSection = var1;
      this.stage = new JPanel(new BorderLayout());
      FocusUtils.makeComponentMouseFocusable(this.stage);
   }

   void addContent(JComponent var1) {
      this.stage.removeAll();
      this.stage.add(var1);
      this.stage.invalidate();
      this.stage.repaint();
      ApplicationUi.getFocusManager().addFocusRoot(this.stage, this);
   }

   void removeContent() {
      this.stage.removeAll();
      this.stage.invalidate();
      this.stage.repaint();
      ApplicationUi.getFocusManager().removeFocusRoot(this.stage);
   }

   void decorate(JComponent var1) {
      BorderLayoutBuilder.from(var1).center(this.stage);
   }

   @Override
   public void focusGained(FocusEvent var1) {
      if (!this.hasFocus) {
         this.hasFocus = true;
         this.windowSection.activated();
      }
   }

   @Override
   public void focusLost(FocusEvent var1) {
      if (this.hasFocus) {
         this.windowSection.deactivated();
         this.hasFocus = false;
      }
   }
}
