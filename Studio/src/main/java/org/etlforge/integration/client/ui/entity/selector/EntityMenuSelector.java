package org.etlforge.integration.client.ui.entity.selector;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.etlforge.integration.client.ui.entity.tree.ProjectTreeFragmentCreator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.widget.menu.KongaMenu;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeStatus;

public final class EntityMenuSelector<T extends IntegrationEntity> {
   private static final String ID_KEY = "entityId";
   private final IntegrationProject project;
   private final EntityType type;
   private final Class<T> clazz;
   private EntityFilter filter;
   private EntityMenuSelector.FilterPolicy filterPolicy;
   private String disabledItemToolTip;
   private IntegrationEntity preSelectedEntity;
   private boolean iconDecorateItems;

   public EntityMenuSelector(IntegrationProject var1, EntityType var2, Class<T> var3) {
      Preconditions.checkNotNull(var1, "project");
      Preconditions.checkNotNull(var2, "type");
      Preconditions.checkNotNull(var3, "clazz");
      this.project = var1;
      this.type = var2;
      this.clazz = var3;
      this.filter = EntityFilter.ACCEPT_ALL;
      this.filterPolicy = EntityMenuSelector.FilterPolicy.HIDE;
      this.iconDecorateItems = true;
   }

   public void setFilter(EntityFilter var1) {
      this.filter = var1 != null ? var1 : EntityFilter.ACCEPT_ALL;
   }

   public void setFilterPolicy(EntityMenuSelector.FilterPolicy var1) {
      Preconditions.checkNotNull(var1, "plicy");
      this.filterPolicy = var1;
   }

   public void setPreselectedEntity(IntegrationEntity var1) {
      this.preSelectedEntity = var1;
   }

   public void setIconDecorateItems(boolean var1) {
      this.iconDecorateItems = var1;
   }

   public void setDisabledItemsToolTip(String var1) {
      this.disabledItemToolTip = var1;
   }

   public KongaMenu getMenu(String var1, Icon var2, Receiver<T> var3) {
      KongaMenu var4 = new KongaMenu(var1, var2);

      for (JMenuItem var6 : this.buildMenu(var3)) {
         var4.add(var6);
      }

      return var4;
   }

   public JPopupMenu getPopupMenu(Receiver<T> var1) {
      JPopupMenu var2 = new JPopupMenu();

      for (JMenuItem var4 : this.buildMenu(var1)) {
         var2.add(var4);
      }

      return var2;
   }

   private List<JMenuItem> buildMenu(Receiver<T> var1) {
      ProjectTreeFragmentCreator var2 = new ProjectTreeFragmentCreator(this.project);
      RootFolder var3 = this.project.getRootFolder(this.type);
      IntegrationEntityTreeNode var4 = new IntegrationEntityTreeNode(var3);
      var2.createFragment(var4, this.type);
      ArrayList var5 = Lists.newArrayList();
      if (var4.getChildCount() > 0) {
         this.addMenuItems(var5, var4, new EntityMenuSelector.Listener(var1));
      } else {
         var5.add(this.createEmptyItem());
      }

      return var5;
   }

   private void addMenuItems(List<JMenuItem> var1, KongaTreeNode var2, EntityMenuSelector<T>.Listener var3) {
      for (KongaTreeNode var5 : var2.children()) {
         IntegrationEntityTreeNode var6 = (IntegrationEntityTreeNode)var5;
         IntegrationEntity var7 = var6.getUserObject();
         if (var7 instanceof Folder) {
            this.addFolder(var1, var6, var7, var3);
         } else {
            this.addEntity(var1, var3, var6, var7);
         }
      }
   }

   private void addFolder(List<JMenuItem> var1, IntegrationEntityTreeNode var2, IntegrationEntity var3, EntityMenuSelector<T>.Listener var4) {
      JMenu var5 = new JMenu(var3.getName());
      var5.setIcon(var2.getNodeIcon(NodeStatus.CLOSED));
      var1.add(var5);
      if (var2.getChildCount() > 0) {
         ArrayList var6 = Lists.newArrayList();
         this.addMenuItems(var6, var2, var4);

         for (Object __etlItem128 : var6) {
            JMenuItem var8 = (JMenuItem) __etlItem128;
            var5.add(var8);
         }
      } else {
         var5.add(this.createEmptyItem());
      }

      var1.add(var5);
   }

   private void addEntity(List<JMenuItem> var1, EntityMenuSelector<T>.Listener var2, IntegrationEntityTreeNode var3, IntegrationEntity var4) {
      boolean var5 = this.filter.apply(var4);
      if (var5 || this.filterPolicy == EntityMenuSelector.FilterPolicy.DISABLE) {
         JMenuItem var6 = this.createAndAddEntityItem(var1, var3, var4, var2);
         if (!var5) {
            var6.setEnabled(false);
            if (this.disabledItemToolTip != null) {
               var6.setToolTipText(this.disabledItemToolTip);
            }
         }
      }
   }

   private JMenuItem createAndAddEntityItem(List<JMenuItem> var1, IntegrationEntityTreeNode var2, IntegrationEntity var3, EntityMenuSelector<T>.Listener var4) {
      Icon var5 = this.iconDecorateItems ? var2.getNodeIcon(NodeStatus.CLOSED) : null;
      JMenuItem var6 = this.createEntityItem(var3, var5);
      var1.add(var6);
      var6.putClientProperty("entityId", var3.getID());
      var6.addActionListener(var4);
      var6.setToolTipText(ToolTipStringFactory.getToolTip(var3));
      return var6;
   }

   protected JMenuItem createEntityItem(IntegrationEntity var1, Icon var2) {
      return var1 == this.preSelectedEntity ? new JCheckBoxMenuItem(var1.getName(), var2, true) : new JMenuItem(var1.getName(), var2);
   }

   private JMenuItem createEmptyItem() {
      JMenuItem var1 = new JMenuItem("[empty]");
      var1.setEnabled(false);
      return var1;
   }

   public enum FilterPolicy {
      HIDE,
      DISABLE;
   }

   private class Listener implements ActionListener {
      private final Receiver<T> callback;

      public Listener(Receiver<T> var2) {
         this.callback = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         JMenuItem var2 = (JMenuItem)var1.getSource();
         IntegrationEntityId var3 = (IntegrationEntityId)var2.getClientProperty("entityId");
         IntegrationEntity var4 = EntityMenuSelector.this.project.getItemLookup().getEntity(var3, EntityMenuSelector.this.clazz);
         if (var4 != null) {
            this.callback.handle((T)var4);
         }
      }
   }
}
