package org.etlforge.integration.client.ui.interchange.component.tree;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.SupportedLookAndFeel;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.window.AbstractWindowSectionContent;
import org.etlforge.application.ui.window.ActiveObjectService;
import org.etlforge.application.ui.window.WindowSection;
import org.etlforge.application.ui.window.WindowSectionContent;
import org.etlforge.integration.client.entity.search.ProjectItemOrder;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.search.ProjectItemTreeOrder;
import org.etlforge.integration.client.ui.entity.tree.TreeValidationService;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.ProcessFilterInstaller;
import org.etlforge.integration.client.ui.interchange.content.ExpansionMemento;
import org.etlforge.integration.client.ui.interchange.content.ProjectContentViewer;
import org.etlforge.integration.client.ui.interchange.content.SelectionMemento;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.EntityUtils;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.entity.RootFolder;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.event.IntegrationProjectEvent;
import org.etlforge.integration.data.project.event.ProjectListener;
import org.etlforge.integration.data.project.validation.ValidationModel;
import org.etlforge.integration.data.project.validation.ValidationModelEvent;
import org.etlforge.integration.data.project.validation.ValidationModelListener;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.panel.KongaPanel;

public final class MultiTreeContentViewer extends AbstractWindowSectionContent implements ProjectContentViewer {
   private IntegrationProject project;
   private final ProjectSection projectSection;
   private final LinkedHashMap<EntityType, MultiTreeSection> categoryTrees = Maps.newLinkedHashMap();
   private final ImmutableList<MultiTreeSection> sections;
   private final MultiTreeFilterService filter;
   private final ActiveObjectService activeObjectService;
   private final ProcessFilterInstaller processFilterInstaller;
   private TreeValidationService validationService;
   private ProcessFilterHeader processFilterHeader;
   private MultiTreeContentViewer.Layout layout;
   private final MultiTreeContentViewer.ItemCountTrigger itemCountTrigger = new MultiTreeContentViewer.ItemCountTrigger();

   public MultiTreeContentViewer(WindowSection var1, MultiTreeSectionFactory var2, ProcessFilterInstaller var3) {
      super("project.multi-tree", var1);
      this.processFilterInstaller = var3;
      this.activeObjectService = var1.getWindow().getActiveObjectService();
      this.sections = var2.createCategorySections(this);

      for (MultiTreeSection var5 : this.sections) {
         for (EntityType var7 : var5.getEntityTypes()) {
            this.categoryTrees.put(var7, var5);
         }
      }

      this.projectSection = var2.createProjectSection(this, this.sections);
      this.filter = new MultiTreeFilterService(this, this.projectSection, this.sections);
      this.configureFilterField();
      this.updateExpansionMode();
   }

   private void configureFilterField() {
      MultiTreeNameFilterField var1 = this.filter.getFilterField();
      var1.setKeyUpHandler(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            MultiTreeContentViewer.this.projectSection.setSelected(true);
         }
      });
      var1.setKeyDownHandler(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            for (MultiTreeSection var3 : MultiTreeContentViewer.this.filter.getVisibleSections()) {
               if (var3.hasSelection()) {
                  var3.requestFocus();
                  return;
               }
            }

            MultiTreeContentViewer.this.selectRoot();
         }
      });
   }

   void updateExpansionMode() {
      this.projectSection.setExpansionMode(this.getExpansionMode());
   }

   public IntegrationProject getProject() {
      return this.project;
   }

   @Override
   public String getTitle() {
      return "Design";
   }

   @Override
   public Icon getIcon() {
      return ClientIcons.INTEGRATION_24;
   }

   @Override
   public void decorate(JComponent var1) {
      this.layout = new MultiTreeContentViewer.Layout();
      this.layout.decorate(var1);
   }

   void rebuildLayout() {
      if (this.layout != null) {
         this.layout.rebuildSections();
      }
   }

   @Override
   public void newProjectOpened(IntegrationProject var1) {
      this.setProject(var1);
   }

   private void setProject(final IntegrationProject var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            if (MultiTreeContentViewer.this.project != null) {
               MultiTreeContentViewer.this.project.removeProjectListener(MultiTreeContentViewer.this.itemCountTrigger);
            }

            MultiTreeContentViewer.this.project = var1;
            MultiTreeContentViewer.this.filter.disposeFilterRefresher();
            MultiTreeContentViewer.this.projectSection.setProject(var1);

            for (MultiTreeSection var2 : MultiTreeContentViewer.this.sections) {
               var2.setProject(var1);
            }

            if (var1 == null) {
               MultiTreeContentViewer.this.clearItemCounts();
               MultiTreeContentViewer.this.activeObjectService.setActiveObjects(MultiTreeContentViewer.this.projectSection, Lists.newArrayList());
            } else {
               MultiTreeContentViewer.this.countAllItems(var1);
               var1.addProjectListener(MultiTreeContentViewer.this.itemCountTrigger);
            }

            MultiTreeContentViewer.this.filter.setProject(var1);
            MultiTreeContentViewer.this.installValidationModel(var1);
            MultiTreeContentViewer.this.projectSection.setExpansionMode(MultiTreeContentViewer.this.getExpansionMode());
         }
      });
   }

   private void clearItemCounts() {
      for (MultiTreeSection var2 : this.categoryTrees.values()) {
         var2.setItemCount(0);
      }
   }

   private void countAllItems(IntegrationProject var1) {
      assert var1 != null;
      MultiTreeContentViewer.ItemCounter var2 = new MultiTreeContentViewer.ItemCounter(var1);
      var2.execute();
   }

   private void installValidationModel(IntegrationProject var1) {
      ValidationModel var2 = null;
      if (var1 != null) {
         ManagedProject var3 = ProjectUtils.getManagedProject(var1);
         var2 = var3.getValidationModel();
         this.updateValidationIndicators(var2);
      }

      this.validationService.setModel(var2);
   }

   private void updateValidationIndicators(ValidationModel var1) {
      this.projectSection.updateValidationIndicator();

      for (MultiTreeSection var3 : this.sections) {
         var3.updateValidationIndicator(var1);
      }
   }

   @Override
   public void projectClosed() {
      this.setProject(null);
   }

   @Override
   public WindowSectionContent getWindowContent() {
      return this;
   }

   @Override
   public void selectRoot() {
      List var1 = this.filter.getVisibleSections();
      if (!var1.isEmpty()) {
         ((MultiTreeSection)var1.get(0)).setSelected(true);
      }
   }

   @Override
   public void selectEntity(final IntegrationEntity var1) {
      final MultiTreeSection var2 = this.getSectionFor(var1);
      if (var2 != null) {
         UiUtils.runOnEventThread(new Runnable() {
            @Override
            public void run() {
               var2.selectEntity(var1);
            }
         });
      }
   }

   private MultiTreeSection getSectionFor(IntegrationEntity var1) {
      EntityType var2 = EntityUtils.getItemType(var1);
      return this.categoryTrees.get(var2);
   }

   @Override
   public List<IntegrationEntity> getSelectedEntities() {
      ArrayList var1 = Lists.newArrayList();

      for (MultiTreeSection var3 : this.filter.getVisibleSections()) {
         var3.collectSelectedItems(var1);
      }

      return var1;
   }

   @Override
   public List<IntegrationEntity> getExpandedEntities() {
      ArrayList var1 = Lists.newArrayList();

      for (MultiTreeSection var3 : this.filter.getVisibleSections()) {
         var3.collectExpandedItems(var1);
      }

      return var1;
   }

   @Override
   public void setExpandedEntities(List<IntegrationEntity> var1) {
      for (MultiTreeSection var3 : this.sections) {
         var3.restoreExpansionState(var1);
      }
   }

   @Override
   public void renameEntity(IntegrationEntity var1) {
      if (var1 instanceof IntegrationProject) {
         this.projectSection.renameProject((IntegrationProject)var1);
      } else {
         MultiTreeSection var2 = this.getSectionFor(var1);
         if (var2 != null) {
            var2.rename(var1);
         }
      }
   }

   @Override
   public void refresh(Folder var1) {
      MultiTreeSection var2 = this.getSectionFor(var1);
      if (var2 != null) {
         var2.refresh(var1);
      }
   }

   @Override
   public void install() {
      this.validationService = this.installValidationService();
      this.activeObjectService.addActiveObjectProvider(this.projectSection);

      for (MultiTreeSection var2 : this.sections) {
         this.activeObjectService.addActiveObjectProvider(var2);
      }
   }

   private TreeValidationService installValidationService() {
      TreeValidationService var1 = new TreeValidationService();
      var1.addValidationListener(new ValidationModelListener() {
         @Override
         public void validationWasPerformed(ValidationModelEvent var1) {
            MultiTreeContentViewer.this.updateValidationIndicators(var1.getSource());
         }
      });
      this.projectSection.setValidationService(var1);

      for (MultiTreeSection var3 : this.sections) {
         var3.tree.setValidationService(var1);
      }

      return var1;
   }

   @Override
   public void uninstall() {
      this.activeObjectService.removeActiveObjectProvider(this.projectSection);
      this.validationService.dispose();

      for (MultiTreeSection var2 : this.sections) {
         this.activeObjectService.removeActiveObjectProvider(var2);
         var2.uninstall();
      }
   }

   @Override
   public void expand(RootFolder var1) {
      MultiTreeSection var2 = this.getSectionFor(var1);
      var2.expand();
   }

   @Override
   public void expandAll(RootFolder var1) {
      if (var1 == null) {
         this.expandAll();
      } else {
         MultiTreeSection var2 = this.getSectionFor(var1);
         this.expandSectionCompletely(var2);
      }
   }

   private void expandAll() {
      for (MultiTreeSection var2 : this.sections) {
         this.expandSectionCompletely(var2);
      }
   }

   private void expandSectionCompletely(MultiTreeSection var1) {
      var1.expand();
      var1.expandTree();
   }

   @Override
   public void collapseAll(RootFolder var1) {
      if (var1 == null) {
         this.collapseAll();
      } else {
         MultiTreeSection var2 = this.getSectionFor(var1);
         this.collapseSection(var2);
      }
   }

   private void collapseAll() {
      for (MultiTreeSection var2 : this.sections) {
         this.collapseSection(var2);
      }
   }

   private void collapseSection(MultiTreeSection var1) {
      var1.collapse();
      var1.collapseTree();
   }

   ExpansionMode getExpansionMode() {
      return this.filter.getExpansionMode();
   }

   @Override
   public <T extends IntegrationEntity> ProjectItemOrder<T> getItemOrder(EntityType var1, Class<T> var2) {
      MultiTreeSection var3 = this.categoryTrees.get(var1);
      if (var3 == null) {
         throw new IllegalArgumentException("Cannot create a ProjectItemOrder for the type " + var1);
      } else {
         return new ProjectItemTreeOrder<>(var3.tree, var1, var2);
      }
   }

   @Override
   public void giveFocusToFilterField() {
      this.filter.getFilterField().requestFocus();
   }

   void inactivateOtherSections(@Nullable MultiTreeSection var1) {
      for (MultiTreeSection var3 : this.sections) {
         if (var3 != var1) {
            var3.inactivate();
         }
      }

      this.projectSection.inactivate();
   }

   void inactivateAllSections() {
      for (MultiTreeSection var2 : this.sections) {
         var2.inactivate();
      }
   }

   void moveToNextSection(MultiTreeSection var1) {
      List var2 = this.filter.getVisibleSections();
      int var3 = var2.indexOf(var1);
      if (var3 >= 0 && var3 < var2.size() - 1) {
         MultiTreeSection var4 = (MultiTreeSection)var2.get(var3 + 1);
         var4.setSelected(true);
      }
   }

   void moveToPreviousSection(MultiTreeSection var1, boolean var2) {
      List var3 = this.filter.getVisibleSections();
      int var4 = var3.indexOf(var1);
      if (var4 == 0) {
         this.giveFocusToFilterField();
      } else if (var4 > 0) {
         MultiTreeSection var5 = (MultiTreeSection)var3.get(var4 - 1);
         if (var2) {
            var5.moveIntoFromBelow();
         } else {
            var5.setSelected(true);
         }
      }
   }

   @Override
   public void setProcessFilter(ProcessFilter var1) {
      this.filter.setProcessFilter(var1);
      SelectionMemento var2 = new SelectionMemento(this);
      ExpansionMemento var3 = new ExpansionMemento(this);
      if (var1 == null) {
         this.processFilterHeader.dispose();
         this.processFilterHeader = null;
      } else {
         this.processFilterHeader = new ProcessFilterHeader(var1, new ProcessFilterRemover(this.processFilterInstaller));
      }

      if (this.layout != null) {
         this.layout.rebuildAll();
      }

      var3.restore();
      if (var1 == null) {
         var2.restore();
      }
   }

   private class ItemCountTrigger extends ProjectListener.Adapter {
      private ItemCountTrigger() {
      }

      @Override
      public void entitiesAdded(IntegrationProjectEvent var1) {
         this.doCount(var1);
      }

      @Override
      public void entitiesRevived(IntegrationProjectEvent var1) {
         this.doCount(var1);
      }

      @Override
      public void entitiesDeleted(IntegrationProjectEvent var1) {
         this.doCount(var1);
      }

      private void doCount(IntegrationProjectEvent var1) {
         MultiTreeContentViewer.ItemCounter var2 = MultiTreeContentViewer.this.new ItemCounter(var1);
         var2.execute();
      }
   }

   private class ItemCounter extends KongaSwingWorker<Multiset<MultiTreeSection>, Void> {
      private final IntegrationProject project;
      private final IntegrationProjectEvent evt;

      public ItemCounter(IntegrationProject var2) {
         this.project = var2;
         this.evt = null;
      }

      public ItemCounter(IntegrationProjectEvent var2) {
         this.project = var2.getSource();
         this.evt = var2;
      }

      protected Multiset<MultiTreeSection> doInBackground() throws Exception {
         HashMultiset var1 = HashMultiset.create();
         Predicate var2 = this.getTypeFilter();

         for (Object __etlItem510 : Iterables.filter(MultiTreeContentViewer.this.categoryTrees.keySet(), var2)) {
            EntityType var4 = (EntityType) __etlItem510;
            int var5 = this.project.getAllEntitiesOfType(var4).size();
            MultiTreeSection var6 = MultiTreeContentViewer.this.categoryTrees.get(var4);
            var1.add(var6, var5);
         }

         return var1;
      }

      private Predicate<EntityType> getTypeFilter() {
         if (this.evt == null) {
            return Predicates.alwaysTrue();
         }

         HashSet var1 = new HashSet();

         for (IntegrationEntity var3 : this.evt.getEntities()) {
            var1.add(var3.getEntityType());
         }

         return Predicates.in(var1);
      }

      protected void onSuccess(Multiset<MultiTreeSection> var1) {
         if (this.project == MultiTreeContentViewer.this.project) {
            for (Entry var3 : var1.entrySet()) {
               MultiTreeSection var4 = (MultiTreeSection)var3.getElement();
               int var5 = var3.getCount();
               var4.setItemCount(var5);
            }
         }
      }
   }

   private class Layout {
      private final BorderLayoutBuilder layout = new BorderLayoutBuilder();
      private final BorderLayoutBuilder sectionsLayout;
      private final Border borderWhenEmpty = this.createBorderWhenEmpty();

      public Layout() {
         this.layoutTop();
         this.sectionsLayout = this.createEmptySectionsLayout();
         JComponent var2 = this.layoutTrees();
         this.sectionsLayout.north(var2).center(new JLabel(" "));
         KongaScrollPane var3 = new KongaScrollPane(this.sectionsLayout);
         this.layout.center(var3);
      }

      private Border createBorderWhenEmpty() {
         TitledBorder var1 = new TitledBorder(Empty.border(10, 0, 0, 0), "No items match the filter");
         var1.setTitleFont(TextStyles.DefaultItalic.getFont());
         var1.setTitleColor(Color.GRAY);
         var1.setTitlePosition(1);
         var1.setTitleJustification(1);
         return var1;
      }

      private void layoutTop() {
         OneColumnPanel var1 = new OneColumnPanel();
         var1.setWeightX(1.0);
         var1.add(MultiTreeContentViewer.this.projectSection);
         if (MultiTreeContentViewer.this.processFilterHeader != null) {
            var1.add(MultiTreeContentViewer.this.processFilterHeader);
         }

         byte var2 = 0;
         if (SupportedLookAndFeel.SYSTEM.isInstalled() && SystemUtils.IS_OS_WINDOWS) {
            var2 = 2;
         }

         var1.withInsets(1, 5, var2, 5);
         var1.add(MultiTreeContentViewer.this.filter.getFilterField());
         this.layout.north(var1);
      }

      private BorderLayoutBuilder createEmptySectionsLayout() {
         KongaPanel var1 = new KongaPanel();
         var1.setPainter(StyleSheet.LEFT_TO_RIGHT_GRADIENT);
         BorderLayoutBuilder var2 = BorderLayoutBuilder.from(var1);
         var2.withBorder(Empty.border(5, 10, 0, 10));
         return var2;
      }

      private JComponent layoutTrees() {
         OneColumnPanel var1 = new OneColumnPanel().withInsets(0, 0, 10, 0);
         var1.setWeightX(1.0);

         for (MultiTreeSection var3 : MultiTreeContentViewer.this.filter.getVisibleSections()) {
            var1.add(var3);
         }

         if (var1.isEmpty()) {
            var1.withBorder(this.borderWhenEmpty);
         }

         return var1.deeplyNonOpaque().container();
      }

      public void decorate(JComponent var1) {
         BorderLayoutBuilder.from(var1).center(this.layout);
      }

      public void rebuildSections() {
         this.sectionsLayout.removeAll();
         JComponent var1 = this.layoutTrees();
         this.sectionsLayout.north(var1).center(new JLabel(" "));
         UiUtils.revalidate(this.layout.container());
      }

      public void rebuildAll() {
         this.layout.removeNorth();
         this.layoutTop();
         this.rebuildSections();
      }
   }
}
