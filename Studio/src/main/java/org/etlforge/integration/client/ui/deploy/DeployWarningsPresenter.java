package org.etlforge.integration.client.ui.deploy;

import com.google.common.base.Function;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.deploy.serverapi.DeployResult;
import org.etlforge.integration.client.deploy.serverapi.EntityWarning;
import org.etlforge.integration.data.entity.EntityTypeAndNameSorter;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.IntegrationEntityLookup;
import org.etlforge.integration.uidelegate.entity.list.IntegrationEntityListDecorator;
import org.etlforge.ui.Size;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.Selection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class DeployWarningsPresenter implements UiProvider {
   private final IntegrationEntityLookup objectLookup;
   private final ImmutableSet<String> globalWarnings;
   private final Multimap<IntegrationEntityId, EntityWarning> entityWarnings = ArrayListMultimap.create();
   private JTextArea globalWarningsArea;
   private KongaList entityList;
   private JTextArea messageArea;
   private OneColumnPanel contentPanel;

   public DeployWarningsPresenter(DeployResult var1, IntegrationEntityLookup var2) {
      this.objectLookup = var2;
      this.globalWarnings = var1.getGlobalWarnings();

      for (EntityWarning var4 : var1.getEntityWarnings()) {
         this.entityWarnings.put(var4.getIntegrationEntityId(), var4);
      }

      this.createComponents();
      this.setInitialState();
      this.layoutComponents();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.contentPanel.decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (!this.globalWarnings.isEmpty()) {
         this.globalWarningsArea.requestFocusInWindow();
      } else if (!this.entityWarnings.isEmpty()) {
         this.entityList.requestFocus();
      }
   }

   public void setSelectedEntity(IntegrationEntityId var1) {
      DeployWarningsPresenter.ListItem var2 = this.getEntityListItem(var1);
      Selection var3 = var2 != null ? SelectionFactory.newOrderedSelection(var2) : Selection.EMPTY;
      this.entityList.setSelection(var3);
   }

   private void createComponents() {
      this.createGlobalWarningsArea();
      this.createItemList();
      this.createMessageArea();
   }

   private void createGlobalWarningsArea() {
      this.globalWarningsArea = KongaTextArea.viewer(Size.rows(8), Size.columns(40));
      this.globalWarningsArea.setText(this.getGlobalWarningsText());
   }

   private String getGlobalWarningsText() {
      if (this.globalWarnings.isEmpty()) {
         return Strings.get("Deploy.Warnings.Global.NoGlobalWarnings");
      }

      StringBuilder var1 = new StringBuilder(Strings.get("Deploy.Warnings.Global.WarningsHeader"));
      var1.append("\n\n");

      for (String var3 : this.globalWarnings) {
         var1.append(var3);
         var1.append("\n");
      }

      return var1.toString();
   }

   private void createItemList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setCellRenderer(new DefaultListDecorRenderer());
      var1.setVisibleRowCount(8);
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      this.entityList = var1.newList(this.getAffectedItems());
      this.entityList.addSelectionListener(new DeployWarningsPresenter.ListSelectionHandler());
   }

   private List<DeployWarningsPresenter.ListItem> getAffectedItems() {
      ArrayList var1 = Lists.newArrayList();
      ArrayList var2 = Lists.newArrayList();
      if (!this.entityWarnings.isEmpty()) {
         for (IntegrationEntityId var5 : Sets.newHashSet(this.entityWarnings.keySet())) {
            IntegrationEntity var6 = this.objectLookup.getEntity(var5);
            if (var6 != null) {
               var1.add(var6);
            } else {
               var2.add(var5);
            }
         }

         Collections.sort(var1, new EntityTypeAndNameSorter());
      }

      DeployWarningsPresenter.ListItemFactory var7 = new DeployWarningsPresenter.ListItemFactory();
      List var8 = KongaListUtils.transform(var1, var7);
      var8.addAll(KongaListUtils.transform(var2, var7));
      return var8;
   }

   private DeployWarningsPresenter.ListItem getEntityListItem(IntegrationEntityId var1) {
      if (var1 == null) {
         return null;
      }

      ListModel var2 = this.entityList.getModel();
      int var3 = 0;

      for (int var4 = var2.getSize(); var3 < var4; var3++) {
         Object var5 = var2.getElementAt(var3);
         if (var5 instanceof DeployWarningsPresenter.ListItem) {
            DeployWarningsPresenter.ListItem var6 = (DeployWarningsPresenter.ListItem)var5;
            if (var6.getEntityId().equals(var1)) {
               return var6;
            }
         }
      }

      return null;
   }

   private void createMessageArea() {
      this.messageArea = KongaTextArea.viewer(Size.rows(6), Size.columns(50));
   }

   private void setInitialState() {
      this.entityList.selectFirst();
   }

   private void layoutComponents() {
      this.contentPanel = new OneColumnPanel();
      this.contentPanel.setFill(OneColumnPanel.NONE);
      this.contentPanel.setWeightX(1.0);
      Insets var1 = new Insets(2, 2, 2, 2);
      Insets var2 = new Insets(1, 2, 7, 2);
      if (this.globalWarningsArea != null) {
         this.layoutGlobalWarnings(var1, var2);
      }

      if (this.entityList != null) {
         this.layoutEntityList(var1, var2);
      }
   }

   private void layoutGlobalWarnings(Insets var1, Insets var2) {
      JLabel var3 = new JLabel(Strings.get("Deploy.Warnings.Global.Caption"));
      var3.setIcon(CommonIcons.WARNING_16);
      this.contentPanel.withInsets(var1);
      this.contentPanel.add(var3);
      this.contentPanel.withInsets(var2);
      this.contentPanel.fillOut(new JScrollPane(this.globalWarningsArea));
   }

   private void layoutEntityList(Insets var1, Insets var2) {
      this.contentPanel.setWeightY(0.0);
      this.contentPanel.withInsets(var1);
      JLabel var3 = new JLabel(Strings.get("Deploy.Warnings.Item.Caption"));
      var3.setIcon(CommonIcons.WARNING_16);
      this.contentPanel.add(var3);
      this.contentPanel.withInsets(var2);
      this.contentPanel.fillOut(this.entityList);
      this.contentPanel.setWeightY(0.0);
      this.contentPanel.withInsets(var1);
      JLabel var4 = new JLabel(Strings.get("Deploy.Warnings.Item.DetailsCaption"));
      this.contentPanel.add(var4);
      this.contentPanel.setWeightY(1.0);
      this.contentPanel.fillOut(new JScrollPane(this.messageArea));
   }

   private static final class IntegrationEntityListItem extends IntegrationEntityListDecorator implements DeployWarningsPresenter.ListItem {
      public IntegrationEntityListItem(IntegrationEntity var1) {
         super(var1);
      }

      @Override
      public IntegrationEntityId getEntityId() {
         return this.getEntity().getID();
      }
   }

   private interface ListItem {
      IntegrationEntityId getEntityId();
   }

   private static final class ListItemFactory implements Function<Object, DeployWarningsPresenter.ListItem> {
      private ListItemFactory() {
      }

      public DeployWarningsPresenter.ListItem apply(Object var1) {
         if (var1 instanceof IntegrationEntity) {
            return new DeployWarningsPresenter.IntegrationEntityListItem((IntegrationEntity)var1);
         } else if (var1 instanceof IntegrationEntityId) {
            return new DeployWarningsPresenter.UnknownEntityListItem((IntegrationEntityId)var1);
         } else {
            throw new RuntimeException("Unexpected object: " + var1);
         }
      }
   }

   private final class ListSelectionHandler implements SelectionListener {
      private ListSelectionHandler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         Object var2 = ((OrderedSelection)var1.getSelection()).head();
         DeployWarningsPresenter.this.messageArea.setText(this.getMessage(var2));
         DeployWarningsPresenter.this.messageArea.setCaretPosition(0);
      }

      private String getMessage(Object var1) {
         String var2 = "";
         if (var1 instanceof DeployWarningsPresenter.ListItem) {
            IntegrationEntityId var3 = ((DeployWarningsPresenter.ListItem)var1).getEntityId();
            var2 = this.getWarningMessage(var3);
            if (var2.length() > 0) {
               var2 = var2 + "\n\n";
            }
         }

         return var2;
      }

      private String getWarningMessage(IntegrationEntityId var1) {
         Collection var2 = DeployWarningsPresenter.this.entityWarnings.get(var1);
         if (var2.isEmpty()) {
            return "";
         }

         StringBuilder var3 = new StringBuilder();

         for (Object __etlItem276 : var2) {
            EntityWarning var5 = (EntityWarning) __etlItem276;
            if (var3.length() > 0) {
               var3.append("\n");
            }

            var3.append(var5.getWarning());
         }

         return var3.toString();
      }
   }

   private static final class UnknownEntityListItem implements ListDecorator, DeployWarningsPresenter.ListItem {
      private final IntegrationEntityId entityId;
      private final String text;

      public UnknownEntityListItem(IntegrationEntityId var1) {
         this.entityId = var1;
         this.text = "[ID = " + var1 + "]";
      }

      @Override
      public void decorate(ListDecor var1, boolean var2) {
         var1.setText(this.text);
         var1.setToolTip(Strings.get("Deploy.Warnings.Item.Unknown"));
         var1.setIcon(CommonIcons.ERROR_16);
      }

      @Override
      public IntegrationEntityId getEntityId() {
         return this.entityId;
      }
   }
}
