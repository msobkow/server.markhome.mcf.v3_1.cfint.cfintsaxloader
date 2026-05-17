
// Description: Java 25 XML SAX Element Handler for SecSysRoleEnables

/*
 *	server.markhome.mcf.CFInt
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFInt - Internet Essentials
 *	
 *	This file is part of Mark's Code Fractal CFInt.
 *	
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *	
 *	http://www.apache.org/licenses/LICENSE-2.0
 *	
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 *	
 */

package server.markhome.mcf.v3_1.cfint.cfintsaxloader;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.time.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.*;
import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.dbutil.*;
import server.markhome.mcf.v3_1.cflib.inz.Inz;
import server.markhome.mcf.v3_1.cflib.xml.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;

/*
 *	CFIntSaxLoaderSecSysRoleEnablesParse XML SAX Element Handler implementation
 *	for SecSysRoleEnables.
 */
public class CFIntSaxLoaderSecSysRoleEnables
	extends CFLibXmlCoreElementHandler
{
	public CFIntSaxLoaderSecSysRoleEnables( CFIntSaxLoader saxLoader ) {
		super( saxLoader );
	}

	public void startElement(
		String		uri,
		String		localName,
		String		qName,
		Attributes	attrs )
	throws SAXException
	{
		final String S_ProcName = "startElement";
		ICFIntSecSysRoleEnablesObj origBuff = null;
		ICFIntSecSysRoleEnablesEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// SecSysRoleEnables Attributes
		String attrEnableGroup = null;
		// SecSysRoleEnables References
		ICFIntSecSysRoleObj refSysRole = null;
		ICFIntSecSysGrpObj refEnableGroup = null;
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "SecSysRoleEnables" );

			CFIntSaxLoader saxLoader = (CFIntSaxLoader)getParser();
			if( saxLoader == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser()" );
			}

			ICFIntSchemaObj schemaObj = saxLoader.getSchemaObj();
			if( schemaObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"getParser().getSchemaObj()" );
			}

			// Instantiate an edit buffer for the parsed information
			origBuff = (ICFIntSecSysRoleEnablesObj)schemaObj.getSecSysRoleEnablesTableObj().newInstance();
			editBuff = (ICFIntSecSysRoleEnablesEditObj)origBuff.beginEdit();

			// Extract Attributes
			numAttrs = attrs.getLength();
			for( idxAttr = 0; idxAttr < numAttrs; idxAttr++ ) {
				attrLocalName = attrs.getLocalName( idxAttr );
				if( attrLocalName.equals( "Id" ) ) {
					if( attrId != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrId = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "EnableGroup" ) ) {
					if( attrEnableGroup != null ) {
						throw new CFLibUniqueIndexViolationException( getClass(),
							S_ProcName,
							S_LocalName,
							attrLocalName );
					}
					attrEnableGroup = attrs.getValue( idxAttr );
				}
				else if( attrLocalName.equals( "schemaLocation" ) ) {
					// ignored
				}
				else {
					throw new CFLibUnrecognizedAttributeException( getClass(),
						S_ProcName,
						getParser().getLocationInfo(),
						attrLocalName );
				}
			}

			// Ensure that required attributes have values
			if( ( attrEnableGroup == null ) || ( attrEnableGroup.length() <= 0 ) ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"EnableGroup" );
			}

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );
			curContext.putNamedValue( "EnableGroup", attrEnableGroup );

			// Convert string attributes to native Java types
			// and apply the converted attributes to the editBuff.

			Integer natId;
			if( ( attrId != null ) && ( attrId.length() > 0 ) ) {
				natId = Integer.valueOf( Integer.parseInt( attrId ) );
			}
			else {
				natId = null;
			}
			// Get the scope/container object

			CFLibXmlCoreContext parentContext = curContext.getPrevContext();
			Object scopeObj;
			if( parentContext != null ) {
				scopeObj = parentContext.getNamedValue( "Object" );
			}
			else {
				scopeObj = null;
			}

			// Resolve and apply required Container reference

			if( scopeObj == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"scopeObj" );
			}
			else if( scopeObj instanceof ICFIntSecSysRoleObj ) {
				refSysRole = (ICFIntSecSysRoleObj) scopeObj;
				editBuff.setRequiredContainerSysRole( refSysRole );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"scopeObj",
					scopeObj,
					"ICFIntSecSysRoleObj" );
			}

			// Lookup refEnableGroup by key name value attr
			if( ( attrEnableGroup != null ) && ( attrEnableGroup.length() > 0 ) ) {
				refEnableGroup = (ICFIntSecSysGrpObj)schemaObj.getSecSysGrpTableObj().readSecSysGrpByUNameIdx( attrEnableGroup );
				if( refEnableGroup == null ) {
					throw new CFLibNullArgumentException( getClass(),
						S_ProcName,
						0,
						"Resolve EnableGroup reference named \"" + attrEnableGroup + "\" to table SecSysGrp" );
				}
			}
			else {
				refEnableGroup = null;
			}
			editBuff.setRequiredParentEnableGroup( refEnableGroup );

			CFIntSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getSecSysRoleEnablesLoaderBehaviour();
			ICFIntSecSysRoleEnablesEditObj editSecSysRoleEnables = null;
			ICFIntSecSysRoleEnablesObj origSecSysRoleEnables = (ICFIntSecSysRoleEnablesObj)schemaObj.getSecSysRoleEnablesTableObj().readSecSysRoleEnablesByIdIdx( refSysRole.getRequiredSecSysRoleId(),
			refEnableGroup.getRequiredName() );
			if( origSecSysRoleEnables == null ) {
				editSecSysRoleEnables = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editSecSysRoleEnables = (ICFIntSecSysRoleEnablesEditObj)origSecSysRoleEnables.beginEdit();
						editSecSysRoleEnables.setRequiredParentEnableGroup( editBuff.getRequiredParentEnableGroup() );
						break;
					case Replace:
						editSecSysRoleEnables = (ICFIntSecSysRoleEnablesEditObj)origSecSysRoleEnables.beginEdit();
						editSecSysRoleEnables.deleteInstance();
						editSecSysRoleEnables = null;
						origSecSysRoleEnables = null;
						editSecSysRoleEnables = editBuff;
						break;
				}
			}

			if( editSecSysRoleEnables != null ) {
				if( origSecSysRoleEnables != null ) {
					editSecSysRoleEnables.update();
				}
				else {
					origSecSysRoleEnables = (ICFIntSecSysRoleEnablesObj)editSecSysRoleEnables.create();
				}
				editSecSysRoleEnables = null;
			}

			curContext.putNamedValue( "Object", origSecSysRoleEnables );
		}
		catch( RuntimeException e ) {
			throw new SAXException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getName() + " - " + e.getMessage(),
				e );
		}
		catch( Error e ) {
			throw new SAXException( "Near " + getParser().getLocationInfo() + ": Caught and rethrew " + e.getClass().getName() + " - " + e.getMessage() );
		}
	}

	public void endElement(
		String		uri,
		String		localName,
		String		qName )
	throws SAXException
	{
	}
}
