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

import java.util.Map;
import java.util.Map.Entry;

import org.cristalise.kernel.persistency.outcomebuilder.StructuralException;
import org.cristalise.kernel.utils.Logger;
import org.exolab.castor.types.AnyNode;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.SimpleType;
import org.json.JSONArray;
import org.json.JSONObject;

public class ComboField extends StringField {

    ListOfValues vals;
    String selected;

    public ComboField(SimpleType type, AnyNode listNode) {
        super();
        contentType = type;
        vals = new ListOfValues();
        vals.createLOV(type, listNode);
    }

    @Override
    public String getDefaultValue() {
        if (vals.getDefaultKey() != null) return vals.get(vals.getDefaultKey()).toString();
        else                              return "";
    }

    public void setDefaultValue(String defaultVal) {
        vals.setDefaultValue(defaultVal);
    }

    @Override
    public String getText() {
        return vals.get(selected).toString();
    }

    @Override
    public void setText(String text) {
        if (vals.containsValue(text)) {
            selected = vals.findKey(text);
        }
        else
            Logger.error("Illegal value for ComboField name:'"+getName()+"' value:'"+text+"'");
    }

    @Override
    public void setDecl(AttributeDecl model) throws StructuralException {
        super.setDecl(model);
        setDefaultValue(model.getDefaultValue());
    }

    @Override
    public void setDecl(ElementDecl model) throws StructuralException {
        super.setDecl(model);
        setDefaultValue(model.getDefaultValue());
    }

    @Override
    public String getNgDynamicFormsControlType() {
        return "SELECT";
    }

    private JSONArray getNgDynamicFormsOptions() {
        JSONArray options = new JSONArray();

        JSONObject emptyOption = new JSONObject();
        emptyOption.put("label", "Select value");
        //emptyOption.put("value", null);
 
        options.put(emptyOption);

        for (Entry<String, Object> entry: vals.entrySet()) {
            JSONObject option = new JSONObject();

            option.put("label", entry.getKey());
            option.put("value", entry.getValue());

            options.put(option);
        }

        return options;
    }

    @Override
    public JSONObject generateNgDynamicForms(Map<String, Object> inputs) {
        JSONObject select = getCommonFieldsNgDynamicForms();

        select.put("options", getNgDynamicFormsOptions());

        return select;
    }
}