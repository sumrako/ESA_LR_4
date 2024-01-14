<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html"/>
    <xsl:template match="ArrayList">
        <html>
            <body>
                <h1>Tickets</h1>
                <table border="1">
                    <thead>
                        <tr>
                            <th>Id</th>
                            <th>Seat</th>
                            <th>Cost</th>
                            <th>Timetable</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="item">
                            <tr>
                                <td><xsl:value-of select="id"/></td>
                                <td>
                                    Seat : <xsl:value-of select="seatDto/seat"/> | Row <xsl:value-of select="seatDto/row"/>
                                </td>
                                <td><xsl:value-of select="cost"/></td>
                                <td>
                                    <xsl:value-of select="timetable/performance/name"/> | <xsl:value-of select="timetable/date"/> | hall(<xsl:value-of select="timetable/hall"/>)
                                </td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>