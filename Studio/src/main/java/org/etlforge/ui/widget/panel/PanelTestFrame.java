package org.etlforge.ui.widget.panel;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.etlforge.ui.util.UiUtils;

public class PanelTestFrame extends JFrame {
   public PanelTestFrame(JComponent var1) {
      this(var1, false);
   }

   public PanelTestFrame(JComponent var1, boolean var2) {
      super("Test Panel");
      this.setDefaultCloseOperation(3);
      if (var2) {
         this.setContentPane(new JScrollPane(var1));
      } else {
         this.setContentPane(var1);
      }

      this.registerKeyBindings();
   }

   public void publish() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            PanelTestFrame.this.pack();
            PanelTestFrame.this.setLocationRelativeTo(null);
            PanelTestFrame.this.setVisible(true);
         }
      });
   }

   public void setExitOnClose(boolean var1) {
      int var2 = var1 ? 3 : 2;
      this.setDefaultCloseOperation(var2);
   }

   private void registerKeyBindings() {
      InputMap var1 = this.getRootPane().getInputMap(2);
      ActionMap var2 = this.getRootPane().getActionMap();
      var1.put(KeyStroke.getKeyStroke(27, 0), "esc");
      var2.put("esc", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            PanelTestFrame.this.dispose();
            if (PanelTestFrame.this.getDefaultCloseOperation() == 3) {
               System.exit(0);
            }
         }
      });
   }

   public void addKeyBinding(KeyStroke var1, Object var2, Action var3) {
      this.getRootPane().getInputMap(2).put(var1, var2);
      this.getRootPane().getActionMap().put(var2, var3);
   }
}
