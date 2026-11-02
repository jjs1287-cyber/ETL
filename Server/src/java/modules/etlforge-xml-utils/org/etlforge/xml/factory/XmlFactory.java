package org.etlforge.xml.factory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.etlforge.lang.Arrays2;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlFactory<E> {
   private final Class<E> targetClass;
   private XmlFactory.Ctor<E> ctor;

   public static <T> XmlFactory<T> newFactory(Class<T> var0) {
      return new XmlFactory(var0);
   }

   public XmlFactory(Class<E> var1) {
      Preconditions.checkNotNull(var1, "targetClass");
      this.targetClass = var1;
      this.ctor = new XmlFactory.DefaultCtor<>();
   }

   public XmlFactory<E> constructor(XmlFactory.Ctor<E> var1) {
      this.ctor = var1;
      return this;
   }

   public XmlFactory<E> constructor(Attribute... var1) {
      this.ctor = new XmlFactory.AttributeCtor<>(var1);
      return this;
   }

   public XmlFactory<E> constructor(String var1) {
      return this.constructor(var1, new Class[0], new Object[0]);
   }

   public XmlFactory<E> constructor(String var1, Object var2) {
      return this.constructor(var1, new Class[]{var2.getClass()}, new Object[]{var2});
   }

   public XmlFactory<E> optionalArgConstructor(String var1, Object var2) {
      XmlFactory.ClassNameCtor var3 = new XmlFactory.ClassNameCtor(Attribute.name(var1), new Class[]{var2.getClass()}, new Object[]{var2});
      var3.setArgumentsOptional(true);
      this.ctor = var3;
      return this;
   }

   public XmlFactory<E> constructor(String var1, Class[] var2, Object[] var3) {
      this.ctor = new XmlFactory.ClassNameCtor<>(Attribute.name(var1), var2, var3);
      return this;
   }

   public E evaluate(Element var1) throws Exception {
      return this.ctor.newInstance(var1, this.targetClass);
   }

   public List<E> evaluateAll(Elements var1) throws Exception {
      ArrayList var2 = Lists.newArrayList();

      for (Element var4 : var1) {
         var2.add(this.evaluate(var4));
      }

      return var2;
   }

   public List<E> evaluateAll(String var1, String var2) throws Exception {
      InputStream var3 = null;

      try {
         var3 = this.getClass().getClassLoader().getResourceAsStream(var1);
         return this.evaluateAll(var3, var2);
      } finally {
         KongaXmlUtils.close(var3);
      }
   }

   public List<E> evaluateAll(InputStream var1, String var2) throws Exception {
      DocumentBuilder var3 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document var4 = var3.parse(var1);
      return this.evaluateAll(var4, var2);
   }

   public List<E> evaluateAll(Node var1, String var2) throws Exception {
      return this.evaluateAll(Elements.matching(var2, var1));
   }

   private static class AttributeCtor<T> implements XmlFactory.Ctor<T> {
      private final Attribute[] args;

      public AttributeCtor(Attribute... var1) {
         this.args = (Attribute[])var1.clone();
      }

      @Override
      public T newInstance(final Element var1, Class<T> var2) throws Exception {
         Class[] var3 = Arrays2.transform(this.args, Class.class, Attribute.TO_VALUE_TYPE);
         Constructor var4 = var2.getConstructor(var3);
         Object[] var5 = Arrays2.transform(this.args, new Function<Attribute, Object>() {
            public Object apply(Attribute var1x) {
               return var1x.evaluate(var1);
            }
         });
         return (T)var4.newInstance(var5);
      }
   }

   private static class ClassNameCtor<T> implements XmlFactory.Ctor<T> {
      private final Attribute classNameAttr;
      private final Class[] argTypes;
      private final Object[] argVals;
      private boolean argumentsOptional;

      public ClassNameCtor(Attribute var1, Class[] var2, Object[] var3) {
         this.classNameAttr = var1;
         this.argTypes = (Class[])var2.clone();
         this.argVals = (Object[])var3.clone();
      }

      public void setArgumentsOptional(boolean var1) {
         this.argumentsOptional = var1;
      }

      @Override
      public T newInstance(Element var1, Class<T> var2) throws Exception {
         String var3 = (String)this.classNameAttr.evaluate(var1);
         Class var4 = Class.forName(var3).asSubclass(var2);
         if (this.argumentsOptional) {
            for (Constructor var8 : var4.getConstructors()) {
               if (Arrays.equals(this.argTypes, var8.getParameterTypes())) {
                  return (T)var8.newInstance(this.argVals);
               }
            }

            return (T)var4.newInstance();
         } else {
            Constructor var5 = var4.getConstructor(this.argTypes);
            return (T)var5.newInstance(this.argVals);
         }
      }
   }

   public interface Ctor<T> {
      T newInstance(Element var1, Class<T> var2) throws Exception;
   }

   private static class DefaultCtor<T> implements XmlFactory.Ctor<T> {
      private DefaultCtor() {
      }

      @Override
      public T newInstance(Element var1, Class<T> var2) throws Exception {
         return (T)var2.newInstance();
      }
   }
}
