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

import thredds.catalog2.Service;
import thredds.catalog2.builder.ServiceBuilder;
import thredds.catalog2.builder.ThreddsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for those classes that contain services: CatalogImpl and ServiceImpl.
 *
 * @author edavis
 */
class ServiceContainer
{
  private final List<Service> services;

  ServiceContainer( List<ServiceBuilder> serviceBuilders ) {
    if ( serviceBuilders == null || serviceBuilders.isEmpty() )
      this.services = null;
    else {
      for ( ServiceBuilder serviceBuilder : serviceBuilders)
        if ( serviceBuilder.isBuildable() != ThreddsBuilder.Buildable.YES )
          throw new IllegalArgumentException( "Can't construct ServiceContainer with ServiceBuilders [" + serviceBuilder.getName() + "] that are not buildable.");

      this.services = new ArrayList<Service>();
      for ( ServiceBuilder serviceBuilder : serviceBuilders ) {
        if ( serviceBuilder.isBuildable() != ThreddsBuilder.Buildable.YES) {
          //this.services = null;
          throw new IllegalArgumentException( "Can't construct ServiceContainer when contained SserviceBuilder is not buildable.");
        }
        this.services.add( serviceBuilder.build());
      }
    }
  }

  boolean isEmpty() {
    return this.services == null || this.services.isEmpty();
  }

  int size() {
    if ( this.services == null )
      return 0;
    return this.services.size();
  }

  List<Service> getServices() {
    if ( this.services == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Service>( this.services ) );
  }

  boolean containsServiceName( String name ) {
    if ( name == null || this.services == null || this.services.isEmpty() )
      return false;

    return null != this.getServiceByName( name );
  }

  Service getServiceByName( String name ) {
    if ( name == null || this.services == null || this.services.isEmpty() )
      return null;

    for ( Service s : this.services )
      if ( s.getName().equals( name ))
        return s;
    return null;
  }
}