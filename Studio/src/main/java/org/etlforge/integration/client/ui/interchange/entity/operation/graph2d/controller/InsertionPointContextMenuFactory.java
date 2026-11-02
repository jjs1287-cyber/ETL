package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.OperationGraphConfig;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityConnection;
import org.etlforge.integration.data.entity.operation.pipeline.ActivityInsertionPolicy;
import org.etlforge.integration.data.entity.operation.pipeline.OperationPipeline;
import org.etlforge.integration.data.entity.operation.pipeline.PipelineActivityType;
import org.etlforge.ui.widget.menu.KongaMenuItem;

final class InsertionPointContextMenuFactory {
   private final OperationGraphController controller;
   private final String menuText;
   private final Icon icon;

   public InsertionPointContextMenuFactory(OperationGraphController var1, String var2, Icon var3) {
      this.controller = var1;
      this.menuText = var2;
      this.icon = var3;
   }

   public JPopupMenu createPopupMenu(OperationPipeline var1, ActivityConnection var2) {
      JPopupMenu var3 = new JPopupMenu();
      var3.add(this.createMenuItem(var1, var2, false));
      return var3;
   }

   public JMenuItem createMenu(OperationPipeline var1, ActivityConnection var2) {
      return this.createMenuItem(var1, var2, true);
   }

   private JMenuItem createMenuItem(OperationPipeline var1, ActivityConnection var2, boolean var3) {
      ActivityInsertionPolicy var4 = var1.getInsertionPolicy();
      ImmutableSet var5 = var4.getAllowedActivities(var2);
      List var6 = this.createInsertActions(var5, var1, var2);
      return var6.size() == 1 && !var3 ? this.createSingleActionMenuItem(var6) : this.createMenuImpl(var6);
   }

   private JMenuItem createSingleActionMenuItem(List<Action> var1) {
      assert var1.size() == 1;
      Action var2 = (Action)var1.get(0);
      String var3 = this.menuText + " " + var2.getValue("Name");
      var2.putValue("Name", var3);
      return new KongaMenuItem(var2);
   }

   private JMenuItem createMenuImpl(List<Action> var1) {
      JMenu var2 = new JMenu(this.menuText);
      var2.setIcon(this.icon);
      if (var1.isEmpty()) {
         var2.add(this.getEmptyItem());
      } else {
         for (Action var4 : var1) {
            var2.add(new KongaMenuItem(var4));
         }
      }

      return var2;
   }

   private JMenuItem getEmptyItem() {
      JMenuItem var1 = new JMenuItem("[empty]");
      var1.setToolTipText("Nothing can be inserted here");
      var1.setEnabled(false);
      return var1;
   }

   private List<Action> createInsertActions(Set<PipelineActivityType> var1, OperationPipeline var2, ActivityConnection var3) {
      List var4 = Iterables2.sortedCopy(var1, new Comparator<PipelineActivityType>() {
         public int compare(PipelineActivityType var1, PipelineActivityType var2x) {
            return var1.toString().compareTo(var2x.toString());
         }
      });
      ArrayList var5 = Lists.newArrayList();
      OperationGraphConfig var6 = this.controller.getGraph().getGraphConfig();

      for (Object __etlItem90 : var4) {
         PipelineActivityType var8 = (PipelineActivityType) __etlItem90;
         var5.add(new InsertionPointContextMenuFactory.CreateNewEntryAction(var2, var3, var8, var6.getSmallIcon(var8)));
      }

      return var5;
   }

   private class CreateNewEntryAction extends AbstractAction {
      private final OperationPipeline pipeline;
      private final ActivityConnection node;
      private final PipelineActivityType type;

      public CreateNewEntryAction(OperationPipeline var2, ActivityConnection var3, PipelineActivityType var4, Icon var5) {
         super(var4.getDisplayName());
         this.pipeline = var2;
         this.node = var3;
         this.type = var4;
         this.putValue("SmallIcon", var5);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         InsertionPointContextMenuFactory.this.controller.insertNewActivity(this.pipeline, this.node, this.type.createEmptyActivity());
      }
   }
}
