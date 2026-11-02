package org.etlforge.application.extension;

import java.lang.reflect.Constructor;

public final class ExtensionObjectConstructorFactory implements ExtensionObjectFactory {
   private final Class extensionType;
   private final Constructor constructor;

   public ExtensionObjectConstructorFactory(Class var1, Constructor var2) {
      this.extensionType = var1;
      this.constructor = var2;
   }

   @Override
   public Class[] getExtensionTypes() {
      return new Class[]{this.extensionType};
   }

   @Override
   public Object getExtensionObject(Object var1, Class var2) {
      if (var2.equals(this.extensionType)) {
         try {
            return this.constructor.newInstance(var1);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return null;
   }
}
