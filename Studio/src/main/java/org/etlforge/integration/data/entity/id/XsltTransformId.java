package org.etlforge.integration.data.entity.id;

import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntityId;

public final class XsltTransformId extends IntegrationEntityId {
    public XsltTransformId() {}
    public XsltTransformId(String value) { super(value); }
    @Override public EntityType getEntityType() { return EntityType.XsltTransform; }
}
