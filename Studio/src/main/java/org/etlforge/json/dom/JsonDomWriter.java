package org.etlforge.json.dom;

import com.google.common.base.Charsets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.etlforge.io.Indentation;
import org.etlforge.system.SystemUtils;

public final class JsonDomWriter {
   private static final String NEW_LINE = SystemUtils.LINE_SEPARATOR;
   private Indentation indent;
   private Writer writer;

   public static void writeToFile(RootNode var0, File var1) throws IOException {
      writeToFile(var0, var1, Charsets.UTF_8);
   }

   public static void writeToFile(RootNode var0, File var1, Charset var2) throws IOException {
      BufferedWriter var3 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), var2));

      try {
         JsonDomWriter var4 = new JsonDomWriter();
         var4.write(var0, var3);
      } finally {
         var3.close();
      }
   }

   public void write(RootNode var1, Writer var2) throws IOException {
      this.writer = var2;
      this.indent = new Indentation();
      this.writeImpl(var1);
   }

   private void writeImpl(JsonNode var1) throws IOException {
      if (var1 instanceof ArrayNode) {
         this.writeArrayNode((ArrayNode)var1, true);
      } else if (var1 instanceof ObjectNode) {
         this.writeObjectNode((ObjectNode)var1, true);
      } else if (var1 instanceof FieldNode) {
         this.writeFieldNode((FieldNode)var1);
      } else {
         if (!(var1 instanceof ScalarValueNode)) {
            throw new RuntimeException("Unexpected node: " + (var1 == null ? "null" : var1.getClass()));
         }

         this.writeScalar((ScalarValueNode)var1, true);
      }
   }

   private void writeArrayNode(ArrayNode var1, boolean var2) throws IOException {
      this.writeArrayOrObjectNode("[", "]", var2, var1.getChildren());
   }

   private void writeArrayOrObjectNode(String var1, String var2, boolean var3, Iterable<? extends JsonNode> var4) throws IOException {
      if (var3) {
         this.writeWithIndent(var1);
      } else {
         this.writer.write(var1);
      }

      this.increaseIndent();
      boolean var5 = true;

      for (JsonNode var7 : var4) {
         if (var5) {
            var5 = false;
         } else {
            this.writer.write(",");
         }

         this.newLine();
         this.writeImpl(var7);
      }

      this.newLine();
      this.decreaseIndent();
      this.writeWithIndent(var2);
   }

   private void writeWithIndent(String var1) throws IOException {
      this.indent();
      this.writer.write(var1);
   }

   private void indent() throws IOException {
      this.indent.writeTo(this.writer);
   }

   private void newLine() throws IOException {
      this.writer.write(NEW_LINE);
   }

   private void increaseIndent() {
      this.indent = this.indent.increase();
   }

   private void decreaseIndent() {
      this.indent = this.indent.decrease();
   }

   private void writeObjectNode(ObjectNode var1, boolean var2) throws IOException {
      this.writeArrayOrObjectNode("{", "}", var2, var1.getChildren());
   }

   private void writeFieldNode(FieldNode var1) throws IOException {
      this.writeWithIndent("\"" + var1.getName() + "\": ");
      ValueNode var2 = var1.getValue();
      if (var2 instanceof ScalarValueNode) {
         this.writeScalar((ScalarValueNode)var2, false);
      } else if (var2 instanceof ArrayNode) {
         this.writeArrayNode((ArrayNode)var2, false);
      } else {
         if (!(var2 instanceof ObjectNode)) {
            throw new RuntimeException("Unexpected value node: " + (var2 == null ? null : var2.getClass()));
         }

         this.writeObjectNode((ObjectNode)var2, false);
      }
   }

   private void writeScalar(ScalarValueNode var1, boolean var2) throws IOException {
      if (var2) {
         this.indent();
      }

      this.writer.write(var1.formatForOutput());
   }
}
