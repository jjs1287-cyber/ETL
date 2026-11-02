package org.etlforge.integration.client.ui.de.global;

import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTable;
import org.etlforge.integration.client.ui.de.global.tree.DataElementsTreeWidget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.ProcessFilter;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.integration.data.script.de.GlobalDataElementsListener;
import org.etlforge.integration.dataelement.DefaultDataElementTemplate;
import org.etlforge.integration.dataelement.ui.DeTemplateTransferable;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiProviderAdapter;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionAction;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.selection.SelectionWidget;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.util.clipboard.ClipboardUtils;

public class GlobalDataElementsBrowser implements Disposable, UiProvider, SelectionWidget {
   private GlobalDataElements deStore;
   private final GlobalDataElementsListener deStoreListener;
   private final DataElementsWidget deWidget;
   private final DataElementReferenceTable referencesTable;
   private final GlobalDataElementsBrowser.ViewReferencesAction viewRefsAction;
   private final GlobalDataElementsBrowser.CopyAction copyAction;
   private ExpressionSourceDisplayer sourceDisplayer;

   public GlobalDataElementsBrowser() {
      this(true);
   }

   public GlobalDataElementsBrowser(boolean var1) {
      this(null, var1);
   }

   public GlobalDataElementsBrowser(DataElementsWidget var1, boolean var2) {
      this.deWidget = var1 != null ? var1 : new DataElementsTreeWidget();
      this.deStoreListener = new GlobalDataElementsBrowser.DeStoreListener();
      this.viewRefsAction = new GlobalDataElementsBrowser.ViewReferencesAction();
      this.copyAction = new GlobalDataElementsBrowser.CopyAction();
      this.viewRefsAction.setSelectionSource(var1);
      this.copyAction.setSelectionSource(var1);
      if (var2) {
         this.referencesTable = new DataElementReferenceTable();
      } else {
         this.referencesTable = null;
      }

      this.configureWidget(var2);
   }

   private void configureWidget(boolean var1) {
      this.deWidget.setActionProvider(new Supplier<List<Action>>() {
         public List<Action> get() {
            ArrayList var1x = Lists.newArrayList();
            var1x.add(GlobalDataElementsBrowser.this.copyAction);
            if (GlobalDataElementsBrowser.this.referencesTable == null) {
               var1x.add(GlobalDataElementsBrowser.this.viewRefsAction);
            }

            return var1x;
         }
      });
      if (var1) {
         this.deWidget.addSelectionListener(new GlobalDataElementsBrowser.SelectionListenerImpl());
      }
   }

   @Override
   public void dispose() {
      if (this.deStore != null) {
         this.deStore.removeListener(this.deStoreListener);
      }

      if (this.referencesTable != null) {
         this.referencesTable.dispose();
      }
   }

   public void setNameFilter(Predicate<String> var1) {
      this.deWidget.setFilter(var1);
   }

   @Override
   public void selectFirst() {
      this.deWidget.selectFirst();
   }

   @Override
   public boolean isEmpty() {
      return this.deWidget.isEmpty();
   }

   public void setGlobalDataElements(GlobalDataElements var1) {
      if (var1 != this.deStore) {
         if (this.deStore != null) {
            this.deStore.removeListener(this.deStoreListener);
         }

         this.deStore = var1;
         if (var1 != null) {
            var1.addListener(this.deStoreListener);
            ProcessFilter var2 = var1.getProcessFilter();
            this.deWidget.setProcess(var2 == null ? null : var2.getProcess());
         } else {
            this.deWidget.setProcess(null);
         }

         this.deWidget.setDataElements(var1);
      }
   }

   public void setIncludeBuiltInDataElements(boolean var1) {
      this.deWidget.setIncludeBuiltInDataElements(var1);
   }

   public void setExpressionSourceDisplayer(ExpressionSourceDisplayer var1) {
      this.sourceDisplayer = var1;
      if (this.referencesTable != null) {
         this.referencesTable.setExpressionSourceDisplayer(var1);
      }
   }

   public void setProcess(IntegrationEntity var1) {
      this.deWidget.setProcess(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.referencesTable != null) {
         this.layoutWithReferences(var1);
      } else {
         this.deWidget.decorate(var1);
      }
   }

   private void layoutWithReferences(UiCanvas var1) {
      KongaSplitPane var2 = new KongaSplitPane(1);
      var2.setOneTouchExpandable(true);
      var2.setResizeWeight(0.5);
      JPanel var3 = BorderLayoutBuilder.fillWith(this.referencesTable);
      ComponentTraverser.traverse(var3, new Acceptor<Component>() {
         public boolean accept(Component var1) {
            var1.setMinimumSize(new Dimension(1, 1));
            return !(var1 instanceof JTable);
         }
      });
      var2.split(this.deWidget, new UiProviderAdapter(var3));
      var1.display(var2);
   }

   public void setDefaultAction(SelectionAction var1) {
      this.deWidget.setDefaultAction(var1);
   }

   @Override
   public void requestFocus() {
      this.deWidget.requestFocus();
   }

   private void rebuild() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            Object var1 = ((OrderedSelection)GlobalDataElementsBrowser.this.deWidget.getSelection()).head();
            GlobalDataElementsBrowser.this.deWidget.update();
            if (GlobalDataElementsBrowser.this.referencesTable != null && var1 instanceof String) {
               GlobalDataElementsBrowser.this.showReferences((String)var1);
            }
         }
      });
   }

   private void showReferences(String var1) {
      Set var2 = this.deStore.getReferences(var1);
      this.referencesTable.setReferences(var2);
   }

   private static class CopyAction extends AbstractSingleObjectSelectionAction<String> {
      public CopyAction() {
         super(String.class, "Copy");
      }

      protected void run(String var1, ActionEvent var2) {
         DefaultDataElementTemplate var3 = new DefaultDataElementTemplate(var1);
         ClipboardUtils.copyToSystemClipboard(new DeTemplateTransferable(var3));
      }
   }

   private class DeStoreListener implements GlobalDataElementsListener {
      private DeStoreListener() {
      }

      @Override
      public void dataElementStoreChanged() {
         GlobalDataElementsBrowser.this.rebuild();
      }

      @Override
      public void dataElementStoreRebuilt() {
         GlobalDataElementsBrowser.this.rebuild();
      }

      @Override
      public void processFilterChanged(ProcessFilter var1) {
         GlobalDataElementsBrowser.this.deWidget.setProcess(var1 == null ? null : var1.getProcess());
      }
   }

   private class SelectionListenerImpl implements SelectionListener {
      private SelectionListenerImpl() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         OrderedSelection var2 = (OrderedSelection)var1.getSelection();
         if (var2.size() == 1) {
            GlobalDataElementsBrowser.this.showReferences((String)var2.head());
         } else {
            GlobalDataElementsBrowser.this.referencesTable.clear();
         }
      }
   }

   private class ViewReferencesAction extends AbstractSingleObjectSelectionAction<String> {
      public ViewReferencesAction() {
         super(String.class, "View References...");
      }

      protected void run(final String var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ViewReferencesAction.this.showReferences(var1);
            }
         });
      }

      private void showReferences(String var1) {
         Set var2 = GlobalDataElementsBrowser.this.deStore.getReferences(var1);
         DataElementReferenceTable var3 = new DataElementReferenceTable();
         var3.setReferences(var2).setCaption("References to " + var1 + ":").setExpressionSourceDisplayer(GlobalDataElementsBrowser.this.sourceDisplayer);
         UiProviderDialogDisplayer.display(UiUtils.getActiveWindow(), var3, "Data Element References", KongaDialog.OK_ONLY, GlobalDataElementsBrowser.class);
      }
   }
}
