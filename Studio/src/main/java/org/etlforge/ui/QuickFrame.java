package org.etlforge.ui;

import com.google.common.base.Supplier;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.etlforge.ui.canvas.ContainerCanvas;
import org.etlforge.ui.wait.WaitLock;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;

public final class QuickFrame implements WaitService {
   private final JFrame frame;
   private final WaitService waitService;

   private QuickFrame(String var1) {
      this.frame = createEmptyFrame(var1);
      this.waitService = new WindowWaitService(this.frame);
   }

   private static JFrame createEmptyFrame(String var0) {
      JFrame var1 = new JFrame(var0);
      var1.setDefaultCloseOperation(3);
      return var1;
   }

   public QuickFrame(UiProvider var1, String var2) {
      this(var2);
      this.layout(var1);
   }

   public QuickFrame(JComponent var1, String var2) {
      this(var2);
      this.layout(var1);
   }

   public static QuickFrame show(final UiProvider var0, final String var1) {
      return UiThreadSupplier.call(new Supplier<QuickFrame>() {
         public QuickFrame get() {
            QuickFrame var1x = new QuickFrame(var0, var1);
            var1x.publish();
            return var1x;
         }
      });
   }

   public static QuickFrame show(final JComponent var0, final String var1) {
      return UiThreadSupplier.call(new Supplier<QuickFrame>() {
         public QuickFrame get() {
            QuickFrame var1x = new QuickFrame(var0, var1);
            var1x.publish();
            return var1x;
         }
      });
   }

   private void layout(final UiProvider var1) {
      this.frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowOpened(WindowEvent var1x) {
            var1.requestFocus();
         }
      });
      ContainerCanvas.decorate(var1, this.frame.getContentPane(), "Center");
   }

   private void layout(JComponent var1) {
      this.frame.getContentPane().add(var1, "Center");
      this.publish();
   }

   public void addKeyBinding(Object var1, Action var2) {
      this.addKeyBinding(var1, (KeyStroke)var2.getValue("AcceleratorKey"), var2);
   }

   public void addKeyBinding(Object var1, KeyStroke var2, Action var3) {
      InputMap var4 = this.frame.getRootPane().getInputMap(1);
      var4.put(var2, var1);
      ActionMap var5 = this.frame.getRootPane().getActionMap();
      var5.put(var1, var3);
   }

   public void publish() {
      ((JComponent)this.frame.getContentPane()).setBorder(Empty.border(10));
      this.frame.pack();
      this.frame.setLocationRelativeTo(null);
      this.frame.setVisible(true);
   }

   public JFrame getFrame() {
      return this.frame;
   }

   @Override
   public WaitLock startWait() {
      return this.waitService.startWait();
   }

   @Override
   public WaitLock startWait(int var1) {
      return this.waitService.startWait(var1);
   }

   public void doNotCloseOnExit() {
      this.frame.setDefaultCloseOperation(2);
   }
}
