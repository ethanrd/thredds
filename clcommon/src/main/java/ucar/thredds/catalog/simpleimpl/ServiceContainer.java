package ucar.thredds.catalog.simpleimpl;

import ucar.thredds.catalog.Service;
import ucar.thredds.catalog.ServiceType;
import ucar.thredds.catalog.builder.BuilderException;
import ucar.thredds.catalog.builder.BuilderIssues;
import ucar.thredds.catalog.builder.ServiceBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for those classes that contain services: CatalogImpl and ServiceImpl.
 *
 * @author edavis
 */
public class ServiceContainer
{
  private List<ServiceImpl> services;

//  private final GlobalServiceContainer globalServiceContainer;

  private boolean isBuilt;

  ServiceContainer() // GlobalServiceContainer globalServiceContainer )
  {
//    if ( globalServiceContainer == null )
//      throw new IllegalArgumentException( "" );

    this.isBuilt = false;
//    this.globalServiceContainer = globalServiceContainer;
  }

  ServiceImpl getServiceByGloballyUniqueName( String name ) {
//    return this.globalServiceContainer.getServiceByGloballyUniqueName( name );
    return null;
  }

  boolean isEmpty()
  {
    if ( this.services == null )
      return true;
    return this.services.isEmpty();
  }

  int size()
  {
    if ( this.services == null )
      return 0;
    return this.services.size();
  }

  /**
   * Create a new ServiceImpl and add it to this container.
   *
   * @param name the name of the ServiceImpl.
   * @param type the ServiceType of the ServiceImpl.
   * @param baseUri the base URI of the ServiceImpl.
   * @return the ServiceImpl that was created and added to this container.
   * @throws IllegalArgumentException if name, type, or baseUri are null.
   * @throws IllegalStateException if build() has been called on this ServiceContainer.
   */
  ServiceImpl addService( String name, ServiceType type, String baseUri )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This ServiceContainer has been built.");

    if ( this.services == null )
      this.services = new ArrayList<ServiceImpl>();

    ServiceImpl service = new ServiceImpl( name, type, baseUri); //, this.globalServiceContainer );

    boolean addedService = this.services.add( service );
    assert addedService;

//    this.globalServiceContainer.addService( service );

    return service;
  }

  /**
   * Remove the given Service from this container if it is present.
   *
   * @param service the Service to remove.
   * @return true if the Service was present and has been removed, otherwise false.
   * @throws IllegalStateException if build() has been called on this ServiceContainer.
   */
  boolean removeService( ServiceImpl service )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This ServiceContainer has been built." );

    if ( service == null )
      return false;

    if ( this.services.remove( service))
    {
//      boolean success = this.globalServiceContainer.removeService( service );
//      assert success;
      return true;
    }

    return false;
  }

  List<ServiceImpl> getServiceImpls()
  {
    if ( this.services == null || this.services.isEmpty() )
      return Collections.emptyList();
    return Collections.unmodifiableList( this.services );
  }

  List<Service> getServices()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "This Service has escaped from its ServiceBuilder without being finished()." );

    if ( this.services == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Service>( this.services ) );
  }

  List<ServiceBuilder> getServiceBuilders() {
    if ( this.isBuilt)
      throw new IllegalStateException( "This ServiceBuilder has been finished()." );
    if ( this.services == null || this.services.isEmpty())
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ServiceBuilder>( this.services ) );
  }

  ServiceImpl findReferencableServiceImplByName( String name ) {
    //    return this.globalServiceContainer.findReferencableServiceBuilderByName( name );
    for (ServiceImpl curService : this.services ) {
      if ( curService.getName().equals( name ) )
        return curService;
    }
    return null;

  }

  boolean containsServiceName( String name )
  {
    if ( name == null )
      return false;

    if ( this.services == null )
      return false;

    return null != this.getServiceImplByName( name );
  }

  Service getServiceByName( String name )
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "This Service has escaped from its ServiceBuilder without being finished()." );

    if ( name == null )
      return null;

    return this.getServiceImplByName( name );
  }

  private ServiceImpl getServiceImplByName( String name )
  {
    if ( this.services != null )
      for ( ServiceImpl s : this.services )
        if ( s.getName().equals( name ))
          return s;
    return null;
  }

  ServiceBuilder getServiceBuilderByName( String name )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This ServiceBuilder has been finished()." );

    if ( name == null )
      return null;

    return this.getServiceImplByName( name );
  }

  boolean isBuilt()
  {
    return this.isBuilt;
  }

  /**
   * Gather any issues with the current state of this ServiceContainer.
   *
   * @return any BuilderIssues with the current state of this ServiceContainer.
   */
  BuilderIssues checkForIssues()
  {
    BuilderIssues issues = new BuilderIssues();

    // Check on contained ServiceImpl objects.
    if ( this.services != null )
      for ( ServiceImpl sb : this.services )
        issues.addAllIssues( sb.checkForIssues());

    return issues;
  }

  /**
   * Call build() on all contained services.
   *
   * @throws BuilderException if any of the contained services are not in a valid state.
   */
  void build()
          throws IllegalStateException
  {
    if ( this.isBuilt )
      return;

    // Build contained ServiceImpl objects.
    if ( this.services != null )
      for ( ServiceImpl sb : this.services )
        sb.build();

    this.isBuilt = true;
    return;
  }

}
