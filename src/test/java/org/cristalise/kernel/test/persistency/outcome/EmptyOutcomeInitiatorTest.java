/**
 * This file is part of the CRISTAL-iSE XPath Outcome Initiator module.
 * Copyright (c) 2001-2016 The CRISTAL Consortium. All rights reserved.
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
package org.cristalise.kernel.test.persistency.outcome;

import static org.junit.Assert.fail;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.cristalise.kernel.entity.agent.Job;
import org.cristalise.kernel.persistency.outcome.EmptyOutcomeInitiator;
import org.cristalise.kernel.persistency.outcome.Outcome;
import org.cristalise.kernel.utils.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 */
public class EmptyOutcomeInitiatorTest extends OutcomeInitiatorTestBase {

    EmptyOutcomeInitiator emptyOI;

    @Before
    public void setUp() throws Exception {
        emptyOI = new EmptyOutcomeInitiator();
    }

    /**
     * 
     * @param type
     * @throws Exception
     */
    private void checkEmptyOutcome(String type) throws Exception {
        String xsd      = new String(Files.readAllBytes(Paths.get(root+type+".xsd")));
        String expected = new String(Files.readAllBytes(Paths.get(root+type+".xml")));

        Job j = mockJob(xsd);

        Outcome actual = emptyOI.initOutcomeInstance(j);

        if (!type.equals("Module")) actual.validateAndCheck();

        Logger.msg(actual.getData());

        if (!compareXML(expected, actual.getData())) fail("");
    }

    @Test
    public void generateDateXML() throws Exception {
        checkEmptyOutcome("DateField");
    }

    @Test @Ignore("Fails on Travis, wierd")
    public void generateTimeXML() throws Exception {
        checkEmptyOutcome("TimeField");
    }

    @Test @Ignore("Fails on Travis, wierd")
    public void generateDateTimeXML() throws Exception {
        checkEmptyOutcome("DateTimeField");
    }

    public void generateStringXML() throws Exception {
        checkEmptyOutcome("StringField");
    }

    @Test
    public void generateBooleanXML() throws Exception {
        checkEmptyOutcome("BooleanField");
    }
    
    @Test
    public void generateIntegerXML() throws Exception {
        checkEmptyOutcome("IntegerField");
    }

    @Test
    public void generateDecimalXML() throws Exception {
        checkEmptyOutcome("DecimalField");
    }

    @Test
    public void generateStateMachineXML() throws Exception {
        checkEmptyOutcome("StateMachine");
    }

    @Test
    public void generatePatientDetailsXML() throws Exception {
        checkEmptyOutcome("PatientDetails");
    }

    @Test
    public void generateModuleXML() throws Exception {
        checkEmptyOutcome("Module");
    }

    @Test @Ignore("default value is not used to generate the XML")
    public void counterIDWithDefault() throws Exception {
        checkEmptyOutcome("CounterID");
    }
}
