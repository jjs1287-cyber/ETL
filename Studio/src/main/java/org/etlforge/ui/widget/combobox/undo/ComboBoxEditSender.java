package org.etlforge.ui.widget.combobox.undo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import org.etlforge.ui.undo.AbstractUndoableEditSender;
import org.etlforge.ui.undo.UndoableEditSender;

public class ComboBoxEditSender extends AbstractUndoableEditSender implements UndoableEditSender {
   private final JComboBox comboBox;
   private final ComboBoxEditSender.EditProducer editProducer;

   public ComboBoxEditSender(JComboBox var1) {
      this.comboBox = var1;
      this.editProducer = this.installEditProducer();
   }

   private ComboBoxEditSender.EditProducer installEditProducer() {
      ComboBoxEditSender.EditProducer var1 = new ComboBoxEditSender.EditProducer(this);
      var1.install(this.comboBox);
      return var1;
   }

   @Override
   public void dispose() {
      if (!this.isDisposed()) {
         this.editProducer.uninstall(this.comboBox);
         super.dispose();
      }
   }

   private static class EditProducer extends AbstractUndoableEditSender.AbstractEditProducer implements ActionListener {
      private Object currentItem;

      public EditProducer(ComboBoxEditSender var1) {
         super(var1);
      }

      public void install(JComboBox var1) {
         this.currentItem = var1.getSelectedItem();
         var1.addActionListener(this);
      }

      public void uninstall(JComboBox var1) {
         var1.removeActionListener(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (!this.isBlocked()) {
            JComboBox var2 = (JComboBox)var1.getSource();
            Object var3 = var2.getSelectedItem();
            if (var3 != this.currentItem) {
               UndoableComboBoxEdit var4 = new UndoableComboBoxEdit(var2, var3, this.currentItem, this, this);
               this.currentItem = var3;
               this.handleEdit(var4);
            }
         }
      }
   }
}
