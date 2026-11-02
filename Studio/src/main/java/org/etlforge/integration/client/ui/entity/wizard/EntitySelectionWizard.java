package org.etlforge.integration.client.ui.entity.wizard;

import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageException;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class EntitySelectionWizard<T extends IntegrationEntity> extends ApplicationWindowWizard<T> {
   private final EntitySelectionPage<T> page;
   private T selected;

   public EntitySelectionWizard(Class<T> var1, EntityType var2, EntityExplorerSupport var3) {
      super(var3.getWindow());
      this.page = this.createPage(var1, var2, var3);
      WizardPageList var4 = new WizardPageList();
      var4.addPage(this.page);
      this.setPageChain(var4);
   }

   private EntitySelectionPage<T> createPage(Class<T> var1, EntityType var2, EntityExplorerSupport var3) {
      EntitySelectionPage var4 = new EntitySelectionPage(this, var2.getDisplayName() + " Selection Wizard", var3, var2, var1, null);
      var4.setMainCaption("Select " + var2.getIndefiniteArticle() + " " + var2.getDisplayName());
      var4.setSelectorLabel("&Available " + var2.getPlural());
      return var4;
   }

   @Override
   public WizardPageChain getPageChain() {
      return super.getPageChain();
   }

   public void setSelectedEntity(T var1) {
      this.page.setSelectedEntity(var1);
   }

   public void setSelectedEntityId(IntegrationEntityId var1) {
      this.page.setSelectedEntityId(var1);
   }

   public void setCaption(String var1) {
      this.page.setSubCaption(var1);
   }

   protected T finishWizard() throws WizardPageException {
      this.selected = this.page.getSelectedEntity();
      return this.selected;
   }

   @Override
   protected boolean onWizardCancelled() {
      this.selected = null;
      return true;
   }

   public T getSelectedEntity() {
      return this.selected;
   }
}
