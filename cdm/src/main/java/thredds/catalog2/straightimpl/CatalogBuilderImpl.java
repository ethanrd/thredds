/*
 * Copyright 1998-2009 University Corporation for Atmospheric Research/Unidata
 *
 * Portions of this software were developed by the Unidata Program at the
 * University Corporation for Atmospheric Research.
 *
 * Access and use of this software shall impose the following obligations
 * and understandings on the user. The user is granted the right, without
 * any fee or cost, to use, copy, modify, alter, enhance and distribute
 * this software, and any derivative works thereof, and its supporting
 * documentation for any purpose whatsoever, provided that this entire
 * notice appears in all copies of the software, derivative works and
 * supporting documentation.  Further, UCAR requests that the user credit
 * UCAR/Unidata in any publications that result from the use of this
 * software or in any product that includes this software. The names UCAR
 * and/or Unidata, however, may not be used in any advertising or publicity
 * to endorse or promote any products or commercial entity unless specific
 * written permission is obtained from UCAR/Unidata. The user also
 * understands that UCAR/Unidata is not obligated to provide the user with
 * any support, consulting, training or assistance of any kind with regard
 * to the use, operation and performance of this software nor to provide
 * the user with any updates, revisions, new versions or "bug fixes."
 *
 * THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package thredds.catalog2.straightimpl;

import thredds.catalog.ServiceType;
import thredds.catalog2.*;
import thredds.catalog2.builder.*;
import ucar.nc2.units.DateType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
class CatalogBuilderImpl implements CatalogBuilder
{
  private String name;
  private String docBaseUri;
  private String version;
  private DateType expires;
  private DateType lastModified;

  private ServiceBuilderContainer servicserviceBuilderContainerContainer;
  private CatalogWideServiceBuilderTracker catalogWideServiceBuilderTracker;

  private DatasetNodeContainer datasetContainer;

  private PropertyBuilderContainer propertyBuilderContainer;

  private BuilderIssues builderIssues;
  private Buildable isBuildable;

  CatalogBuilderImpl() {
    this( null, null, null, null, null);
  }

  CatalogBuilderImpl(String name, String docBaseUri, String version, DateType expires, DateType lastModified) {
    this.docBaseUri = docBaseUri != null ? docBaseUri : "";
    this.name = name;
    this.version = version;
    this.expires = expires;
    this.lastModified = lastModified;

    this.catalogWideServiceBuilderTracker = new CatalogWideServiceBuilderTracker();
    this.servicserviceBuilderContainerContainer = new ServiceBuilderContainer( catalogWideServiceBuilderTracker);

    //this.datasetContainer = new DatasetNodeContainer( null );
    this.propertyBuilderContainer = new PropertyBuilderContainer();
  }

  DatasetNodeContainer getDatasetNodeContainer() {
    return this.datasetContainer;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  @Override
  public String getDocBaseUriAsString() {
    return this.docBaseUri;
  }

  public void setDocBaseUri( String docBaseUri ) {
    this.docBaseUri = docBaseUri != null ? docBaseUri : "";
  }

  public void setVersion( String version ) {
    this.version = version;
  }

  public String getVersion() {
    return this.version;
  }

  public void setExpires( DateType expires ) {
    this.expires = expires;
  }

  public DateType getExpires() {
    return this.expires;
  }

  public void setLastModified( DateType lastModified ) {
    this.lastModified = lastModified;
  }

  public DateType getLastModified() {
    return this.lastModified;
  }

  public ServiceBuilder addService( String name, ServiceType type, String baseUri ) {
    return this.servicserviceBuilderContainerContainer.addService( name, type, baseUri );
  }

  public boolean removeService( ServiceBuilder serviceBuilder ) {
    if ( serviceBuilder == null )
      return false;

    return this.servicserviceBuilderContainerContainer.removeService( (ServiceImpl) serviceBuilder );
  }

  public List<ServiceBuilder> getServiceBuilders()
  {
    return this.servicserviceBuilderContainerContainer.getServiceBuilders();
  }

  public ServiceBuilder getServiceBuilderByName( String name )
  {
    return this.servicserviceBuilderContainerContainer.getServiceBuilderByName( name );
  }

  public ServiceBuilder findServiceBuilderByNameGlobally( String name )
  {
    return this.catalogWideServiceBuilderTracker.getServiceByGloballyUniqueName( name );
  }

  public void addProperty( String name, String value )
  {
    this.propertyBuilderContainer.addProperty(name, value);
  }

  public boolean removeProperty( String name )
  {
    return this.propertyBuilderContainer.removeProperty( name );
  }

  public List<String> getPropertyNames() {
    return this.propertyBuilderContainer.getPropertyNames();
  }

  public String getPropertyValue( String name ) {
    return this.propertyBuilderContainer.getPropertyValue( name );
  }

  public List<Property> getProperties() {
    return this.propertyBuilderContainer.getProperties();
  }

  public Property getPropertyByName( String name ) {
    return this.propertyBuilderContainer.getPropertyByName( name );
  }

  public DatasetBuilder addDataset( String name ) {
    DatasetImpl di = new DatasetImpl( name, this, null );
    this.datasetContainer.addDatasetNode( di );
    return di;
  }

  public CatalogRefBuilder addCatalogRef( String name, String reference ) {
    CatalogRefImpl crb = new CatalogRefImpl( name, reference, this, null );
    this.datasetContainer.addDatasetNode( crb );
    return crb;
  }

  public boolean removeDataset( DatasetNodeBuilder builder ) {
    if ( builder == null )
      throw new IllegalArgumentException( "DatasetNodeBuilder may not be null.");

    return this.datasetContainer.removeDatasetNode( (DatasetNodeImpl) builder );
  }

  public List<DatasetNode> getDatasets() {
    return this.datasetContainer.getDatasets();
  }

  public DatasetNode getDatasetById( String id ) {
    return this.datasetContainer.getDatasetById( id );
  }

  public DatasetNode findDatasetByIdGlobally( String id ) {
    return this.datasetContainer.getDatasetNodeByGloballyUniqueId( id );
  }

  public List<DatasetNodeBuilder> getDatasetNodeBuilders() {
    return this.datasetContainer.getDatasetNodeBuilders();
  }

  public DatasetNodeBuilder getDatasetNodeBuilderById( String id ) {
    return this.datasetContainer.getDatasetNodeBuilderById( id);
  }

  public DatasetNodeBuilder findDatasetNodeBuilderByIdGlobally( String id ) {
    return this.datasetContainer.getDatasetNodeByGloballyUniqueId( id );
  }

  public Buildable isBuildable() {
    return this.isBuildable;
  }

  public BuilderIssues checkForIssues()
  {
    BuilderIssues issues = new BuilderIssues();

    this.gatherIssues( issues, false );
    return issues;
  }

  public Catalog build() throws IllegalStateException
  {
    BuilderIssues allIssues = new BuilderIssues();
    this.gatherIssues( allIssues, true );

    this.threddsCatalogIssueContainer = new ThreddsCatalogIssuesImpl( allIssues);

    return new CatalogImpl();
  }

  private ThreddsCatalogIssueContainer threddsCatalogIssueContainer;
  public ThreddsCatalogIssueContainer getCatalogIssues()
  {
    return this.threddsCatalogIssueContainer;
  }

  BuilderIssues externalIssues = new BuilderIssues();

  BuilderIssues getLocalIssues()
  {
    BuilderIssues localIssues = new BuilderIssues();

    try {
      new URI( this.docBaseUri );
    }
    catch ( URISyntaxException e ) {
      localIssues.addIssue( new BuilderIssue( BuilderIssue.Severity.WARNING, "Document base URI [" + this.docBaseUri + "] has bad URI syntax.", this, e ) );
    }
    return localIssues;
  }

  void gatherIssues( BuilderIssues container, boolean build)
  {
    if ( this.isBuilt)
      return;

    container.addAllIssues( getLocalIssues() );
    if ( externalIssues != null && ! externalIssues.isEmpty())
      container.addAllIssues( externalIssues );

//    this.catalogWideServiceBuilderTracker.gatherIssues( container, build, this ) ;
//    this.servicserviceBuilderContainerContainer.gatherIssues( container, build);
//    this.datasetContainer.gatherIssues( container, build);
//    this.propertyBuilderContainer.gatherIssues( container, build);

    if ( build) {
      isBuilt = true;
    }
  }
}
