package org.etlforge.integration.data.entity;

import java.io.StringReader;
import java.util.Collections;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import org.etlforge.integration.data.entity.id.XsltTransformId;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.util.persist.Persistor;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public final class XsltTransform extends IntegrationEntity {
    @IdProperty public static final String ID = "xslttransform_id";
    @NameProperty public static final String NAME = "xslttransform_name";
    private static final String[] PROPERTIES = { "xslttransform_id", "xslttransform_name", "xslt" };
    public XsltTransform(String name) { this(null, name); }
    public XsltTransform(XsltTransformId id, String name) {
        super(EntityType.XsltTransform, id, name);
        setXslt("<xsl:stylesheet version=\"3.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "  <xsl:output method=\"xml\" encoding=\"UTF-8\" indent=\"yes\"/>\n"
            + "  <xsl:mode on-no-match=\"shallow-copy\"/>\n</xsl:stylesheet>\n");
    }
    @Override public XsltTransformId getID() { return (XsltTransformId)super.getID(); }
    @Override public String[] getPropertyKeys() { return PROPERTIES.clone(); }
    public String getXslt() { return getProperty("xslt", ""); }
    public void setXslt(String value) { setStringProperty("xslt", value == null ? "" : value); }
    @Override protected Set<String> getKeysToSkipWhenPersisting() { return Collections.singleton("xslt"); }
    @Override protected void writeAdditionalData(Persistor persistor) { persistor.putText("xslt", getXslt()); }
    @Override protected void restoreAdditionalData(Persistor persistor) { setXslt(persistor.getText("xslt")); }
    @Override public boolean isDeepCopyable() { return true; }
    @Override protected void validateOtherPropertiesThanTheName(ValidationMessageCollector messages) {
        try { validateStylesheet(getXslt()); }
        catch (Exception failure) { messages.addMessage("Invalid XSLT: " + failure.getMessage(), ValidationStatus.INVALID); }
    }
    public static void validateStylesheet(String text) throws Exception {
        if (text == null || text.trim().isEmpty()) throw new IllegalArgumentException("A stylesheet is required.");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setXIncludeAware(false);
        Element root = factory.newDocumentBuilder().parse(new InputSource(new StringReader(text))).getDocumentElement();
        if (!"http://www.w3.org/1999/XSL/Transform".equals(root.getNamespaceURI())
            || !("stylesheet".equals(root.getLocalName()) || "transform".equals(root.getLocalName()))) {
            throw new IllegalArgumentException("The root element must be xsl:stylesheet or xsl:transform.");
        }
    }
}
