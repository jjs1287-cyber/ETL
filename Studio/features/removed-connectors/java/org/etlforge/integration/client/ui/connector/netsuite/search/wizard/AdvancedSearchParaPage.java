package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.connector.netsuite.lookup.NetSuiteDefinitionQueryBuilderLookup;
import org.etlforge.integration.data.connector.NetSuiteEndpoint;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteConnectorObject;

public class AdvancedSearchParaPage extends AbstractWizardPage {
   private final AdvancedSearchPropertyModel model = new AdvancedSearchPropertyModel();
   private final AdvancedSearchParaUi ui = new AdvancedSearchParaUi(this.model);
   private final NetSuiteDefinitionQueryBuilderLookup searchDefLookup;

   public AdvancedSearchParaPage(Wizard var1, NetSuiteDefinitionQueryBuilderLookup var2) {
      super(var1, "Advanced Property");
      this.searchDefLookup = var2;
      this.setCanContinue(true);
   }

   public void setEndpointAndNetSuiteObject(NetSuiteEndpoint var1, NetSuiteConnectorObject var2) {
      AdvancedSearchModelPreparer.prepareModel(this.model, var1, var2, this.searchDefLookup, this.getWaitService());
   }

   public AdvancedSearchPropertyModel getAdvancedSearchPropertyModel() {
      return this.model;
   }

   @Override
   protected JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(null);
      var1.add(this.ui, true, true);
      return var1.asCenterBorderLayout();
   }

   @Override
   public final boolean prepareForForwardNavigation() {
      return this.ui.isValid();
   }
}
