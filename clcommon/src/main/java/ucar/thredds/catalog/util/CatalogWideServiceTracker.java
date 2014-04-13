package ucar.thredds.catalog.util;

import ucar.thredds.catalog.Service;
import ucar.thredds.catalog.builder.ServiceBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ToDo How does a compound service end up with a single CatalogWideServiceTracker rather than building one for each service?

/**
 * Helper class for tracking services by globally unique names. The THREDDS
 * Catalog specification (and XML Schema) does not allow multiple services with
 * the same name. However, in practice, we allow services with duplicate names
 * and we track those here as well.
 *
 * @author edavis
 */
final class CatalogWideServiceTracker
{
  /** List of all services including those with duplicate names. */
  private final List<Service> allServices;

  /** Map of services (keyed on their names) that will be found when referenced by a dataset access. */
  private Map<String,Service> referencableServiceBuilders;

  CatalogWideServiceTracker( List<ServiceBuilder> allServices ) {
    if ( allServices == null || allServices.isEmpty() ) {
      this.allServices = null;
      this.referencableServiceBuilders = null;
      return;
    }

    this.allServices = new ArrayList<Service>();
    this.referencableServiceBuilders = new HashMap<String, Service>();
    for ( ServiceBuilder serviceBuilder : allServices ) {
      Service service = serviceBuilder.build();
      this.allServices.add( service);
      if ( ! this.referencableServiceBuilders.containsKey( service.getName())) {
        this.referencableServiceBuilders.put( service.getName(), service);
      }
    }
  }

  boolean isServiceNameInUseGlobally( String name ) {
    if ( name == null
        || this.referencableServiceBuilders == null
        || this.referencableServiceBuilders.isEmpty()  )
      return false;
    return this.referencableServiceBuilders.containsKey( name );
  }

  Service getServiceByGloballyUniqueName( String name )
  {
    if ( name == null
        || this.referencableServiceBuilders == null
        || this.referencableServiceBuilders.isEmpty()  )
      return null;

    return this.referencableServiceBuilders.get( name);
  }

  boolean isEmpty() {
    return this.referencableServiceBuilders == null || this.referencableServiceBuilders.isEmpty();
  }

  int numberOfServicesWithGloballyUniqueNames() {
    if ( this.referencableServiceBuilders == null)
      return 0;
    return this.referencableServiceBuilders.size();
  }

  int numberOfServicesWithDuplicateNames() {
    return this.allServices.size() - this.referencableServiceBuilders.size();
  }

}
