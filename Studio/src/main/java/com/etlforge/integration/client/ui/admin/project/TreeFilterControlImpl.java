package com.etlforge.integration.client.ui.admin.project;

import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.admin.resources.EnterpriseAdminResources;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import org.etlforge.integration.client.ui.project.info.AbstractTreeFilterControl;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.tree.KongaTreeCompositeFilter;
import org.etlforge.ui.widget.tree.KongaTreeFilter;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class TreeFilterControlImpl extends AbstractTreeFilterControl {
   private final JCheckBox operationChoice = new JCheckBox(EnterpriseAdminResources.getString("ProjectConsole.TreeFilter.Operations"), true);
   private final JCheckBox scheduleChoice = new JCheckBox(EnterpriseAdminResources.getString("ProjectConsole.TreeFilter.Schedules"), true);

   public TreeFilterControlImpl() {
      TreeFilterControlImpl.SwitchListener var1 = new TreeFilterControlImpl.SwitchListener();
      this.operationChoice.addActionListener(var1);
      this.scheduleChoice.addActionListener(var1);
      this.setNonTextFilter(this.getFilter());
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.operationChoice, this.scheduleChoice);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.operationChoice.requestFocusInWindow();
   }

   private KongaTreeFilter getFilter() {
      ArrayList var1 = Lists.newArrayList();
      if (!this.operationChoice.isSelected()) {
         var1.add(new TreeFilterControlImpl.Rejector(EntityType.Operation));
      }

      if (!this.scheduleChoice.isSelected()) {
         var1.add(new TreeFilterControlImpl.Rejector(EntityType.Schedule));
      }

      return var1.isEmpty() ? null : new KongaTreeCompositeFilter(var1);
   }

   private static class Rejector implements KongaTreeFilter {
      private final EntityType type;

      public Rejector(EntityType var1) {
         this.type = var1;
      }

      @Override
      public boolean apply(KongaTreeNode var1) {
         Object var2 = var1.getUserObject();
         if (var2 instanceof IntegrationEntity) {
            EntityType var3 = EntityUtils.getItemType((IntegrationEntity)var2);
            return var3 != this.type;
         } else {
            return true;
         }
      }
   }

   private class SwitchListener implements ActionListener {
      private SwitchListener() {
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TreeFilterControlImpl.this.setNonTextFilter(TreeFilterControlImpl.this.getFilter());
      }
   }
}
