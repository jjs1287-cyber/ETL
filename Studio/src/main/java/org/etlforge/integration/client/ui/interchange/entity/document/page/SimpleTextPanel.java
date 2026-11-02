package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Optional;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.DelimitedFieldTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.DelimitedFieldTableModel;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FieldTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FieldTableModel;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FixedWidthFieldTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.FixedWidthFieldTableModel;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.ValidationTableService;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Field;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.layout.OneColumnPanel;

public final class SimpleTextPanel extends TextDocumentDefiner {
   private static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private final TextDocument document;
   private final SimpleTextStructure textStructure;
   private final DelimiterAndQualifierPanel delimQualifierPanel;
   private final NumberOfFieldsValidationUi validationPanel;
   private final FieldTable fieldTable;
   private final FieldTableModel fieldModel;
   private final ValidationEditor validationEditor;

   public SimpleTextPanel(IntegrationProject var1, TextDocument var2) {
      this(var1, var2, var2.isFixedField());
   }

   public SimpleTextPanel(IntegrationProject var1, TextDocument var2, boolean var3) {
      this.document = var2;
      this.textStructure = (SimpleTextStructure)var2.getDataStructure();
      this.delimQualifierPanel = this.createDelimQualPanel(var3);
      this.fieldModel = this.createModel(var3);
      this.fieldTable = var3
         ? new FixedWidthFieldTable((FixedWidthFieldTableModel)this.fieldModel)
         : new DelimitedFieldTable((DelimitedFieldTableModel)this.fieldModel);
      this.fieldTable.addPropertyChangeListener("DocDefTable#TableContents", new SimpleTextPanel.TableListener());
      this.validationPanel = this.createValidationPanel();
      this.bindValidationPanelToNumberOfRowsInTable();
      this.validationEditor = this.configureFieldValidation(var1);
      this.layoutComponents();
   }

   private DelimiterAndQualifierPanel createDelimQualPanel(boolean var1) {
      if (var1) {
         return null;
      }

      DelimiterAndQualifierPanel var2 = new DelimiterAndQualifierPanel(this.document);
      this.addInputPanel(var2);
      return var2;
   }

   private FieldTableModel createModel(boolean var1) {
      FieldTableModel var2 = var1 ? FixedWidthFieldTableModel.withValidation() : DelimitedFieldTableModel.withValidation();
      var2.addUndoableEditListener(this);
      if (this.textStructure != null) {
         for (Field var4 : this.textStructure.getFieldList()) {
            var2.addRow(new Field(var4));
         }
      }

      return var2;
   }

   private NumberOfFieldsValidationUi createValidationPanel() {
      NumberOfFieldsValidationUi var1 = new NumberOfFieldsValidationUi();
      var1.setValue(this.document.getNumberOfFieldsValidation());
      var1.setNumberOfFieldsInStructure(this.fieldModel.getRowCount());
      var1.registerAsChangeSource(this);
      return var1;
   }

   private void bindValidationPanelToNumberOfRowsInTable() {
      this.fieldModel.addTableModelListener(new TableModelListener() {
         @Override
         public void tableChanged(TableModelEvent var1) {
            int var2 = SimpleTextPanel.this.fieldModel.getRowCount();
            SimpleTextPanel.this.validationPanel.setNumberOfFieldsInStructure(var2);
         }
      });
   }

   private ValidationEditor configureFieldValidation(IntegrationProject var1) {
      ValidationEditor var2 = new ValidationEditor(var1, this.fieldModel);
      var2.setUndoRedoListener(new UndoableEditListener() {
         @Override
         public void undoableEditHappened(UndoableEditEvent var1) {
            SimpleTextPanel.this.inputChanged(new InputChangeEvent(SimpleTextPanel.this, "validation"));
            SimpleTextPanel.this.undoableEditHappened(var1);
         }
      });
      this.fieldTable.installFieldValidation(new ValidationTableService(var2));
      return var2;
   }

   private void layoutComponents() {
      this.setOpaque(false);
      OneColumnPanel var1 = new OneColumnPanel();
      var1.setOpaque(true, false);
      var1.withInsets(0, 5, 10, 10);
      if (this.delimQualifierPanel == null) {
         var1.add(this.validationPanel);
      } else {
         var1.add(Horizontal.box().of(this.delimQualifierPanel.getUi(), this.validationPanel));
      }

      var1.add(TextStyles.InfoTextHeader.makeLabel(RESOURCES.getString("Title.FieldTable")));
      var1.fillOut(this.fieldTable.getDisplayer());
      BorderLayoutBuilder.from(this.getUi()).center(var1);
   }

   @Override
   public void stopEditing() {
      this.fieldTable.stopEditing();
   }

   @Override
   public void validateInput() throws InputPanelException {
      try {
         this.fieldModel.validateModel();
      } catch (TextDocumentDefinitionException var2) {
         throw new InputPanelException(var2.getMessage());
      }

      this.validationPanel.validateInput();
   }

   @Override
   public void applyToTextDocument(TextDocument var1) {
      this.applyDelimitersAndQualifiers(var1);
      this.applyStructure(var1);
      this.applyValidation(var1);
   }

   private void applyDelimitersAndQualifiers(TextDocument var1) {
      if (this.delimQualifierPanel != null) {
         this.delimQualifierPanel.applyToTextDocument(var1);
      }
   }

   private void applyStructure(TextDocument var1) {
      SimpleTextStructure var2 = this.getTextStructure(var1);
      var2.setFieldList(this.fieldModel.getFields());
      var1.documentStructureChanged();
   }

   private SimpleTextStructure getTextStructure(TextDocument var1) {
      TextStructure var2 = var1.getDataStructure();
      if (var2 == null) {
         var2 = var1.createTextStructure();
      }

      if (var2 instanceof SimpleTextStructure) {
         return (SimpleTextStructure)var2;
      } else {
         throw new IllegalStateException("Complex text structure encountered where a simple structure was expected.");
      }
   }

   private void applyValidation(TextDocument var1) {
      Optional var2 = this.validationPanel.getValue();
      var1.setNumberOfFieldsValidation(var2);
   }

   public void setProjectExplorer(EntityExplorerSupport var1) {
      this.validationEditor.setProjectExplorer(var1);
   }

   private class TableListener implements PropertyChangeListener {
      private TableListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (SimpleTextPanel.this.isSendingInputChangeEvents()) {
            SimpleTextPanel.this.fireInputChanged(SimpleTextPanel.this.fieldTable, "Table");
         }
      }
   }
}
