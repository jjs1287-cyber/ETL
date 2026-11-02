package org.etlforge.integration.client.ui.script.builder;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.operation.RunOperationFilter;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class DefaultProjectItemTreeModelBuilder implements ProjectItemTreeModelBuilder {
   @Override
   public KongaTreeModel buildModel(IntegrationProject var1) {
      DefaultProjectItemTreeModelBuilder.Impl var2 = new DefaultProjectItemTreeModelBuilder.Impl(var1);
      return var2.buildModel();
   }

   private static class Impl {
      private final IntegrationProject project;

      public Impl(IntegrationProject var1) {
         this.project = var1;
      }

      public KongaTreeModel buildModel() {
         DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("Project Items", true);
         this.buildDatabaseBranch(var1);
         this.buildFileLocationBranch(var1);
         this.buildLdapLocationBranch(var1);
         this.buildOperationSubTree(var1);
         this.buildEntityTypeSubTree(EntityType.XsltTransform, "ProjectItemTree.Help.XsltTransforms", var1);
         this.buildEntityTypeSubTree(EntityType.Script, "ProjectItemTree.Help.Scripts", var1);
         this.buildEntityTypeSubTree(EntityType.EmailMessage, "ProjectItemTree.Help.EmailMessages", var1);
         this.buildEntityTypeSubTree(EntityType.SalesforceConnector, "ProjectItemTree.Help.SalesforceOrgs", var1);
         return new DefaultKongaTreeModel(var1);
      }

      private void buildDatabaseBranch(KongaTreeNode var1) {
         String var2 = Strings.get("ProjectItemTree.Databases");
         this.buildLocatableSubTree(var1, var2, "ProjectItemTree.Help.Databases", new LocatableFilter(DataLocationType.Database));
      }

      private void buildFileLocationBranch(KongaTreeNode var1) {
         String var2 = Strings.get("ProjectItemTree.FileLocations");
         this.buildLocatableSubTree(var1, var2, "ProjectItemTree.Help.FileLocations", new FileBasedLocationFilter());
      }

      private void buildLdapLocationBranch(KongaTreeNode var1) {
         String var2 = Strings.get("ProjectItemTree.LDAP");
         this.buildLocatableSubTree(var1, var2, "ProjectItemTree.Help.LDAP", new LocatableFilter(DataLocationType.LDAP));
      }

      private void buildLocatableSubTree(KongaTreeNode var1, String var2, String var3, EntityFilter var4) {
         DocumentedProjectItemNode var5 = new DocumentedProjectItemNode(var1, var2, var3);
         DefaultKongaTreeNode var6 = new DefaultKongaTreeNode(var5, EntityType.Source.toString(), true);
         ItemTreeBuilder var7 = new ItemTreeBuilder(this.project, EntityType.Source);
         var7.setFilter(var4);
         var7.buildTree(var6);
         DefaultKongaTreeNode var8 = new DefaultKongaTreeNode(var5, EntityType.Target.toString(), true);
         ItemTreeBuilder var9 = new ItemTreeBuilder(this.project, EntityType.Target);
         var9.setFilter(var4);
         var9.buildTree(var8);
      }

      private void buildOperationSubTree(KongaTreeNode var1) {
         ItemTreeBuilder var2 = new ItemTreeBuilder(this.project, EntityType.Operation);
         var2.setFilter(new RunOperationFilter());
         DocumentedProjectItemNode var3 = new DocumentedProjectItemNode(var1, EntityType.Operation.getPlural(), "ProjectItemTree.Help.Operations");
         var2.buildTree(var3);
      }

      private void buildEntityTypeSubTree(EntityType var1, String var2, KongaTreeNode var3) {
         if (this.project.getRootFolder(var1) == null) {
            return;
         }
         ItemTreeBuilder var4 = new ItemTreeBuilder(this.project, var1);
         DocumentedProjectItemNode var5 = new DocumentedProjectItemNode(var3, var1.getPlural(), var2);
         var4.buildTree(var5);
      }
   }
}
