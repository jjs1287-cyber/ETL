package org.etlforge.integration.data.structure.text;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.etlforge.collections.KongaListUtils;

final class FieldValidatorImpl implements TextStructureValidator {
   private final ImmutableList<Field> fields;

   FieldValidatorImpl(Iterable<Field> var1) {
      this.fields = ImmutableList.copyOf(var1);
   }

   @Override
   public Collection<ValidationIssue> validate() {
      Collection var1 = null;
      if (this.fields.isEmpty()) {
         ValidationIssue var2 = ValidationIssue.FACTORY.createFatal("No fields have been defined.");
         var1 = KongaListUtils.asList(var2);
      } else {
         var1 = this.validateFields();
      }

      return var1;
   }

   private Collection<ValidationIssue> validateFields() {
      HashSet var1 = Sets.newHashSet();
      HashSet var2 = Sets.newHashSet();
      ArrayList var3 = Lists.newArrayList();
      ArrayList var4 = Lists.newArrayList();
      this.collectProblems(var1, var2, var3, var4);
      ArrayList var5 = Lists.newArrayList();
      if (!var2.isEmpty() || !var3.isEmpty() || !var4.isEmpty()) {
         var5.addAll(this.getIssues(var2, var3, var4));
      }

      return var5;
   }

   private void collectProblems(Set<String> var1, Set<String> var2, List<String> var3, List<Integer> var4) {
      int var5 = 0;

      for (int var6 = this.fields.size(); var5 < var6; var5++) {
         Field var7 = this.fields.get(var5);
         String var8 = var7.getName();
         if (StringUtils.isBlank(var8)) {
            var4.add(var5 + 1);
         } else {
            var8 = StringUtils.strip(var8, " \t");
            if (TextStructureNameUtils.isFieldNameIllegal(var8)) {
               var3.add(var7.fieldName);
            } else if (var1.contains(var8)) {
               var2.add(var8);
            } else {
               var1.add(var8);
            }
         }
      }
   }

   private List<ValidationIssue> getIssues(Collection<String> var1, Collection<String> var2, Collection<Integer> var3) {
      ArrayList var4 = Lists.newArrayList();

      for (String var6 : var1) {
         var4.add(ValidationIssue.FACTORY.createFatal("Duplicate name: " + var6));
      }

      for (String var9 : var2) {
         var4.add(ValidationIssue.FACTORY.createFatal("Illegal name: " + var9));
      }

      for (Integer var10 : var3) {
         var4.add(ValidationIssue.FACTORY.createFatal("Field without name: " + var10));
      }

      return var4;
   }
}
