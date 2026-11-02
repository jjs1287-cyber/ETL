package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.operation.OperationTypeMnemonics;
import org.etlforge.integration.data.entity.operation.OperationType;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

final class OperationTypeMenuBuilder {
   private final OperationGraphController controller;
   private final OperationWrapperNode node;
   private final Map<JRadioButtonMenuItem, OperationType> items;

   public OperationTypeMenuBuilder(OperationGraphController var1, OperationWrapperNode var2) {
      this.controller = var1;
      this.node = var2;
      this.items = Maps.newLinkedHashMap();
      OperationType var3 = var2.getPipeline().getType();

      for (OperationType var5 : OperationType.getSelectableTypes()) {
         JRadioButtonMenuItem var6 = this.createMenuItem(var3, var5);
         this.items.put(var6, var5);
      }

      ButtonUtils.makeMutuallyExclusive(this.items.keySet());
      ButtonUtils.addActionListener(new OperationTypeMenuBuilder.Listener(), this.items.keySet());
   }

   private JRadioButtonMenuItem createMenuItem(OperationType var1, OperationType var2) {
      JRadioButtonMenuItem var3 = new JRadioButtonMenuItem("", var2 == var1);
      ButtonUtils.decorate(var3, OperationTypeMnemonics.get(var2));
      return var3;
   }

   public JMenu buildMenu() {
      KongaMenu var1 = new KongaMenu(Strings.get("OpType.Menu.Name"));
      var1.setToolTipText(Strings.get("OpType.Menu.ToolTip"));

      for (JRadioButtonMenuItem var3 : this.items.keySet()) {
         var1.add(var3);
      }

      return var1;
   }

   public JPopupMenu buildPopupMenu() {
      KongaPopupMenu var1 = new KongaPopupMenu();
      var1.addItems(this.items.keySet().toArray());
      return var1;
   }

   private class Listener implements ActionListener {
      private Listener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         OperationType var2 = OperationTypeMenuBuilder.this.items.get(var1.getSource());
         OperationTypeMenuBuilder.this.controller.setOperationType(OperationTypeMenuBuilder.this.node, var2);
      }
   }
}
