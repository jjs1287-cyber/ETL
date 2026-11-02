package org.etlforge.integration.client.ui.script.builder;

public final class ProjectItemSelectorFactory implements ScriptBuilderSectionFactory {
   private final ProjectItemTreeModelBuilder treeModelBuilder;

   public ProjectItemSelectorFactory(ProjectItemTreeModelBuilder var1) {
      this.treeModelBuilder = var1;
   }

   @Override
   public ScriptBuilderSection newSection(ScriptBuilderModel var1) {
      ProjectItemTree var2 = new ProjectItemTree(var1, this.treeModelBuilder);
      return new ProjectItemTreePanel(var2);
   }
}
