package org.etlforge.ui.util.file;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.io.FileUtils;
import org.etlforge.io.Folder;
import org.etlforge.lang.Arrays2;
import org.etlforge.lang.KongaStringUtils;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.contentassist.CompletionContext;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;
import org.etlforge.util.file.FileComparators;

public final class FilePathCompletionContext implements CompletionContext {
   private final FileFilter filter;
   private final boolean caseSensitive;

   public FilePathCompletionContext() {
      this(FileUtils.ACCEPT_ALL);
   }

   public FilePathCompletionContext(FileFilter var1) {
      this.filter = var1;
      this.caseSensitive = !SystemUtils.IS_OS_WINDOWS;
   }

   @Override
   public boolean delete(CompletionToken var1) {
      return false;
   }

   @Override
   public CompletionToken[] getAvailableTokens(Document var1, int var2) {
      String var3 = DocumentUtils.getText(var1, 0, var2);
      if (var3.contains(File.separator)) {
         File[] var4 = this.getAvailablePaths(var3);
         if (var4.length > 0) {
            return Arrays2.transform(var4, CompletionToken.class, new FilePathCompletionContext.TokenFactory(var2));
         }
      }

      return new CompletionToken[0];
   }

   private File[] getAvailablePaths(String var1) {
      List var2 = this.collectMatchingFilePaths(var1);
      this.removeSingleMatchIfIdenticalToPreamble(var2, var1);
      Collections.sort(var2, FileComparators.FOLDERS_FIRST);
      return (File[]) var2.toArray(new File[var2.size()]);
   }

   private List<File> collectMatchingFilePaths(String var1) {
      String var2 = StringUtils.substringBeforeLast(var1, File.separator) + File.separator;
      Folder var3 = new Folder(var2);
      File[] var4 = var3.listFiles(this.filter);
      ArrayList var5 = Lists.newArrayList();
      String var6 = StringUtils.substringAfterLast(var1, File.separator);

      for (File var10 : var4) {
         if (this.isMatch(var6, var10)) {
            var5.add(var10);
         }
      }

      return var5;
   }

   private void removeSingleMatchIfIdenticalToPreamble(List<File> var1, String var2) {
      if (var1.size() == 1) {
         File var3 = (File)var1.get(0);
         if (var3.getAbsolutePath().equals(var2)) {
            var1.remove(0);
         }
      }
   }

   private boolean isMatch(String var1, File var2) {
      return this.caseSensitive ? var2.getName().startsWith(var1) : StringUtils.startsWithIgnoreCase(var2.getName(), var1);
   }

   private static final class TokenFactory implements Function<File, CompletionToken> {
      private final int offset;
      private final FileSystemView view;

      public TokenFactory(int var1) {
         this.offset = var1;
         this.view = FileSystemView.getFileSystemView();
      }

      public CompletionToken apply(File var1) {
         String var2 = this.getPath(var1);
         DefaultCompletionToken var3 = new DefaultCompletionToken(var2, 0, this.offset);
         var3.setIcon(this.view.getSystemIcon(var1));
         return var3;
      }

      private String getPath(File var1) {
         String var2 = var1.getAbsolutePath();
         if (var1.isDirectory()) {
            var2 = KongaStringUtils.ensureEndsWith(var2, File.separator);
         }

         return var2;
      }
   }
}
