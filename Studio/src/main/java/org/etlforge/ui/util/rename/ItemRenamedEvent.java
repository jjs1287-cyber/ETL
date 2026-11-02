package org.etlforge.ui.util.rename;

import java.util.EventObject;

public class ItemRenamedEvent extends EventObject {
   private Object m_item;
   private String m_sNewName;
   private String m_sOldName;

   public ItemRenamedEvent(Object var1, Object var2, String var3, String var4) {
      super(var1);
      this.m_item = var2;
      this.m_sNewName = var3;
      this.m_sOldName = var4;
   }

   public Object getItem() {
      return this.m_item;
   }

   public String getNewName() {
      return this.m_sNewName;
   }

   public String getOldName() {
      return this.m_sOldName;
   }
}
