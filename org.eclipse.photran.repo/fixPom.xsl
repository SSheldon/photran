<xsl:stylesheet version="2.0" xmlns:xsl='http://www.w3.org/1999/XSL/Transform' 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:p="http://maven.apache.org/POM/4.0.0"
	exclude-result-prefixes="p xsi #default">
	
	<xsl:param name="newVersion"/>
	
	<xsl:output encoding="UTF-8" method="xml" indent="yes" />

	<xsl:template match="p:project">
		<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
			<xsl:copy-of select="namespace::*" />
			<xsl:apply-templates />
		</project>
	</xsl:template>

	<xsl:template match="p:version[preceding-sibling::p:groupId='org.eclipse.photran']">
		<version><xsl:value-of select="$newVersion"/>-SNAPSHOT</version>
	</xsl:template>
	
	<xsl:template match="p:version[preceding-sibling::p:groupId='org.eclipse.ptp.features']">
		<version><xsl:value-of select="$newVersion"/>-SNAPSHOT</version>
	</xsl:template>
	
	<xsl:template match="p:version[preceding-sibling::p:artifactId='org.eclipse.photran.repo']">
		<version><xsl:value-of select="$newVersion"/>-SNAPSHOT</version>
	</xsl:template>
	
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
	
	<xsl:template match="comment()">
   		<xsl:copy/>
	</xsl:template>
	
</xsl:stylesheet>
