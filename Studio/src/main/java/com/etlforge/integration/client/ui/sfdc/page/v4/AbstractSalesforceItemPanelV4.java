package com.etlforge.integration.client.ui.sfdc.page.v4;

import com.etlforge.integration.client.ui.sfdc.page.ImplementingObjectsLinkPanel;
import javax.swing.Action;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;

public abstract class AbstractSalesforceItemPanelV4<T extends IntegrationEntity & SalesforceWizardEntity> extends IntegrationEntityPanel {
   private final ImplementingObjectsLinkPanel links;
   private Action runAction;
   private Action testAction;
   private CredentialsEditor credentialsEditor;

   public AbstractSalesforceItemPanelV4(ApplicationWindow var1, T var2, ImplementingObjectsLinkPanel var3) {
      super(var1, var2);
      this.links = var3;
      var3.registerAsChangeSource(this);
   }

   public final void setRunAction(Action var1) {
      this.runAction = var1;
   }

   public final void setTestAction(Action var1) {
      this.testAction = var1;
   }

   public final void setCredentialsEditor(CredentialsEditor var1) {
      this.credentialsEditor = var1;
   }

   @Override
   public final void dispose() {
      this.links.dispose();
      this.disposeAdditionalResources();
      super.dispose();
   }

   protected void disposeAdditionalResources() {
   }

   protected final OneColumnPanel startDefaultLayout() {
      OneColumnPanel var1 = new OneColumnPanel();
      var1.withInsets(0, 0, 10, 0);
      var1.setWeightX(1.0);
      this.layoutActions(var1);
      return var1;
   }

   private void layoutActions(OneColumnPanel var1) {
      if (this.runAction != null || this.testAction != null) {
         var1.add(LayoutUtils.layoutActions(this.runAction, this.testAction));
      }
   }

   protected final void layoutCredentialsEditorAndLinks(OneColumnPanel var1) {
      var1.addAll(this.credentialsEditor, this.links);
   }

   protected final UiProvider getLinksLayout() {
      return this.links;
   }

   protected final CredentialsEditor getCredentialsEditor() {
      return this.credentialsEditor;
   }

   @Override
   protected final void validate() throws IntegrationDataPanelException {
      this.links.validate();
      this.validateOtherThingsThanLinks();
   }

   protected void validateOtherThingsThanLinks() throws IntegrationDataPanelException {
   }
}
