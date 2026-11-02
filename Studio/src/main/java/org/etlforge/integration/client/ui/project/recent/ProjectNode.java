package org.etlforge.integration.client.ui.project.recent;

import java.io.File;
import java.util.Date;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.project.ProjectLocation;
import org.etlforge.integration.client.ui.project.resources.ProjectUiResources;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.util.decorator.DateCell;
import org.etlforge.ui.util.decorator.TreeNodeDecor;
import org.etlforge.ui.util.decorator.TreeNodeDecorator;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.util.version.Version;

final class ProjectNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
   private static final TextDecorator STYLE = getStyle();

   public ProjectNode(ProjectLocation var1) {
      super(var1, true);
      this.addPropertyNode(var1);
      this.addLastAccessedNode(var1);
      this.addLastModifiedNode(var1);
      this.addVersionRequiredNode(var1);
   }

   @Override
   public void decorate(TreeNodeDecor var1) {
      var1.setText(this.getDisplayName());
      var1.setIcon(ProjectUiResources.PROJECT_16);
      var1.setTextStyle(STYLE);
   }

   @Override
   public String getDisplayName() {
      return this.getUserObject().getProjectName();
   }

   public ProjectLocation getUserObject() {
      return (ProjectLocation)super.getUserObject();
   }

   private void addPropertyNode(ProjectLocation var1) {
      File var2 = var1.getLocation().getParentFile();
      String var3 = Strings.get("Project.Location") + ": " + var2.getAbsolutePath();
      new ProjectNode.PropertyNode(this, var3);
   }

   private void addLastAccessedNode(ProjectLocation var1) {
      this.addDateNode(Strings.get("Project.LastAccessed"), var1.getLastAccessed());
   }

   private void addLastModifiedNode(ProjectLocation var1) {
      this.addDateNode(Strings.get("Project.LastModified"), var1.getLastModified());
   }

   private void addVersionRequiredNode(ProjectLocation var1) {
      this.addVersionNode(Strings.get("Project.RequiresVersion"), var1.getVersionRequired());
   }

   private void addDateNode(String var1, Date var2) {
      DateCell var3 = new DateCell(var2);
      String var4 = var1 + ": " + var3.getDisplayString();
      new ProjectNode.PropertyNode(this, var4);
   }

   private void addVersionNode(String var1, Version var2) {
      String var3 = var1 + ": " + var2.toString();
      new ProjectNode.PropertyNode(this, var3);
   }

   private static TextDecorator getStyle() {
      TextStyle var0 = TextStyles.TreeLeafText;
      TextStyle var1 = TextStyle.newStyle(var0, 1);
      return var1.asTextDecorator();
   }

   private static class PropertyNode extends DefaultKongaTreeNode implements TreeNodeDecorator {
      private static final TextDecorator PROPERTY_STYLE = TextStyles.TreeLeafText.asTextDecorator();

      public PropertyNode(KongaTreeNode var1, String var2) {
         super(var1, var2, false);
      }

      @Override
      public void decorate(TreeNodeDecor var1) {
         var1.setText(this.getUserObject());
         var1.setIcon(null);
         var1.setTextStyle(PROPERTY_STYLE);
      }

      public String getUserObject() {
         return (String)super.getUserObject();
      }

      @Override
      public String getDisplayName() {
         return this.getUserObject();
      }
   }
}
