package org.etlforge.xml;

import java.io.IOException;
import java.io.Writer;
import org.w3c.dom.Document;

public interface DomBuilder {
   void addAttribute(String var1, String var2);

   DomBuilder addChild(String var1);

   void addValue(String var1);

   Document getDocument();

   void writeXml(Writer var1) throws IOException;
}
