package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import javax.swing.JComponent;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.UiCompanion;
import org.etlforge.integration.client.ui.entity.factory.Request;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.entity.Schedule;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.TwoColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.Property;

public final class ProjectItemSelector<T extends IntegrationEntity> implements UiProvider, Disposable {
   private final TreeWidget<T> itemSelector;
   private final EditorPanelService itemDisplayer;
   private final boolean useDialog;
   private boolean saving;

   public static ProjectItemSelector<Source> forSources(EntityExplorerSupport var0, EditorPanel var1) {
      return new ProjectItemSelector<>(Source.class, EntityType.Source, var0, var1);
   }

   public static ProjectItemSelector<Schedule> forSchedules(EntityExplorerSupport var0, EditorPanel var1) {
      ProjectItemSelector var2 = new ProjectItemSelector<>(Schedule.class, EntityType.Schedule, var0, var1);
      var2.setOptional(true);
      var2.setSelectorLabel("Run on schedule:");
      return var2;
   }

   public static ProjectItemSelector<TextDocument> forTextDocuments(EntityExplorerSupport var0, EditorPanel var1) {
      return new ProjectItemSelector<>(TextDocument.class, EntityType.Document, var0, var1);
   }

   public ProjectItemSelector(Class<T> var1, EntityType var2, EntityExplorerSupport var3, EditorPanel var4) {
      this(var1, var2, var3, var4, false);
   }

   public ProjectItemSelector(Class<T> var1, EntityType var2, EntityExplorerSupport var3, EditorPanel var4, boolean var5) {
      this.itemDisplayer = new EditorPanelService(var3, var4, var5);
      this.itemSelector = this.createSelector(var2, var1);
      this.useDialog = var5;
   }

   private TreeWidget<T> createSelector(EntityType var1, Class<T> var2) {
      TreeWidget var3 = new TreeWidget(this.itemDisplayer.getProjectExplorer(), var1, var2, null, Request.Source.WIZARD);
      var3.setEditAutomaticallyOnSelection(true);
      var3.addSelectionListener(new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            if (ProjectItemSelector.this.getSelectedItem() == null) {
               ProjectItemSelector.this.saveAndCloseEDT();
            }
         }
      });
      return var3;
   }

   private boolean saveAndCloseEDT() {
      assert EventQueue.isDispatchThread();
      if (this.saving) {
         return true;
      }

      this.saving = true;

      try {
         return this.itemDisplayer.prepareForRemoval(true);
      } finally {
         this.saving = false;
      }
   }

   public void setEditAutomaticallyOnSelect(boolean var1) {
      this.itemSelector.setEditAutomaticallyOnSelection(var1);
   }

   public void setSelectorLabel(String var1) {
      this.itemSelector.setSelectorLabel(var1);
   }

   public void setNewEntityCallback(UiEntityFactoryCallback var1) {
      this.itemSelector.setNewEntityCallback(var1);
   }

   public void setFilter(EntityFilter var1) {
      this.itemSelector.setFilter(var1);
   }

   public void setOptional(boolean var1) {
      this.itemSelector.setIncludeNoneItem(var1);
   }

   public void setEnabled(boolean var1) {
      if (!var1) {
         this.itemDisplayer.forceCloseWithoutSaving();
         this.itemSelector.clearSelection();
      }

      this.itemSelector.setEnabled(var1);
   }

   public T getSelectedItem() {
      return this.itemSelector.getSelectedEntity();
   }

   public void setSelectedItemId(IntegrationEntityId var1) {
      this.itemSelector.setSelectedEntity(var1);
   }

   public void clearSelection() {
      this.itemDisplayer.forceCloseAfterSaving();
      this.itemSelector.clearSelection();
   }

   public void forceCloseAfterSaving() {
      this.itemDisplayer.forceCloseAfterSaving();
   }

   public void openEditorFor(T var1) {
      this.itemDisplayer.openEditorFor(var1);
   }

   @Override
   public void dispose() {
      this.itemSelector.dispose();
      this.closeWithoutSaving();
   }

   public void closeWithoutSaving() {
      this.itemDisplayer.forceCloseWithoutSaving();
   }

   public boolean closeGracefully() {
      return this.itemDisplayer.prepareForRemoval(false);
   }

   public boolean saveAndClose() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            return ProjectItemSelector.this.saveAndCloseEDT();
         }
      });
   }

   public boolean save() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            boolean var1 = true;
            if (ProjectItemSelector.this.itemDisplayer.getOpenEditor() != null) {
               var1 = ProjectItemSelector.this.itemDisplayer.saveCurrentEditor();
            } else {
               IntegrationEntity var2 = ProjectItemSelector.this.getSelectedItem();
               if (var2 != null && var2.isTransient()) {
                  try {
                     var1 = var2.save();
                  } catch (InterchangeSavingException var4) {
                     ErrorLog.attention(this.getClass(), null, var4);
                     UiCompanion.of(var2).openPage();
                     var1 = false;
                  }
               }
            }

            return var1;
         }
      });
   }

   public void addSelectionListener(SelectionListener var1) {
      this.itemSelector.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.itemSelector.removeSelectionListener(var1);
   }

   public void selectOnlyAvailableItem() {
      this.itemSelector.selectOnlyAvailableItem();
   }

   public InputField asInputField(String var1) {
      return this.itemSelector.asInputField(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      JComponent var2 = this.getSelectorComponentForLayout(false);
      if (this.useDialog) {
         var1.display(var2);
      } else {
         Vertical.borderLayout().withGap(8).of(var2, this.itemDisplayer).decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.itemSelector.requestFocus();
   }

   public void layoutIn(TwoColumnPanel var1) {
      JComponent[] var2 = this.itemSelector.getLayoutComponents(true);
      var1.addRow(var2[0], var2[1]);
      var1.withRightFill(GridBagLayoutPanel.Fill.BOTH).withRightWeights(1.0, 1.0);
      var1.addRow(null, BorderLayoutBuilder.fillWith(this.itemDisplayer));
   }

   public JComponent getSelectorComponentForLayout(boolean var1) {
      return var1 ? new BorderLayoutBuilder().north(this.itemSelector).container() : InvisiblePanel.of(this.itemSelector);
   }

   public JComponent getEditorComponentForLayout() {
      return BorderLayoutBuilder.fillWith(this.itemDisplayer);
   }

   public Binding bindWith(Property<T> var1) {
      return new SelectionWidgetBinding<>(this.itemSelector, var1);
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      this.itemSelector.installUndoRedo(var1);
   }

   public boolean accepts(IntegrationEntity var1) {
      return this.itemSelector.accepts(var1);
   }
}
