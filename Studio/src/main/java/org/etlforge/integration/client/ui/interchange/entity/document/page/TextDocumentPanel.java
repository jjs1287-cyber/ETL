package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.ApplyAbortedException;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.structure.editor.text.SimpleStructureFileTemplateWizard;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.SegmentType;
import org.etlforge.integration.data.structure.text.TextStructureType;
import org.etlforge.system.callback.CallbackResult;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.DefaultComponentAction;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public final class TextDocumentPanel extends IntegrationEntityPanel {
   private static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private static final String TYPE_AND_FORMAT = "TypeAndFormat";
   private static final String STRUCTURE = "Structure";
   private final IntegrationProject project;
   private final Optional<EntityExplorerSupport> projectExplorer;
   private final NewTextDocumentModel model;
   private final JPanel stagePanel = createStagePanel();
   private final Action createManuallyAction = new TextDocumentPanel.CreateManuallyAction();
   private final Action createFromFileAction = new TextDocumentPanel.CreateFromFileAction();
   private final List<TextDocumentDefiner> documentDefiners = Lists.newArrayList();
   private final List<NewDocumentListener> newDocumentListeners = new CopyOnWriteArrayList<>();
   private final Action viewStructureTreeAction;
   private boolean isInFirstPage;

   public TextDocumentPanel(ApplicationWindow var1, IntegrationProject var2, TextDocument var3) {
      this(var1, var2, var3, null, null);
   }

   public TextDocumentPanel(ApplicationWindow var1, IntegrationProject var2, TextDocument var3, EntityExplorerSupport var4, Action var5) {
      super(var1, var3);
      Preconditions.checkNotNull(var3, "document");
      this.project = Preconditions.checkNotNull(var2, "project");
      this.projectExplorer = Optional.fromNullable(var4);
      this.model = new NewTextDocumentModel(var3);
      this.viewStructureTreeAction = var5;
      this.getNameField().addInputChangeListener(new InputChangeListener() {
         @Override
         public void inputChanged(InputChangeEvent var1) {
            TextDocumentPanel.this.model.setDocumentName(TextDocumentPanel.this.getNameField().getValue().trim());
         }
      });
      this.createComponents(var3);
      this.layoutComponents();
      this.registerListeners();
      this.installDefaultAction();
      UiUtils.setOpaqueRecursivelySkipDefaults(this.getUi(), false);
   }

   private static JPanel createStagePanel() {
      JPanel var0 = new JPanel();
      var0.setOpaque(false);
      var0.setLayout(new CardLayout());
      return var0;
   }

   public TextDocument getDisplayedObject() {
      return (TextDocument)super.getDisplayedObject();
   }

   public void stopEditing() {
      for (TextDocumentDefiner var2 : this.documentDefiners) {
         var2.stopEditing();
      }
   }

   @Override
   public void validateInput() throws InputPanelException {
      for (TextDocumentDefiner var2 : this.documentDefiners) {
         var2.validateInput();
      }
   }

   @Override
   protected void validate() throws IntegrationDataPanelException {
      if (this.isInFirstPage) {
         throw new IntegrationDataPanelException("No fields have been defined.");
      }

      this.stopEditing();
      this.validateEnteredData();
   }

   @Override
   protected void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException {
      this.applyStructure();
   }

   public void applyStructure(TextDocument var1) throws TextDocumentDefinitionException {
      for (TextDocumentDefiner var3 : this.documentDefiners) {
         var3.applyToTextDocument(var1);
      }
   }

   public void addNewDocumentListener(NewDocumentListener var1) {
      this.newDocumentListeners.add(var1);
   }

   public void removeNewDocumentListener(NewDocumentListener var1) {
      this.newDocumentListeners.remove(var1);
   }

   private void createComponents(TextDocument var1) {
      if (var1.isUnknownDoc()) {
         this.createComponentsForNewDocument();
      } else {
         this.createComponentsForExistingDocument(var1);
      }
   }

   private void createComponentsForNewDocument() {
      NewDocumentPanel var1 = new NewDocumentPanel(this.model, this.createManuallyAction, this.createFromFileAction);
      this.stagePanel.add(var1.getUi(), "TypeAndFormat");
      this.documentDefiners.add(var1);
      this.model.addPropertyChangeListener(new TextDocumentPanel.ModelListener());
      this.isInFirstPage = true;
   }

   private void createComponentsForExistingDocument(TextDocument var1) {
      TextDocumentDefiner var2 = null;
      if (var1.isSimpleDoc()) {
         SimpleTextPanel var3 = new SimpleTextPanel(this.project, var1);
         if (this.projectExplorer.isPresent()) {
            var3.setProjectExplorer(this.projectExplorer.get());
         }

         var2 = var3;
      } else {
         var2 = new ComplexTextPanel(var1);
      }

      this.documentDefiners.add(var2);
      this.addInputPanel(var2);
      this.stagePanel.add(this.layoutDefiner(var2), "Structure");
   }

   private JComponent layoutDefiner(TextDocumentDefiner var1) {
      if (this.viewStructureTreeAction == null) {
         return var1.getUi();
      }

      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 5).nonOpaque();
      KongaButton var3 = new KongaButton(this.viewStructureTreeAction);
      var2.center(var1.getUi()).south(InvisiblePanel.of(var3));
      return var2.container();
   }

   private void registerListeners() {
      JTextField var1 = (JTextField)this.getNameField().getInputComponent();
      var1.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            if (TextDocumentPanel.this.createManuallyAction != null && TextDocumentPanel.this.createManuallyAction.isEnabled()) {
               TextDocumentPanel.this.createManuallyAction.actionPerformed(var1);
            }
         }
      });
   }

   private void installDefaultAction() {
      DefaultComponentAction.whenAncestorOfFocusOwner(this.getUi(), this.createManuallyAction);
   }

   private void layoutComponents() {
      BorderLayoutBuilder var1 = new BorderLayoutBuilder(0, 10).nonOpaque().withBorder(Empty.border(5, 0, 5, 0));
      var1.north(this.getNameFieldLayout()).center(this.getSubComponentPanel(this.stagePanel));
      BorderLayoutBuilder.from(this.getUi()).center(var1);
   }

   private void validateEnteredData() throws IntegrationDataPanelException {
      try {
         this.validateInput();
      } catch (ValidationAbortedException var2) {
         throw new ApplyAbortedException(var2.getMessage());
      } catch (InputPanelException var3) {
         throw new IntegrationDataPanelException(var3.getMessage());
      }
   }

   private void applyStructure() throws IntegrationDataPanelException {
      try {
         this.applyStructure(this.getDisplayedObject());
      } catch (TextDocumentDefinitionException var2) {
         throw new IntegrationDataPanelException(var2.getMessage());
      }
   }

   private TextDocumentDefiner getNextDefiner() {
      TextDocument var1 = this.getDisplayedObject();
      TextStructureType var2 = this.model.getStructureType();
      boolean var3 = this.model.getSegmentType() == SegmentType.FIXED_FIELD;
      if (var2 == TextStructureType.SIMPLE) {
         SimpleTextPanel var4 = new SimpleTextPanel(this.project, var1, var3);
         if (this.projectExplorer.isPresent()) {
            var4.setProjectExplorer(this.projectExplorer.get());
         }

         return var4;
      } else {
         return new ComplexTextPanel(var1, var3);
      }
   }

   private void moveToNextPage() {
      this.model.updateDocumentType();
      TextDocumentDefiner var1 = this.getNextDefiner();
      this.documentDefiners.add(var1);
      this.addInputPanel(var1);
      UiUtils.setOpaqueRecursivelySkipDefaults(var1.getUi(), false);
      this.stagePanel.add(this.layoutDefiner(var1), "Structure");
      ((CardLayout)this.stagePanel.getLayout()).show(this.stagePanel, "Structure");

      for (NewDocumentListener var3 : this.newDocumentListeners) {
         var3.newDocumentCreated();
      }

      this.setInitialFocus();
      this.isInFirstPage = false;
   }

   public void rebuildPage() {
      this.documentDefiners.clear();
      this.stagePanel.removeAll();
      this.createComponentsForExistingDocument(this.getDisplayedObject());
      UiUtils.revalidate(this.getUi());
   }

   private class CreateFromFileAction extends AbstractAction implements CallbackResult {
      public CreateFromFileAction() {
         super(TextDocumentPanel.RESOURCES.getString("Action.CreateFromFile"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CreateFromFileAction.this.executeImpl();
            }
         });
      }

      private void executeImpl() {
         SimpleStructureFileTemplateWizard var1 = new SimpleStructureFileTemplateWizard(
            TextDocumentPanel.this.getWindow(),
            TextDocumentPanel.this.getDisplayedObject(),
            TextDocumentPanel.this.getNameField().getValue().trim(),
            TextDocumentPanel.this.model.getSegmentType(),
            this
         );
         var1.start();
      }

      @Override
      public void cancelled(Object var1) {
      }

      @Override
      public void failed(Object var1) {
         if (var1 instanceof Exception) {
            String var2 = "Failed to create the text structure from the selected file.";
            ErrorLog.attention(TextDocumentPanel.class, var2, (Exception)var1);
         }
      }

      @Override
      public void succeeded(Object var1) {
         TextDocumentPanel.this.getDisplayedObject().setStructure((SimpleTextStructure)var1);
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               CreateFromFileAction.this.setEnabled(false);
               TextDocumentPanel.this.moveToNextPage();
            }
         });
      }
   }

   private class CreateManuallyAction extends AbstractAction {
      public CreateManuallyAction() {
         super(TextDocumentPanel.RESOURCES.getString("Action.CreateManually"));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.setEnabled(false);
         TextDocumentPanel.this.moveToNextPage();
      }
   }

   private class ModelListener implements PropertyChangeListener {
      private ModelListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = var1.getPropertyName();
         if (var2.equals("name")) {
            boolean var3 = TextDocumentPanel.this.getNameField().isFieldValid();
            TextDocumentPanel.this.createManuallyAction.setEnabled(var3);
            this.updateCreateFromFileAvailability();
         } else if (this.isTypeChange(var2)) {
            this.updateCreateFromFileAvailability();
         }
      }

      private boolean isTypeChange(String var1) {
         return var1.equals("structure_type") || var1.equals("segment_type");
      }

      private void updateCreateFromFileAvailability() {
         boolean var1 = this.isCreateFromFileAvailable();
         TextDocumentPanel.this.createFromFileAction.setEnabled(var1);
      }

      private boolean isCreateFromFileAvailable() {
         TextStructureType var1 = TextDocumentPanel.this.model.getStructureType();
         if (var1 != TextStructureType.SIMPLE) {
            return false;
         }

         SegmentType var2 = TextDocumentPanel.this.model.getSegmentType();
         return var2 != SegmentType.DELIMITED ? false : TextDocumentPanel.this.getNameField().isFieldValid();
      }
   }
}
