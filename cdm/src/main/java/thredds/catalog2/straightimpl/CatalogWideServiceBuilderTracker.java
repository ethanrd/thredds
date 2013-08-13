package thredds.catalog2.straightimpl;

import thredds.catalog2.builder.BuilderIssue;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ThreddsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for tracking all services in a catalog by globally unique names.
 * The THREDDS Catalog specification (and XML Schema) does not allow multiple
 * services with the same name. However, in practice, we allow services with
 * duplicate names and we track those here as well. When an accessible dataset
 * references a service, the service first added (or first in document order)
 * with the referenced name will be found and used for the access method
 *
 * @author edavis
 * @since 4.0
 */
class CatalogWideServiceBuilderTracker implements ThreddsBuilder
{
  /** List of all services including those with duplicate names. */
  private List<ServiceBuilderImpl> allServices;

  /** Map of services (keyed on their names) that will be found when referenced by a dataset access. */
  private Map<String,ServiceBuilderImpl> referencableServiceBuilders;

  private Buildable isBuildable;
  private BuilderIssues builderIssues;

  CatalogWideServiceBuilderTracker() {
    // ToDo Don't create lists/maps here, do it on the fly.
    this.allServices = new ArrayList<ServiceBuilderImpl>();
    this.referencableServiceBuilders = new HashMap<String,ServiceBuilderImpl>();
    this.isBuildable = Buildable.YES;
  }

  boolean isServiceNameInUseGlobally( String name ) {
    return this.referencableServiceBuilders.containsKey( name );
  }

  ServiceBuilderImpl getReferenceableService( String name) {
    if ( name == null )
      return null;

    return this.referencableServiceBuilders.get( name );
  }

  void addService( ServiceBuilderImpl service ) {
    if ( service == null )
      return;

    this.isBuildable = Buildable.DONT_KNOW;
    this.allServices.add(service);
    if ( ! this.referencableServiceBuilders.containsKey( service.getName() ) ) {
      ServiceBuilderImpl previousValue = this.referencableServiceBuilders.put( service.getName(), service );
      assert previousValue == null;
    }
  }

  boolean removeService( ServiceBuilderImpl service )
  {
    if ( service == null )
      return false;

    this.isBuildable = Buildable.DONT_KNOW;
    boolean removedFromAll = this.allServices.remove( service);
    ServiceBuilderImpl foundByNameInRefServices = this.referencableServiceBuilders.get( service.getName());
    if ( removedFromAll ) {
      // Check that there are no other occurrences in allServices list.
      if ( this.allServices.indexOf( service) == -1 ) {
        if ( foundByNameInRefServices != null && foundByNameInRefServices == service ) {
          assert this.referencableServiceBuilders.remove( service.getName()) == service;
          // Promote first service with given name to reference-able.
          for ( ServiceBuilderImpl curService : this.allServices ) {
            if ( curService.getName() == service.getName()) {
              this.referencableServiceBuilders.put(service.getName(), curService);
              break;
            }
          }
        }
      }
      return true;

    } else {
      // It wasn't in the allServices list. Shouldn't be in reference-able service list.
      assert this.referencableServiceBuilders.get( service.getName()) != service;
      return false;
    }
  }

  boolean isEmpty() {
    return this.referencableServiceBuilders.isEmpty();
  }

  int numberOfReferenceableServices() {
    return this.referencableServiceBuilders.size();
  }

  int numberOfNonReferencableServicess() {
    return this.allServices.size() - this.referencableServiceBuilders.size();
  }

  /**
   * Helper method that checks for duplicate service names. Used by CatalogBuilder.isBuildable() and
   * ServiceBuilder.isBuildable().
   *
   * @return true if no issues, false otherwise.
   */
  @Override
  public BuilderIssues checkForIssues( )
  {
    this.builderIssues = new BuilderIssues();
    if ( this.allServices.isEmpty())
      return this.builderIssues;

    for ( ServiceBuilderImpl s : this.allServices )
      this.builderIssues.addIssue(
            new BuilderIssue( BuilderIssue.Severity.WARNING, "Catalog contains duplicate service name [" + s.getName() + "].", s));

    if (this.builderIssues.isValid())
      this.isBuildable = Buildable.YES;
    else
      this.isBuildable = Buildable.NO;

    return this.builderIssues;
  }

  public Buildable isBuildable() {
    return this.isBuildable;

  }

  public CatalogWideServiceTracker build() {
    if (this.isBuildable != Buildable.YES)
      throw new IllegalStateException( "Not in buildable state [" + this.isBuildable +"]");

//    if ( ! this.referencableServiceBuilders.isEmpty()) {
//      Map<String,ServiceImpl> tmpServices = new HashMap<String,ServiceImpl>();
//
//    }
//    return new CatalogWideServiceTracker( this.referencableServiceBuilders, this.nonReferencableServices);
    return null;
  }
}
