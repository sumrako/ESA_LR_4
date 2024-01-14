<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="ArrayList">
        <html>
            <body>
                <h1>Timetable</h1>
                <table border="1">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Performance</th>
                            <th>Hall</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="item">
                            <tr>
                                <td><xsl:value-of select="id"/></td>
                                <td>
                                    <xsl:value-of select="performance/name"/> | <xsl:value-of select="performance/genre"/> | <xsl:value-of select="performance/country"/>
                                </td>
                                <td><xsl:value-of select="hall"/></td>
                                <td><xsl:value-of select="date"/></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>