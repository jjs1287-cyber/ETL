package org.etlforge.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.basic.BasicFileChooserUI;

public class KongaFileChooser extends JFileChooser {
   private Predicate<File> filter = Predicates.alwaysTrue();

   public KongaFileChooser() {
   }

   public KongaFileChooser(File var1, FileSystemView var2) {
      super(var1, var2);
   }

   public KongaFileChooser(File var1) {
      super(var1);
   }

   public KongaFileChooser(FileSystemView var1) {
      super(var1);
   }

   public KongaFileChooser(String var1, FileSystemView var2) {
      super(var1, var2);
   }

   public KongaFileChooser(String var1) {
      super(var1);
   }

   @Override
   public final void setSelectedFile(File var1) {
      super.setSelectedFile(var1);
      this.applyWorkAroundForBug4893572(var1);
   }

   private void applyWorkAroundForBug4893572(File var1) {
      if (this.getUI() instanceof BasicFileChooserUI) {
         BasicFileChooserUI var2 = (BasicFileChooserUI)this.getUI();
         if (var1 != null) {
            var2.setFileName(var1.getName());
         }
      }
   }

   public final void setApprovalFilter(Predicate<File> var1) {
      this.filter = Preconditions.checkNotNull(var1);
   }

   @Override
   public final void approveSelection() {
      if (this.isSelectedFileApproved()) {
         super.approveSelection();
      }
   }

   private boolean isSelectedFileApproved() {
      File var1 = this.getSelectedFile();
      return this.filter.apply(var1);
   }
}
