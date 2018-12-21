<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <!--<xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>-->
    <xsl:output method="html" encoding="utf-8"
                doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
                doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

    <xsl:template match="view">
        <html>
            <head>
                <title>
                    <xsl:value-of select="header/title"/>
                </title>
            </head>
            <body>
                <xsl:for-each select="body/form">
                    <form>

                        <xsl:attribute name="name">
                            <xsl:value-of select="name"/>
                        </xsl:attribute>
                        <xsl:attribute name="action">
                            <xsl:value-of select="action"/>
                        </xsl:attribute>
                        <xsl:attribute name="method">
                            <xsl:value-of select="method"/>
                        </xsl:attribute>


                        <xsl:for-each select="textView[name='userName']">
                            <label>
                                <xsl:value-of select="label"/>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="name"/>
                                </xsl:attribute>
                            </label>
                            <label>
                                <xsl:value-of select="value"/>
                            </label>
                        </xsl:for-each>

                        <br/>
                        <xsl:for-each select="textView[name='userAge']">
                            <label>
                                <xsl:value-of select="label"/>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="name"/>
                                </xsl:attribute>
                            </label>
                            <label>
                                <xsl:value-of select="value"/>
                            </label>
                        </xsl:for-each>

                        <br/>
                        <xsl:for-each select="buttonView[name='logoutButton']">
                            <input>
                                <xsl:attribute name="type">submit</xsl:attribute>
                                <xsl:attribute name="value">
                                    <xsl:value-of select="label"/>
                                </xsl:attribute>
                                <xsl:attribute name="name">
                                    <xsl:value-of select="name"/>
                                </xsl:attribute>
                            </input>
                        </xsl:for-each>
                    </form>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>