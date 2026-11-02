package org.etlforge.ui;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.etlforge.ui.widget.KongaScrollPane;

public class UiProviderTestFrame extends JFrame {
   public UiProviderTestFrame(final UiProvider var1) {
      super("Test Frame");
      this.setDefaultCloseOperation(3);
      this.setContentPane(new KongaScrollPane(var1));
      this.addWindowListener(new WindowAdapter() {
         @Override
         public void windowOpened(WindowEvent var1x) {
            var1.requestFocus();
         }
      });
   }

   public void addKeyBinding(KeyStroke var1, Object var2, Action var3) {
      InputMap var4 = this.getRootPane().getInputMap(1);
      var4.put(var1, var2);
      ActionMap var5 = this.getRootPane().getActionMap();
      var5.put(var2, var3);
   }

   public void publish() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            UiProviderTestFrame.this.pack();
            UiProviderTestFrame.this.setLocationRelativeTo(null);
            UiProviderTestFrame.this.setVisible(true);
         }
      });
   }

   public static void display(UiProvider var0) {
      UiProviderTestFrame var1 = new UiProviderTestFrame(var0);
      var1.publish();
   }
}
