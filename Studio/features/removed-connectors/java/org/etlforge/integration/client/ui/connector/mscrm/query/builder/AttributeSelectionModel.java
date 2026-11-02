package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import java.util.List;
import org.etlforge.integration.mscrm.FunctionType;
import org.etlforge.integration.mscrm.metadata.MSCrmAttribute;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.PrimaryIdPredicate;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.name.NameSorter;
import org.etlforge.xml.XmlStringBuilder;

final class AttributeSelectionModel extends AbstractPropertyBasedModel {
   public static final String AVAILABLE_ATTRS = "available_attrs";
   public static final String SELECTED_ATTRS = "selected_attrs";
   public static final String DISABLED_ATTRS = "disabled_attrs";
   public static final String USE_ALL_ATTRS = "use_all_attrs";
   private final ListProperty<MSCrmAttribute> availableAttrs = this.newListProperty("available_attrs");
   private final ListProperty<MSCrmAttribute> selectedAttrs = this.newListProperty("selected_attrs");
   private final ListProperty<MSCrmAttribute> disabledAttrs = this.newListProperty("disabled_attrs");
   private final Property<Boolean> useAllAttrs = this.newProperty("use_all_attrs", false);
   private Predicate<MSCrmAttribute> primaryIdCondition = Predicates.alwaysFalse();

   public AttributeSelectionModel() {
   }

   AttributeSelectionModel(AttributeSelectionModel var1) {
      this.availableAttrs.setSilently(var1.availableAttrs.get());
      this.selectedAttrs.setSilently(var1.selectedAttrs.get());
      this.disabledAttrs.setSilently(var1.disabledAttrs.get());
      this.useAllAttrs.setSilently(var1.useAllAttrs.get());
   }

   public void setObject(MSCrmObject var1) {
      this.selectedAttrs.clear();
      this.useAllAttrs.set(false);
      List var2 = this.getAvailableAttributesFromObject(var1);
      this.availableAttrs.set(var2);
      this.primaryIdCondition = new PrimaryIdPredicate(var1);
      ImmutableList var3 = FluentIterable.from(var2).filter(this.primaryIdCondition).toImmutableList();
      this.disabledAttrs.set(var3);
      if (!var3.isEmpty()) {
         this.selectedAttrs.set(var3);
      }
   }

   private List<MSCrmAttribute> getAvailableAttributesFromObject(MSCrmObject var1) {
      Iterable var2 = FunctionType.RETRIEVE.getMatchingAttributes(var1);
      return NameSorter.naturalOrdering().sortedCopy(var2);
   }

   public void clearSelection() {
      ImmutableList var1 = FluentIterable.from(this.availableAttrs.get()).filter(this.primaryIdCondition).toImmutableList();
      this.selectedAttrs.set(var1);
   }

   public List<MSCrmAttribute> getAvailableAttributes() {
      return this.availableAttrs.get();
   }

   public void setUseAllAttributes(boolean var1) {
      this.useAllAttrs.set(var1);
   }

   public void addAttribute(MSCrmAttribute var1) {
      this.selectedAttrs.add(var1);
   }

   public void appendToXml(XmlStringBuilder var1) {
      if (this.useAllAttrs.get()) {
         var1.addEmptyElement("all-attributes");
      } else {
         for (MSCrmAttribute var4 : this.selectedAttrs.get()) {
            var1.addElement("attribute");
            var1.addAttribute("name", var4.getName());
            var1.closeElement();
         }
      }
   }
}
