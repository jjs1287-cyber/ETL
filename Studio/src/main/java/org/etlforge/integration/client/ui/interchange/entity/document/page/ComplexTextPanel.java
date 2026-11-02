package org.etlforge.integration.client.ui.interchange.entity.document.page;

import com.google.common.base.Preconditions;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.SegmentTable;
import org.etlforge.integration.client.ui.interchange.entity.document.page.table.SegmentTableModel;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.text.Segment;
import org.etlforge.ui.input.InputPanelException;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.OneColumnPanel;

public final class ComplexTextPanel extends TextDocumentDefiner {
   public static final String STRUCTURE_DEFINITION_PROPERTY = "ComplexTextPanel#StructureChanged";
   private static final ResourceBundle RESOURCES = TextDocumentPage.getResources();
   private final TextDocument document;
   private final DelimiterAndQualifierPanel delimQualifierPanel;
   private final SegmentTable segmentTable;
   private final SegmentTableModel segmentModel;

   public ComplexTextPanel(TextDocument var1) {
      this(var1, var1.isFixedField());
   }

   public ComplexTextPanel(TextDocument var1, boolean var2) {
      this.document = var1;
      this.sanityCheckOfStructure();
      if (var2) {
         this.delimQualifierPanel = null;
      } else {
         this.delimQualifierPanel = new DelimiterAndQualifierPanel(var1);
         this.addInputPanel(this.delimQualifierPanel);
      }

      this.segmentModel = new SegmentTableModel(var1);
      this.segmentModel.addUndoableEditListener(this);
      this.segmentTable = new SegmentTable(this.segmentModel, var2);
      this.segmentTable.addPropertyChangeListener("DocDefTable#TableContents", new ComplexTextPanel.TableListener());
      this.layoutComponents();
   }

   private void sanityCheckOfStructure() {
      TextStructure var1 = this.document.getDataStructure();
      Preconditions.checkArgument(var1 == null || var1 instanceof ComplexTextStructure, "document does not have a complex structure.");
   }

   private void layoutComponents() {
      this.setOpaque(false);
      OneColumnPanel var1 = new OneColumnPanel().nonOpaque().withInsets(0, 5, 10, 10);
      if (this.delimQualifierPanel != null) {
         var1.noFill(this.delimQualifierPanel.getUi());
      }

      var1.add(TextStyles.InfoTextHeader.makeLabel(RESOURCES.getString("Title.FieldTable")));
      var1.fillOut(this.segmentTable.getDisplayer());
      BorderLayoutBuilder.from(this.getUi()).center(var1);
   }

   @Override
   public void stopEditing() {
      this.segmentTable.stopEditing();
   }

   @Override
   public void validateInput() throws InputPanelException {
      try {
         this.segmentModel.validateModel();
      } catch (TextDocumentDefinitionException var2) {
         throw new InputPanelException(var2.getMessage());
      }

      super.validateInput();
   }

   @Override
   public void applyToTextDocument(TextDocument var1) {
      this.applyDelimitersAndQualifiers(var1);
      this.applyStructure(var1);
   }

   private void applyDelimitersAndQualifiers(TextDocument var1) {
      if (this.delimQualifierPanel != null) {
         this.delimQualifierPanel.applyToTextDocument(var1);
      }
   }

   private void applyStructure(TextDocument var1) {
      ComplexTextStructure var2 = this.getTextStructure(var1);
      this.addSegmentInformation(var1, var2);
   }

   private ComplexTextStructure getTextStructure(TextDocument var1) {
      TextStructure var2 = var1.getDataStructure();
      if (var2 == null) {
         var2 = var1.createTextStructure();
      }

      if (var2 instanceof ComplexTextStructure) {
         return (ComplexTextStructure)var2;
      } else {
         throw new IllegalStateException("Simple text structure encountered where a complex structure was expected.");
      }
   }

   private void addSegmentInformation(TextDocument var1, ComplexTextStructure var2) {
      var2.removeAllSegments();

      for (Segment var4 : this.segmentModel.getSegments()) {
         var2.addSegment(var4);
      }

      var1.documentStructureChanged();
   }

   private class TableListener implements PropertyChangeListener {
      private TableListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         if (ComplexTextPanel.this.isSendingInputChangeEvents()) {
            ComplexTextPanel.this.fireInputChanged(ComplexTextPanel.this.segmentTable, "Table");
         }
      }
   }
}
