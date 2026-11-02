package org.etlforge.integration.structure.crom;

class ExtraAttribute implements Cloneable {
   boolean m_NSQualify;
   String m_Namespace;
   String m_SchemaType;
   String m_SchemaTypeNamespace;

   public ExtraAttribute clone() {
      try {
         return (ExtraAttribute)super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }
}
