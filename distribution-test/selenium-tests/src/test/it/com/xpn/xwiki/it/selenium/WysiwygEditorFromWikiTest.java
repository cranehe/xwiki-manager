/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.it.selenium;

import com.xpn.xwiki.it.selenium.framework.AbstractXWikiTestCase;
import com.xpn.xwiki.it.selenium.framework.AlbatrossSkinExecutor;
import com.xpn.xwiki.it.selenium.framework.XWikiTestSuite;

import junit.framework.Test;

/**
 * Tests the WYSIWYG editor (content edited in Wiki mode and then switched in WYSIWYG mode).
 *
 * @version $Id: $
 */
public class WysiwygEditorFromWikiTest extends AbstractXWikiTestCase
{
    public static Test suite()
    {
        XWikiTestSuite suite = new XWikiTestSuite("Tests the wiki editor");
        suite.addTestSuite(WysiwygEditorFromWikiTest.class, AlbatrossSkinExecutor.class);
        return suite;
    }

    protected void setUp() throws Exception
    {
        super.setUp();
        loginAsAdmin();
        open("/xwiki/bin/edit/Test/WysiwygEdit?editor=wiki");
    }

    public void testIndentedOrderedList() throws Exception
    {
        setFieldValue("content", "1. level 1\n11. level 2");
        clickLinkWithText("WYSIWYG");

        assertHTMLGeneratedByWysiwyg("ol/li[text()='level 1']");
        assertHTMLGeneratedByWysiwyg("ol/ol/li[text()='level 2']");

        clickLinkWithText("Wiki");
        assertEquals("1. level 1\n11. level 2", getFieldValue("content"));
    }

    public void testAutomaticConversionFromHashSyntaxToNumberSyntaxForOrderedLists()
    {
        setFieldValue("content", "# item 1\n## item 2\n# item 3");
        clickLinkWithText("WYSIWYG");
        clickLinkWithText("Wiki");

        assertEquals("1. item 1\n11. item 2\n1. item 3", getFieldValue("content"));
    }

    public void testHorizontalLineBeforeTableMacro()
    {
        setFieldValue("content", "----\n\n{table}\na | b\nc | d\n{table}");
        clickLinkWithText("WYSIWYG");
        clickLinkWithText("Wiki");

        assertEquals("----\n\n{table}\na | b\nc | d\n{table}", getFieldValue("content"));
    }

    public void testBulletedLists() throws Exception
    {
        setFieldValue("content", "- item 1\n-- item 2\n- item 3");
        clickLinkWithText("WYSIWYG");

        assertHTMLGeneratedByWysiwyg("ul/li[text()='item 1']");
        assertHTMLGeneratedByWysiwyg("ul/ul/li[text()='item 2']");
        assertHTMLGeneratedByWysiwyg("ul/li[text()='item 3']");
        
        clickLinkWithText("Wiki");
        assertEquals("- item 1\n-- item 2\n- item 3", getFieldValue("content"));
    }
}
