package org.etlforge.integration.client.ui.interchange.entity.script;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.button.KongaButton;

public final class ScriptPadFrameDisplayer implements ScriptPadDisplayer {
   private JFrame frame;
   private ScriptPad pad;
   private final ApplicationWindow appWin;
   private final Action closeAction;

   public ScriptPadFrameDisplayer(ApplicationWindow var1) {
      this.appWin = var1;
      this.closeAction = new ScriptPadFrameDisplayer.ClosePadAction();
   }

   @Override
   public void open(ScriptPad var1) {
      if (var1 != this.pad) {
         this.close();
      }

      if (this.frame != null) {
         this.frame.toFront();
      } else {
         this.pad = var1;
         this.frame = this.createFrame();
         this.frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent var1) {
               ScriptPadFrameDisplayer.this.doCleanup();
            }
         });
         this.frame.setVisible(true);
      }
   }

   private JFrame createFrame() {
      assert this.frame == null : "There is already a Script Pad open.";
      JFrame var1 = new JFrame("ETLForge Script Pad");
      var1.setDefaultCloseOperation(2);
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10);
      var2.center(this.pad.getUi());
      BoxBuilder var3 = BoxBuilder.horizontal();
      Action var4 = this.pad.getUi().getTestAction();
      var3.add(var4).glue().add(new KongaButton(this.closeAction));
      var2.south(var3);
      var2.withBorder(Empty.border(10));
      var2.asContentPaneFor(var1);
      var1.pack();
      var1.setLocationRelativeTo(this.appWin.getFrame());
      this.installEscapeKey(var1);
      UiUtils.makeDefaultButton(var2.container(), var4);
      return var1;
   }

   private void installEscapeKey(JFrame var1) {
      var1.getRootPane().getInputMap(2).put(KeyUtils.ESCAPE, "escape");
      var1.getRootPane().getActionMap().put("escape", this.closeAction);
   }

   private void doCleanup() {
      this.pad.onPadClosed();
      this.pad = null;
      this.frame = null;
   }

   @Override
   public void close() {
      if (this.frame != null) {
         this.frame.dispose();
      }
   }

   @Override
   public void requestAttention() {
      if (this.frame != null) {
         this.frame.toFront();
      }
   }

   private class ClosePadAction extends AbstractAction {
      public ClosePadAction() {
         super("&Close");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         ScriptPadFrameDisplayer.this.close();
      }
   }
}
