package org.etlforge.xml;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;

public final class XmlFileWriter {
   private final Charset encoding;
   private int indentNumber = 2;

   public XmlFileWriter() {
      this(Charsets.UTF_8);
   }

   public XmlFileWriter(Charset var1) {
      this.encoding = Preconditions.checkNotNull(var1);
   }

   public void setIndentNumber(int var1) {
      this.indentNumber = var1;
   }

   public void write(Document var1, File var2) throws IOException {
      OutputStreamWriter var3 = new OutputStreamWriter(new FileOutputStream(var2), this.encoding);

      try {
         this.write(var1, var3);
      } finally {
         KongaXmlUtils.close(var3);
      }
   }

   public void write(Document var1, Writer var2) {
      try {
         var1.getDocumentElement().normalize();
         XmlPrettyPrinter.withIndent(this.indentNumber).prettyPrint(var1, var2);
      } catch (TransformerException var4) {
         throw new RuntimeException(var4);
      }
   }
}
