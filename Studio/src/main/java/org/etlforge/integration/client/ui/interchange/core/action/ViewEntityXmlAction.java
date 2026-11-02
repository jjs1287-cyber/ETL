package org.etlforge.integration.client.ui.interchange.core.action;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import org.etlforge.application.ui.action.AbstractApplicationAction;
import org.etlforge.application.ui.widget.XmlFileViewer;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.FileBasedProjectPersistor;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class ViewEntityXmlAction extends AbstractApplicationAction {
   public static final String ID = "action.ic.ViewXml";
   private final ApplicationWindow appWin;
   private final IntegrationEntity item;

   public ViewEntityXmlAction(ApplicationWindow var1, IntegrationEntity var2) {
      super("action.ic.ViewXml", InterchangeActionDictionary.DICTIONARY);
      this.appWin = var1;
      this.item = var2;
      this.setEnabled(true);
   }

   @Override
   public void execute(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewEntityXmlAction.this.openXml();
         }
      });
   }

   private void openXml() {
      File var1 = this.getXmlFile(this.item);
      if (var1 != null && var1.exists()) {
         XmlFileViewer var2 = XmlFileViewer.getViewer(this.appWin);
         var2.viewFile(var1);
      }
   }

   private File getXmlFile(IntegrationEntity var1) {
      ManagedProject var2 = ProjectUtils.getManagedProject(var1);
      ProjectPersistor var3 = var2.getProjectPersistor();
      return var3 instanceof FileBasedProjectPersistor ? ((FileBasedProjectPersistor)var3).getFile(var1) : null;
   }
}
