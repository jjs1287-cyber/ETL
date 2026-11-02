package com.etlforge.integration.client.ui.sfdc.wizard.file;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import org.etlforge.application.ui.Alert;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.csv.CsvFileTableUi;
import org.etlforge.util.csv.CSVParams;
import org.etlforge.util.csv.CsvFile;

public final class SampleFileTabularViewer implements SampleFileViewer {
   private final CsvFileTableUi table;

   public SampleFileTabularViewer(Component var1, SampleFile var2, CSVParams var3) {
      CsvFile var4 = this.parseFile(var1, var2, var3);
      this.table = new CsvFileTableUi(var4);
   }

   private CsvFile parseFile(Component var1, SampleFile var2, CSVParams var3) {
      try {
         return CsvFile.newFactory().withCsvParams(var3).withEncoding(var2.getCharset()).limitRecordCountTo(10).build(new File(var2.getPath()));
      } catch (IOException var5) {
         Alert.error(var1, "The file could not be read: " + var5.getMessage(), "Error");
         return CsvFile.empty();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.table.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.table.requestFocus();
   }
}
