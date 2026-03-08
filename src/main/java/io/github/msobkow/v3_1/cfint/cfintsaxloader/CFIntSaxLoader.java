// Description: Java 25 XML SAX Parser for CFInt.

/*
 *	io.github.msobkow.CFInt
 *
 *	Copyright (c) 2016-2026 Mark Stephen Sobkow
 *	
 *	Mark's Code Fractal 3.1 CFInt - Internet Essentials
 *	
 *	This file is part of Mark's Code Fractal CFInt.
 *	
 *	Mark's Code Fractal CFInt is available under dual commercial license from
 *	Mark Stephen Sobkow, or under the terms of the GNU Library General Public License,
 *	Version 3 or later.
 *	
 *	Mark's Code Fractal CFInt is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Library General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	Mark's Code Fractal CFInt is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU Library General Public License
 *	along with Mark's Code Fractal CFInt.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	If you wish to modify and use this code without publishing your changes in order to
 *	tie it to proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 *	
 */

package io.github.msobkow.v3_1.cfint.cfintsaxloader;

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

import io.github.msobkow.v3_1.cflib.*;
import io.github.msobkow.v3_1.cflib.xml.*;
import io.github.msobkow.v3_1.cfsec.cfsec.*;
import io.github.msobkow.v3_1.cfint.cfint.*;
import io.github.msobkow.v3_1.cfsec.cfsecobj.*;
import io.github.msobkow.v3_1.cfint.cfintobj.*;

public class CFIntSaxLoader
	extends CFLibXmlCoreSaxParser
	implements ContentHandler
{

	// The namespace URI of the supported schema
	public final static String	SCHEMA_XMLNS = "https://msobkow.github.io/msobkow/v3_1/xsd/cfint-structured.xsd";

	// The source for loading the supported schema
	public final static String	SCHEMA_URI = "https://msobkow.github.io/msobkow/v3_1/xsd/cfint-structured.xsd";
	public final static String	SCHEMA_ROOT_URI = "https://msobkow.github.io/msobkow/v3_1/xsd/cfint-structured.xsd";
	public final static String CFSEC_XMLNS = "https://msobkow.github.io/msobkow/v3_1/xsd/cfsec-structured.xsd";
	public final static String CFSEC_URI = "https://msobkow.github.io/msobkow/xsd/v3_1/cfsec-structured.xsd";
	public final static String CFSEC_ROOT_URI = "https://msobkow.github.io/msobkow/xsd/v3_1/cfsec-structured.xsd";


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
	private LoaderBehaviourEnum hostNodeLoaderBehaviour = LoaderBehaviourEnum.Update;
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
	private LoaderBehaviourEnum secDeviceLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secGroupLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secSessionLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum secUserLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum serviceLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum serviceTypeLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum subProjectLoaderBehaviour = LoaderBehaviourEnum.Update;
	private LoaderBehaviourEnum sysClusterLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum tSecGroupLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum tSecGrpIncLoaderBehaviour = LoaderBehaviourEnum.Insert;
	private LoaderBehaviourEnum tSecGrpMembLoaderBehaviour = LoaderBehaviourEnum.Insert;
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
	private CFIntSaxLoaderHostNode hostNodeHandler = null;
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
	private CFIntSaxLoaderSecDevice secDeviceHandler = null;
	private CFIntSaxLoaderSecGroup secGroupHandler = null;
	private CFIntSaxLoaderSecGrpInc secGrpIncHandler = null;
	private CFIntSaxLoaderSecGrpMemb secGrpMembHandler = null;
	private CFIntSaxLoaderSecSession secSessionHandler = null;
	private CFIntSaxLoaderSecUser secUserHandler = null;
	private CFIntSaxLoaderService serviceHandler = null;
	private CFIntSaxLoaderServiceType serviceTypeHandler = null;
	private CFIntSaxLoaderSubProject subProjectHandler = null;
	private CFIntSaxLoaderSysCluster sysClusterHandler = null;
	private CFIntSaxLoaderTSecGroup tSecGroupHandler = null;
	private CFIntSaxLoaderTSecGrpInc tSecGrpIncHandler = null;
	private CFIntSaxLoaderTSecGrpMemb tSecGrpMembHandler = null;
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
			clusterHandler.addElementHandler( "HostNode", getHostNodeHandler() );
			clusterHandler.addElementHandler( "Tenant", getTenantHandler() );
			clusterHandler.addElementHandler( "SecGroup", getSecGroupHandler() );
			clusterHandler.addElementHandler( "SysCluster", getSysClusterHandler() );
		}
		return( clusterHandler );
	}
	protected CFIntSaxLoaderHostNode getHostNodeHandler() {
		if( hostNodeHandler == null ) {
			hostNodeHandler = new CFIntSaxLoaderHostNode( this );
			hostNodeHandler.addElementHandler( "Service", getServiceHandler() );
		}
		return( hostNodeHandler );
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
	protected CFIntSaxLoaderSecDevice getSecDeviceHandler() {
		if( secDeviceHandler == null ) {
			secDeviceHandler = new CFIntSaxLoaderSecDevice( this );
		}
		return( secDeviceHandler );
	}
	protected CFIntSaxLoaderSecGroup getSecGroupHandler() {
		if( secGroupHandler == null ) {
			secGroupHandler = new CFIntSaxLoaderSecGroup( this );
			secGroupHandler.addElementHandler( "SecGrpInc", getSecGrpIncHandler() );
			secGroupHandler.addElementHandler( "SecGrpMemb", getSecGrpMembHandler() );
		}
		return( secGroupHandler );
	}
	protected CFIntSaxLoaderSecGrpInc getSecGrpIncHandler() {
		if( secGrpIncHandler == null ) {
			secGrpIncHandler = new CFIntSaxLoaderSecGrpInc( this );
		}
		return( secGrpIncHandler );
	}
	protected CFIntSaxLoaderSecGrpMemb getSecGrpMembHandler() {
		if( secGrpMembHandler == null ) {
			secGrpMembHandler = new CFIntSaxLoaderSecGrpMemb( this );
		}
		return( secGrpMembHandler );
	}
	protected CFIntSaxLoaderSecSession getSecSessionHandler() {
		if( secSessionHandler == null ) {
			secSessionHandler = new CFIntSaxLoaderSecSession( this );
		}
		return( secSessionHandler );
	}
	protected CFIntSaxLoaderSecUser getSecUserHandler() {
		if( secUserHandler == null ) {
			secUserHandler = new CFIntSaxLoaderSecUser( this );
			secUserHandler.addElementHandler( "SecDevice", getSecDeviceHandler() );
		}
		return( secUserHandler );
	}
	protected CFIntSaxLoaderService getServiceHandler() {
		if( serviceHandler == null ) {
			serviceHandler = new CFIntSaxLoaderService( this );
		}
		return( serviceHandler );
	}
	protected CFIntSaxLoaderServiceType getServiceTypeHandler() {
		if( serviceTypeHandler == null ) {
			serviceTypeHandler = new CFIntSaxLoaderServiceType( this );
		}
		return( serviceTypeHandler );
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
	protected CFIntSaxLoaderTSecGroup getTSecGroupHandler() {
		if( tSecGroupHandler == null ) {
			tSecGroupHandler = new CFIntSaxLoaderTSecGroup( this );
			tSecGroupHandler.addElementHandler( "TSecGrpInc", getTSecGrpIncHandler() );
			tSecGroupHandler.addElementHandler( "TSecGrpMemb", getTSecGrpMembHandler() );
		}
		return( tSecGroupHandler );
	}
	protected CFIntSaxLoaderTSecGrpInc getTSecGrpIncHandler() {
		if( tSecGrpIncHandler == null ) {
			tSecGrpIncHandler = new CFIntSaxLoaderTSecGrpInc( this );
		}
		return( tSecGrpIncHandler );
	}
	protected CFIntSaxLoaderTSecGrpMemb getTSecGrpMembHandler() {
		if( tSecGrpMembHandler == null ) {
			tSecGrpMembHandler = new CFIntSaxLoaderTSecGrpMemb( this );
		}
		return( tSecGrpMembHandler );
	}
	protected CFIntSaxLoaderTenant getTenantHandler() {
		if( tenantHandler == null ) {
			tenantHandler = new CFIntSaxLoaderTenant( this );
			tenantHandler.addElementHandler( "TSecGroup", getTSecGroupHandler() );
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
			saxDocHandler.addElementHandler( "SecSession", getSecSessionHandler() );
			saxDocHandler.addElementHandler( "SecUser", getSecUserHandler() );
			saxDocHandler.addElementHandler( "ServiceType", getServiceTypeHandler() );
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

	public LoaderBehaviourEnum getHostNodeLoaderBehaviour() {
		return( hostNodeLoaderBehaviour );
	}

	public void setHostNodeLoaderBehaviour( LoaderBehaviourEnum value ) {
		hostNodeLoaderBehaviour = value;
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

	public LoaderBehaviourEnum getSecDeviceLoaderBehaviour() {
		return( secDeviceLoaderBehaviour );
	}

	public void setSecDeviceLoaderBehaviour( LoaderBehaviourEnum value ) {
		secDeviceLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecGroupLoaderBehaviour() {
		return( secGroupLoaderBehaviour );
	}

	public void setSecGroupLoaderBehaviour( LoaderBehaviourEnum value ) {
		secGroupLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecGrpIncLoaderBehaviour() {
		return( secGrpIncLoaderBehaviour );
	}

	public void setSecGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		secGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecGrpMembLoaderBehaviour() {
		return( secGrpMembLoaderBehaviour );
	}

	public void setSecGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		secGrpMembLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecSessionLoaderBehaviour() {
		return( secSessionLoaderBehaviour );
	}

	public void setSecSessionLoaderBehaviour( LoaderBehaviourEnum value ) {
		secSessionLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getSecUserLoaderBehaviour() {
		return( secUserLoaderBehaviour );
	}

	public void setSecUserLoaderBehaviour( LoaderBehaviourEnum value ) {
		secUserLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getServiceLoaderBehaviour() {
		return( serviceLoaderBehaviour );
	}

	public void setServiceLoaderBehaviour( LoaderBehaviourEnum value ) {
		serviceLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getServiceTypeLoaderBehaviour() {
		return( serviceTypeLoaderBehaviour );
	}

	public void setServiceTypeLoaderBehaviour( LoaderBehaviourEnum value ) {
		serviceTypeLoaderBehaviour = value;
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

	public LoaderBehaviourEnum getTSecGroupLoaderBehaviour() {
		return( tSecGroupLoaderBehaviour );
	}

	public void setTSecGroupLoaderBehaviour( LoaderBehaviourEnum value ) {
		tSecGroupLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTSecGrpIncLoaderBehaviour() {
		return( tSecGrpIncLoaderBehaviour );
	}

	public void setTSecGrpIncLoaderBehaviour( LoaderBehaviourEnum value ) {
		tSecGrpIncLoaderBehaviour = value;
	}

	public LoaderBehaviourEnum getTSecGrpMembLoaderBehaviour() {
		return( tSecGrpMembLoaderBehaviour );
	}

	public void setTSecGrpMembLoaderBehaviour( LoaderBehaviourEnum value ) {
		tSecGrpMembLoaderBehaviour = value;
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
