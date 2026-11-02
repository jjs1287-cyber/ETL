package org.etlforge.integration.client.ui.project.info;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.integration.client.scheduler.ScheduledOperation;
import org.etlforge.integration.client.server.ui.ServerInfoNode;
import org.etlforge.integration.client.ui.entity.tree.IntegrationEntityTreeRenderer;
import org.etlforge.integration.client.ui.entity.tree.ProjectTreeFragmentCreator;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.id.ScheduleId;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tree.IntegrationEntityTreeNode;
import org.etlforge.integration.uidelegate.project.ProjectNode;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.NodeObjectConverter;
import org.etlforge.util.name.NameSorter;

final class TreeSupport {
   private final ProjectConsoleModel consoleModel;

   public TreeSupport(ProjectConsoleModel var1) {
      this.consoleModel = var1;
   }

   public NodeObjectConverter getSelectionConverter() {
      return new TreeSupport.SelectionConverter();
   }

   public TreeCellRenderer getNodeRenderer() {
      return new TreeSupport.Renderer();
   }

   public DefaultKongaTreeModel buildModel() {
      List var1 = this.consoleModel.getProjects();
      Collections.sort(var1, NameSorter.ignoreCase());
      ServerInfoNode var2 = new ServerInfoNode(this.consoleModel.getCurrentServer());

      for (Object __etlItem51 : this.getProjectNodes(var1)) {
         IntegrationEntityTreeNode var4 = (IntegrationEntityTreeNode) __etlItem51;
         var2.addChild(var4);
      }

      return new DefaultKongaTreeModel(var2);
   }

   private List<IntegrationEntityTreeNode> getProjectNodes(List<IntegrationProject> var1) {
      ArrayList var2 = new ArrayList();

      for (IntegrationProject var4 : var1) {
         ProjectNode var5 = this.createProjectTree(var4);
         var2.add(var5);
      }

      return var2;
   }

   private ProjectNode createProjectTree(IntegrationProject var1) {
      var1.refreshItemLookup();
      ProjectTreeFragmentCreator var2 = new ProjectTreeFragmentCreator(var1);
      ProjectNode var3 = new ProjectNode(var1);
      IntegrationEntityTreeNode var4 = this.insertOperations(var1, var2, var3);
      IntegrationEntityTreeNode var5 = this.insertSchedules(var1, var2, var3);
      ArrayListMultimap var6 = ArrayListMultimap.create();
      this.insertScheduleNodesUnderOperations(var1, var4, var6);
      this.insertOperationNodesUnderSchedules(var5, var6);
      return var3;
   }

   private IntegrationEntityTreeNode insertOperations(IntegrationProject var1, ProjectTreeFragmentCreator var2, ProjectNode var3) {
      IntegrationEntityTreeNode var4 = new IntegrationEntityTreeNode(var1.getRootFolder(EntityType.Operation));
      var3.addChild(var4);
      var2.createFragment(var4, EntityType.Operation);
      return var4;
   }

   private IntegrationEntityTreeNode insertSchedules(IntegrationProject var1, ProjectTreeFragmentCreator var2, ProjectNode var3) {
      IntegrationEntityTreeNode var4 = new IntegrationEntityTreeNode(var1.getRootFolder(EntityType.Schedule));
      var3.addChild(var4);
      var2.createFragment(var4, EntityType.Schedule);
      return var4;
   }

   private void insertScheduleNodesUnderOperations(IntegrationProject var1, KongaTreeNode var2, Multimap<ScheduleId, Operation> var3) {
      IterableEnumeration var4 = var2.breadthFirstEnumeration();

      while (var4.hasMoreElements()) {
         KongaTreeNode var5 = (KongaTreeNode)var4.nextElement();
         Object var6 = var5.getUserObject();
         if (var6 instanceof Operation) {
            IntegrationEntityTreeNode var7 = (IntegrationEntityTreeNode)var5;
            Operation var8 = (Operation)var6;
            Schedule var9 = var1.getItemLookup().getEntity(var8.getScheduleId(), Schedule.class);
            if (var9 != null) {
               var7.setAllowsChildren(true);
               IntegrationEntityTreeNode var10 = new IntegrationEntityTreeNode(var9);
               var7.addChild(var10);
               var3.put(var9.getID(), var8);
            }
         }
      }
   }

   private void insertOperationNodesUnderSchedules(KongaTreeNode var1, ListMultimap<ScheduleId, Operation> var2) {
      IterableEnumeration var3 = var1.breadthFirstEnumeration();

      while (var3.hasMoreElements()) {
         KongaTreeNode var4 = (KongaTreeNode)var3.nextElement();
         Object var5 = var4.getUserObject();
         if (var5 instanceof Schedule) {
            IntegrationEntityTreeNode var6 = (IntegrationEntityTreeNode)var4;
            Schedule var7 = (Schedule)var5;
            List var8 = var2.get(var7.getID());
            Collections.sort(var8, NameSorter.ignoreCase());

            for (Object __etlItem127 : var8) {
               Operation var10 = (Operation) __etlItem127;
               var6.setAllowsChildren(true);
               IntegrationEntityTreeNode var11 = new IntegrationEntityTreeNode(var10);
               var6.addChild(var11);
            }
         }
      }
   }

   private static class Renderer extends IntegrationEntityTreeRenderer {
      private Renderer() {
      }

      @Override
      public JComponent getTreeCellRendererComponent(JTree var1, Object var2, boolean var3, boolean var4, boolean var5, int var6, boolean var7) {
         JComponent var8 = super.getTreeCellRendererComponent(var1, var2, var3, var4, var5, var6, var7);
         boolean var9 = true;
         if (var2 instanceof KongaTreeNode) {
            KongaTreeNode var10 = (KongaTreeNode)var2;
            Object var11 = var10.getUserObject();
            if (var11 instanceof Schedule) {
               var9 = this.checkEnabledState(var10, (Schedule)var11);
            } else if (var11 instanceof Operation) {
               var9 = this.checkEnabledState(var10, (Operation)var11);
            }
         }

         var8.setEnabled(var9);
         return var8;
      }

      private boolean checkEnabledState(KongaTreeNode var1, Schedule var2) {
         boolean var3 = true;
         if (var2.isEnabled()) {
            KongaTreeNode var4 = var1.getParent();
            if (var4 != null) {
               Object var5 = var4.getUserObject();
               if (var5 instanceof Operation) {
                  var3 = ((Operation)var5).isScheduleEnabled();
               }
            }
         } else {
            var3 = false;
         }

         return var3;
      }

      private boolean checkEnabledState(KongaTreeNode var1, Operation var2) {
         KongaTreeNode var3 = var1.getParent();
         if (var3 != null) {
            Object var4 = var3.getUserObject();
            if (var4 instanceof Schedule) {
               Schedule var5 = (Schedule)var4;
               return var5.isEnabled() && var2.isScheduleEnabled();
            }
         }

         return true;
      }
   }

   private static class SelectionConverter extends NodeObjectConverter.Adapter {
      private SelectionConverter() {
      }

      @Override
      public Object toObject(TreeNode var1) {
         if (var1 instanceof IntegrationEntityTreeNode) {
            IntegrationEntityTreeNode var2 = (IntegrationEntityTreeNode)var1;
            IntegrationEntity var3 = var2.getUserObject();
            EntityType var4 = var3.getEntityType();
            if (var4 == EntityType.Operation) {
               return this.handleOperationNode(var2, var3);
            } else {
               return var4 == EntityType.Schedule ? this.handleScheduleNode(var2, var3) : var3;
            }
         } else {
            return null;
         }
      }

      private Object handleOperationNode(IntegrationEntityTreeNode var1, IntegrationEntity var2) {
         KongaTreeNode var3 = var1.getParent();
         if (var3 != null) {
            Object var4 = var3.getUserObject();
            if (var4 instanceof Schedule) {
               Schedule var5 = (Schedule)var4;
               return new ScheduledOperation(var5, (Operation)var2);
            }
         }

         return var1.getUserObject();
      }

      private Object handleScheduleNode(IntegrationEntityTreeNode var1, IntegrationEntity var2) {
         KongaTreeNode var3 = var1.getParent();
         if (var3 != null) {
            Object var4 = var3.getUserObject();
            if (var4 instanceof Operation) {
               Operation var5 = (Operation)var4;
               return new ScheduledOperation((Schedule)var2, var5);
            }
         }

         return var1.getUserObject();
      }
   }
}
