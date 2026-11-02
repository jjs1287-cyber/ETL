package org.etlforge.integration.client.ui.entity;

import javax.swing.JComponent;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.DisposableWindowElementResource;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.integration.data.project.IntegrationProject;

public final class ModificationMonitorWindowContent extends AbstractWindowSectionContent {
   public static final String ID = ModificationMonitorWindowContent.class.getName();
   private final ModificationMonitorUi ui = new ModificationMonitorUi();

   public ModificationMonitorWindowContent(WindowSection var1) {
      super(ID, var1);
      this.addResource(new DisposableWindowElementResource(this.ui));
   }

   @Override
   public String getTitle() {
      return "Last Modified At";
   }

   @Override
   public void decorate(JComponent var1) {
      this.doDefaultDecoration(var1, this.ui);
   }

   public void setProject(IntegrationProject var1) {
      this.ui.setProject(var1);
   }
}
