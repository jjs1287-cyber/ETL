package org.etlforge.integration.client.ui.interchange.entity.webservice.page;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.mapping.webservice.WebServiceCallStructures;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.entity.webservice.WebServiceCallStructuresPanel;
import org.etlforge.integration.client.ui.interchange.entity.webservice.prefs.ShowWebServiceStructuresPreference;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.entity.WebServiceCall;
import org.etlforge.ui.canvas.ComponentCanvasFactory;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DropDownPanel;

public class WebServiceCallPanel extends IntegrationEntityPanel {
   private final WebServiceCallDetailsPanel detailsPanel;
   private final WebServiceCallSecurityPanel securityPanel;
   private final WebServiceCallRestrictionsPanel restrictionsPanel;
   private final WebServiceCallStructuresPanel structuresPanel;

   public WebServiceCallPanel(ApplicationWindow var1, WebServiceCall var2) {
      super(var1, var2);
      this.detailsPanel = new WebServiceCallDetailsPanel(var2);
      this.securityPanel = new WebServiceCallSecurityPanel(var2);
      this.restrictionsPanel = new WebServiceCallRestrictionsPanel(var2);
      this.structuresPanel = ShowWebServiceStructuresPreference.showStructures() ? new WebServiceCallStructuresPanel() : null;
      this.layoutComponents();
      this.addInputFields();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   public void displayStructures(WebServiceCallStructures var1) {
      if (this.structuresPanel != null) {
         this.structuresPanel.displayStructures(var1);
      }
   }

   public void setEditWsdlAction(Action var1) {
      this.detailsPanel.setEditWsdlAction(var1);
   }

   Collection<BoundProperty> getBoundPropertyControls() {
      return this.detailsPanel.getBoundPropertyControls();
   }

   private void layoutComponents() {
      JPanel var1 = new JPanel(new GridBagLayout());
      GridBagConstraints var2 = this.createInitialConstraints();
      this.layoutNameField(var1, var2);
      this.layoutDetailsPanel(var1, var2);
      this.layoutSecurityPanel(var1, var2);
      this.layoutRestrictionsPanel(var1, var2);
      this.layoutStructureTree(var1, var2);
      this.setLayout(new FlowLayout(0));
      this.add(var1);
   }

   private void layoutStructureTree(JPanel var1, GridBagConstraints var2) {
      if (this.structuresPanel != null) {
         var2.gridx = 0;
         var2.gridy = 3;
         var2.gridwidth = 2;
         ComponentCanvasFactory.gridBag(var1, var2, this.structuresPanel);
      }
   }

   private void layoutRestrictionsPanel(JPanel var1, GridBagConstraints var2) {
      var2.gridx = 1;
      JComponent var3 = this.getDropDownPanel(this.restrictionsPanel.getUi(), Strings.get("Page.Restrictions.Title"));
      var1.add(var3, var2);
   }

   private void layoutSecurityPanel(JPanel var1, GridBagConstraints var2) {
      var2.gridy = 2;
      var2.gridwidth = 1;
      JComponent var3 = this.getDropDownPanel(this.securityPanel.getUi(), Strings.get("Page.Security.Title"));
      var1.add(var3, var2);
   }

   private void layoutDetailsPanel(JPanel var1, GridBagConstraints var2) {
      var2.insets = new Insets(5, 5, 5, 15);
      var2.fill = 3;
      var2.gridy = 1;
      var2.gridwidth = 2;
      JComponent var3 = this.getDropDownPanel(this.detailsPanel.getUi(), Strings.get("Page.Details.Title"));
      var1.add(var3, var2);
   }

   private void layoutNameField(JPanel var1, GridBagConstraints var2) {
      var2.insets = new Insets(5, 5, 15, 5);
      var1.add(this.getNameFieldLayout(), var2);
   }

   private GridBagConstraints createInitialConstraints() {
      GridBagConstraints var1 = new GridBagConstraints();
      var1.anchor = 18;
      var1.gridx = 0;
      var1.gridy = 0;
      return var1;
   }

   private JComponent getDropDownPanel(JPanel var1, String var2) {
      DropDownPanel var3 = new DropDownPanel(var1, var2, true);
      JComponent var4 = var3.getDisplayer();
      Box var5 = Box.createVerticalBox();
      var5.add(var4);
      var5.add(Box.createVerticalGlue());
      return var5;
   }

   private void addInputFields() {
      this.addInputPanel(this.detailsPanel);
      this.addInputPanel(this.securityPanel);
      this.addInputPanel(this.restrictionsPanel);
   }

   public WebServiceCall getDisplayedObject() {
      return (WebServiceCall)super.getDisplayedObject();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() {
      WebServiceCall var1 = this.getDisplayedObject();
      this.detailsPanel.applyValuesTo(var1);
      this.securityPanel.applyValuesTo(var1);
      var1.setCallRestrictions(this.restrictionsPanel.getCallRestrictions(var1));
   }
}
