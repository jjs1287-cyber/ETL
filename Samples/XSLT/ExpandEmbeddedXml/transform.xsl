<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
  <xsl:mode on-no-match="shallow-copy"/>
  <xsl:template match="/outbox/entry/xml">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:sequence select="parse-xml(string(.))/*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
