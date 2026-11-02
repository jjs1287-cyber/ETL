package org.etlforge.integration.client.ui.structure.editor.ldap;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import org.etlforge.application.ui.Alert;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.search.string.StringSearchModelDefiner;
import org.etlforge.application.ui.search.string.StringSearchRule;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ldap.ui.ObjectClassDefinitionRetriever;
import org.etlforge.integration.client.mapping.serverapi.ldap.GetObjectClassesCallback;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.decorator.DefaultListDecorRenderer;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.list.ListTransferPolicy;
import org.etlforge.ui.widget.list.SourceAndTargetList;
import org.etlforge.ui.widget.list.SourceAndTargetListListener;
import org.etlforge.util.name.NameSorter;

final class ObjectClassSelectionUi implements UiProvider {
   private final ObjectClassSelectionPage page;
   private final ObjectClassSelectionModel model;
   private final StringSearchModelDefiner nameSearchUi;
   private final SourceAndTargetList classLists;
   private final Action refreshAction;
   private LdapStructureServerCache ldapCache;

   public ObjectClassSelectionUi(ObjectClassSelectionPage var1, ObjectClassSelectionModel var2) {
      this.page = var1;
      this.model = var2;
      var2.addPropertyChangeListener(new ObjectClassSelectionUi.ModelListener());
      this.nameSearchUi = this.createNameSearchUi();
      this.classLists = this.createClassLists();
      this.refreshAction = new ObjectClassSelectionUi.RefreshAction();
      AvailableClassListFilter.install(this.classLists, this.nameSearchUi.getSearchModel());
   }

   public void setLdapCache(LdapStructureServerCache var1) {
      this.ldapCache = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      ObjectClassSelectionUi.Layout var2 = new ObjectClassSelectionUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.nameSearchUi.requestFocus();
      this.classLists.selectFirst();
   }

   private StringSearchModelDefiner createNameSearchUi() {
      StringSearchModelDefiner var1 = new StringSearchModelDefiner(
         StringSearchRule.CONTAINS_IGNORE_CASE, StringSearchRule.STARTS_WITH_IGNORE_CASE, StringSearchRule.ENDS_WITH_IGNORE_CASE
      );
      var1.setSelectedRule(StringSearchRule.CONTAINS_IGNORE_CASE);
      var1.setEmptyStringMatchesEverything(true);
      var1.setCaption(PackageResources.Wizard.ClassSelector.SEARCH);
      var1.setToolTip(PackageResources.Wizard.ClassSelector.SEARCH_TOOLTIP);
      return var1;
   }

   private SourceAndTargetList createClassLists() {
      SourceAndTargetList var1 = new SourceAndTargetList(ComponentFactories.newKongaListFactory(), ComponentFactories.newKongaListFactory());
      var1.setTransferPolicy(new ObjectClassSelectionUi.ClassListTransferPolicy());
      var1.setTitles(PackageResources.Wizard.ClassSelector.AVAILABLE, PackageResources.Wizard.ClassSelector.SELECTED);
      var1.setTitleDecorator(new LabelDecorator() {
         @Override
         public void decorate(Label var1) {
            var1.setBackgroundColor(StyleSheet.ComponentTableHeader.getBackground());
            var1.setTextColor(TextStyles.ComponentTableHeaderText.getColor());
            var1.setFont(TextStyles.ComponentTableHeaderText.getFont());
            var1.setBorder(StyleSheet.ComponentTableHeader.getBorder());
         }
      });
      var1.setPrototypeCellValue("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
      var1.setVisibleRowCount(15);
      var1.addListener(new ObjectClassSelectionUi.ListListener());
      var1.setCellRenderer(new DefaultListDecorRenderer());
      return var1;
   }

   private void updateAvailableClasses(ObjectClassDefinition[] var1) {
      Arrays.sort(var1, NameSorter.ignoreCase());
      ClassListItem[] var2 = new ClassListItem[var1.length];
      int var3 = 0;

      for (int var4 = var1.length; var3 < var4; var3++) {
         var2[var3] = new ClassListItem(var1[var3]);
      }

      this.classLists.setSourceItems(var2, false);
      this.classLists.selectFirst();
      if (var1.length == 0) {
         ActionUtils.run(this.refreshAction, this, "");
      }
   }

   private void updateSelectedClasses(List<ObjectClassDefinition> var1) {
      final ArrayList var2 = Lists.newArrayList();

      for (ObjectClassDefinition var4 : var1) {
         var2.add(new ClassListItem(var4));
      }

      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ObjectClassSelectionUi.this.classLists.setTargetItems(var2.toArray(new ClassListItem[var2.size()]));
         }
      });
   }

   private static final class ClassListTransferPolicy implements ListTransferPolicy {
      private ClassListTransferPolicy() {
      }

      @Override
      public Object[] filterTargetTransfer(Object[] var1) {
         ArrayList var2 = Lists.newArrayList();

         for (Object var6 : var1) {
            ClassListItem var7 = (ClassListItem)var6;
            if (!var7.isAuxiliary()) {
               var2.add(var6);
            }
         }

         if (var2.size() < var1.length) {
            Alert.info(PackageResources.Wizard.ClassSelector.AUXILIARY_MESSAGE, PackageResources.Wizard.ClassSelector.AUXILIARY_TITLE);
         }

         return var2.toArray();
      }

      @Override
      public boolean isSelectable(Object var1) {
         return true;
      }
   }

   private final class Layout {
      private final OneColumnPanel col = new OneColumnPanel().withInsets(5, 5, 5, 5);

      public Layout() {
         this.addRefreshButton();
         this.addNameSearchUi();
         this.addClassLists();
         this.installEnterShortcut();
      }

      public void decorate(UiCanvas var1) {
         JPanel var2 = BorderLayoutBuilder.fillWith(this.col.deeplyNonOpaque());
         ObjectClassSelectionUi.this.classLists.installSourceListKeyTraversalBindings(var2);
         var1.display(var2);
      }

      private void addRefreshButton() {
         this.col.noFill(new KongaButton(ObjectClassSelectionUi.this.refreshAction));
      }

      private void addNameSearchUi() {
         this.col.noFill(ObjectClassSelectionUi.this.nameSearchUi);
      }

      private void addClassLists() {
         this.col.fillOut(ObjectClassSelectionUi.this.classLists);
      }

      private void installEnterShortcut() {
         ActionKeyBinding.install(this.col.container(), KeyUtils.ENTER, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent var1) {
               ObjectClassSelectionUi.this.classLists.transferSelectedItems();
            }
         });
      }
   }

   private final class ListListener implements SourceAndTargetListListener {
      private ListListener() {
      }

      @Override
      public void targetItemsChanged(SourceAndTargetList var1, final Object[] var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ArrayList var1x = Lists.newArrayList();

               for (Object var5 : var2) {
                  if (var5 instanceof ClassListItem) {
                     var1x.add(((ClassListItem)var5).getClassDefinition());
                  }
               }

               ObjectClassSelectionUi.this.model.setSelectedClasses(var1x);
            }
         });
      }
   }

   private final class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if ("classes".equals(var2)) {
            ObjectClassSelectionUi.this.updateAvailableClasses(ObjectClassSelectionUi.this.model.getObjectClasses());
            ObjectClassSelectionUi.this.updateSelectedClasses(ObjectClassSelectionUi.this.model.getSelectedClasses());
         } else if ("selected_classes".equals(var2)) {
            ObjectClassSelectionUi.this.updateSelectedClasses(ObjectClassSelectionUi.this.model.getSelectedClasses());
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  ObjectClassSelectionUi.this.page.setCanContinue(!ObjectClassSelectionUi.this.classLists.isTargetEmpty());
               }
            });
         }
      }
   }

   private final class RefreshAction extends AbstractAction implements GetObjectClassesCallback {
      public RefreshAction() {
         ActionDecorators.REFRESH.decorate(this);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         LdapLocation var2 = ObjectClassSelectionUi.this.model.getLocation();
         if (var2 != null) {
            this.setEnabled(false);
            ObjectClassDefinitionRetriever var3 = new ObjectClassDefinitionRetriever();
            var3.setLdapCache(ObjectClassSelectionUi.this.ldapCache);
            WaitService var4 = ObjectClassSelectionUi.this.page.getWaitService();
            var3.retrieveClassDefinitions(var2, this, false, var4);
         }
      }

      @Override
      public void objectClassesRetrieved(ObjectClassDefinition[] var1) {
         try {
            this.updateModel(var1);
         } finally {
            this.enable();
         }
      }

      private void enable() {
         ActionUtils.setEnabledSafely(this, true);
      }

      @Override
      public void cancelled() {
         this.enable();
      }

      @Override
      public void caught(Throwable var1) {
         this.enable();
      }

      private void updateModel(final ObjectClassDefinition[] var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ObjectClassSelectionUi.this.model.setObjectClasses(var1);
            }
         });
      }
   }
}
