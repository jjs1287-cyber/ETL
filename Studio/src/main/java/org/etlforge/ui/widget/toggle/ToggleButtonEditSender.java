package org.etlforge.ui.widget.toggle;

import com.google.common.base.Preconditions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JToggleButton;
import org.etlforge.ui.undo.AbstractUndoableEditSender;

public class ToggleButtonEditSender extends AbstractUndoableEditSender {
   private final JToggleButton toggle;
   private final ToggleButtonEditSender.EditProducer editProducer;

   public ToggleButtonEditSender(JToggleButton var1) {
      Preconditions.checkNotNull(var1, "toggle");
      this.toggle = var1;
      this.editProducer = ToggleButtonEditSender.EditProducer.install(this);
   }

   @Override
   public void dispose() {
      if (!this.isDisposed()) {
         this.editProducer.uninstall(this.toggle);
         super.dispose();
      }
   }

   private static final class EditProducer extends AbstractUndoableEditSender.AbstractEditProducer implements ActionListener {
      private boolean value;

      private EditProducer(ToggleButtonEditSender var1) {
         super(var1);
         this.value = var1.toggle.isSelected();
      }

      public static ToggleButtonEditSender.EditProducer install(ToggleButtonEditSender var0) {
         ToggleButtonEditSender.EditProducer var1 = new ToggleButtonEditSender.EditProducer(var0);
         var0.toggle.addActionListener(var1);
         return var1;
      }

      public void uninstall(JToggleButton var1) {
         var1.removeActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (!this.isBlocked()) {
            JToggleButton var2 = (JToggleButton)var1.getSource();
            boolean var3 = var2.isSelected();
            if (var3 != this.value) {
               ToggleButtonUndoableEdit var4 = new ToggleButtonUndoableEdit(var2, this, this);
               this.value = var3;
               this.handleEdit(var4);
            }
         }
      }
   }
}
