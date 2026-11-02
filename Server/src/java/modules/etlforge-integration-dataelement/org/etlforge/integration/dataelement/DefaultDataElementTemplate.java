package org.etlforge.integration.dataelement;

public class DefaultDataElementTemplate implements DataElementTemplate {
   private Object defaultValue;
   private String description;
   private String name;
   private DataElementType type = DataElementType.UNDEFINED;

   public DefaultDataElementTemplate(String var1) {
      this.name = var1;
   }

   public void setDefaultValue(Object var1) {
      this.defaultValue = var1;
   }

   public void setDescription(String var1) {
      this.description = var1;
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public void setType(DataElementType var1) {
      this.type = var1;
   }

   @Override
   public DataElementType getType() {
      return this.type;
   }

   @Override
   public Object getDefaultValue() {
      return this.defaultValue;
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public String getName() {
      return this.name;
   }

   @Override
   public String toString() {
      String var1 = this.defaultValue == null ? "<undefined>" : this.defaultValue.toString();
      return this.name + " (Def: " + (var1.length() > 20 ? var1.substring(0, 17) + "..." : var1) + ")";
   }
}
