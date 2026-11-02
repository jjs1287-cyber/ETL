package com.etlforge.integration.client.ui.sfdc.delete.page.v4;

import com.etlforge.integration.client.ui.sfdc.page.ImplementingObjectsLinkPanel;
import com.etlforge.integration.client.ui.sfdc.page.v4.AbstractSalesforceItemPanelV4;
import com.etlforge.integration.data.entity.SalesforceDelete;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.layout.OneColumnPanel;

public final class SalesforceDeletePanelV4 extends AbstractSalesforceItemPanelV4<SalesforceDelete> {
   public SalesforceDeletePanelV4(ApplicationWindow var1, SalesforceDelete var2, ImplementingObjectsLinkPanel var3) {
      super(var1, var2, var3);
   }

   @Override
   public void display() {
      super.display();
      this.doLayout();
   }

   private void doLayout() {
      OneColumnPanel var1 = this.startDefaultLayout();
      this.layoutCredentialsEditorAndLinks(var1);
      this.getDefaultLayout().add(var1.nonOpaque().invisible(), true);
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      throw new NotImplementedYetException();
   }
}
