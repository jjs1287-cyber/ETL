package org.etlforge.ui.util.find;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.util.Interval;
import org.etlforge.util.IntervalFactory;

public final class SearchAndReplaceTextComponentTarget implements SearchAndReplaceTarget {
   private final JTextComponent textComponent;
   private final SearchAndReplaceTextComponentTarget.TextComponentListener textListener;
   private SearchAndReplaceTextTarget worker;
   private TextSearchParameters currentParams;
   private int offset;

   public SearchAndReplaceTextComponentTarget(JTextComponent var1) {
      this.textComponent = var1;
      this.textListener = new SearchAndReplaceTextComponentTarget.TextComponentListener();
      var1.getDocument().addDocumentListener(this.textListener);
      var1.addCaretListener(this.textListener);
   }

   @Override
   public SearchTarget.Match nextMatch(TextSearchParameters var1) {
      SearchAndReplaceTextTarget var2 = this.getWorker(var1);
      TextSearchMatch var3 = var2.nextMatch(var1);
      if (var3 == null && var1.isWrap()) {
         var3 = this.wrapSearch(var1);
      }

      if (var3 != null) {
         this.highlightMatch(var3);
         return var3.shiftStart(this.offset);
      } else {
         return null;
      }
   }

   private SearchAndReplaceTextTarget getWorker(TextSearchParameters var1) {
      if (this.isNewWorkerNeeded(var1)) {
         this.worker = this.createNewWorker(var1);
         this.currentParams = var1;
      }

      return this.worker;
   }

   private boolean isNewWorkerNeeded(TextSearchParameters var1) {
      if (this.worker == null) {
         return true;
      }

      boolean var2 = Objects.equal(var1, this.currentParams);
      return !var2;
   }

   private SearchAndReplaceTextTarget createNewWorker(TextSearchParameters var1) {
      String var2 = this.getTextToSearch(var1);
      return new SearchAndReplaceTextTarget(var2);
   }

   private String getTextToSearch(TextSearchParameters var1) {
      Document var2 = this.textComponent.getDocument();
      if (var1.getDirection() == SearchDirection.DOWN) {
         this.offset = this.textComponent.getSelectionEnd();
         return DocumentUtils.getText(var2, this.offset, var2.getLength() - this.offset);
      } else {
         this.offset = 0;
         int var3 = this.textComponent.getSelectionStart();
         return DocumentUtils.getText(var2, 0, var3);
      }
   }

   private TextSearchMatch wrapSearch(TextSearchParameters var1) {
      this.worker = this.getWorkerForWrappedSearch();
      return this.worker.nextMatch(var1);
   }

   private SearchAndReplaceTextTarget getWorkerForWrappedSearch() {
      this.offset = 0;
      Document var1 = this.textComponent.getDocument();
      String var2 = DocumentUtils.getText(var1, 0, var1.getLength());
      return new SearchAndReplaceTextTarget(var2);
   }

   private void highlightMatch(Interval var1) {
      int var2 = var1.startsAt() + this.offset;
      int var3 = var2 + var1.length();
      this.textListener.blocked = true;
      this.textComponent.setCaretPosition(var2);
      this.textComponent.moveCaretPosition(var3);
      this.textListener.blocked = false;
   }

   @Override
   public void replace(SearchTarget.Match var1, String var2) {
      TextSearchMatch var3 = ((TextSearchMatch)var1).shiftStart(-this.offset);
      this.highlightMatch(var3);
      this.textListener.blocked = true;
      this.textComponent.replaceSelection(var2);
      if (this.offset > 0) {
         this.offset = this.offset + (var2.length() - var3.length());
         if (this.offset < 0) {
            this.offset = 0;
         }
      }

      this.textListener.blocked = false;
   }

   @Override
   public List<TextSearchMatch> replaceAll(TextSearchParameters var1) {
      this.currentParams = var1.newDirection(SearchDirection.DOWN);
      String var2 = this.textComponent.getText();
      this.worker = new SearchAndReplaceTextTarget(var2);
      this.offset = 0;
      List var3 = this.worker.replaceAll(this.currentParams);
      String var4 = this.worker.getText();
      if (!var4.equals(var2)) {
         this.textComponent.setText(var4);
         TextSearchMatch var5 = (TextSearchMatch)var3.get(var3.size() - 1);
         int var6 = var1.getReplaceString().length();
         Interval var7 = IntervalFactory.fromStartAndLength(var5.startsAt(), var6);
         this.highlightMatch(var7);
      }

      return this.offset == 0 ? var3 : this.applyOffset(var3);
   }

   private List<TextSearchMatch> applyOffset(List<TextSearchMatch> var1) {
      ArrayList var2 = Lists.newArrayList();

      for (TextSearchMatch var4 : var1) {
         var2.add(var4.shiftStart(this.offset));
      }

      return var2;
   }

   private class TextComponentListener extends DocumentContentChangeListener implements CaretListener {
      public boolean blocked;

      private TextComponentListener() {
      }

      @Override
      public void caretUpdate(CaretEvent var1) {
         this.updateTarget();
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         this.updateTarget();
      }

      private void updateTarget() {
         if (!this.blocked) {
            SearchAndReplaceTextComponentTarget.this.worker = null;
         }
      }
   }
}
