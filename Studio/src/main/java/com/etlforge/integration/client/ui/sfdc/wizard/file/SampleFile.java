package com.etlforge.integration.client.ui.sfdc.wizard.file;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.LineProcessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.integration.data.structure.SimpleTextStructure;
import org.etlforge.integration.data.structure.text.FieldDelimiter;
import org.etlforge.integration.data.structure.text.FlatTextFileStructureFactory;
import org.etlforge.integration.data.structure.text.InvalidTemplateException;
import org.etlforge.integration.data.structure.text.Prototype;
import org.etlforge.system.ArgCheck;
import org.etlforge.system.SystemUtils;

public final class SampleFile {
   private final File file;
   private final Charset charset;
   private final ImmutableList<String> top;
   private final boolean complete;
   private final long size;

   public static SampleFile create(File var0, Charset var1) throws IOException {
      return create(var0, var1, 10);
   }

   public static SampleFile create(File var0, Charset var1, int var2) throws IOException {
      ArgCheck.checkPositive(var2, "maxNumberOfLines");
      SampleFile.SampleFileBuilder var3 = new SampleFile.SampleFileBuilder(var0, var1, var2);
      return var3.build();
   }

   public SampleFile(File var1, List<String> var2, boolean var3) {
      this(var1, Charset.defaultCharset(), var2, var3);
   }

   public SampleFile(File var1, Charset var2, List<String> var3, boolean var4) {
      this.file = Preconditions.checkNotNull(var1);
      this.charset = Preconditions.checkNotNull(var2);
      this.top = ImmutableList.copyOf(var3);
      this.complete = var4;
      this.size = var1.length();
   }

   public boolean isComplete() {
      return this.complete;
   }

   public SimpleTextStructure asTextStructure(Prototype var1, FieldDelimiter var2) throws IOException, InvalidTemplateException {
      FlatTextFileStructureFactory var3 = new FlatTextFileStructureFactory()
         .withCharset(this.charset)
         .withPrototype(var1)
         .withDelimiter(var2)
         .doNotInferNumericTypes();
      return var3.createStructure(this.file);
   }

   public File asFile() {
      return this.file;
   }

   public String getPath() {
      return this.file.getAbsolutePath();
   }

   public String getExtension() {
      return Files.getFileExtension(this.file.getName()).toLowerCase();
   }

   public String getTopOfFile() {
      StringBuilder var1 = new StringBuilder();

      for (String var3 : this.top) {
         if (var1.length() > 0) {
            var1.append(SystemUtils.LINE_SEPARATOR);
         }

         var1.append(var3);
      }

      return var1.toString();
   }

   public boolean hasHeader() {
      return !this.isEmpty() && !this.firstLine().isEmpty();
   }

   private String firstLine() {
      return this.top.get(0).trim();
   }

   public List<String> getHeader() {
      if (this.isEmpty()) {
         return null;
      }

      ArrayList var1 = Lists.newArrayList();
      String var2 = this.firstLine();
      String[] var3 = var2.split(",");

      for (String var7 : var3) {
         var1.add(var7.trim());
      }

      return var1;
   }

   public boolean isEmpty() {
      return this.top.isEmpty();
   }

   public InputStreamReader read() throws IOException {
      return new InputStreamReader(new FileInputStream(this.file), this.charset);
   }

   public InputSupplier<FileInputStream> asInputSupplier() {
      return Files.newInputStreamSupplier(this.file);
   }

   public Charset getCharset() {
      return this.charset;
   }

   public boolean isLikelySameAs(SampleFile var1) {
      return this.size == var1.size && this.file.equals(var1.file);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this).addValue(this.file.getAbsolutePath()).toString();
   }

   private static class SampleFileBuilder {
      private final File file;
      private final Charset charset;
      private final int maxNumberOfLines;
      private final List<String> top = Lists.newArrayList();
      private boolean complete;

      public SampleFileBuilder(File var1, Charset var2, int var3) {
         this.file = var1;
         this.charset = var2;
         this.maxNumberOfLines = var3;
      }

      public SampleFile build() throws IOException {
         return Files.readLines(this.file, this.charset, new LineProcessor<SampleFile>() {
            public SampleFile getResult() {
               return new SampleFile(SampleFileBuilder.this.file, SampleFileBuilder.this.charset, SampleFileBuilder.this.top, SampleFileBuilder.this.complete);
            }

            @Override
            public boolean processLine(String var1) {
               if (SampleFileBuilder.this.top.size() > SampleFileBuilder.this.maxNumberOfLines) {
                  SampleFileBuilder.this.complete = false;
                  return false;
               } else {
                  SampleFileBuilder.this.top.add(var1);
                  return true;
               }
            }
         });
      }
   }
}
