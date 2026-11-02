package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import java.beans.PropertyChangeListener;
import org.etlforge.integration.client.ui.connector.mscrm.prefs.NameOptionPreference;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.xml.XmlStringBuilder;

public abstract class AbstractQueryBuilderModel extends AbstractPropertyBasedModel implements QueryBuilderModel {
   private final ImmutableList<MSCrmObject> path;
   private final AttributeSelectionModel attributeSelectionModel;
   private final AttributeOrderModel attributeOrderModel;
   private final FilterDefinitionModel filterModel;
   private final LinkEntitiesModel linkEntitiesModel;

   protected AbstractQueryBuilderModel(MSCrmObject var1) {
      this(ImmutableList.of(Preconditions.checkNotNull(var1, "object")));
   }

   protected AbstractQueryBuilderModel(ImmutableList<MSCrmObject> var1) {
      this.path = Preconditions.checkNotNull(var1, "path");
      Preconditions.checkArgument(!var1.isEmpty(), "path cannot be empty");
      MSCrmObject var2 = this.getObject();
      this.attributeSelectionModel = new AttributeSelectionModel();
      this.attributeSelectionModel.setObject(var2);
      this.attributeOrderModel = new AttributeOrderModel(this.attributeSelectionModel.getAvailableAttributes());
      this.filterModel = new FilterDefinitionModel(var2);
      this.linkEntitiesModel = new LinkEntitiesModel(var1);
   }

   AbstractQueryBuilderModel(
      ImmutableList<MSCrmObject> var1, AttributeSelectionModel var2, AttributeOrderModel var3, FilterDefinitionModel var4, LinkEntitiesModel var5
   ) {
      this.path = Preconditions.checkNotNull(var1, "path");
      Preconditions.checkArgument(!var1.isEmpty(), "path cannot be empty");
      this.attributeSelectionModel = Preconditions.checkNotNull(var2, "attributeSelectionModel");
      this.attributeOrderModel = Preconditions.checkNotNull(var3, "attributeOrderModel");
      this.filterModel = Preconditions.checkNotNull(var4, "filterModel");
      this.linkEntitiesModel = Preconditions.checkNotNull(var5, "linkEntitiesModel");
   }

   @Override
   public final ImmutableList<MSCrmObject> getPath() {
      return this.path;
   }

   @Override
   public final String getTitle() {
      return Joiner.on(" > ").join(Iterables.transform(this.path, NameOptionPreference.INSTANCE.get()));
   }

   @Override
   public final MSCrmObject getObject() {
      return this.path.get(this.path.size() - 1);
   }

   @Override
   public final AttributeSelectionModel getAttributeSelectionModel() {
      return this.attributeSelectionModel;
   }

   @Override
   public final AttributeOrderModel getAttributeOrderModel() {
      return this.attributeOrderModel;
   }

   @Override
   public final FilterDefinitionModel getFilterDefinitionModel() {
      return this.filterModel;
   }

   @Override
   public final LinkEntitiesModel getLinkEntitiesModel() {
      return this.linkEntitiesModel;
   }

   @Override
   public final void listenToAllChanges(PropertyChangeListener var1) {
      this.attributeSelectionModel.addPropertyChangeListener(var1);
      this.attributeOrderModel.addPropertyChangeListener(var1);
      this.filterModel.addPropertyChangeListener(var1);
      this.linkEntitiesModel.addPropertyChangeListener(var1);
      this.listenToAdditionalProperties(var1);
   }

   protected void listenToAdditionalProperties(PropertyChangeListener var1) {
   }

   @Override
   public final void stopListeningToAllChanges(PropertyChangeListener var1) {
      this.attributeSelectionModel.removePropertyChangeListener(var1);
      this.attributeOrderModel.removePropertyChangeListener(var1);
      this.filterModel.removePropertyChangeListener(var1);
      this.linkEntitiesModel.removePropertyChangeListener(var1);
      this.stopListeningToAdditionalProperties(var1);
   }

   protected void stopListeningToAdditionalProperties(PropertyChangeListener var1) {
   }

   protected void appendToXml(XmlStringBuilder var1) {
      this.attributeSelectionModel.appendToXml(var1);
      this.attributeOrderModel.appendToXml(var1);
      this.filterModel.appendToXml(var1);
      this.linkEntitiesModel.appendToXml(var1);
   }
}
