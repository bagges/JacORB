/*
 *        JacORB - a free Java ORB
 *
 *   Copyright (C) 1999-2002 Gerald Brose
 *
 *   This library is free software; you can redistribute it and/or
 *   modify it under the terms of the GNU Library General Public
 *   License as published by the Free Software Foundation; either
 *   version 2 of the License, or (at your option) any later version.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *   Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this library; if not, write to the Free
 *   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */
package org.jacorb.ir.gui.remoteobject;

import org.jacorb.ir.gui.typesystem.*;
import org.jacorb.ir.gui.typesystem.remote.*;
import org.omg.CORBA.*;
/**
 * This class was generated by a SmartGuide.
 * 
 */
public class RemoteObject 
    extends ObjectRepresentant implements AbstractContainer
{


    /**
     * This method was created by a SmartGuide.
     * @param counterPart java.lang.Object
     */
    protected RemoteObject (org.omg.CORBA.Object counterPart, 
                            TypeSystemNode type, String name) 
    {
	super(counterPart,type,name);
    
}
    /**
     * F�r alle Attributes erzeuge ObjectRepresentants und gebe sie zur�ck
     * @return ModelParticipant[]
     */
    public ModelParticipant[] contents() {
	if (counterPart!=null) {
            IRAttribute[] allFields = (IRAttribute[])((Interface)this.typeSystemNode).getAllFields();
            ObjectRepresentant[] result = new ObjectRepresentant[allFields.length];
            for (int i=0; i<allFields.length; i++) {
                Request request = ((org.omg.CORBA.Object)counterPart)._request("_get_"+allFields[i].getName());
                IRNode associatedNode = (IRNode)allFields[i].getAssociatedTypeSystemNode();
                request.set_return_type(associatedNode.getTypeCode());	// was ist mit Alias-Typen?
                try {
                    request.invoke();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }	
                Any any = request.return_value();	
                if (any!=null) {
                    result[i] = ObjectRepresentantFactory.create(
                                                                 ObjectRepresentantFactory.objectFromAny(any),
                                                                 allFields[i].getAssociatedTypeSystemNode(),
                                                                 allFields[i]);		
                }
                else {
                    result[i] = ObjectRepresentantFactory.create(null, allFields[i].getAssociatedTypeSystemNode(),allFields[i]);
                }		
            }	
            return result;
	}	// if (counterPart!=null)
	else {
            return new ModelParticipant[0];
	}	
    }
    /**
     * This method was created by a SmartGuide.
     * @return java.lang.Object
     * @param op org.jacorb.ir;.gui.typesystem.remote.IROperation
     * @param params java.lang.String[]
     */
    public java.lang.Object invokeOperation(IROperation op, String[] params) {
	ModelParticipant[] irParams = op.contents();
	Request request =  ((org.omg.CORBA.Object)counterPart)._request(op.getName());
	IRNode associatedNode = (IRNode)op.getAssociatedTypeSystemNode();
	request.set_return_type(associatedNode.getTypeCode());	// was ist mit Alias-Typen?	
	for (int i=0; i<irParams.length; i++) {
            IRParameter irParam = (IRParameter)irParams[i];
            switch (irParam.getMode().value()) {
            case ParameterMode._PARAM_IN:
                ObjectRepresentantFactory.insertFromString(request.add_in_arg(),params[i],irParam.getTypeCode().kind());
                break;
            case ParameterMode._PARAM_OUT:
                ObjectRepresentantFactory.insertFromString(request.add_out_arg(),params[i],irParam.getTypeCode().kind());
                break;
            case ParameterMode._PARAM_INOUT:
                ObjectRepresentantFactory.insertFromString(request.add_inout_arg(),params[i],irParam.getTypeCode().kind());
                break;
            }	
	}					
	request.invoke();	
	Any any = request.return_value();	
	if (any!=null) {
            return ObjectRepresentantFactory.objectFromAny(any);
	}	
	return null;
    }
}











