package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.deploy.DeployFacade;
import org.etlforge.integration.client.ui.interchange.entity.webservice.edit.wsdl.WsdlEditor;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.integration.data.persist.EntityPersistor;

public final class EditWsdlAction extends AbstractAction {
   private final ApplicationWindow appWin;
   private final WebServiceCall wsCall;
   private final EntityPersistor persistor;
   private final DeployFacade deployFacade;

   public EditWsdlAction(ApplicationWindow var1, WebServiceCall var2, EntityPersistor var3, DeployFacade var4) {
      super(Strings.get("Page.EditWsdl.Name"));
      this.appWin = var1;
      this.wsCall = var2;
      this.persistor = var3;
      this.deployFacade = var4;
      this.putValue("ShortDescription", Strings.get("Page.EditWsdl.ToolTip"));
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EditWsdlAction.this.executeImpl();
         }
      });
   }

   private void executeImpl() {
      if (this.wsCall.isPartOfSalesforcePartnerWizard()) {
         Alert.info(this.appWin.getFrame(), "This Web Service Method is part of a Salesforce wizard.\nIts WSDL cannot be edited.", "Not Editable");
      } else {
         WsdlEditor var1 = new WsdlEditor(this.wsCall, this.persistor, this.deployFacade);
         var1.setWaitService(this.appWin);
         var1.start();
      }
   }
}
