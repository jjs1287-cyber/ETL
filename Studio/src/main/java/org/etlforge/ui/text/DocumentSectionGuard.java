package org.etlforge.ui.text;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.Position;
import javax.swing.text.DocumentFilter.FilterBypass;

public class DocumentSectionGuard {
   private final AbstractDocument document;
   private final DocumentFilter filter;
   private final List<DocumentSectionGuard.Section> guardedSections = new ArrayList<>();
   private boolean active;

   public DocumentSectionGuard(Document var1) {
      Preconditions.checkArgument(var1 instanceof AbstractDocument, "A DocumentSectionGuard can only be installed in javax.swing.text.AbstractDocuments");
      this.document = (AbstractDocument)var1;
      this.filter = new DocumentSectionGuard.Filter();
      this.document.setDocumentFilter(this.filter);
      this.active = true;
   }

   public boolean isActive() {
      return this.active;
   }

   public void setActive(boolean var1) {
      this.active = var1;
   }

   public final void uninstall() {
      this.document.setDocumentFilter(null);
   }

   public final void clear() {
      this.guardedSections.clear();
   }

   public final void guard(int var1, int var2) throws BadLocationException {
      DocumentSectionGuard.Section var3 = new DocumentSectionGuard.Section(this.document.createPosition(var1 + 1), this.document.createPosition(var2 - 1));
      this.guardedSections.add(var3);
   }

   public final boolean isGuarded(int var1) {
      for (DocumentSectionGuard.Section var3 : this.guardedSections) {
         if (var1 >= var3.start.getOffset() && var1 <= var3.end.getOffset()) {
            return true;
         }
      }

      return false;
   }

   protected void attemptToMutateGuardedSection(int var1, int var2) {
   }

   private class Filter extends DocumentFilter {
      private Filter() {
      }

      @Override
      public void insertString(FilterBypass var1, int var2, String var3, AttributeSet var4) throws BadLocationException {
         if (this.isInsertAllowed(var2)) {
            super.insertString(var1, var2, var3, var4);
         }
      }

      @Override
      public void remove(FilterBypass var1, int var2, int var3) throws BadLocationException {
         if (this.isRemoveAllowed(var2, var3)) {
            super.remove(var1, var2, var3);
         }
      }

      private boolean isRemoveAllowed(int var1, int var2) {
         if (DocumentSectionGuard.this.active) {
            int var3 = var1 + var2;
            return this.isUnprotected(var1, var3);
         } else {
            return true;
         }
      }

      private boolean isUnprotected(int var1, int var2) {
         for (DocumentSectionGuard.Section var4 : DocumentSectionGuard.this.guardedSections) {
            if (var1 <= var4.end.getOffset() && var2 >= var4.start.getOffset()) {
               this.reportEnforcement(var4);
               return false;
            }
         }

         return true;
      }

      @Override
      public void replace(FilterBypass var1, int var2, int var3, String var4, AttributeSet var5) throws BadLocationException {
         if (this.isMutationAllowed(var2, var3)) {
            super.replace(var1, var2, var3, var4, var5);
         }
      }

      private boolean isInsertAllowed(int var1) {
         if (DocumentSectionGuard.this.active) {
            for (DocumentSectionGuard.Section var3 : DocumentSectionGuard.this.guardedSections) {
               if (var1 > var3.start.getOffset() && var1 < var3.end.getOffset()) {
                  this.reportEnforcement(var3);
                  return false;
               }
            }
         }

         return true;
      }

      private boolean isMutationAllowed(int var1, int var2) {
         if (DocumentSectionGuard.this.active) {
            int var3 = var1 + var2;
            return this.isUnprotected(var1, var3);
         } else {
            return true;
         }
      }

      private void reportEnforcement(DocumentSectionGuard.Section var1) {
         DocumentSectionGuard.this.attemptToMutateGuardedSection(var1.start.getOffset() - 1, var1.end.getOffset() + 1);
      }
   }

   private static final class Section {
      public final Position start;
      public final Position end;

      public Section(Position var1, Position var2) {
         this.start = var1;
         this.end = var2;
      }

      @Override
      public String toString() {
         return "[" + this.start + ", " + this.end + "]";
      }
   }
}
