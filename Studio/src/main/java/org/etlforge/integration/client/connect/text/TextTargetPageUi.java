package org.etlforge.integration.client.connect.text;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.wizard.EditorPanelService;
import org.etlforge.integration.client.ui.entity.wizard.TreeWidget;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.NonWizardObjectsFilter;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.NColumnPanel;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.UiUtils;

public final class TextTargetPageUi<T> implements UiProvider, Disposable {
   private final TreeWidget<TextDocument> documentSelector;
   private final TextStructureFactory<T> structureFactory;
   private volatile T structureInput;
   private final EditorPanelService documentDisplayer;
   private final JPanel factoryPanel;
   private final JPanel panel;

   public TextTargetPageUi(EntityExplorerSupport var1, TextStructureFactory<T> var2, EditorPanel var3, String var4) {
      this.documentDisplayer = new EditorPanelService(var1, var3);
      this.documentSelector = this.createDocumentSelector(this.documentDisplayer.getProjectExplorer());
      this.structureFactory = var2;
      this.factoryPanel = this.createFactoryPanel(var4);
      this.panel = new JPanel(new BorderLayout());
      this.rebuildLayout(false);
   }

   private TreeWidget<TextDocument> createDocumentSelector(EntityExplorerSupport var1) {
      EntityFilter var2 = EntityFilters.and(EntityFilters.allOfType(EntityType.Document), new NonWizardObjectsFilter());
      return new TreeWidget<>(var1, EntityType.Document, TextDocument.class, var2);
   }

   private JPanel createFactoryPanel(String var1) {
      return Horizontal.box()
         .addAllWithSpace(10, var1, new TextTargetPageUi.AutoCreateStructureAction())
         .nonOpaque()
         .withBorder(StyleSheet.getDefaultTitleBorder("Auto-Create Text Structure"))
         .container();
   }

   public void addSelectionListener(SelectionListener var1) {
      this.documentSelector.addSelectionListener(var1);
   }

   public void removeSelectionListener(SelectionListener var1) {
      this.documentSelector.removeSelectionListener(var1);
   }

   public boolean isInputValid() {
      return this.getSelectedDocument() != null;
   }

   public TextDocument getSelectedDocument() {
      return this.documentSelector.getSelectedEntity();
   }

   public void setStructureFactoryInput(T var1) {
      this.structureInput = (T)var1;
      boolean var2 = this.structureFactory.isAvailableFor(this.structureInput);
      this.rebuildLayout(var2);
   }

   private void rebuildLayout(boolean var1) {
      TextTargetPageUi.Layout var2 = new TextTargetPageUi.Layout();
      if (var1) {
         var2.addFactoryPanel(this.factoryPanel);
      }

      var2.addSelector();
      var2.addToContainer(this.panel, "Center");
   }

   public boolean allowsRemoval() {
      return this.documentDisplayer.prepareForRemoval(true);
   }

   @Override
   public void dispose() {
      this.documentSelector.dispose();
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.documentSelector.requestFocus();
   }

   private class AutoCreateStructureAction extends AbstractAction {
      public AutoCreateStructureAction() {
         super("&Create Structure");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         TextTargetPageUi.CreateStructureWorkflow var2 = TextTargetPageUi.this.new CreateStructureWorkflow();
         var2.execute();
      }
   }

   private class CreateStructureWorkflow extends KongaSwingWorker<TextDocument, Void> {
      private CreateStructureWorkflow() {
      }

      protected TextDocument doInBackground() throws Exception {
         return TextTargetPageUi.this.structureFactory.createStructure(TextTargetPageUi.this.structureInput);
      }

      protected void onSuccess(TextDocument var1) {
         TextTargetPageUi.this.documentDisplayer.getProjectExplorer().openPageFor(var1);
         TextTargetPageUi.this.documentSelector.addNonProjectItem(var1);
         TextTargetPageUi.this.documentSelector.setSelectedEntity(var1);
      }
   }

   private class Layout {
      private final NColumnPanel grid = this.createControlGrid();

      public Layout() {
      }

      private NColumnPanel createControlGrid() {
         NColumnPanel var1 = new NColumnPanel(3, GridBagLayoutPanel.Anchor.WEST);
         var1.setWeights(0, 1.0, 0.0).setFill(0, GridBagLayoutPanel.Fill.HORIZONTAL);
         var1.setInsets(0, 0, 10, 5).setOpaque(false, false);
         return var1;
      }

      public void addSelector() {
         JComponent[] var1 = TextTargetPageUi.this.documentSelector.getLayoutComponents(false);
         this.grid.addRow(var1[0], var1[1], null);
      }

      public void addFactoryPanel(JPanel var1) {
         this.grid.add(var1, 3);
      }

      public void addToContainer(JPanel var1, Object var2) {
         var1.removeAll();
         CaptionLayout.of(this.grid.invisible(), TextTargetPageUi.this.documentDisplayer).nonOpaque().addToParent(var1, var2);
         UiUtils.revalidate(var1);
      }
   }
}
