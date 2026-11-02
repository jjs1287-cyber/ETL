package org.etlforge.xml.factory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.etlforge.lang.StringConverter;
import org.w3c.dom.Element;

public final class Attribute {
   static final Function<Attribute, Class> TO_VALUE_TYPE = new Function<Attribute, Class>() {
      public Class apply(Attribute var1) {
         return var1.getValueType();
      }
   };
   private final String name;
   private boolean required = true;
   private Class valueType = String.class;
   private String defVal;
   private StringConverter generator = StringConverter.IDENTITY;

   public static Attribute name(String var0) {
      Preconditions.checkArgument(StringUtils.isNotBlank(var0), "name may not be null or blank");
      return new Attribute(var0);
   }

   private Attribute(String var1) {
      this.name = var1;
   }

   public Attribute required(boolean var1) {
      this.required = var1;
      return this;
   }

   public Attribute valueType(Class var1) {
      Preconditions.checkNotNull(var1, "type");
      this.valueType = var1;
      return this;
   }

   public Attribute defaultValue(String var1) {
      this.defVal = var1;
      return this;
   }

   public Attribute generator(StringConverter var1) {
      this.generator = var1;
      return this;
   }

   public Object evaluate(Element var1) {
      String var2 = this.getValue(var1);
      if (var2 == null) {
         if (this.required) {
            throw new RuntimeException("Missing attribute " + this.name + " in element " + var1.getNodeName());
         } else {
            return null;
         }
      } else {
         StringConverter var3 = this.getGenerator();
         if (var3 == null) {
            throw new RuntimeException("I do not know how to convert the value of attribute " + this.name + " to " + this.valueType.getName());
         } else {
            return var3.apply(var2);
         }
      }
   }

   Class getValueType() {
      return this.valueType;
   }

   private String getValue(Element var1) {
      String var2 = var1.getAttribute(this.name);
      return var2 != null ? var2 : this.defVal;
   }

   private StringConverter<?> getGenerator() {
      return this.generator != null ? this.generator : StringConverter.builtIn(this.valueType);
   }
}
