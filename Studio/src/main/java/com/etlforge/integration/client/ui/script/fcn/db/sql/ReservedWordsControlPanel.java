package com.etlforge.integration.client.ui.script.fcn.db.sql;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BoxBuilder;

public final class ReservedWordsControlPanel implements UiProvider {
   private final Receiver<String> receiver;
   private final List<JButton> buttons;

   public ReservedWordsControlPanel(SqlStringArea var1, String... var2) {
      this(asReceiver(var1), var2);
   }

   private static Receiver<String> asReceiver(final SqlStringArea var0) {
      return new Receiver<String>() {
         public void handle(String var1) {
            var0.insertReservedWord(var1);
         }
      };
   }

   public ReservedWordsControlPanel(Receiver<String> var1, String... var2) {
      Preconditions.checkNotNull(var1, "receiver");
      Preconditions.checkArgument(var2.length > 0, "At least one word must be provided");
      this.receiver = var1;
      this.buttons = Lists.newArrayList();
      ReservedWordsControlPanel.ButtonListener var3 = new ReservedWordsControlPanel.ButtonListener();

      for (String var7 : var2) {
         JButton var8 = new JButton(var7);
         var8.addActionListener(var3);
         this.buttons.add(var8);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(10, this.buttons);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.buttons.get(0).requestFocusInWindow();
   }

   private final class ButtonListener implements ActionListener {
      private ButtonListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JButton var2 = (JButton)var1.getSource();
         String var3 = var2.getText();
         ReservedWordsControlPanel.this.receiver.handle(var3);
      }
   }
}
