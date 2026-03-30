// Description: Java 25 XML SAX Parser for CFInt.

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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.*;
import java.math.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;
import org.apache.commons.codec.binary.Base64;
import org.xml.sax.*;

import server.markhome.mcf.v3_1.cflib.*;
import server.markhome.mcf.v3_1.cflib.xml.*;
import server.markhome.mcf.v3_1.cfsec.cfsec.*;
import server.markhome.mcf.v3_1.cfint.cfint.*;
import server.markhome.mcf.v3_1.cfsec.cfsecobj.*;
import server.markhome.mcf.v3_1.cfint.cfintobj.*;

public class CFIntSaxLoader
	extends CFLibXmlCoreSaxParser
	implements ContentHandler
{

	// The namespace URI of the supported schema
	public final static String	SCHEMA_XMLNS = "http://mcf.markhome.server/mcf/v3_1/xsd/cfint-structured.xsd";

	// The source for loading the supported schema
	public final static String	SCHEMA_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfint-structured.xsd";
	public final static String	SCHEMA_ROOT_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfint-structured.xsd";
	public final static String CFSEC_XMLNS = "http://mcf.markhome.server/mcf/v3_1/xsd/cfsec-structured.xsd";
	public final static String CFSEC_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfsec-structured.xsd";
	public final static String CFSEC_ROOT_URI = "http://mcf.markhome.server/mcf/v3_1/xsd/cfsec-structured.xsd";


	// The schema instance to load in to

	private ICFIntSchemaObj schemaObj = null;

	// The cluster to use for loading

	private ICFSecClusterObj useCluster = null;

	public ICFSecClusterObj getUseCluster() {
		return( useCluster );
	}

	public void setUseCluster( ICFSecClusterObj value ) {
		useCluster = value;
	}

	// The tenant to use for loading

	private ICFSecTenantObj useTenant = null;

	public ICFSecTenantObj getUseTenant() {
		return( useTenant );
	}

	public void setUseTenant( ICFSecTenantObj value ) {
		useTenant = value;
	}

	// Loader behaviour configuration attributes

	public enum LoaderBehaviourEnum {
		Insert,
		Update,
		Replace
	};
	private LoaderBehaviourEnum clusterLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum iSOCcyLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOCtryLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOCtryCcyLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum iSOCtryLangLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum iSOLangLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum iSOTZoneLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum licenseLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum majorVersionLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum mimeTypeLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum minorVersionLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum secClusGrpLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secClusGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secClusGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSessionLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSysGrpLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSysGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSysGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secTentGrpLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secTentGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secTentGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserEMConfLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserPWHistoryLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserPWResetLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserPasswordLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum subProjectLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum sysClusterLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum tenantLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum tldLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum topDomainLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum topProjectLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum uRLProtocolLoaderBehaviour = LoaderBehaviourEnum.Update;


	// Constructors

	public CFIntSaxLoader() {
		super();
		setRootElementHandler( getSaxRootHandler() );
		initParser();
	}

	public CFIntSaxLoader( ICFLibMessageLog logger ) {
		super( logger );
		setRootElementHandler( getSaxRootHandler() );
		initParser();
	}

	// Element Handler instances

	private CFIntSaxLoaderCluster clusterHandler = null;
	private CFIntSaxLoaderISOCcy iSOCcyHandler = null;
	private CFIntSaxLoaderISOCtry iSOCtryHandler = null;
	private CFIntSaxLoaderISOCtryCcy iSOCtryCcyHandler = null;
	private CFIntSaxLoaderISOCtryLang iSOCtryLangHandler = null;
	private CFIntSaxLoaderISOLang iSOLangHandler = null;
	private CFIntSaxLoaderISOTZone iSOTZoneHandler = null;
	private CFIntSaxLoaderLicense licenseHandler = null;
	private CFIntSaxLoaderMajorVersion majorVersionHandler = null;
	private CFIntSaxLoaderMimeType mimeTypeHandler = null;
	private CFIntSaxLoaderMinorVersion minorVersionHandler = null;
	private CFIntSaxLoaderSecClusGrp secClusGrpHandler = null;
	private CFIntSaxLoaderSecClusGrpInc secClusGrpIncHandler = null;
	private CFIntSaxLoaderSecClusGrpMemb secClusGrpMembHandler = null;
	private CFIntSaxLoaderSecSession secSessionHandler = null;
	private CFIntSaxLoaderSecSysGrp secSysGrpHandler = null;
	private CFIntSaxLoaderSecSysGrpInc secSysGrpIncHandler = null;
	private CFIntSaxLoaderSecSysGrpMemb secSysGrpMembHandler = null;
	private CFIntSaxLoaderSecTentGrp secTentGrpHandler = null;
	private CFIntSaxLoaderSecTentGrpInc secTentGrpIncHandler = null;
	private CFIntSaxLoaderSecTentGrpMemb secTentGrpMembHandler = null;
	private CFIntSaxLoaderSecUser secUserHandler = null;
	private CFIntSaxLoaderSecUserEMConf secUserEMConfHandler = null;
	private CFIntSaxLoaderSecUserPWHistory secUserPWHistoryHandler = null;
	private CFIntSaxLoaderSecUserPWReset secUserPWResetHandler = null;
	private CFIntSaxLoaderSecUserPassword secUserPasswordHandler = null;
	private CFIntSaxLoaderSubProject subProjectHandler = null;
	private CFIntSaxLoaderSysCluster sysClusterHandler = null;
	private CFIntSaxLoaderTenant tenantHandler = null;
	private CFIntSaxLoaderTld tldHandler = null;
	private CFIntSaxLoaderTopDomain topDomainHandler = null;
	private CFIntSaxLoaderTopProject topProjectHandler = null;
	private CFIntSaxLoaderURLProtocol uRLProtocolHandler = null;
	private CFIntSaxRootHandler saxRootHandler = null;

	private CFIntSaxDocHandler saxDocHandler = null;

	// Schema object accessors

	// SchemaObj accessors

	public ICFIntSchemaObj getSchemaObj() {
		return( schemaObj );
	}

	public void setSchemaObj( ICFIntSchemaObj value ) {
		schemaObj = value;
	}

	// Element Handler Resolver Factories

	protected CFIntSaxLoaderCluster getClusterHandler() {
		if( clusterHandler == null ) {
			clusterHandler = new CFIntSaxLoaderCluster( this );
			clusterHandler.addElementHandler( "Tenant", getTenantHandler() );
			clusterHandler.addElementHandler( "SysCluster", getSysClusterHandler() );
		}
		return( clusterHandler );
	}
	protected CFIntSaxLoaderISOCcy getISOCcyHandler() {
		if( iSOCcyHandler == null ) {
			iSOCcyHandler = new CFIntSaxLoaderISOCcy( this );
		}
		return( iSOCcyHandler );
	}
	protected CFIntSaxLoaderISOCtry getISOCtryHandler() {
		if( iSOCtryHandler == null ) {
			iSOCtryHandler = new CFIntSaxLoaderISOCtry( this );
			iSOCtryHandler.addElementHandler( "ISOCtryCcy", getISOCtryCcyHandler() );
			iSOCtryHandler.addElementHandler( "ISOCtryLang", getISOCtryLangHandler() );
		}
		return( iSOCtryHandler );
	}
	protected CFIntSaxLoaderISOCtryCcy getISOCtryCcyHandler() {
		if( iSOCtryCcyHandler == null ) {
			iSOCtryCcyHandler = new CFIntSaxLoaderISOCtryCcy( this );
		}
		return( iSOCtryCcyHandler );
	}
	protected CFIntSaxLoaderISOCtryLang getISOCtryLangHandler() {
		if( iSOCtryLangHandler == null ) {
			iSOCtryLangHandler = new CFIntSaxLoaderISOCtryLang( this );
		}
		return( iSOCtryLangHandler );
	}
	protected CFIntSaxLoaderISOLang getISOLangHandler() {
		if( iSOLangHandler == null ) {
			iSOLangHandler = new CFIntSaxLoaderISOLang( this );
		}
		return( iSOLangHandler );
	}
	protected CFIntSaxLoaderISOTZone getISOTZoneHandler() {
		if( iSOTZoneHandler == null ) {
			iSOTZoneHandler = new CFIntSaxLoaderISOTZone( this );
		}
		return( iSOTZoneHandler );
	}
	protected CFIntSaxLoaderLicense getLicenseHandler() {
		if( licenseHandler == null ) {
			licenseHandler = new CFIntSaxLoaderLicense( this );
		}
		return( licenseHandler );
	}
	protected CFIntSaxLoaderMajorVersion getMajorVersionHandler() {
		if( majorVersionHandler == null ) {
			majorVersionHandler = new CFIntSaxLoaderMajorVersion( this );
			majorVersionHandler.addElementHandler( "MinorVersion", getMinorVersionHandler() );
		}
		return( majorVersionHandler );
	}
	protected CFIntSaxLoaderMimeType getMimeTypeHandler() {
		if( mimeTypeHandler == null ) {
			mimeTypeHandler = new CFIntSaxLoaderMimeType( this );
		}
		return( mimeTypeHandler );
	}
	protected CFIntSaxLoaderMinorVersion getMinorVersionHandler() {
		if( minorVersionHandler == null ) {
			minorVersionHandler = new CFIntSaxLoaderMinorVersion( this );
		}
		return( minorVersionHandler );
	}
	protected CFIntSaxLoaderSecClusGrp getSecClusGrpHandler() {
		if( secClusGrpHandler == null ) {
			secClusGrpHandler = new CFIntSaxLoaderSecClusGrp( this );
		}
		return( secClusGrpHandler );
	}
	protected CFIntSaxLoaderSecClusGrpInc getSecClusGrpIncHandler() {
		if( secClusGrpIncHandler == null ) {
			secClusGrpIncHandler = new CFIntSaxLoaderSecClusGrpInc( this );
		}
		return( secClusGrpIncHandler );
	}
	protected CFIntSaxLoaderSecClusGrpMemb getSecClusGrpMembHandler() {
		if( secClusGrpMembHandler == null ) {
			secClusGrpMembHandler = new CFIntSaxLoaderSecClusGrpMemb( this );
		}
		return( secClusGrpMembHandler );
	}
	protected CFIntSaxLoaderSecSession getSecSessionHandler() {
		if( secSessionHandler == null ) {
			secSessionHandler = new CFIntSaxLoaderSecSession( this );
		}
		return( secSessionHandler );
	}
	protected CFIntSaxLoaderSecSysGrp getSecSysGrpHandler() {
		if( secSysGrpHandler == null ) {
			secSysGrpHandler = new CFIntSaxLoaderSecSysGrp( this );
		}
		return( secSysGrpHandler );
	}
	protected CFIntSaxLoaderSecSysGrpInc getSecSysGrpIncHandler() {
		if( secSysGrpIncHandler == null ) {
			secSysGrpIncHandler = new CFIntSaxLoaderSecSysGrpInc( this );
		}
		return( secSysGrpIncHandler );
	}
	protected CFIntSaxLoaderSecSysGrpMemb getSecSysGrpMembHandler() {
		if( secSysGrpMembHandler == null ) {
			secSysGrpMembHandler = new CFIntSaxLoaderSecSysGrpMemb( this );
		}
		return( secSysGrpMembHandler );
	}
	protected CFIntSaxLoaderSecTentGrp getSecTentGrpHandler() {
		if( secTentGrpHandler == null ) {
			secTentGrpHandler = new CFIntSaxLoaderSecTentGrp( this );
		}
		return( secTentGrpHandler );
	}
	protected CFIntSaxLoaderSecTentGrpInc getSecTentGrpIncHandler() {
		if( secTentGrpIncHandler == null ) {
			secTentGrpIncHandler = new CFIntSaxLoaderSecTentGrpInc( this );
		}
		return( secTentGrpIncHandler );
	}
	protected CFIntSaxLoaderSecTentGrpMemb getSecTentGrpMembHandler() {
		if( secTentGrpMembHandler == null ) {
			secTentGrpMembHandler = new CFIntSaxLoaderSecTentGrpMemb( this );
		}
		return( secTentGrpMembHandler );
	}
	protected CFIntSaxLoaderSecUser getSecUserHandler() {
		if( secUserHandler == null ) {
			secUserHandler = new CFIntSaxLoaderSecUser( this );
		}
		return( secUserHandler );
	}
	protected CFIntSaxLoaderSecUserEMConf getSecUserEMConfHandler() {
		if( secUserEMConfHandler == null ) {
			secUserEMConfHandler = new CFIntSaxLoaderSecUserEMConf( this );
		}
		return( secUserEMConfHandler );
	}
	protected CFIntSaxLoaderSecUserPWHistory getSecUserPWHistoryHandler() {
		if( secUserPWHistoryHandler == null ) {
			secUserPWHistoryHandler = new CFIntSaxLoaderSecUserPWHistory( this );
		}
		return( secUserPWHistoryHandler );
	}
	protected CFIntSaxLoaderSecUserPWReset getSecUserPWResetHandler() {
		if( secUserPWResetHandler == null ) {
			secUserPWResetHandler = new CFIntSaxLoaderSecUserPWReset( this );
		}
		return( secUserPWResetHandler );
	}
	protected CFIntSaxLoaderSecUserPassword getSecUserPasswordHandler() {
		if( secUserPasswordHandler == null ) {
			secUserPasswordHandler = new CFIntSaxLoaderSecUserPassword( this );
		}
		return( secUserPasswordHandler );
	}
	protected CFIntSaxLoaderSubProject getSubProjectHandler() {
		if( subProjectHandler == null ) {
			subProjectHandler = new CFIntSaxLoaderSubProject( this );
			subProjectHandler.addElementHandler( "MajorVersion", getMajorVersionHandler() );
		}
		return( subProjectHandler );
	}
	protected CFIntSaxLoaderSysCluster getSysClusterHandler() {
		if( sysClusterHandler == null ) {
			sysClusterHandler = new CFIntSaxLoaderSysCluster( this );
		}
		return( sysClusterHandler );
	}
	protected CFIntSaxLoaderTenant getTenantHandler() {
		if( tenantHandler == null ) {
			tenantHandler = new CFIntSaxLoaderTenant( this );
			tenantHandler.addElementHandler( "Tld", getTldHandler() );
		}
		return( tenantHandler );
	}
	protected CFIntSaxLoaderTld getTldHandler() {
		if( tldHandler == null ) {
			tldHandler = new CFIntSaxLoaderTld( this );
			tldHandler.addElementHandler( "TopDomain", getTopDomainHandler() );
		}
		return( tldHandler );
	}
	protected CFIntSaxLoaderTopDomain getTopDomainHandler() {
		if( topDomainHandler == null ) {
			topDomainHandler = new CFIntSaxLoaderTopDomain( this );
			topDomainHandler.addElementHandler( "TopProject", getTopProjectHandler() );
			topDomainHandler.addElementHandler( "License", getLicenseHandler() );
		}
		return( topDomainHandler );
	}
	protected CFIntSaxLoaderTopProject getTopProjectHandler() {
		if( topProjectHandler == null ) {
			topProjectHandler = new CFIntSaxLoaderTopProject( this );
			topProjectHandler.addElementHandler( "SubProject", getSubProjectHandler() );
		}
		return( topProjectHandler );
	}
	protected CFIntSaxLoaderURLProtocol getURLProtocolHandler() {
		if( uRLProtocolHandler == null ) {
			uRLProtocolHandler = new CFIntSaxLoaderURLProtocol( this );
		}
		return( uRLProtocolHandler );
	}
	// Root Element Handler Resolver Factory

	protected CFIntSaxRootHandler getSaxRootHandler() {
		if( saxRootHandler == null ) {
			saxRootHandler = new CFIntSaxRootHandler( this );
			saxRootHandler.addElementHandler( "CFInt", getSaxDocHandler() );
			saxRootHandler.addElementHandler( "CFSec", getSaxDocHandler() );
		}
		return( saxRootHandler );
	}

	// Root Element Handler

	/*
	 *	CFIntSaxRootHandler XML SAX Root Element Handler implementation
	 */
	public class CFIntSaxRootHandler
		extends CFLibXmlCoreElementHandler
	{
		public CFIntSaxRootHandler( CFIntSaxLoader saxLoader ) {
			super( saxLoader );
		}

		public void startElement(
			String		uri,
			String		localName,
			String		qName,
			Attributes	attrs )
		throws SAXException
		{
		}

		public void endElement(
			String		uri,
			String		localName,
			String		qName )
		throws SAXException
		{
		}
	}

	// Document Element Handler Resolver Factory

	protected CFIntSaxDocHandler getSaxDocHandler() {
		if( saxDocHandler == null ) {
			saxDocHandler = new CFIntSaxDocHandler( this );
			saxDocHandler.addElementHandler( "Cluster", getClusterHandler() );
			saxDocHandler.addElementHandler( "ISOCcy", getISOCcyHandler() );
			saxDocHandler.addElementHandler( "ISOCtry", getISOCtryHandler() );
			saxDocHandler.addElementHandler( "ISOLang", getISOLangHandler() );
			saxDocHandler.addElementHandler( "ISOTZone", getISOTZoneHandler() );
			saxDocHandler.addElementHandler( "MimeType", getMimeTypeHandler() );
			saxDocHandler.addElementHandler( "SecClusGrp", getSecClusGrpHandler() );
			saxDocHandler.addElementHandler( "SecSession", getSecSessionHandler() );
			saxDocHandler.addElementHandler( "SecSysGrp", getSecSysGrpHandler() );
			saxDocHandler.addElementHandler( "SecTentGrp", getSecTentGrpHandler() );
			saxDocHandler.addElementHandler( "SecUser", getSecUserHandler() );
			saxDocHandler.addElementHandler( "SecUserPWHistory", getSecUserPWHistoryHandler() );
			saxDocHandler.addElementHandler( "URLProtocol", getURLProtocolHandler() );
		}
		return( saxDocHandler );
	}

	// Document Element Handler

	/*
	 *	CFIntSaxDocHandler XML SAX Doc Element Handler implementation
	 */
	public class CFIntSaxDocHandler
		extends CFLibXmlCoreElementHandler
	{
		public CFIntSaxDocHandler( CFIntSaxLoader saxLoader ) {
			super( saxLoader );
		}

		public void startElement(
			String		uri,
			String		localName,
			String		qName,
			Attributes	attrs )
		throws SAXException
		{
		}

		public void endElement(
			String		uri,
			String		localName,
			String		qName )
		throws SAXException
		{
		}
	}

	// Loader behaviour configuration accessors

	public LoaderBehaviourEnum getClusterLoaderBehaviour() {
		return( clusterLoaderBehaviour );
	}

	public void setClusterLoaderBehaviour( LoaderBehaviourEnum value ) {
		clusterLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCcyLoaderBehaviour() {
		return( iSOCcyLoaderBehaviour );
	}

	public void setISOCcyLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCcyLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCtryLoaderBehaviour() {
		return( iSOCtryLoaderBehaviour );
	}

	public void setISOCtryLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCtryLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCtryCcyLoaderBehaviour() {
		return( iSOCtryCcyLoaderBehaviour );
	}

	public void setISOCtryCcyLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCtryCcyLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOCtryLangLoaderBehaviour() {
		return( iSOCtryLangLoaderBehaviour );
	}

	public void setISOCtryLangLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOCtryLangLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOLangLoaderBehaviour() {
		return( iSOLangLoaderBehaviour );
	}

	public void setISOLangLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOLangLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getISOTZoneLoaderBehaviour() {
		return( iSOTZoneLoaderBehaviour );
	}

	public void setISOTZoneLoaderBehaviour( LoaderBehaviourEnum value ) {
		iSOTZoneLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getLicenseLoaderBehaviour() {
		return( licenseLoaderBehaviour );
	}

	public void setLicenseLoaderBehaviour( LoaderBehaviourEnum value ) {
		licenseLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getMajorVersionLoaderBehaviour() {
		return( majorVersionLoaderBehaviour );
	}

	public void setMajorVersionLoaderBehaviour( LoaderBehaviourEnum value ) {
		majorVersionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getMimeTypeLoaderBehaviour() {
		return( mimeTypeLoaderBehaviour );
	}

	public void setMimeTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		mimeTypeLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getMinorVersionLoaderBehaviour() {
		return( minorVersionLoaderBehaviour );
	}

	public void setMinorVersionLoaderBehaviour( LoaderBehaviourEnum value ) {
		minorVersionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecClusGrpLoaderBehaviour() {
		return( secClusGrpLoaderBehaviour );
	}

	public void setSecClusGrpLoaderBehaviour( LoaderBehaviourEnum value ) {
		secClusGrpLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecClusGrpIncLoaderBehaviour() {
		return( secClusGrpIncLoaderBehaviour );
	}

	public void setSecClusGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secClusGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecClusGrpMembLoaderBehaviour() {
		return( secClusGrpMembLoaderBehaviour );
	}

	public void setSecClusGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secClusGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSessionLoaderBehaviour() {
		return( secSessionLoaderBehaviour );
	}

	public void setSecSessionLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSessionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSysGrpLoaderBehaviour() {
		return( secSysGrpLoaderBehaviour );
	}

	public void setSecSysGrpLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSysGrpLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSysGrpIncLoaderBehaviour() {
		return( secSysGrpIncLoaderBehaviour );
	}

	public void setSecSysGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSysGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSysGrpMembLoaderBehaviour() {
		return( secSysGrpMembLoaderBehaviour );
	}

	public void setSecSysGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSysGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecTentGrpLoaderBehaviour() {
		return( secTentGrpLoaderBehaviour );
	}

	public void setSecTentGrpLoaderBehaviour( LoaderBehaviourEnum value ) {
		secTentGrpLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecTentGrpIncLoaderBehaviour() {
		return( secTentGrpIncLoaderBehaviour );
	}

	public void setSecTentGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secTentGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecTentGrpMembLoaderBehaviour() {
		return( secTentGrpMembLoaderBehaviour );
	}

	public void setSecTentGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secTentGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserLoaderBehaviour() {
		return( secUserLoaderBehaviour );
	}

	public void setSecUserLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserEMConfLoaderBehaviour() {
		return( secUserEMConfLoaderBehaviour );
	}

	public void setSecUserEMConfLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserEMConfLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserPWHistoryLoaderBehaviour() {
		return( secUserPWHistoryLoaderBehaviour );
	}

	public void setSecUserPWHistoryLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserPWHistoryLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserPWResetLoaderBehaviour() {
		return( secUserPWResetLoaderBehaviour );
	}

	public void setSecUserPWResetLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserPWResetLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserPasswordLoaderBehaviour() {
		return( secUserPasswordLoaderBehaviour );
	}

	public void setSecUserPasswordLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserPasswordLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSubProjectLoaderBehaviour() {
		return( subProjectLoaderBehaviour );
	}

	public void setSubProjectLoaderBehaviour( LoaderBehaviourEnum value ) {
		subProjectLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSysClusterLoaderBehaviour() {
		return( sysClusterLoaderBehaviour );
	}

	public void setSysClusterLoaderBehaviour( LoaderBehaviourEnum value ) {
		sysClusterLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTenantLoaderBehaviour() {
		return( tenantLoaderBehaviour );
	}

	public void setTenantLoaderBehaviour( LoaderBehaviourEnum value ) {
		tenantLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTldLoaderBehaviour() {
		return( tldLoaderBehaviour );
	}

	public void setTldLoaderBehaviour( LoaderBehaviourEnum value ) {
		tldLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTopDomainLoaderBehaviour() {
		return( topDomainLoaderBehaviour );
	}

	public void setTopDomainLoaderBehaviour( LoaderBehaviourEnum value ) {
		topDomainLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTopProjectLoaderBehaviour() {
		return( topProjectLoaderBehaviour );
	}

	public void setTopProjectLoaderBehaviour( LoaderBehaviourEnum value ) {
		topProjectLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getURLProtocolLoaderBehaviour() {
		return( uRLProtocolLoaderBehaviour );
	}

	public void setURLProtocolLoaderBehaviour( LoaderBehaviourEnum value ) {
		uRLProtocolLoaderBehaviour = value;
	}

	// Parse a file

	public void parseFile( String url ) {
		parse( url );
	}
}
