/**
 * This file is part of the CRISTAL-iSE kernel.
 * Copyright (c) 2001-2015 The CRISTAL Consortium. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 3 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 * http://www.fsf.org/licensing/licenses/lgpl.html
 */

package org.cristalise.kernel.persistency.outcome;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.cristalise.kernel.entity.agent.Job;
import org.cristalise.kernel.utils.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 */
public class XPathOutcomeInitiatorTest extends OutcomeInitiatorTestBase {

    XPathOutcomeInitiator xpathOI;

    @Before
    public void setUp() throws Exception {
        xpathOI = new XPathOutcomeInitiator();
    }

    /**
     * 
     * @param type
     * @param xpath
     * @param value
     * @throws Exception
     */
    private void checkUpdatedOutcome(String type, String xpath, String value) throws Exception {
        String xsd      = new String(Files.readAllBytes(Paths.get(root+type+".xsd")));
        String expected = new String(Files.readAllBytes(Paths.get(root+type+"Updated.xml")));

        Job j = mockJob(xsd);

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(xpath, value);

        when(j.matchhActPropNames("^/")).thenReturn(resultMap);

        String actual = xpathOI.initOutcome(j);

        Logger.msg(actual);

        if(!compareXML(expected, actual)) fail("");
    }

    @Test
    public void updateSingleElement() throws Exception {
        checkUpdatedOutcome("IntegerField", "/IntegerField/counter", "123");
    }

}