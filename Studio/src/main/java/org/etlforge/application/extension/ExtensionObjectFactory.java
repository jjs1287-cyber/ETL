package org.etlforge.application.extension;

public interface ExtensionObjectFactory {
   Class[] getExtensionTypes();

   Object getExtensionObject(Object var1, Class var2);
}
