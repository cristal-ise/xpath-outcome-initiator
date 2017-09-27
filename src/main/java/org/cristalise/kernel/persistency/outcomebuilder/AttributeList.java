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
package org.cristalise.kernel.persistency.outcomebuilder;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import org.cristalise.kernel.persistency.outcomebuilder.field.StringField;
import org.cristalise.kernel.utils.Logger;
import org.exolab.castor.xml.schema.AttributeDecl;
import org.exolab.castor.xml.schema.ComplexType;
import org.exolab.castor.xml.schema.ElementDecl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class AttributeList extends OutcomeStructure {

    ArrayList<StringField> attrSet = new ArrayList<StringField>();

    public AttributeList(ElementDecl model) {
        AttributeDecl thisDecl;
        this.model = model;

        // simple types have no attributes
        if (model.getType().isSimpleType()) return;

        ComplexType content = (ComplexType)model.getType();

        for (Enumeration<?> fields = content.getAttributeDecls(); fields.hasMoreElements();) {
            thisDecl = (AttributeDecl)fields.nextElement();
            Logger.msg(8, "Includes Attribute "+thisDecl.getName());

            // read help
            help = OutcomeStructure.extractHelp(thisDecl);

            // Add entry
            try {
                StringField entry = StringField.getEditField(thisDecl);
                attrSet.add(entry);
                //if (readOnly) entry.setEditable(false);
            }
            catch (StructuralException e) {
                Logger.error(e);
            }
        }
    }

    public void setInstance(Element data) throws StructuralException {
        this.myElement = data;
        for (StringField thisField : attrSet) {
            Logger.msg(8, "Populating Attribute "+thisField.getName());
            Attr thisAttr = myElement.getAttributeNode(thisField.getName());
            if (thisAttr == null)
                thisAttr = newAttribute(myElement, (AttributeDecl)thisField.getModel());
            thisField.setData(thisAttr);
        }
    }

    public Attr newAttribute(Element parent, AttributeDecl attr) {

        parent.setAttribute(attr.getName(), attr.getFixedValue()!=null?attr.getFixedValue():attr.getDefaultValue());
        return parent.getAttributeNode(attr.getName());
    }

    public String validateAttributes() {
        if (model.getType().isComplexType()) {
            ComplexType content = (ComplexType)model.getType();

            for (Enumeration<?> fields = content.getAttributeDecls(); fields.hasMoreElements();) {
                AttributeDecl thisDecl = (AttributeDecl)fields.nextElement();
                String attrVal = myElement.getAttribute(thisDecl.getName());

                if (attrVal.length() == 0 && thisDecl.isOptional()) {
                    myElement.removeAttribute(thisDecl.getName());
                }
            }
        }
        return null;
    }

    public void initNew(Element parent) {
        AttributeDecl thisDecl;
        StringField thisField;
        Attr thisAttr;
        this.myElement = parent;

        if (model.getType().isSimpleType()) return; // no attributes in simple types

        ComplexType content = (ComplexType)model.getType();

        for (Iterator<StringField> e = attrSet.iterator(); e.hasNext();) {
            thisField = e.next();

            thisDecl = content.getAttributeDecl(thisField.getName());
            // HACK: if we don't resolve the reference, the type will be null
            if (thisDecl.isReference()) thisDecl = thisDecl.getReference();
            thisAttr = newAttribute(myElement, thisDecl);
            // add into parent - fill in field
            try {
                thisField.setData(thisAttr);
            } catch (Exception ex) { } // impossible name mismatch
        }
    }

    @Override
    public void addInstance(Element myElement, Document parentDoc) throws OutcomeException {
        // TODO Auto-generated method stub
    }

    @Override
    public Element initNew(Document parent) {
        // TODO Auto-generated method stub
        return null;
    }
}