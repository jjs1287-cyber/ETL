package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.etlforge.integration.client.ui.sfdc.wizard.SalesforceWizard;
import java.util.Collections;
import org.etlforge.application.ui.wizard.Wizard;
import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.integration.client.connect.text.NewFileStructureCallback;
import org.etlforge.integration.client.ui.entity.wizard.EntitySelectionPage;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.button.KongaCheckBox;

final class SelectionPage implements WizardPage {
   private final EntitySelectionPage<TextDocument> impl;
   private final KongaCheckBox hasHeaderChoice = new KongaCheckBox("The source file has a &header");

   public SelectionPage(SalesforceWizard<?> var1) {
      String var2 = "Select " + EntityType.Document.getDisplayName();
      this.impl = new EntitySelectionPage<>(var1, var2, var1.getProjectExplorer(), EntityType.Document, TextDocument.class, null);
      this.impl.setMainCaption(var2);
      this.impl.setSelectorLabel("Select an e&xisting " + EntityType.Document.getDisplayName() + ", or create a new one:");
      this.impl.setNewEntityCallback(new NewFileStructureCallback());
      this.hasHeaderChoice.setBorder(Empty.border(0, 8, 0, 0));
      this.impl.setAdditionalComponents(Collections.singletonList(this.hasHeaderChoice));
   }

   public SourceFileStructure getFileStructure() {
      return new SourceFileStructure(this.impl.getSelectedEntity(), this.hasHeaderChoice.isSelected());
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.impl.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.impl.requestFocus();
   }

   @Override
   public String getTitle() {
      return this.impl.getTitle();
   }

   @Override
   public void addToWizard() {
      this.impl.addToWizard();
   }

   @Override
   public void removeFromWizard() {
      this.impl.removeFromWizard();
   }

   @Override
   public Wizard getWizard() {
      return this.impl.getWizard();
   }

   @Override
   public boolean canContinue() {
      return this.impl.canContinue();
   }

   @Override
   public boolean prepareForForwardNavigation() {
      return this.impl.prepareForForwardNavigation();
   }

   @Override
   public boolean prepareForBackwardNavigation() {
      return this.impl.prepareForBackwardNavigation();
   }

   @Override
   public String getHelpId() {
      return this.impl.getHelpId();
   }
}
