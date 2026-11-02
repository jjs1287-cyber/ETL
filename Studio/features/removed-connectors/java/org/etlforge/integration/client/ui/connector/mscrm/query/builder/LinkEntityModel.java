package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.integration.mscrm.metadata.Relationship;
import org.etlforge.util.Property;
import org.etlforge.util.StringProperty;
import org.etlforge.xml.XmlStringBuilder;

public final class LinkEntityModel extends AbstractQueryBuilderModel {
   public static final String LINK_TYPE = "link_type";
   public static final String ALIAS = "alias";
   public static final String VISIBLE = "visible";
   private final Relationship relationship;
   private final Property<LinkType> linkType = this.newProperty("link_type", LinkType.OUTER);
   private final StringProperty alias = this.newAutoTrimmedStringProperty("alias", "");
   private final Property<Boolean> visible = this.newProperty("visible", Boolean.FALSE);

   public LinkEntityModel(LinkEntityModel var1) {
      super(
         var1.getPath(),
         new AttributeSelectionModel(var1.getAttributeSelectionModel()),
         new AttributeOrderModel(var1.getAttributeOrderModel()),
         new FilterDefinitionModel(var1.getFilterDefinitionModel()),
         new LinkEntitiesModel(var1.getLinkEntitiesModel())
      );
      this.relationship = var1.relationship;
      this.linkType.setSilently(var1.linkType.get());
      this.alias.setSilently(var1.alias.get());
      this.visible.setSilently(var1.visible.get());
   }

   public LinkEntityModel(ImmutableList<MSCrmObject> var1, Relationship var2) {
      super(var1);
      this.relationship = Preconditions.checkNotNull(var2, "relationship");
      this.linkType.setAllowsNull(false);
      this.alias.setAllowsNull(false);
      this.visible.setAllowsNull(false);
   }

   public Relationship getRelationship() {
      return this.relationship;
   }

   public void setAlias(String var1) {
      this.alias.set(Strings.nullToEmpty(var1));
   }

   public void setLinkType(LinkType var1) {
      this.linkType.set(var1);
   }

   public void setVisible(boolean var1) {
      this.visible.set(var1);
   }

   @Override
   public void appendToXml(XmlStringBuilder var1) {
      var1.addElement("link-entity");
      this.addLinkAttributes(var1);
      super.appendToXml(var1);
      var1.closeElement();
   }

   private void addLinkAttributes(XmlStringBuilder var1) {
      if (!this.alias.isEmpty()) {
         var1.addAttribute("alias", this.alias.get());
      }

      var1.addAttribute("name", this.relationship.getLinkEntityName());
      var1.addAttribute("from", this.relationship.getFromAttribute());
      var1.addAttribute("to", this.relationship.getToAttribute());
      var1.addAttribute("link-type", this.linkType.get().getXml());
      if (this.relationship.isIntersect()) {
         var1.addAttribute("intersect", "true");
      }

      var1.addAttribute("visible", this.visible.get() ? "true" : "false");
   }

   @Override
   public String buildXml() {
      XmlStringBuilder var1 = new XmlStringBuilder();
      var1.setProcessingInstruction(null);
      this.appendToXml(var1);
      return var1.getXml();
   }

   @Override
   protected void listenToAdditionalProperties(PropertyChangeListener var1) {
      this.linkType.addListener(var1);
      this.alias.addListener(var1);
      this.visible.addListener(var1);
   }

   @Override
   protected void stopListeningToAdditionalProperties(PropertyChangeListener var1) {
      this.linkType.removeListener(var1);
      this.alias.removeListener(var1);
      this.visible.removeListener(var1);
   }

   public String getDisplayName() {
      return this.alias.isEmpty() ? this.relationship.getLinkEntityName() : this.alias.get();
   }
}
