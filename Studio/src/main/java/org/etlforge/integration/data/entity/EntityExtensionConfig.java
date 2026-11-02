package org.etlforge.integration.data.entity;

import java.lang.reflect.Constructor;
import org.etlforge.application.extension.ExtensionObjectConstructor;
import org.etlforge.application.extension.ExtensionObjectConstructorFactory;
import org.etlforge.application.extension.ExtensionObjectFactory;
import org.etlforge.application.extension.ExtensionObjectService;
import org.etlforge.xml.sax.EndElementHandler;
import org.etlforge.xml.sax.KongaSaxParser;
import org.etlforge.xml.sax.RelativePathRoot;

public final class EntityExtensionConfig {
   private final ExtensionObjectService service;

   public EntityExtensionConfig(ExtensionObjectService var1) {
      this.service = var1;
   }

   public void loadDefaultConfig() throws Exception {
      this.loadConfig("Data.EntityExtensions.xml");
   }

   public void loadConfig(String var1) throws Exception {
      KongaSaxParser var2 = new KongaSaxParser(new EntityExtensionConfig.ExtensionParser());
      var2.parseResource(var1);
   }

   @RelativePathRoot("IntegrationEntityExtensions/Extension")
   private class ExtensionParser {
      private Class type;
      private ExtensionObjectFactory factory;

      private ExtensionParser() {
      }

      @EndElementHandler("Type")
      public void type(String var1) throws ClassNotFoundException {
         this.type = Class.forName(var1);
      }

      @EndElementHandler("Factory/Class")
      public void factoryClass(String var1) throws Exception {
         this.factory = this.createFactory(var1);
      }

      private ExtensionObjectFactory createFactory(String var1) throws Exception {
         Class var2 = Class.forName(var1);

         for (Constructor var6 : var2.getConstructors()) {
            if (var6.isAnnotationPresent(ExtensionObjectConstructor.class)) {
               return new ExtensionObjectConstructorFactory(this.type, var6);
            }
         }

         return ExtensionObjectFactory.class.cast(var2.newInstance());
      }

      @EndElementHandler("Factory/Target")
      public void target(String var1) throws ClassNotFoundException {
         Class var2 = Class.forName(var1);
         EntityExtensionConfig.this.service.registerFactory(var2, this.factory);
      }
   }
}
