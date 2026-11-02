package org.etlforge.application.ui.widget;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import javax.swing.JTextArea;
import org.etlforge.application.ui.job.KongaSwingWorker;

public final class FileContentDisplayer extends KongaSwingWorker<List<String>, Void> {
   private final File file;
   private final JTextArea textArea;
   private volatile int numberOfLinesToDisplay = 5;
   private volatile Charset charset = Charset.defaultCharset();

   public FileContentDisplayer(File var1, JTextArea var2) {
      this.file = Preconditions.checkNotNull(var1, "file");
      this.textArea = Preconditions.checkNotNull(var2, "textArea");
   }

   public void setCharsetName(Charset var1) {
      this.charset = Preconditions.checkNotNull(var1);
   }

   public void setNumberOfLinesToDisplay(int var1) {
      Preconditions.checkArgument(this.numberOfLinesToDisplay > 0, "numberOfLinesToDisplay must be > 0 (was " + this.numberOfLinesToDisplay + ")");
      this.numberOfLinesToDisplay = var1;
   }

   protected List<String> doInBackground() throws Exception {
      return Files.readLines(this.file, this.charset, new LineProcessor<List<String>>() {
         private final List<String> lines = Lists.newArrayListWithExpectedSize(FileContentDisplayer.this.numberOfLinesToDisplay + 1);

         @Override
         public boolean processLine(String var1) throws IOException {
            if (FileContentDisplayer.this.isCancelled()) {
               return false;
            } else {
               this.lines.add(var1);
               if (this.lines.size() == FileContentDisplayer.this.numberOfLinesToDisplay) {
                  this.lines.add("...");
                  return false;
               } else {
                  return true;
               }
            }
         }

         public List<String> getResult() {
            return this.lines;
         }
      });
   }

   protected void onSuccess(List<String> var1) {
      if (!this.isCancelled()) {
         this.textArea.setText("");

         for (String var3 : var1) {
            this.textArea.append(var3);
            this.textArea.append("\n");
         }

         this.textArea.setCaretPosition(0);
      }
   }
}
