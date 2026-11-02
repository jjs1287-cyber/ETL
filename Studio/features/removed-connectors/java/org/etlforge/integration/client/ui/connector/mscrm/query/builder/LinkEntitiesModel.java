package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.Relationship;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.ListProperty;
import org.etlforge.xml.XmlStringBuilder;

public final class LinkEntitiesModel extends AbstractPropertyBasedModel {
   public static final String LINKS = "links";
   private final ImmutableList<MSCrmObject> path;
   private final Set<Relationship> relationships = Sets.newHashSet();
   private final ListProperty<LinkEntityModel> links = this.newListProperty("links");

   public LinkEntitiesModel(ImmutableList<MSCrmObject> var1) {
      this.path = Preconditions.checkNotNull(var1, "path");
      Preconditions.checkArgument(var1.size() >= 1, "path cannot be empty");
      this.links.set(Collections.emptyList());
   }

   LinkEntitiesModel(LinkEntitiesModel var1) {
      this.path = var1.path;

      for (LinkEntityModel var3 : var1.links.get()) {
         this.add(new LinkEntityModel(var3));
      }
   }

   public MSCrmObject getObject() {
      return this.path.get(this.path.size() - 1);
   }

   public void add(LinkEntityModel var1) {
      Relationship var2 = var1.getRelationship();
      if (!this.relationships.add(var2)) {
         throw new IllegalArgumentException("There is already a link for the relationship " + var2);
      }

      this.links.add(var1);
   }

   public void removeAll(Iterable<LinkEntityModel> var1) {
      this.links.removeAll(var1);

      for (LinkEntityModel var3 : var1) {
         this.relationships.remove(var3.getRelationship());
      }
   }

   public void replace(LinkEntityModel var1, LinkEntityModel var2) {
      this.relationships.remove(var1.getRelationship());
      this.relationships.add(var2.getRelationship());
      this.links.identityReplace(var1, var2);
   }

   public boolean contains(Relationship var1) {
      return this.relationships.contains(var1);
   }

   public ImmutableList<MSCrmObject> getPath() {
      return this.path;
   }

   public void appendToXml(XmlStringBuilder var1) {
      for (LinkEntityModel var3 : this.links) {
         var3.appendToXml(var1);
      }
   }
}
