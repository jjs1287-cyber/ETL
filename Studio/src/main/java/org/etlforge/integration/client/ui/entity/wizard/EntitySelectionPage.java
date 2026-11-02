package org.etlforge.integration.client.ui.entity.wizard;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import java.awt.EventQueue;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler.TransferSupport;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ApplicationWindowWizard;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.EntityDropHandler;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.IntegrationEntityId;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.undo.UndoRedoInstaller;

public class EntitySelectionPage<T extends IntegrationEntity> extends AbstractWizardPage {
   private final EntityExplorerSupport projectExplorer;
   private final EntityType entityType;
   private final Class<T> entityClass;
   private EntityFilter filter;
   private ProjectItemSelector<T> selectionWidget;
   private String mainCaption;
   private String subCaption;
   private EditorPanel editorPanel;
   private ImmutableList<JComponent> additionalComponents = ImmutableList.of();
   private UiEntityFactoryCallback newEntityCallback;
   private IntegrationEntityId selectedEntityId;
   private String selectorLabel;

   public EntitySelectionPage(ApplicationWindowWizard var1, String var2, EntityExplorerSupport var3, EntityType var4, Class<T> var5, EntityFilter var6) {
      super(var1, var2);
      this.projectExplorer = var3;
      this.entityClass = var5;
      this.entityType = var4;
      this.filter = var6;
   }

   private void updateCanContinueState() {
      boolean var1 = this.getSelectedEntity() != null;
      this.setCanContinue(var1);
   }

   @Override
   protected final void onFirstDisplay() {
      ApplicationWindow var1 = ((ApplicationWindowWizard)this.getWizard()).getWindow();
      this.editorPanel = new EditorPanel(var1);
      this.selectionWidget = new ProjectItemSelector<>(this.entityClass, this.entityType, this.projectExplorer, this.editorPanel);
      this.selectionWidget.setFilter(this.filter);
      if (this.selectorLabel != null) {
         this.selectionWidget.setSelectorLabel(this.selectorLabel);
      }

      if (this.selectedEntityId != null) {
         this.selectionWidget.setSelectedItemId(this.selectedEntityId);
      } else {
         this.selectionWidget.selectOnlyAvailableItem();
      }

      if (this.newEntityCallback != null) {
         this.selectionWidget.setNewEntityCallback(this.newEntityCallback);
      }

      this.registerListeners();
      this.installUndoRedo();
      this.updateCanContinueState();
      this.setInitialState();
   }

   protected void setInitialState() {
   }

   @Override
   protected final JPanel layoutPage() {
      JPanel var1 = this.layoutTopPanel();
      WizardPageLayout var2 = WizardStyleSheet.emptyPageLayout(this.mainCaption, this.subCaption);
      CaptionLayout var3 = CaptionLayout.of(var1, this.selectionWidget.getEditorComponentForLayout());
      var2.add(var3, true, true);
      JPanel var4 = var2.asCenterBorderLayout();
      var4.setTransferHandler(new EntitySelectionPage.DropHandlerImpl());
      return var4;
   }

   private JPanel layoutTopPanel() {
      return new OneColumnPanel().withInsets(5, 5, 5, 5).add(this.selectionWidget).addAll(this.additionalComponents).deeplyNonOpaque().invisible();
   }

   @Override
   public final boolean prepareForForwardNavigation() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            boolean var1 = EntitySelectionPage.this.selectionWidget.save();
            if (var1) {
               T var2 = EntitySelectionPage.this.getSelectedEntity();
               return EntitySelectionPage.this.validateSelectedEntity(var2);
            } else {
               return false;
            }
         }
      });
   }

   @Override
   public final boolean prepareForBackwardNavigation() {
      return this.selectionWidget.closeGracefully();
   }

   protected boolean validateSelectedEntity(T var1) {
      return true;
   }

   @Override
   public final void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            EntitySelectionPage.this.selectionWidget.requestFocus();
         }
      });
   }

   public final void setMainCaption(String var1) {
      this.mainCaption = var1;
   }

   public final void setSubCaption(String var1) {
      this.subCaption = var1;
   }

   public final void setNewEntityCallback(UiEntityFactoryCallback var1) {
      if (this.selectionWidget != null) {
         this.selectionWidget.setNewEntityCallback(var1);
      } else {
         this.newEntityCallback = var1;
      }
   }

   public final void setFilter(EntityFilter var1) {
      if (this.selectionWidget != null) {
         this.selectionWidget.setFilter(var1);
      } else {
         this.filter = var1;
      }
   }

   public final void addSelectionListener(SelectionListener var1) {
      this.selectionWidget.addSelectionListener(var1);
   }

   public final void setAdditionalComponents(List<? extends JComponent> var1) {
      this.additionalComponents = ImmutableList.copyOf(var1);
   }

   public final T getSelectedEntity() {
      return UiThreadSupplier.call(new Supplier<T>() {
         public T get() {
            return EntitySelectionPage.this.selectionWidget.getSelectedItem();
         }
      });
   }

   public final void setSelectedEntity(IntegrationEntity var1) {
      this.setSelectedEntityId(var1 == null ? null : var1.getID());
   }

   public final void setSelectedEntityId(IntegrationEntityId var1) {
      if (this.selectionWidget != null) {
         this.selectionWidget.setSelectedItemId(var1);
      } else {
         this.selectedEntityId = var1;
      }
   }

   @Override
   protected final void dispose() {
      this.selectionWidget.dispose();
      this.disposeAddtionalResources();
   }

   protected void disposeAddtionalResources() {
   }

   public final void setSelectorLabel(String var1) {
      if (this.selectionWidget != null) {
         this.selectionWidget.setSelectorLabel(var1);
      } else {
         this.selectorLabel = var1;
      }
   }

   private void registerListeners() {
      SelectionListener var1 = new SelectionListener() {
         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            EntitySelectionPage.this.updateCanContinueState();
         }
      };
      this.addSelectionListener(var1);
   }

   private void installUndoRedo() {
      UndoRedoInstaller var1 = this.getUndoRedoInstaller();
      this.selectionWidget.installUndoRedo(var1);
   }

   private final class DropHandlerImpl extends EntityDropHandler {
      private DropHandlerImpl() {
      }

      @Override
      protected boolean accepts(IntegrationEntity var1, TransferSupport var2) {
         return EntitySelectionPage.this.selectionWidget.accepts(var1);
      }

      @Override
      protected boolean handleDrop(IntegrationEntity var1, TransferSupport var2) {
         EntitySelectionPage.this.selectionWidget.setSelectedItemId(var1.getID());
         return true;
      }
   }
}
