package com.etlforge.integration.client.ui.sfdc.page;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.sfdc.RetryPolicyEditor;
import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceConnector;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.widget.DropDownPanel;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public abstract class AbstractSalesforceItemPanel<T extends IntegrationEntity & SalesforceWizardEntity> extends IntegrationEntityPanel {
   private ImplementingObjectsLinkPanel links;
   private final TreeWidget<SalesforceConnector> connectorSelector;
   private final TreeWidget<Schedule> scheduleSelector;
   private final UTF8Option utf8Choice;
   private Action runAction;
   private Action testAction;
   private Action deleteAction;
   private final List<Action> additionalActions = Lists.newArrayList();

   public AbstractSalesforceItemPanel(ApplicationWindow var1, T var2, EntityExplorerSupport var3) {
      super(var1, var2);
      this.connectorSelector = this.createConnectorSelector((T)var2, var3);
      this.scheduleSelector = this.createScheduleSelector((T)var2, var3);
      this.utf8Choice = UTF8Option.forWizardItem((T)var2).asChangeSourceIn(this);
   }

   private TreeWidget<SalesforceConnector> createConnectorSelector(T var1, EntityExplorerSupport var2) {
      TreeWidget var3 = new TreeWidget<>(var2, EntityType.SalesforceConnector, SalesforceConnector.class, null);
      var3.setSelectorLabel("Salesforce &Login:");
      WizardItemBrowser var4 = new WizardItemBrowser((SalesforceWizardEntity)var1);
      SalesforceConnector var5 = var4.getSalesforceConnector();
      var3.setSelectedEntity(var5);
      var3.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            AbstractSalesforceItemPanel.this.handleConnectorSelection(AbstractSalesforceItemPanel.this.getSelectedConnector());
         }
      });
      InputField var6 = var3.asInputField("Salesforce Org");
      this.addInputField(var6);
      return var3;
   }

   private TreeWidget<Schedule> createScheduleSelector(T var1, EntityExplorerSupport var2) {
      TreeWidget var3 = new TreeWidget<>(var2, EntityType.Schedule, Schedule.class, null);
      var3.setSelectorLabel("Run on &Schedule:");
      var3.setIncludeNoneItem(true);
      WizardItemBrowser var4 = new WizardItemBrowser((SalesforceWizardEntity)var1);
      Schedule var5 = var4.getSchedule();
      var3.setSelectedEntity(var5);
      InputField var6 = var3.asInputField("Schedule");
      this.addInputField(var6);
      return var3;
   }

   @Override
   public final T getDisplayedObject() {
      return (T)super.getDisplayedObject();
   }

   protected void handleConnectorSelection(SalesforceConnector var1) {
   }

   public final void setLinks(ImplementingObjectsLinkPanel var1) {
      this.links = var1;
      if (var1 != null) {
         var1.registerAsChangeSource(this);
      }
   }

   protected final UTF8Option getUTF8Choice() {
      return this.utf8Choice;
   }

   public final void addAdditionalAction(Action var1) {
      Preconditions.checkNotNull(var1, "action");
      this.additionalActions.add(var1);
   }

   public final void setRunAction(Action var1) {
      this.runAction = var1;
   }

   public final void setTestAction(Action var1) {
      this.testAction = var1;
   }

   public final void setDeleteAction(Action var1) {
      this.deleteAction = var1;
   }

   public abstract void setSoapHeaderEditor(SoapHeaderEditor var1);

   public abstract void setRetryPolicyEditor(RetryPolicyEditor var1);

   @Override
   protected final void validate() throws IntegrationDataPanelException {
      if (this.links != null) {
         this.links.validate();
      }
   }

   protected final String getFileEncoding() {
      return this.utf8Choice.isSelected() ? Charsets.UTF_8.name() : "";
   }

   @Override
   public final void dispose() {
      this.scheduleSelector.dispose();
      this.connectorSelector.dispose();
      this.disposeLinks();
      this.disposeAdditionalResources();
      super.dispose();
   }

   private void disposeLinks() {
      if (this.links != null) {
         this.links.dispose();
      }
   }

   protected void disposeAdditionalResources() {
   }

   public final SalesforceConnector getSelectedConnector() {
      return this.connectorSelector.getSelectedEntity();
   }

   public final Schedule getSelectedSchedule() {
      return this.scheduleSelector.getSelectedEntity();
   }

   @Override
   public final void display() {
      super.display();
      this.doLayout();
   }

   private void doLayout() {
      AbstractSalesforceItemPanel.Layout var1 = new AbstractSalesforceItemPanel.Layout();
      this.layoutComponents(var1);
   }

   protected void layoutComponents(AbstractSalesforceItemPanel<T>.Layout var1) {
   }

   public final Supplier<SalesforceConnector> getConnectorSupplier() {
      return UiThreadSupplier.wrap(new Supplier<SalesforceConnector>() {
         public SalesforceConnector get() {
            return AbstractSalesforceItemPanel.this.getSelectedConnector();
         }
      });
   }

   public final class Layout {
      private final NColumnPanel grid = this.emptyThreeColumnGrid();

      public Layout() {
         this.setDefaultInsets();
         this.grid
            .addRow(AbstractSalesforceItemPanel.this.getNameField().getLabel(), AbstractSalesforceItemPanel.this.getNameField().getInputComponent(), null);
         this.layoutTreeWidget(AbstractSalesforceItemPanel.this.connectorSelector, EntityType.SalesforceConnector);
         AbstractSalesforceItemPanel.this.wrap(this.grid);
      }

      private void layoutTreeWidget(TreeWidget<?> var1, EntityType var2) {
         JComponent[] var3 = var1.getLayoutComponents(true);
         Icon var4 = EntityIcons.forType(var2).getDefaultIcon(EntityIcons.Size.SMALL);
         ((JLabel)var3[0]).setIcon(var4);
         this.grid.addRow(var3[0], var3[1], var3[2]);
      }

      public void layoutScheduleSelector() {
         this.layoutTreeWidget(AbstractSalesforceItemPanel.this.scheduleSelector, EntityType.Schedule);
      }

      public void layoutActions() {
         if (!this.thereAreNoActions()) {
            this.grid.addRow(null, new JSeparator(), null);
            Grid var1 = this.layoutLeftActions();
            if (AbstractSalesforceItemPanel.this.deleteAction == null) {
               this.grid.addRow(null, var1.invisible(), null);
            } else {
               BoxBuilder var2 = Horizontal.box()
                  .nonOpaque()
                  .add(var1.invisible())
                  .rigid(10)
                  .glue()
                  .add(this.actionButton(AbstractSalesforceItemPanel.this.deleteAction));
               this.grid.addRow(null, var2, null);
            }
         }
      }

      private Grid layoutLeftActions() {
         Grid var1 = Grid.oneRow(10).nonOpaque();
         ArrayList var2 = Lists.newArrayList(AbstractSalesforceItemPanel.this.runAction, AbstractSalesforceItemPanel.this.testAction);
         var2.addAll(AbstractSalesforceItemPanel.this.additionalActions);

         for (Object __etlItem236 : var2) {
            Action var4 = (Action) __etlItem236;
            if (var4 != null) {
               var1.add(this.actionButton(var4));
            }
         }

         return var1;
      }

      private KongaButton actionButton(Action var1) {
         return new KongaButton(var1).withMediumActionIcon();
      }

      private boolean thereAreNoActions() {
         return AbstractSalesforceItemPanel.this.runAction == null
            && AbstractSalesforceItemPanel.this.testAction == null
            && AbstractSalesforceItemPanel.this.additionalActions.isEmpty();
      }

      public void layoutLinks() {
         AbstractSalesforceItemPanel.this.links.layoutIn(this.grid);
      }

      public void layoutOptionsPanel(OptionsPanel var1) {
         OptionsExpansionPanel var2 = new OptionsExpansionPanel(var1);
         this.grid.addRow(null, new JSeparator(), null);
         this.grid.setAnchors(GridBagLayoutPanel.Anchor.NORTHEAST, GridBagLayoutPanel.Anchor.NORTHWEST);
         this.grid.setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.NONE);
         this.grid.setYWeights(0.0, 1.0, 0.0);
         this.grid.setInsets(0, 0, 0, 0, 5);
         this.grid.setInsets(1, 0, 0, 0, 0);
         var2.layoutIn(this.asTwoColumnLayout());
      }

      public void addRow(UiProvider var1) {
         this.grid.add(var1, 3);
      }

      public void addRow(Object... var1) {
         this.grid.addRow(var1);
      }

      public void addOptionsPanel(UiProvider var1) {
         JPanel var2 = InvisiblePanel.of(var1);
         var2.setBorder(Empty.border(0, 24, 0, 0));
         DropDownPanel var3 = new DropDownPanel(var2, "&Options", false);
         var3.setOpaque(false);
         var3.setFocusWhenExpanded(var1);
         this.grid.add(" ").add(var3.getDisplayer(), 2);
      }

      private NColumnPanel emptyThreeColumnGrid() {
         return new NColumnPanel(3)
            .setXWeights(0.0, 1.0, 0.0)
            .setFills(GridBagLayoutPanel.Fill.NONE, GridBagLayoutPanel.Fill.HORIZONTAL, GridBagLayoutPanel.Fill.NONE)
            .setOpaque(false, false)
            .withBorder(Empty.border(12, 10, 0, 5))
            .asFocusCycleRoot();
      }

      private void setDefaultInsets() {
         byte var1 = 10;
         this.grid.setInsets(0, new Insets(0, 0, var1, 5)).setInsets(1, new Insets(0, 0, var1, 5)).setInsets(2, new Insets(0, 0, var1, 0));
      }

      public TwoColumnLayout asTwoColumnLayout() {
         return new TwoColumnLayout() {
            @Override
            public TwoColumnLayout addRow(Component var1, Component var2) {
               Layout.this.grid.addRow(var1, var2, null);
               return this;
            }

            @Override
            public TwoColumnLayout addRow(Component var1) {
               Layout.this.grid.add(var1, 3);
               return this;
            }
         };
      }
   }
}
