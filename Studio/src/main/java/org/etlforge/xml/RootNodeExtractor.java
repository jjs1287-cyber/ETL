package org.etlforge.xml;

import java.io.File;
import java.io.Reader;
import org.w3c.dom.Element;

public abstract class RootNodeExtractor {
   private static RootNodeExtractor domImplementation;
   private static RootNodeExtractor saxImplementation;

   public abstract Element extractRoot(File var1) throws KongaXmlException;

   public abstract Element extractRoot(Reader var1) throws KongaXmlException;

   public static synchronized RootNodeExtractor getDomExtractor() {
      if (domImplementation == null) {
         domImplementation = new DomRootNodeExtractor();
      }

      return domImplementation;
   }

   public static synchronized RootNodeExtractor getSaxExtractor() {
      if (saxImplementation == null) {
         saxImplementation = new SaxRootNodeExtractor();
      }

      return saxImplementation;
   }
}
