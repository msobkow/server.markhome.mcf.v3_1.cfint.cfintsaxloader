
// Description: Java 25 XML SAX Element Handler for SecClusGrpMemb

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
 *	CFIntSaxLoaderSecClusGrpMembParse XML SAX Element Handler implementation
 *	for SecClusGrpMemb.
 */
public class CFIntSaxLoaderSecClusGrpMemb
	extends CFLibXmlCoreElementHandler
{
	public CFIntSaxLoaderSecClusGrpMemb( CFIntSaxLoader saxLoader ) {
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
		ICFIntSecClusGrpMembObj origBuff = null;
		ICFIntSecClusGrpMembEditObj editBuff = null;
		// Common XML Attributes
		String attrId = null;
		// SecClusGrpMemb Attributes
		// SecClusGrpMemb References
		// Attribute Extraction
		String attrLocalName;
		int numAttrs;
		int idxAttr;
		final String S_LocalName = "LocalName";
		try {
			assert qName.equals( "SecClusGrpMemb" );

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
			origBuff = (ICFIntSecClusGrpMembObj)schemaObj.getSecClusGrpMembTableObj().newInstance();
			editBuff = (ICFIntSecClusGrpMembEditObj)origBuff.beginEdit();

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

			// Save named attributes to context
			CFLibXmlCoreContext curContext = getParser().getCurContext();
			curContext.putNamedValue( "Id", attrId );

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

			CFIntSaxLoader.LoaderBehaviourEnum loaderBehaviour = saxLoader.getSecClusGrpMembLoaderBehaviour();
			ICFIntSecClusGrpMembEditObj editSecClusGrpMemb = null;
			ICFIntSecClusGrpMembObj origSecClusGrpMemb = (ICFIntSecClusGrpMembObj)schemaObj.getSecClusGrpMembTableObj().readSecClusGrpMembByIdIdx( editBuff.getRequiredSecClusGrpId(),
			editBuff.getRequiredLoginId() );
			if( origSecClusGrpMemb == null ) {
				editSecClusGrpMemb = editBuff;
			}
			else {
				switch( loaderBehaviour ) {
					case Insert:
						break;
					case Update:
						editSecClusGrpMemb = (ICFIntSecClusGrpMembEditObj)origSecClusGrpMemb.beginEdit();
						break;
					case Replace:
						editSecClusGrpMemb = (ICFIntSecClusGrpMembEditObj)origSecClusGrpMemb.beginEdit();
						editSecClusGrpMemb.deleteInstance();
						editSecClusGrpMemb = null;
						origSecClusGrpMemb = null;
						editSecClusGrpMemb = editBuff;
						break;
				}
			}

			if( editSecClusGrpMemb != null ) {
				if( origSecClusGrpMemb != null ) {
					editSecClusGrpMemb.update();
				}
				else {
					origSecClusGrpMemb = (ICFIntSecClusGrpMembObj)editSecClusGrpMemb.create();
				}
				editSecClusGrpMemb = null;
			}

			curContext.putNamedValue( "Object", origSecClusGrpMemb );
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
