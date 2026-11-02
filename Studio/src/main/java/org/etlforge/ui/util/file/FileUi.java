package org.etlforge.ui.util.file;

import com.google.common.base.Preconditions;
import java.io.File;
import javax.swing.Icon;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringFactory;
import org.etlforge.ui.util.decorator.ListDecor;
import org.etlforge.ui.util.decorator.ListDecorator;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelDecorator;

public final class FileUi implements LabelDecorator, ListDecorator {
   private static final MnemonicStringFactory MNEMONIC_FACTORY = MnemonicStringFactory.NO_MNEMONIC_FACTORY;
   private final File root;
   private final File file;
   private final boolean rootIsAncestor;
   private String viewableString;

   public FileUi(File var1) {
      this(var1, null);
   }

   public FileUi(File var1, File var2) {
      Preconditions.checkNotNull(var1, "f");
      this.file = var1;
      this.root = var2;
      this.rootIsAncestor = isRootAncestor(var1, var2);
   }

   private static boolean isRootAncestor(File var0, File var1) {
      if (var1 == null) {
         return false;
      }

      File var2 = var0.getParentFile();

      while (var2 != null && !var2.equals(var1)) {
         var2 = var2.getParentFile();
      }

      return var2 != null && var2.equals(var1);
   }

   public File getFile() {
      return this.file;
   }

   @Override
   public void decorate(ListDecor var1, boolean var2) {
      var1.setIcon(this.getIcon());
      var1.setText(this.getViewableString());
      var1.setToolTip(this.file.getAbsolutePath());
   }

   @Override
   public void decorate(Label var1) {
      var1.setIcon(this.getIcon());
      MnemonicString var2 = MNEMONIC_FACTORY.fromString(this.getViewableString());
      var1.setText(var2);
   }

   protected String getViewableString() {
      if (this.viewableString == null) {
         String var1 = this.file.getAbsolutePath();
         if (this.rootIsAncestor) {
            String var2 = this.root.getAbsolutePath();
            this.viewableString = var1.substring(var2.length() + 1);
         } else {
            this.viewableString = var1;
         }
      }

      return this.viewableString;
   }

   protected Icon getIcon() {
      return null;
   }

   @Override
   public String toString() {
      return this.getViewableString();
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof FileUi ? this.file.equals(((FileUi)var1).file) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.file.hashCode();
   }
}
