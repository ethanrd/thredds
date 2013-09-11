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
 */
class CatalogWideServiceBuilderTracker implements ThreddsBuilder
{
  /** List of all services including those with duplicate names. */
  private List<ServiceBuilderImpl> allServices;

  /** Map of service names to the service with that name that will be found when referenced by a dataset access. */
  private Map<String,ServiceBuilderImpl> referencableServiceBuilders;

  /** Map of service names to the number of services with each name. */
  private Map<String,Counter> sameNameServiceBuildersCounter;

  private Buildable isBuildable;
  private BuilderIssues builderIssues;

  CatalogWideServiceBuilderTracker() {
    this.isBuildable = Buildable.YES;
  }

  boolean isServiceNameInUseGlobally( String name ) {
    if ( name == null || name.isEmpty()
        || this.referencableServiceBuilders == null
        || this.referencableServiceBuilders.isEmpty() )
      return false;
    return this.referencableServiceBuilders.containsKey( name );
  }

  ServiceBuilderImpl getReferenceableService( String name) {
    if ( name == null || name.isEmpty()
        || this.referencableServiceBuilders == null
        || this.referencableServiceBuilders.isEmpty())
      return null;

    return this.referencableServiceBuilders.get( name );
  }

  void addService( ServiceBuilderImpl service ) {
    if ( service == null )
      return;

    this.isBuildable = Buildable.DONT_KNOW;
    if ( this.allServices == null ) {
      this.allServices = new ArrayList<ServiceBuilderImpl>();
      this.referencableServiceBuilders = new HashMap<String,ServiceBuilderImpl>();
      this.sameNameServiceBuildersCounter = new HashMap<String,Counter>();
    }

    this.allServices.add(service);
    if ( ! this.referencableServiceBuilders.containsKey( service.getName() ) ) {
      ServiceBuilderImpl previousValue = this.referencableServiceBuilders.put( service.getName(), service );
      Counter previousCount = this.sameNameServiceBuildersCounter.put(service.getName(), new Counter());
      assert null == previousValue;
      assert null == previousCount;
    }
    else {
      this.sameNameServiceBuildersCounter.get( service.getName()).increment();
    }
  }

  boolean removeService( ServiceBuilderImpl service ) {
    if ( service == null || this.allServices == null || this.allServices.isEmpty())
      return false;

    int numWithName = this.sameNameServiceBuildersCounter.get( service.getName()).getCount();
    if ( numWithName == 0 )
      return false;
    else {
      boolean removedFromAll = this.allServices.remove( service);
      if ( removedFromAll ) {
        if ( numWithName == 1 ) {
          ServiceBuilderImpl previousReferencableService = this.referencableServiceBuilders.remove(service.getName());
          assert previousReferencableService == service;
        }
        else // if ( numWithName > 1)
        {
          ServiceBuilderImpl foundByNameInRefServices = this.referencableServiceBuilders.get( service.getName());
          assert foundByNameInRefServices != null;
          if ( foundByNameInRefServices == service ) {
            ServiceBuilderImpl previousReferencableService = this.referencableServiceBuilders.remove(service.getName());
            assert previousReferencableService == service;
            ServiceBuilderImpl nextServiceWithName = this.findFirstServiceWithGivenNameInAllServicesList( service.getName());
            assert nextServiceWithName != null;
            this.referencableServiceBuilders.put( nextServiceWithName.getName(), nextServiceWithName);
          }
          this.sameNameServiceBuildersCounter.get( service.getName()).decrement();

        }
        return true;
      } // else {The given service is not known. Other(s) with same name are known.}
      return false;
    }
  }

  private ServiceBuilderImpl findFirstServiceWithGivenNameInAllServicesList( String name) {
    if ( name == null || name.isEmpty() || this.allServices == null || this.allServices.isEmpty())
      return null;
    for ( ServiceBuilderImpl serviceBuilder : this.allServices) {
      if (serviceBuilder.getName().equalsIgnoreCase( name))
        return serviceBuilder;
    }
    return null;
  }

  boolean isEmpty() {
    if ( this.referencableServiceBuilders == null )
      return true;
    return this.referencableServiceBuilders.isEmpty();
  }

  int numberOfReferenceableServices() {
    if ( this.referencableServiceBuilders == null )
      return 0;
    return this.referencableServiceBuilders.size();
  }

  int numberOfNonReferencableServicess() {
    if ( this.allServices == null )
      return 0;
    return this.allServices.size() - this.referencableServiceBuilders.size();
  }

  /**
   * Helper method that checks for duplicate service names. Used by
   * CatalogBuilder.isBuildable() and ServiceBuilder.isBuildable().
   *
   * @return true if no issues, false otherwise.
   */
  @Override
  public BuilderIssues checkForIssues( )
  {
    this.builderIssues = new BuilderIssues();
    if ( this.allServices == null || this.allServices.isEmpty())
      return this.builderIssues;

    for ( String serviceName : this.sameNameServiceBuildersCounter.keySet() ) {
      if ( this.sameNameServiceBuildersCounter.get( serviceName).getCount() > 1 ) {
        ServiceBuilderImpl referencableService = this.referencableServiceBuilders.get( serviceName);
        this.builderIssues.addIssue(
            new BuilderIssue( BuilderIssue.Severity.WARNING, "Catalog contains duplicate service name [" + serviceName + "].", referencableService));
      }
    }

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

    return new CatalogWideServiceTracker( this.allServices, this.builderIssues);
  }

  private static class Counter {
    private int count;
    private Counter() {
      this.count = 1;
    }
    public int getCount() {
      return this.count;
    }
    public int increment() {
      ++this.count;
      return this.count;
    }
    public int decrement() {
      this.count--;
      return this.count;
    }
  }
}
