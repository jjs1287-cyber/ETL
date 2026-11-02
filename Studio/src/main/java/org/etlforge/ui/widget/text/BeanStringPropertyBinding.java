package org.etlforge.ui.widget.text;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import javax.swing.text.JTextComponent;

public final class BeanStringPropertyBinding {
   private final Class<?> beanClass;
   private final PropertyDescriptor property;
   private final JTextComponent textComponent;
   private boolean autoTrim = true;

   public BeanStringPropertyBinding(Class<?> var1, String var2, JTextComponent var3) {
      this.beanClass = Preconditions.checkNotNull(var1);
      this.property = this.getPropertyDescriptor(Preconditions.checkNotNull(var2));
      this.textComponent = Preconditions.checkNotNull(var3);
   }

   private PropertyDescriptor getPropertyDescriptor(String var1) {
      try {
         BeanInfo var2 = Introspector.getBeanInfo(this.beanClass);

         for (PropertyDescriptor var6 : var2.getPropertyDescriptors()) {
            if (var1.equals(var6.getName())) {
               Class var7 = var6.getPropertyType();
               if (!var7.equals(String.class)) {
                  throw new IllegalArgumentException(
                     String.format("%s is a %s property. Only java.lang.String properties are supported.", var1, var7.getName())
                  );
               }

               return var6;
            }
         }

         throw new IllegalArgumentException("No such property: " + var1);
      } catch (IntrospectionException var8) {
         throw new IllegalArgumentException("Not a valid bean class.", var8);
      }
   }

   public void setAutoTrim(boolean var1) {
      this.autoTrim = var1;
   }

   public void updateUi(Object var1) {
      Preconditions.checkArgument(this.beanClass.isInstance(var1), "Not of type " + this.beanClass.getName());

      try {
         Method var2 = this.property.getReadMethod();
         String var3 = (String)var2.invoke(var1);
         this.textComponent.setText(var3);
      } catch (Exception var4) {
         Throwables.propagateIfInstanceOf(var4, RuntimeException.class);
         throw new RuntimeException(var4);
      }
   }

   public void updateBean(Object var1) throws IllegalArgumentException {
      Preconditions.checkArgument(this.beanClass.isInstance(var1), "Not of type " + this.beanClass.getName());

      try {
         String var2 = this.textComponent.getText();
         String var3 = this.autoTrim ? var2.trim() : var2;
         Method var4 = this.property.getWriteMethod();
         var4.invoke(var1, var3);
      } catch (Exception var5) {
         Throwables.propagateIfInstanceOf(var5, IllegalArgumentException.class);
         Throwables.propagateIfInstanceOf(var5, RuntimeException.class);
         throw new RuntimeException(var5);
      }
   }
}
