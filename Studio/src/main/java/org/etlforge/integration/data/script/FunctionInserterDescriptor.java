package org.etlforge.integration.data.script;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.axis.utils.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.io.KongaIoUtils;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

final class FunctionInserterDescriptor {
   public static final FunctionInserterDescriptor NONE = new FunctionInserterDescriptor();
   private final Class<? extends FunctionInserter> inserterClass;
   private final List<ArgumentDescriptor> arguments;
   private final List<FunctionInserterDescriptor.Property> properties;

   private FunctionInserterDescriptor() {
      this(null, null, null);
   }

   private FunctionInserterDescriptor(Class<? extends FunctionInserter> var1, List<ArgumentDescriptor> var2, List<FunctionInserterDescriptor.Property> var3) {
      this.inserterClass = var1;
      this.arguments = var2;
      this.properties = var3;
   }

   public FunctionInserter instantiate(Function var1) throws Exception {
      if (this.inserterClass == null) {
         return null;
      }

      FunctionInserter var2 = null;

      for (Constructor var6 : this.inserterClass.getConstructors()) {
         Class[] var7 = var6.getParameterTypes();
         if (var7.length == 1) {
            var2 = (FunctionInserter)var6.newInstance(var1);
         } else if (var7.length == 2) {
            var2 = (FunctionInserter)var6.newInstance(var1, this.arguments);
         }
      }

      if (var2 == null) {
         throw new Exception("No suitable constructor found in the class " + this.inserterClass.getName());
      }

      this.setProperties(var2);
      return var2;
   }

   private void setProperties(FunctionInserter var1) throws Exception {
      if (!this.properties.isEmpty()) {
         Map var2 = this.createPropertyDescriptorMap(var1);

         for (FunctionInserterDescriptor.Property var4 : this.properties) {
            PropertyDescriptor var5 = (PropertyDescriptor)var2.get(var4.name);
            if (var5 == null) {
               throw new Exception("No such property: " + var4.name);
            }

            Method var6 = var5.getWriteMethod();
            if (var6 == null) {
               throw new Exception("The property " + var4.name + " is read-only.");
            }

            var6.invoke(var1, var4.value);
         }
      }
   }

   private Map<String, PropertyDescriptor> createPropertyDescriptorMap(FunctionInserter var1) throws Exception {
      BeanInfo var2 = Introspector.getBeanInfo(var1.getClass());
      HashMap var3 = Maps.newHashMap();

      for (PropertyDescriptor var7 : var2.getPropertyDescriptors()) {
         var3.put(var7.getName(), var7);
      }

      return var3;
   }

   public static FunctionInserterDescriptor locate(Function var0) {
      String var1 = "functions/" + var0.getName().toLowerCase() + ".wizard.xml";
      InputStream var2 = FunctionInserterDescriptor.class.getClassLoader().getResourceAsStream(var1);
      if (var2 != null) {
         try {
            FunctionInserterDescriptor.Parser var3 = new FunctionInserterDescriptor.Parser(var2);
            return var3.parse();
         } catch (Exception var8) {
            ErrorLog.log(FunctionInserterDescriptor.class, "Invalid function inserter configuration for the function: " + var0.getName(), var8);
         } finally {
            KongaIoUtils.close(var2);
         }
      }

      return NONE;
   }

   private static class Parser {
      private final Document dom;

      public Parser(InputStream var1) throws Exception {
         this.dom = DomParser.parseInputStream(var1);
      }

      public FunctionInserterDescriptor parse() throws Exception {
         Element var1 = this.dom.getDocumentElement();
         String var2 = var1.getAttribute("class");
         Class var3 = Class.forName(var2).asSubclass(FunctionInserter.class);
         List var4 = this.parseArguments(var1);
         List var5 = this.parseProperties(var1);
         return new FunctionInserterDescriptor(var3, var4, var5);
      }

      private List<ArgumentDescriptor> parseArguments(Element var1) {
         ArrayList var2 = Lists.newArrayList();

         for (Element var4 : Elements.matching("Argument", var1)) {
            ArgumentDescriptor var5 = parseArgument(var4);
            var2.add(var5);
         }

         return var2;
      }

      private static ArgumentDescriptor parseArgument(Element var0) {
         String var1 = KongaXmlUtils.getChildElementValue(var0, "Label");
         String var2 = KongaXmlUtils.getChildElementValue(var0, "Description");
         String var3 = KongaXmlUtils.getChildElementValue(var0, "DefaultValue");
         boolean var4 = isOptional(var0);
         return new ArgumentDescriptor(var1, var2, var3, var4);
      }

      private static boolean isOptional(Element var0) {
         String var1 = var0.getAttribute("optional");
         return StringUtils.isEmpty(var1) ? false : KongaXmlUtils.isTrue(var1);
      }

      private List<FunctionInserterDescriptor.Property> parseProperties(Element var1) {
         ArrayList var2 = Lists.newArrayList();

         for (Element var4 : Elements.matching("Property", var1)) {
            String var5 = var4.getAttribute("name");
            boolean var6 = KongaXmlUtils.isTrue(var4.getAttribute("value"));
            var2.add(new FunctionInserterDescriptor.Property(var5, var6));
         }

         return var2;
      }
   }

   private static class Property {
      public final String name;
      public final boolean value;

      public Property(String var1, boolean var2) {
         this.name = var1;
         this.value = var2;
      }
   }
}
