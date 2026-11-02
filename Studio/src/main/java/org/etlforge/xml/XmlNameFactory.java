package org.etlforge.xml;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

public abstract class XmlNameFactory {
   private static XmlNameFactory conformingFactory;
   private static XmlNameFactory nonConformingFactory;

   public abstract ExpandedName newExpandedName(String var1, String var2);

   public abstract ExpandedName newExpandedName(String var1);

   public abstract ExpandedName newExpandedName(Node var1);

   public abstract QualifiedName newQualifiedName(String var1, String var2);

   public abstract QualifiedName newQualifiedName(String var1);

   public abstract QualifiedName newQualifiedName(Node var1);

   public static synchronized XmlNameFactory nonConforming() {
      if (nonConformingFactory == null) {
         nonConformingFactory = new XmlNameFactory.FactoryImpl(false);
      }

      return nonConformingFactory;
   }

   public static synchronized XmlNameFactory conforming() {
      if (conformingFactory == null) {
         conformingFactory = new XmlNameFactory.FactoryImpl(true);
      }

      return conformingFactory;
   }

   private static final class Expanded extends XmlNameFactory.Name implements ExpandedName {
      private static final long serialVersionUID = -4712623244442481908L;
      private final String stringRep;

      public Expanded(String var1, String var2, boolean var3) {
         super(var1, var2, var3);
         this.stringRep = var1 != null && var1.length() != 0 ? "{" + var1 + "}" + var2 : var2;
      }

      @Override
      public String getNamespace() {
         return this.getNamespacePart();
      }

      @Override
      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         }

         if (!(var1 instanceof ExpandedName)) {
            return false;
         }

         ExpandedName var2 = (ExpandedName)var1;
         return this.getNamespace().equals(var2.getNamespace()) && this.getLocalName().equals(var2.getLocalName());
      }

      @Override
      public String toString() {
         return this.stringRep;
      }

      @Override
      public int hashCode() {
         int var1 = 17;
         var1 = 31 * var1 + this.getNamespace().hashCode();
         return 31 * var1 + this.getLocalName().hashCode();
      }
   }

   private static class FactoryImpl extends XmlNameFactory {
      private final boolean conforming;

      public FactoryImpl(boolean var1) {
         this.conforming = var1;
      }

      @Override
      public ExpandedName newExpandedName(String var1, String var2) {
         return new XmlNameFactory.Expanded(var1, var2, this.conforming);
      }

      @Override
      public ExpandedName newExpandedName(String var1) {
         if (var1.startsWith("{")) {
            String var2 = StringUtils.substringBetween(var1, "{", "}");
            String var3 = StringUtils.substringAfter(var1, "}");
            return new XmlNameFactory.Expanded(var2, var3, this.conforming);
         } else {
            return new XmlNameFactory.Expanded("", var1, this.conforming);
         }
      }

      @Override
      public ExpandedName newExpandedName(Node var1) {
         String var2 = var1.getNamespaceURI();
         String var3 = var1.getLocalName();
         return new XmlNameFactory.Expanded(var2, var3, this.conforming);
      }

      @Override
      public QualifiedName newQualifiedName(String var1, String var2) {
         return new XmlNameFactory.Qualified(var1, var2, this.conforming);
      }

      @Override
      public QualifiedName newQualifiedName(String var1) {
         String[] var2 = this.split(var1);
         return new XmlNameFactory.Qualified(var2[0], var2[1], this.conforming);
      }

      @Override
      public QualifiedName newQualifiedName(Node var1) {
         String var2 = var1.getPrefix();
         String var3 = var1.getLocalName();
         return new XmlNameFactory.Qualified(var2, var3, this.conforming);
      }

      private String[] split(String var1) {
         String var2 = var1.trim();
         Preconditions.checkArgument(!var2.isEmpty(), "s cannot be blank");
         int var3 = var2.lastIndexOf(58);
         if (var3 == var2.length() - 1) {
            throw new IllegalArgumentException("s does not contain a local part.");
         }

         String var4 = var3 >= 0 ? var2.substring(0, var3) : "";
         String var5 = var2.substring(var3 + 1);
         return new String[]{var4, var5};
      }
   }

   private abstract static class Name implements Serializable {
      private static final long serialVersionUID = 7309482970901400358L;
      private final String namespacePart;
      private final String localName;

      public Name(String var1, String var2, boolean var3) {
         Preconditions.checkArgument(StringUtils.isNotBlank(var2), "local cannot be null or empty.");
         var2 = var2.trim();
         if (var3) {
            validate(var2);
         }

         if (var1 == null) {
            var1 = "";
         }

         this.namespacePart = var1.trim();
         this.localName = var2;
      }

      protected final String getNamespacePart() {
         return this.namespacePart;
      }

      public final String getLocalName() {
         return this.localName;
      }

      @Override
      public String toString() {
         return this.namespacePart.length() == 0 ? this.localName : this.namespacePart + ":" + this.localName;
      }

      private static void validate(String var0) throws IllegalArgumentException {
         assert var0 != null && var0.trim().length() > 0;
         validateFirstCharacter(var0);
         validateRest(var0);
      }

      private static void validateFirstCharacter(String var0) {
         char var1 = var0.charAt(0);
         if (!isValidBeginningCharacter(var1)) {
            throw new IllegalArgumentException("The local name cannot start with a '" + var1 + "'.");
         }
      }

      private static boolean isValidBeginningCharacter(char var0) {
         return Character.isLetter(var0) || var0 == '_' || var0 == ':';
      }

      private static void validateRest(String var0) {
         int var1 = 1;

         for (int var2 = var0.length(); var1 < var2; var1++) {
            char var3 = var0.charAt(var1);
            if (!isValidNameCharacter(var3)) {
               throw new IllegalArgumentException("The local name cannot contain the character '" + var3 + "'");
            }
         }
      }

      private static boolean isValidNameCharacter(char var0) {
         return Character.isLetter(var0) || Character.isDigit(var0) || var0 == '.' || var0 == '_' || var0 == '-' || var0 == ':';
      }
   }

   private static final class Qualified extends XmlNameFactory.Name implements QualifiedName {
      private static final long serialVersionUID = 2505360339891985690L;

      public Qualified(String var1, String var2, boolean var3) {
         super(var1, var2, var3);
      }

      @Override
      public String getPrefix() {
         return this.getNamespacePart();
      }

      @Override
      public boolean equals(Object var1) {
         if (!(var1 instanceof QualifiedName)) {
            return false;
         }

         QualifiedName var2 = (QualifiedName)var1;
         return Objects.equal(this.getPrefix(), var2.getPrefix()) && Objects.equal(this.getLocalName(), var2.getLocalName());
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(this.getPrefix(), this.getLocalName());
      }
   }
}
