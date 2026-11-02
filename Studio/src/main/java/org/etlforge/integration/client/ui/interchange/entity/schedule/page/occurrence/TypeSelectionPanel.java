package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.etlforge.integration.client.ui.interchange.entity.schedule.resources.ScheduleResources;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.undo.ButtonGroupUndoableEdit;
import org.etlforge.ui.input.undo.UndoableButtonGroup;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.KongaRadioButton;

class TypeSelectionPanel extends InputPanel implements ActionListener {
   private final ScheduleOccurrencePanel mainPanel;
   private final String[] labels = new String[]{
      ScheduleResources.getString("Label.Daily"), ScheduleResources.getString("Label.Weekly"), ScheduleResources.getString("Label.Monthly")
   };
   private final JRadioButton[] selector = new JRadioButton[this.labels.length];

   public TypeSelectionPanel(ScheduleOccurrencePanel var1) {
      this.mainPanel = var1;
      this.setOpaque(false);
      this.createAndAddRadioButtons();
   }

   public void setType(int var1) {
      int var2 = this.getSelectedType();
      int var3 = var1 - 1;
      switch (var3) {
         case 0:
         case 1:
         case 2:
            this.selector[var3].setSelected(true);
            break;
         default:
            this.selector[0].setSelected(true);
      }

      if (var2 != var1) {
         this.actionPerformed(new ActionEvent(this, 1001, "set_type"));
      }
   }

   public int getSelectedType() {
      for (int var1 = 0; var1 < this.selector.length; var1++) {
         if (this.selector[var1].isSelected()) {
            return var1 + 1;
         }
      }

      return -1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      for (int var2 = 0; var2 < this.selector.length; var2++) {
         if (this.selector[var2].isSelected()) {
            this.mainPanel.setOccurrenceType(var2 + 1);
            break;
         }
      }

      this.fireInputChanged(this.selector, null);
   }

   private void createAndAddRadioButtons() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(3, 0, 10, 10);
      ButtonGroup var2 = this.createButtonGroup();

      for (int var3 = 0; var3 < this.labels.length; var3++) {
         JRadioButton var4 = this.createRadioButton(this.labels[var3], var2);
         var4.addActionListener(this);
         var1.add(var4);
         this.selector[var3] = var4;
      }

      this.wrap(var1);
   }

   private ButtonGroup createButtonGroup() {
      TypeSelectionPanel.RadioButtonGroup var1 = new TypeSelectionPanel.RadioButtonGroup();
      this.addChangeSource(var1);
      return var1;
   }

   private JRadioButton createRadioButton(String var1, ButtonGroup var2) {
      KongaRadioButton var3 = new KongaRadioButton(var1, false);
      var2.add(var3);
      var3.setOpaque(false);
      return var3;
   }

   private class RadioButtonGroup extends UndoableButtonGroup {
      private RadioButtonGroup() {
      }

      @Override
      protected UndoableEdit createUndoableEdit(ButtonModel var1, ButtonModel var2) {
         return new ButtonGroupUndoableEdit(this, var1, var2) {
            @Override
            public void redo() throws CannotRedoException {
               super.redo();
               TypeSelectionPanel.this.actionPerformed(new ActionEvent(this, 1001, "redo"));
            }

            @Override
            public void undo() throws CannotUndoException {
               super.undo();
               TypeSelectionPanel.this.actionPerformed(new ActionEvent(this, 1001, "undo"));
            }
         };
      }
   }
}
