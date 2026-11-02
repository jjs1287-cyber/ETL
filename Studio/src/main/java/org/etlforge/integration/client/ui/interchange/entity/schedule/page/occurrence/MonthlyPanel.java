package org.etlforge.integration.client.ui.interchange.entity.schedule.page.occurrence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.undo.ButtonGroupUndoableEdit;
import org.etlforge.ui.input.undo.UndoableButtonGroup;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.widget.button.KongaRadioButton;

class MonthlyPanel extends InputPanel {
   private JRadioButton type_A_selector;
   private JRadioButton type_B_selector;
   private final Monthly_A_Panel panelA;
   private final Monthly_B_Panel panelB;

   public MonthlyPanel(Monthly_A_Panel var1, Monthly_B_Panel var2) {
      this.panelA = var1;
      this.panelB = var2;
      this.setOpaque(false);
      this.createSelectors();
      this.addInputComponents();
      this.layoutComponents();
   }

   @Override
   public void setActive(boolean var1) {
      super.setActive(var1);
      if (var1) {
         if (this.getMonthlyType() == 0) {
            this.panelB.setActive(false);
         } else {
            this.panelA.setActive(false);
         }
      }
   }

   public int getMonthlyType() {
      return this.type_A_selector.isSelected() ? 0 : 1;
   }

   public void setMonthlyType(int var1) {
      if (var1 == 0) {
         this.type_A_selector.setSelected(true);
      } else {
         this.type_B_selector.setSelected(true);
      }

      this.setSubPanelState();
   }

   private void createSelectors() {
      MonthlyPanel.RadioButtonGroup var1 = new MonthlyPanel.RadioButtonGroup();
      this.addChangeSource(var1);
      ActionListener var2 = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            MonthlyPanel.this.setSubPanelState();
         }
      };
      this.type_A_selector = this.createRadioButton(var1, var2);
      this.type_B_selector = this.createRadioButton(var1, var2);
   }

   private JRadioButton createRadioButton(ButtonGroup var1, ActionListener var2) {
      KongaRadioButton var3 = new KongaRadioButton();
      var1.add(var3);
      var3.setOpaque(false);
      var3.addActionListener(var2);
      return var3;
   }

   private void addInputComponents() {
      this.addInputPanel(this.panelA);
      this.addInputPanel(this.panelB);
   }

   private void layoutComponents() {
      TwoColumnPanel var1 = new TwoColumnPanel();
      var1.withAnchor(TwoColumnPanel.NORTHWEST);
      var1.addRow(this.type_A_selector, this.panelA.getUi());
      var1.addRow(this.type_B_selector, this.panelB.getUi());
      this.wrap(var1);
   }

   private void setSubPanelState() {
      if (this.isActive()) {
         if (this.type_A_selector.isSelected()) {
            this.panelA.setActive(true);
            this.panelB.setActive(false);
         } else {
            this.panelA.setActive(false);
            this.panelB.setActive(true);
         }
      }
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
               MonthlyPanel.this.setSubPanelState();
            }

            @Override
            public void undo() throws CannotUndoException {
               super.undo();
               MonthlyPanel.this.setSubPanelState();
            }
         };
      }
   }
}
