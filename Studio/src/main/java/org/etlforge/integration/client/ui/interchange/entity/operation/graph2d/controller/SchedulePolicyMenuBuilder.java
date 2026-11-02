package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.data.entity.operation.SchedulePolicy;

class SchedulePolicyMenuBuilder {
   private final OperationGraphController controller;

   public SchedulePolicyMenuBuilder(OperationGraphController var1) {
      this.controller = var1;
   }

   public JMenu buildMenu(OperationWrapperNode var1) {
      JMenu var2 = new JMenu("Schedule Policy");
      if (var1.getDataObject().getScheduleId() != null) {
         var2.add(this.createMenuItem(var1));
      } else {
         var2.setEnabled(false);
      }

      return var2;
   }

   private JCheckBoxMenuItem createMenuItem(OperationWrapperNode var1) {
      JCheckBoxMenuItem var2 = new JCheckBoxMenuItem(
         "Start even if previously scheduled operation is still running", var1.getDataObject().getSchedulePolicy() == SchedulePolicy.STRICT
      );
      var2.addActionListener(new SchedulePolicyMenuBuilder.Listener(var1));
      return var2;
   }

   private class Listener implements ActionListener {
      private final OperationWrapperNode opNode;

      public Listener(OperationWrapperNode var2) {
         this.opNode = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JCheckBoxMenuItem var2 = (JCheckBoxMenuItem)var1.getSource();
         SchedulePolicy var3 = var2.isSelected() ? SchedulePolicy.STRICT : SchedulePolicy.YIELD_IF_STILL_RUNNING;
         SchedulePolicyMenuBuilder.this.controller.setSchedulePolicy(this.opNode, var3);
      }
   }
}
