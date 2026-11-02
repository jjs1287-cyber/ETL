package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.entity.DefaultPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.webservice.prefs.ShowWebServiceStructuresPreference;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class WebServiceCallPageConfig extends DefaultPageConfig {
   public WebServiceCallPageConfig(ApplicationWindow var1, ProjectPersistor var2) {
      super(var1, var2);
   }

   @Override
   protected void preConfiguration(IntegrationEntityPage var1) {
      WebServiceCallPage var2 = (WebServiceCallPage)var1;
      this.addAdditionalToolbarButtons(var2);
      this.installEditWsdlAction(var2);
   }

   private void addAdditionalToolbarButtons(WebServiceCallPage var1) {
      if (ShowWebServiceStructuresPreference.showStructures()) {
         JButton var2 = this.createToolbarButton(new WebServiceCallPageConfig.RefreshTreeStructuresAction(var1));
         this.addToolbarButton(var2);
      }
   }

   private void installEditWsdlAction(WebServiceCallPage var1) {
      EditWsdlAction var2 = new EditWsdlAction(this.getWindow(), var1.getObject(), this.getPersistor(), this.getDeployFacade());
      var1.setEditWsdlAction(var2);
   }

   private static class RefreshTreeStructuresAction extends AbstractAction {
      private final WebServiceCallPage page;

      public RefreshTreeStructuresAction(WebServiceCallPage var1) {
         this.page = var1;
         ActionDecorators.REFRESH.decorate(this);
         this.putValue("ShortDescription", Strings.get("Page.RefreshStructures.ToolTip"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.page.refreshTreeStructures();
      }
   }
}
