package org.etlforge.integration.mscrm.query;

import com.google.common.base.Preconditions;
import java.util.zip.DataFormatException;
import org.etlforge.system.ArgCheck;
import org.etlforge.util.name.Nameable;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class FetchXml implements Nameable {
   private final String name;
   private CompressedAndBase64EncodedString compressedXml;
   private String xml;
   private final Object lock = new Object();

   public static FetchXml compressed(String var0, CompressedAndBase64EncodedString var1) {
      return new FetchXml(var0, var1);
   }

   private FetchXml(String var1, CompressedAndBase64EncodedString var2) {
      this.name = ArgCheck.checkNotEmpty(var1, "name");
      this.compressedXml = Preconditions.checkNotNull(var2, "compressedXml");
   }

   public FetchXml(String var1, String var2) {
      this.name = ArgCheck.checkNotEmpty(var1, "name");
      this.xml = ArgCheck.checkNotEmpty(var2, "xml");
   }

   @Override
   public String getName() {
      return this.name;
   }

   public String getXml() {
      try {
         synchronized (this.lock) {
            if (this.xml == null) {
               this.xml = this.compressedXml.inflate();
               this.compressedXml = null;
            }

            return this.xml;
         }
      } catch (DataFormatException var4) {
         throw new RuntimeException(var4);
      }
   }

   public Document parseWithDom() throws SAXException {
      return DomParser.parseXmlString(this.getXml());
   }

   public Element getRootElement() throws SAXException {
      Document var1 = this.parseWithDom();
      return var1.getDocumentElement();
   }
}
