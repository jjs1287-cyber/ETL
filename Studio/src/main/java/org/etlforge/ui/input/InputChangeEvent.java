package org.etlforge.ui.input;

import java.util.EventObject;

public class InputChangeEvent extends EventObject {
   private Object m_field;
   private String m_fieldName;
   private Object m_oldValue;
   private Object m_newValue;

   public InputChangeEvent(Object var1, String var2) {
      this(var1, null, var2, null, null);
   }

   public InputChangeEvent(Object var1, Object var2, String var3) {
      this(var1, var2, var3, null, null);
   }

   public InputChangeEvent(Object var1, Object var2, String var3, Object var4, Object var5) {
      super(var1);
      this.m_field = var2;
      this.m_fieldName = var3;
      this.m_oldValue = var4;
      this.m_newValue = var5;
   }

   public Object getField() {
      return this.m_field;
   }

   public String getFieldName() {
      return this.m_fieldName;
   }

   public Object getOldValue() {
      return this.m_oldValue;
   }

   public Object getNewValue() {
      return this.m_newValue;
   }
}
