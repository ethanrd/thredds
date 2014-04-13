package ucar.thredds.catalog.util;

import ucar.thredds.catalog.Service;
import ucar.thredds.catalog.ServiceType;
import ucar.thredds.catalog.ThreddsCatalogIssueContainer;
import ucar.thredds.catalog.builder.BuilderIssues;
import ucar.thredds.catalog.builder.ServiceBuilder;
import ucar.thredds.catalog.builder.ThreddsBuilder;
import ucar.thredds.catalog.builder.ThreddsBuilderFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for those ThreddsBuilder classes that have child ServiceBuilder classes:
 * CatalogBuilder and ServiceBuilder.
 *
 * @author edavis
 */
public class ServiceBuilderContainer // implements ThreddsBuilder
{
  /** List of all contained Service-s */
  private List<ServiceBuilder> serviceBuilderList;

//  private final CatalogWideServiceBuilderTracker catalogWideServiceBuilderTracker;

  private final ThreddsBuilderFactory threddsBuilderFactory;

  private ThreddsBuilder.Buildable isBuildable;
  private BuilderIssues builderIssues;

  public ServiceBuilderContainer( ThreddsBuilderFactory threddsBuilderFactory
//      , CatalogWideServiceBuilderTracker catalogWideServiceBuilderTracker
  ) {
    if ( threddsBuilderFactory == null )
      throw new IllegalArgumentException( "ThreddsBuilderFactory may not be null." );
    this.threddsBuilderFactory = threddsBuilderFactory;

//    if ( catalogWideServiceBuilderTracker == null )
//      throw new IllegalArgumentException( "CatalogWideServiceBuilderTracker may not be null." );
//    this.catalogWideServiceBuilderTracker = catalogWideServiceBuilderTracker;

    this.serviceBuilderList = null;
    this.isBuildable = ThreddsBuilder.Buildable.YES;
  }

//  ServiceBuilder getServiceByGloballyUniqueName( String name ) {
//    return this.catalogWideServiceBuilderTracker.getReferenceableService(name);
//  }

  /**
   * Add a service with the given name and value to this container.
   *
   * @param name the name of the Service to add.
   * @param type the type of the Service to add.
   * @param baseUriAsString the baseUri for the Service
   * @return the ServiceBuilder that was created and added to this container.
   */
  public ServiceBuilder addService( String name, ServiceType type, String baseUriAsString ) {
    ServiceBuilder serviceBuilder = this.threddsBuilderFactory.newServiceBuilder( name, type, baseUriAsString );
//    serviceBuilder.initialize( this.catalogWideServiceBuilderTracker );

    if ( this.serviceBuilderList == null ) {
      this.serviceBuilderList = new ArrayList<ServiceBuilder>();
    }
    this.serviceBuilderList.add( serviceBuilder );

//    this.catalogWideServiceBuilderTracker.addService( serviceBuilder );

    this.isBuildable = ThreddsBuilder.Buildable.DONT_KNOW;
    return serviceBuilder;
  }

  /**
   * Remove the given Service from this container.
   *
   * @param service the Service to be removed.
   * @return true if a Service was removed, otherwise false.
   */
  public boolean removeService( Service service )
  {
    if ( service == null || this.serviceBuilderList == null
        || this.serviceBuilderList.isEmpty() )
      return false;

    if ( ! this.serviceBuilderList.remove( service ) ) {
      return false;
    }
//    boolean success = this.catalogWideServiceBuilderTracker.removeService( service );
//    assert success;
    if ( this.isBuildable == ThreddsBuilder.Buildable.NO)
      this.isBuildable = ThreddsBuilder.Buildable.DONT_KNOW;

    return true;
  }

  /**
   * Return a list of all contained Service-s.
   * @return a list of all contained Service-s.
   */
  public List<ServiceBuilder> getServices() {
    if ( this.serviceBuilderList == null || this.serviceBuilderList.isEmpty() )
      return Collections.emptyList();

    return Collections.unmodifiableList( this.serviceBuilderList );
  }

  boolean isEmpty() {
    if ( this.serviceBuilderList == null )
      return true;
    return this.serviceBuilderList.isEmpty();
  }

  int size() {
    if ( this.serviceBuilderList == null )
      return 0;
    return this.serviceBuilderList.size();
  }

  public ThreddsCatalogIssueContainer getCatalogIssues() {
    if ( this.isBuildable != ThreddsBuilder.Buildable.YES) {
      this.checkForIssues();
    }
    return new ThreddsCatalogIssuesImpl( this.builderIssues );
  }

  public BuilderIssues checkForIssues()
  {
    builderIssues = new BuilderIssues();
    if ( this.serviceBuilderList != null ) {
      for ( ServiceBuilder curServiceBuilder : this.serviceBuilderList ) {
        builderIssues.addAllIssues( curServiceBuilder.checkForIssues() );
      }
    }
    if ( builderIssues.isValid()) {
      this.isBuildable = ThreddsBuilder.Buildable.YES;
    } else {
      this.isBuildable = ThreddsBuilder.Buildable.NO;
    }
    return builderIssues;
  }

  public ThreddsBuilder.Buildable isBuildable() {
    return this.isBuildable;
  }
  /**
   * Build the ServiceContainer
   *
   * @throws IllegalStateException if any of the contained services are not in a valid state.
   */
  public ServiceContainer build() throws IllegalStateException
  {
    if ( this.isBuildable != ThreddsBuilder.Buildable.YES )
      throw new IllegalStateException( "ServiceBuilderContainer is not in buildable state.");

    return new ServiceContainer( this.serviceBuilderList );
  }
}
