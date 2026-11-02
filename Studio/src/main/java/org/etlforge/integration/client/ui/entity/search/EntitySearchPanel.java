package org.etlforge.integration.client.ui.entity.search;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.persist.EntityPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.renderer.IntegrationEntityListDecorRenderer;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.name.NameSorter;

public final class EntitySearchPanel implements UiProvider {
   private final AdvancedEntitySearchModel model;
   private final Supplier<List<IntegrationEntity>> allItems;
   private final SearchOptionsUi optionsUi;
   private final SearchFieldsPanel searchFieldsPanel;
   private final KongaList resultList;
   private final Action searchAction;

   public EntitySearchPanel(IntegrationProject var1, EntityType var2, SearchFieldsPanel var3) {
      this(toSupplier(var1, var2), var3);
   }

   private static Supplier<List<IntegrationEntity>> toSupplier(final IntegrationProject var0, final EntityType var1) {
      return new Supplier<List<IntegrationEntity>>() {
         public List<IntegrationEntity> get() {
            RootFolder var1x = var0.getRootFolder(var1);
            return var1x.getAllItemsRecursively();
         }
      };
   }

   public EntitySearchPanel(List<? extends IntegrationEntity> var1, SearchFieldsPanel var2) {
      this(toSupplier(var1), var2);
   }

   private static Supplier<List<IntegrationEntity>> toSupplier(List<? extends IntegrationEntity> var0) {
      ArrayList var1 = Lists.newArrayList(var0);
      return Suppliers.ofInstance(var1);
   }

   private EntitySearchPanel(Supplier<List<IntegrationEntity>> var1, SearchFieldsPanel var2) {
      this.allItems = var1;
      this.searchFieldsPanel = var2;
      this.model = new AdvancedEntitySearchModel();
      this.model.setMatchingItems((List<IntegrationEntity>)var1.get());
      this.optionsUi = new SearchOptionsUi(this.model);
      this.resultList = this.createResultList();
      this.searchAction = new EntitySearchPanel.SearchAction();
      this.optionsUi.addSearchAction(this.searchAction);
      var2.setSearchAction(this.searchAction);
      this.bindProperties();
   }

   private KongaList createResultList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setCellRenderer(new IntegrationEntityListDecorRenderer());
      var1.setVisibleRowCount(15);
      KongaList var2 = var1.newList();
      var2.setDefaultAction(new EntitySearchPanel.OpenMatchingItemsAction());
      return var2;
   }

   private void bindProperties() {
      ListBinding var1 = new ListBinding<>(this.model.getMatchingItemsProperty(), this.resultList);
      var1.setListOrder(NameSorter.ignoreCase());
      var1.syncUi();
   }

   public void enableReplace(EntityPersistor var1) {
      Preconditions.checkNotNull(var1, "persistor");
      ReplaceServiceImpl var2 = new ReplaceServiceImpl(this.model, var1, this.searchAction);
      this.searchFieldsPanel.setReplaceService(var2);
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.layoutSearchFields();
      UiProvider var3 = this.layoutResultList();
      Horizontal.grid().of(var2, var3).withHGap(10).decorate(var1);
   }

   private UiProvider layoutSearchFields() {
      KongaScrollPane var1 = new KongaScrollPane(this.searchFieldsPanel);
      var1.makeInconspicuous();
      return new BorderLayoutBuilder(0, 10).center(var1).south(this.layoutOptions()).withBorder(StyleSheet.getDefaultTitleBorder("Search Parameters"));
   }

   private JComponent layoutOptions() {
      JPanel var1 = InvisiblePanel.of(this.optionsUi);
      var1.setBorder(StyleSheet.getDefaultTitleBorder("Options"));
      return var1;
   }

   private UiProvider layoutResultList() {
      return new BorderLayoutBuilder()
         .center(this.resultList)
         .withBorder(StyleSheet.getDefaultTitleBorder("Matching Items"))
         .withPreferredSize(new Dimension(150, 100));
   }

   @Override
   public void requestFocus() {
      this.searchFieldsPanel.requestFocus();
   }

   private class OpenMatchingItemsAction extends AbstractSingleTypeSelectionAction<IntegrationEntity> {
      public OpenMatchingItemsAction() {
         super(IntegrationEntity.class);
      }

      @Override
      protected void run(List<IntegrationEntity> var1) {
         for (IntegrationEntity var3 : var1) {
            UiCompanion.of(var3).openPage();
         }
      }
   }

   private final class SearchAction extends AbstractAction {
      public SearchAction() {
         super("Search");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               SearchOptions var1x = EntitySearchPanel.this.optionsUi.getOptions();
               Predicate var2 = EntitySearchPanel.this.searchFieldsPanel.getSearcher(var1x);
               EntitySearchPanel.SearchJob var3 = EntitySearchPanel.this.new SearchJob(var2);
               var3.execute();
            }
         });
      }
   }

   private final class SearchJob extends SwingWorker<List<IntegrationEntity>, Void> {
      private final Predicate<IntegrationEntity> filter;

      public SearchJob(Predicate<IntegrationEntity> var2) {
         this.filter = var2;
      }

      protected List<IntegrationEntity> doInBackground() throws Exception {
         Collection var1 = Collections2.filter(EntitySearchPanel.this.allItems.get(), this.filter);
         ArrayList var2 = Lists.newArrayList(var1);
         Collections.sort(var2, NameSorter.ignoreCase());
         return var2;
      }

      @Override
      protected void done() {
         try {
            List var1 = this.get();
            EntitySearchPanel.this.model.setMatchingItems(var1);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         } catch (ExecutionException var3) {
            var3.printStackTrace();
         }
      }
   }
}
