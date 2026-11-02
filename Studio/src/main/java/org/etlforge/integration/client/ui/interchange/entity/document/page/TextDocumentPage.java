package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.event.UndoableEditEvent;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.integration.client.info.ClientInfo;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPageModel;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.TextStructure;

public final class TextDocumentPage extends IntegrationEntityPage {
   private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(
      "org.etlforge.integration.client.ui.interchange.entity.document.page.resources.PageResources"
   );
   private final IntegrationProject project;
   private int availableUndos;
   private final ViewStructureTreeAction viewStructureAction;

   public TextDocumentPage(IntegrationProject var1, TextDocument var2) {
      super(new IntegrationEntityPageModel(var2));
      this.project = var1;
      this.viewStructureAction = ClientInfo.isDataLoader() ? null : new ViewStructureTreeAction(this);
      this.setHelpId("integration.textstructure.main");
      this.addSaveConsequence(new StructureChangeSaveConsequence(this));
      if (!var2.isTransient()) {
         this.listenToStructureChanges();
      }
   }

   private void listenToStructureChanges() {
      final TextDocumentPage.DocumentListener var1 = new TextDocumentPage.DocumentListener(this.getObject());
      var1.install();
      this.addResource(new AbstractWindowElementResource() {
         @Override
         public void dispose(WindowElement var1x) {
            var1.dispose();
         }
      });
   }

   protected TextDocumentPanel createDataPanel() {
      TextDocumentPanel var1 = new TextDocumentPanel(
         this.getWindow(), this.project, this.getObject(), this.getEntityExplorerSupport(), this.viewStructureAction
      );
      var1.addNewDocumentListener(new NewDocumentListener() {
         @Override
         public void newDocumentCreated() {
            TextDocumentPage.this.listenToStructureChanges();
            TextDocumentPage.this.availableUndos = 0;
         }
      });
      return var1;
   }

   public TextDocument getObject() {
      return (TextDocument)super.getObject();
   }

   public static ResourceBundle getResources() {
      return RESOURCES;
   }

   @Override
   public void undoableEditHappened(UndoableEditEvent var1) {
      if (this.getDetailsPanel().isSendingUndoEvents()) {
         this.availableUndos++;
         super.undoableEditHappened(var1);
      }
   }

   @Override
   public boolean canUndo() {
      return this.availableUndos > 0 && super.canUndo();
   }

   @Override
   public void redoEdit() {
      super.redoEdit();
      this.availableUndos++;
   }

   @Override
   public void undoEdit() {
      this.getDetailsPanel().stopEditing();
      super.undoEdit();
      this.availableUndos--;
   }

   protected TextDocumentPanel getDetailsPanel() {
      return (TextDocumentPanel)super.getDetailsPanel();
   }

   public void applyStructure(TextDocument var1) throws TextDocumentDefinitionException {
      this.getDetailsPanel().applyStructure(var1);
   }

   public TextStructure getStructureRepresentation() throws TextDocumentDefinitionException {
      TextDocument var1 = new TextDocument("__dummy__");
      var1.setDocType(this.getObject().getDocType());
      this.applyStructure(var1);
      return var1.getDataStructure();
   }

   private class DocumentListener implements PropertyChangeListener {
      private final TextDocument document;

      public DocumentListener(TextDocument var2) {
         this.document = var2;
      }

      public void install() {
         this.document.addPropertyChangeListener("document_structure", this);
      }

      public void dispose() {
         this.document.removePropertyChangeListener("document_structure", this);
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         assert "document_structure".equals(var1.getPropertyName());
         TextDocumentPage.this.getDetailsPanel().rebuildPage();
      }
   }
}
