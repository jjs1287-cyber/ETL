package org.etlforge.integration.server.engine.connector.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.etlforge.integration.design.util.XmlElementUtils;
import org.etlforge.integration.server.engine.connector.ConnectorExecutionSystem;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;
import org.etlforge.system.Disposable;

public class XmlWriter implements Disposable {
   private final BufferedWriter writer;

   @Override
   public void dispose() {
      ConnectorExecutionSystem.close(this.writer);
   }

   public XmlWriter(String var1) {
      this(new File(var1));
   }

   public XmlWriter(File var1) {
      try {
         this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(var1), XmlUtils.xmlUtf8Encoding));
      } catch (FileNotFoundException var3) {
         throw new ConnectorServerRuntimeException(var3);
      }
   }

   public XmlWriter appendCloseELement(String var1) throws IOException {
      this.writer.append(XmlElementUtils.endElementTag(var1));
      return this;
   }

   public XmlWriter appendStartELement(String var1) throws IOException {
      this.writer.append(XmlElementUtils.startElementTag(var1));
      return this;
   }

   public XmlWriter append(String var1) throws IOException {
      this.writer.append(var1);
      return this;
   }

   XmlWriter append(char var1) throws IOException {
      this.writer.append(var1);
      return this;
   }

   public XmlWriter appendline() throws IOException {
      return this.append('\n');
   }

   public XmlWriter appendFileUtf8(String var1) throws IOException {
      return this.appendFileUtf8(new File(var1));
   }

   public XmlWriter appendFileUtf8(File var1) throws IOException {
      Charset var2 = ConnectorExecutionSystem.ConnectorDefaultCharset_UTF8;
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), var2));
         XmlContentAppender var4 = new XmlContentAppender(var3, this.writer);
         var4.doWork();
      } finally {
         ConnectorExecutionSystem.close(var3);
      }

      return this;
   }

   public XmlWriter appendFileUtf8(String var1, int var2) throws IOException {
      return this.appendFile(var1, var2, ConnectorExecutionSystem.ConnectorDefaultCharset_UTF8);
   }

   private XmlWriter appendFile(String var1, int var2, Charset var3) throws IOException {
      return this.appendFile(new File(var1), var2, var3);
   }

   private XmlWriter appendFile(File var1, int var2, Charset var3) throws IOException {
      BufferedReader var4 = null;

      try {
         var4 = new BufferedReader(new InputStreamReader(new FileInputStream(var1), var3));
         XmlElementsRemovalUtils var5 = new XmlElementsRemovalUtils(var2, var4, this.writer);
         var5.doWork();
      } finally {
         ConnectorExecutionSystem.close(var4);
      }

      return this;
   }
}
