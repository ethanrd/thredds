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
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ServiceBuilder;
import thredds.catalog2.builder.ThreddsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for those classes that contain ServiceBuilders:
 * CatalogBuilderImpl and ServiceBuilderImpl.
 *
 * @author edavis
 */
class ServiceBuilderContainer implements ThreddsBuilder
{
  private List<ServiceBuilderImpl> serviceBuilders;

  private final CatalogWideServiceBuilderTracker catalogWideServiceBuilderTracker;

  private Buildable isBuildable;
  private BuilderIssues builderIssues;

  ServiceBuilderContainer( CatalogWideServiceBuilderTracker catalogWideServiceBuilderTracker)
  {
    if ( catalogWideServiceBuilderTracker == null )
      throw new IllegalArgumentException( "CatalogWideServiceBuilderTracker may not be null." );

    this.catalogWideServiceBuilderTracker = catalogWideServiceBuilderTracker;
    this.isBuildable = Buildable.YES;
  }

  ServiceBuilderImpl getServiceByGloballyUniqueName( String name ) {
    return this.catalogWideServiceBuilderTracker.getReferenceableService(name);
  }

  boolean isEmpty() {
    if ( this.serviceBuilders == null )
      return true;
    return this.serviceBuilders.isEmpty();
  }

  int size() {
    if ( this.serviceBuilders == null )
      return 0;
    return this.serviceBuilders.size();
  }

  /**
   * Create a new ServiceBuilderImpl and add it to this container.
   *
   * @param name the name of the ServiceBuilderImpl.
   * @param type the ServiceType of the ServiceBuilderImpl.
   * @param baseUri the base URI of the ServiceBuilderImpl.
   * @return the ServiceBuilderImpl that was created and added to this container.
   * @throws IllegalArgumentException if name, type, or baseUri are null.
   * @throws IllegalStateException if build() has been called on this ServiceContainer.
   */
  ServiceBuilderImpl addService( String name, ServiceType type, String baseUri )
  {
    if ( this.serviceBuilders == null )
      this.serviceBuilders = new ArrayList<ServiceBuilderImpl>();

    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( name, type, baseUri, this.catalogWideServiceBuilderTracker);

    boolean addedService = this.serviceBuilders.add( serviceBuilder );
    assert addedService;

    this.catalogWideServiceBuilderTracker.addService( serviceBuilder );
    this.isBuildable = Buildable.DONT_KNOW;

    return serviceBuilder;
  }

  /**
   * Remove the given Service from this container if it is present.
   *
   * @param service the Service to remove.
   * @return true if the Service was present and has been removed, otherwise false.
   * @throws IllegalStateException if build() has been called on this ServiceContainer.
   */
  boolean removeService( ServiceBuilderImpl service )
  {
    if ( service == null )
      return false;

    if ( this.serviceBuilders == null )
      return false;

    if ( this.serviceBuilders.remove( service)) {
      boolean success = this.catalogWideServiceBuilderTracker.removeService( service );
      assert success;
      this.isBuildable = Buildable.DONT_KNOW;
      return true;
    }

    return false;
  }

  List<ServiceBuilder> getServiceBuilders() {
    if ( this.serviceBuilders == null )
      return Collections.emptyList();
    return Collections.unmodifiableList(new ArrayList<ServiceBuilder>(this.serviceBuilders));
  }

  ServiceBuilder getServiceBuilderByName( String name ) {
    return this.getServiceBuilderImplByName(name);
  }

  boolean containsServiceName( String name ) {
    return null != this.getServiceBuilderImplByName(name);
  }

  private ServiceBuilderImpl getServiceBuilderImplByName( String name ) {
    if ( name == null || this.serviceBuilders == null)
      return null;

    for ( ServiceBuilderImpl s : this.serviceBuilders ) {
      if ( s.getName().equals( name ))
        return s;
    }

    return null;
  }

  public Buildable isBuildable() {
    return this.isBuildable;
  }

  /**
   * Gather any issues with the current state of this ServiceContainer.
   *
   * @return any BuilderIssues with the current state of this ServiceContainer.
   */
  public BuilderIssues checkForIssues()
  {
    this.builderIssues = new BuilderIssues();

    // Check on contained ServiceBuilderImpl objects.
    if ( this.serviceBuilders != null )
      for ( ServiceBuilderImpl sb : this.serviceBuilders )
        this.builderIssues.addAllIssues( sb.checkForIssues());

    if ( this.builderIssues.isValid())
      this.isBuildable = Buildable.YES;
    else
      this.isBuildable = Buildable.NO;

    return builderIssues;
  }

  /**
   * Call build() on all contained services.
   *
   * @throws IllegalStateException if any of the contained services are not in a valid state.
   */
  public ServiceContainer build() throws IllegalStateException
  {
    if ( this.isBuildable != Buildable.YES )
      throw new IllegalStateException( "ServiceBuilderContainer is not in buildable state.");

    return new ServiceContainer( this.serviceBuilders );
  }
}