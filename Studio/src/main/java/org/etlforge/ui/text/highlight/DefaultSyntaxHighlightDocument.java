package org.etlforge.ui.text.highlight;

import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.undo.CompoundEdit;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.partition.PartitionHandler;
import org.etlforge.ui.text.partition.TextPartitioner;

public class DefaultSyntaxHighlightDocument extends DefaultStyledDocument implements SyntaxHighlightDocument {
   private final TokenStyles styles;
   private final DefaultSyntaxHighlightDocument.UndoRedoListener undoRedoListener;
   private TextPartitioner partitioner;
   private PartitionHandler damageHandler;
   private CompoundEdit edits;
   private boolean doDamage;
   private boolean undoRedo;

   public DefaultSyntaxHighlightDocument(TokenStyles var1) {
      this.styles = var1;
      this.undoRedoListener = new DefaultSyntaxHighlightDocument.UndoRedoListener();
      this.doDamage = true;
   }

   @Override
   public void setPartitioner(TextPartitioner var1) {
      if (this.partitioner != null) {
         this.partitioner.setDocument(null);
         this.partitioner.removePartitionHandler(this.damageHandler);
         this.removeDocumentListener(this.undoRedoListener);
      }

      if (this.damageHandler == null) {
         this.damageHandler = new DefaultSyntaxHighlightDocument.DamageImpl();
      }

      this.partitioner = var1;
      if (var1 != null) {
         var1.addPartitionHandler(this.damageHandler);
         this.undoRedo = false;
         var1.setDocument(this);
         this.undoRedo = true;
         this.addDocumentListener(this.undoRedoListener);
      }
   }

   @Override
   public void replace(int var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
      this.undoRedo = false;
      this.doDamage = false;
      super.replace(var1, var2, var3, var4);
      this.undoRedo = false;
      if (this.partitioner != null) {
         this.partitioner.handleReplace(var1, var2, var3);
      }

      this.sendCompoundEdit();
      this.doDamage = true;
      this.undoRedo = true;
   }

   private void sendCompoundEdit() {
      if (this.edits != null) {
         this.edits.end();
         UndoableEditEvent var1 = new UndoableEditEvent(this, this.edits);
         super.fireUndoableEditUpdate(var1);
         this.edits = null;
      }
   }

   @Override
   public void insertString(int var1, String var2, AttributeSet var3) throws BadLocationException {
      this.undoRedo = false;
      super.insertString(var1, var2, var3);
      if (this.doDamage) {
         if (this.partitioner != null) {
            this.partitioner.handleInsert(var1, var2);
         }

         this.sendCompoundEdit();
      }

      this.undoRedo = true;
   }

   @Override
   public void remove(int var1, int var2) throws BadLocationException {
      this.undoRedo = false;
      String var3 = DocumentUtils.getText(this, var1, var2);
      super.remove(var1, var2);
      if (this.doDamage) {
         if (this.partitioner != null) {
            this.partitioner.handleRemoval(var1, var2, var3);
         }

         this.sendCompoundEdit();
      }

      this.undoRedo = true;
   }

   @Override
   protected void fireUndoableEditUpdate(UndoableEditEvent var1) {
      if (this.partitioner != null) {
         if (this.edits == null) {
            this.edits = new CompoundEdit();
         }

         this.edits.addEdit(var1.getEdit());
      }
   }

   public void discardAllPendingEdits() {
      this.edits = null;
   }

   private class DamageImpl implements PartitionHandler {
      private DamageImpl() {
      }

      @Override
      public void handle(List<TextPartitioner.Partition> var1) {
         if (!DefaultSyntaxHighlightDocument.this.undoRedo && !var1.isEmpty()) {
            int var2 = ((TextPartitioner.Partition)var1.get(0)).offset;
            this.doColoring(var2, var1);
         }
      }

      private void doColoring(int var1, List<TextPartitioner.Partition> var2) {
         for (TextPartitioner.Partition var4 : var2) {
            AttributeSet var5 = DefaultSyntaxHighlightDocument.this.styles.getStyle(var4.token);
            if (var5 != null) {
               DefaultSyntaxHighlightDocument.this.setCharacterAttributes(var4.offset, var4.endsAt - var4.offset, var5, true);
            }
         }
      }
   }

   private class UndoRedoListener implements DocumentListener {
      private UndoRedoListener() {
      }

      @Override
      public void insertUpdate(DocumentEvent var1) {
         if (DefaultSyntaxHighlightDocument.this.undoRedo && DefaultSyntaxHighlightDocument.this.partitioner != null) {
            int var2 = var1.getOffset();
            int var3 = var1.getLength();
            String var4 = DocumentUtils.getText(var1.getDocument(), var2, var3);
            DefaultSyntaxHighlightDocument.this.partitioner.handleInsert(var2, var4);
         }
      }

      @Override
      public void removeUpdate(DocumentEvent var1) {
         if (DefaultSyntaxHighlightDocument.this.undoRedo && DefaultSyntaxHighlightDocument.this.partitioner != null) {
            int var2 = var1.getOffset();
            int var3 = var1.getLength();
            DefaultSyntaxHighlightDocument.this.partitioner.handleRemoval(var2, var3, null);
         }
      }

      @Override
      public void changedUpdate(DocumentEvent var1) {
      }
   }
}
