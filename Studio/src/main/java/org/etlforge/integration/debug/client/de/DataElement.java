package org.etlforge.integration.debug.client.de;

public final class DataElement {
   private final String name;
   private final DataElementScope scope;
   private String displayName;
   private String type;
   private String value;
   private DataElement[] arrayValues;

   public DataElement(String var1, DataElementScope var2, String var3) {
      this(var1, var2, var3, null);
   }

   public DataElement(String var1, DataElementScope var2, String var3, DataElement[] var4) {
      this.name = var1;
      this.scope = var2;
      this.arrayValues = var4;
      this.type = var3;
      this.displayName = var1;
   }

   public String getName() {
      return this.name;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   public void setDisplayName(String var1) {
      this.displayName = var1;
   }

   public String getTypeName() {
      return this.type;
   }

   public void setTypeName(String var1) {
      this.type = var1;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public DataElementScope getScope() {
      return this.scope;
   }

   public boolean isArray() {
      return this.arrayValues != null;
   }

   public DataElement[] getArrValues() {
      return this.arrayValues;
   }

   public void setArrValues(DataElement[] var1) {
      this.arrayValues = var1 != null ? (DataElement[])var1.clone() : null;
   }

   @Override
   public String toString() {
      return this.name + " [" + this.scope + "] = " + this.value;
   }
}
