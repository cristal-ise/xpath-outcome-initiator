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
package org.cristalise.kernel.persistency.outcomebuilder.field;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.cristalise.kernel.persistency.outcomebuilder.InvalidOutcomeException;
import org.json.JSONObject;

public class DateTimeField extends DateField {

    public DateTimeField() {
        super();
    }

    @Override
    public String getValue(String valueTemplate) {
        if (valueTemplate.equals("now"))
            return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        else
            return valueTemplate;
    }

    @Override
    public JSONObject generateNgDynamicForms(Map<String, Object> inputs) {
        JSONObject date = super.generateNgDynamicForms(inputs);

        JSONObject additional = getAdditionalConfigNgDynamicForms(date);
        additional.put("showTime", true);

        return date;
    }

    @Override
    public void setValue(Object value) throws InvalidOutcomeException {
        super.setData(value.toString());
    }
}
