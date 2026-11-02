package org.etlforge.xml;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.OutputSupplier;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.etlforge.system.ArgCheck;
import org.w3c.dom.Node;

public final class XmlPrettyPrinter {
   private final Integer indent;

   public static XmlPrettyPrinter withIndent(int var0) {
      return new XmlPrettyPrinter(var0);
   }

   public XmlPrettyPrinter(int var1) {
      this.indent = ArgCheck.checkNotNegative(var1, "indent");
   }

   public String prettyPrint(String var1) throws TransformerException {
      StreamSource var2 = new StreamSource(new StringReader(var1));
      StringWriter var3 = new StringWriter(var1.length());
      StreamResult var4 = new StreamResult(var3);
      this.prettyPrint(var2, var4);
      return var3.getBuffer().toString();
   }

   public void prettyPrint(Node var1, Writer var2) throws TransformerException {
      this.prettyPrint(new DOMSource(var1), new StreamResult(var2));
   }

   public String prettyPrintToString(Node var1) throws TransformerException {
      StringWriter var2 = new StringWriter();
      this.prettyPrint(var1, var2);
      return var2.getBuffer().toString();
   }

   public void prettyPrint(File var1, File var2) throws IOException, TransformerException {
      this.prettyPrint(Files.newInputStreamSupplier(var1), Files.newOutputStreamSupplier(var2));
   }

   public void prettyPrint(InputSupplier<? extends InputStream> var1, OutputSupplier<? extends OutputStream> var2) throws IOException, TransformerException {
      InputStream var3 = (InputStream)var1.getInput();
      OutputStream var4 = null;

      try {
         var4 = (OutputStream)var2.getOutput();
         this.prettyPrint(new StreamSource(var3), new StreamResult(var4));
      } finally {
         KongaXmlUtils.close(var3);
         KongaXmlUtils.close(var4);
      }
   }

   private void prettyPrint(Source var1, Result var2) throws TransformerException {
      TransformerFactory var3 = this.createTransformerFactory();
      Transformer var4 = this.createTransform(var3);
      var4.transform(var1, var2);
   }

   private TransformerFactory createTransformerFactory() throws TransformerFactoryConfigurationError {
      TransformerFactory var1 = TransformerFactory.newInstance();

      try {
         var1.setAttribute("indent-number", this.indent);
      } catch (IllegalArgumentException var3) {
      }

      return var1;
   }

   private Transformer createTransform(TransformerFactory var1) throws TransformerConfigurationException {
      Transformer var2 = var1.newTransformer();

      try {
         var2.setOutputProperty("indent", "yes");
         var2.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", this.indent.toString());
      } catch (IllegalArgumentException var4) {
      }

      return var2;
   }
}
