package org.etlforge.integration.client.ui.script.builder;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.etlforge.ui.action.ToggleAction;

public final class ShowLineNumbersAction extends AbstractAction implements ToggleAction {
   private final ExpressionArea target;

   public ShowLineNumbersAction(ExpressionArea var1) {
      super("Show Line Numbers");
      this.target = var1;
      this.target.addPropertyChangeListener("displayRowNumber", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            ShowLineNumbersAction.this.setSelected((Boolean)var1.getNewValue());
         }
      });
   }

   @Override
   public boolean isSelected() {
      return this.target.isDisplayingRowNumbers();
   }

   @Override
   public void setSelected(boolean var1) {
      this.target.setDisplayRowNumbers(var1);
      this.putValue("ToggleAction#ON_OFF", var1);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      this.setSelected(!this.isSelected());
   }
}
