package org.etlforge.integration.json.xml;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.etlforge.io.Indentation;
import org.etlforge.json.ScalarValueType;
import org.etlforge.system.SystemUtils;

final class JsonWriter implements Closeable {
   private Writer writer;
   private Indentation indent;

   public void open(File var1) throws IOException {
      this.open(var1, Charsets.UTF_8);
   }

   public void open(File var1, Charset var2) throws IOException {
      this.open(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), var2)));
   }

   public void open(Writer var1) {
      Preconditions.checkNotNull(var1, "writer");
      this.checkNotOpen();
      this.writer = var1;
      this.indent = new Indentation();
   }

   private void checkNotOpen() {
      Preconditions.checkState(this.writer == null, "Already open");
   }

   @Override
   public void close() throws IOException {
      if (this.writer != null) {
         this.writer.close();
         this.writer = null;
      }
   }

   public void newLine() throws IOException {
      this.writer.write(SystemUtils.LINE_SEPARATOR);
   }

   public void writeIndent() throws IOException {
      this.indent.writeTo(this.writer);
   }

   public void increaseIndent() {
      this.indent = this.indent.increase();
   }

   public void decreaseIndent() {
      this.indent = this.indent.decrease();
   }

   public void openGroup(JsonGroup var1) throws IOException {
      this.writer.write(var1.open);
      this.newLine();
      this.increaseIndent();
      this.writeIndent();
   }

   public void closeGroup(JsonGroup var1) throws IOException {
      this.writer.write(var1.close);
   }

   public void openFieldGroup(String var1, JsonGroup var2) throws IOException {
      this.openField(var1);
      this.openGroup(var2);
   }

   public void openField(String var1) throws IOException {
      this.writer.write("\"" + var1 + "\": ");
   }

   public void writeNullObject(String var1) throws IOException {
      this.writer.write(JsonGroup.OBJECT.open);
      this.writeNullField(var1);
      this.closeGroup(JsonGroup.OBJECT);
   }

   public void writeScalarObject(String var1, ScalarValueType var2, String var3) throws IOException {
      this.writer.write(JsonGroup.OBJECT.open);
      this.writeScalarField(var1, var2, var3);
      this.closeGroup(JsonGroup.OBJECT);
   }

   public void writeNullField(String var1) throws IOException {
      this.writer.write("\"" + var1 + "\": null");
   }

   public void writeScalarField(String var1, ScalarValueType var2, String var3) throws IOException {
      String var4 = this.getScalarValueToOutput(var2, var3);
      this.writer.write("\"" + var1 + "\": " + var4);
   }

   private String getScalarValueToOutput(ScalarValueType var1, String var2) {
      String var3;
      if (var1 != ScalarValueType.NULL && var2 != null) {
         var3 = var1.format(var2);
      } else {
         var3 = "null";
      }

      return var3;
   }

   public void writeNullArrayItem() throws IOException {
      this.writer.write("null");
   }

   public void writeScalarArrayItem(ScalarValueType var1, String var2) throws IOException {
      String var3 = this.getScalarValueToOutput(var1, var2);
      this.writer.write(var3);
   }

   public void insertFieldOrItemSeparator() throws IOException {
      this.writer.write(",");
      this.newLine();
      this.writeIndent();
   }
}
