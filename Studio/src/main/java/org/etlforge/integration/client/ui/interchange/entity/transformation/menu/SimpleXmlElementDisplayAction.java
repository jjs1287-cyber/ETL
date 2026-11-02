package org.etlforge.integration.client.ui.interchange.entity.transformation.menu;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;
import org.etlforge.ui.action.ToggleAction;

public final class SimpleXmlElementDisplayAction extends AbstractAction implements ToggleAction {
   private TreeComponent tree;

   public SimpleXmlElementDisplayAction(String var1) {
      super(var1);
      this.setEnabled(false);
   }

   public void setTree(TreeComponent var1) {
      this.tree = var1;
      var1.addPropertyChangeListener("simpleXmlDisplay", new PropertyChangeListener() {
         @Override
         public void propertyChange(PropertyChangeEvent var1) {
            SimpleXmlElementDisplayAction.this.setSelected((Boolean)var1.getNewValue());
         }
      });
      this.setSelected(var1.isSimpleXmlElementDisplay());
      this.setEnabled(this.isSupported());
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      boolean var2 = this.isSelected();
      this.tree.setSimpleXmlElementDisplay(var2);
   }

   @Override
   public boolean isSelected() {
      Object var1 = this.getValue("SwingSelectedKey");
      return var1 instanceof Boolean ? (Boolean)var1 : false;
   }

   @Override
   public void setSelected(boolean var1) {
      this.putValue("SwingSelectedKey", var1);
   }

   public boolean isSupported() {
      return this.tree.isXml();
   }
}
