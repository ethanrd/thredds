package ucar.thredds.catalog.util;

import ucar.thredds.catalog.Property;
import ucar.thredds.catalog.builder.BuilderIssues;
import ucar.thredds.catalog.builder.ThreddsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for those classes that contain properties (both Nodes and Builders):
 * <ul>
 *   <li>ServiceBuilder, DatasetNodeBuilder, and CatalogBuilder</li>
 *   <li>Service, DatasetNode, and Catalog</li>
 * </ul>
 *
 * @author edavis
 */
class PropertyBuilderContainer //??? implements ThreddsBuilder
{
  private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( getClass() );

  private List<Property> propertiesList;
  private Map<String, Property> propertiesMap;

  private ThreddsBuilder.Buildable isBuildable;
  private BuilderIssues builderIssues;

  PropertyBuilderContainer() {
    this.isBuildable = ThreddsBuilder.Buildable.YES;
    this.propertiesList = null;
    this.propertiesMap = null;
  }

  boolean isEmpty() {
    if ( this.propertiesList == null )
      return true;
    return this.propertiesList.isEmpty();
  }

  int size() {
    if ( this.propertiesList == null )
      return 0;
    return this.propertiesList.size();
  }

  /**
   *
   * @return
   */
  public List<String> getPropertyNames() {
    if ( this.propertiesMap == null )
      return Collections.emptyList();
    return Collections.unmodifiableList(new ArrayList<String>(this.propertiesMap.keySet()));
  }

  boolean containsProperty( String name ) {
    if ( name == null )
      return false;

    if ( this.propertiesMap == null )
      return false;

    if ( this.propertiesMap.get( name ) == null )
      return false;
    return true;
  }

  /**
   *
   * @param name
   * @return
   */
  public Property getProperty( String name ) {
    if ( name == null )
      return null;

    if ( this.propertiesMap == null )
      return null;

    return this.propertiesMap.get( name );
  }

  String getPropertyValue( String name ) {
    if ( name == null )
      return null;

    if ( this.propertiesMap == null )
      return null;

    Property property = this.propertiesMap.get( name );
    if ( property == null )
      return null;
    return property.getValue();
  }

  List<Property> getProperties() {
    if ( this.propertiesMap == null )
      return Collections.emptyList();

    return Collections.unmodifiableList( new ArrayList<Property>( this.propertiesMap.values() ) );
  }

  /**
   * Add a Property with the given name and value to this container.
   *
   * @param name the name of the Property to add.
   * @param value the value of the Property to add.
   * @return ???
   * @throws IllegalArgumentException if name is null or empty or if value is null.
   */
  boolean addProperty( String name, String value ) {
    Property property = new PropertyImpl( name, value );

    if ( this.propertiesMap == null )
      this.propertiesMap = new LinkedHashMap<String, Property>();

    return this.propertiesMap.put(name, property);
  }

  /**
   * Remove the named Property from this container if it was present.
   *
   * @param name the name of the Property to remove.
   * @return the Property previously associated with the name, or null if there was no mapping for name
   */
  Property removeProperty( String name )
  {
    if ( name == null || this.propertiesMap == null || this.propertiesMap.isEmpty() )
      return null;

    return this.propertiesMap.remove(name);
  }

  public Buildable isBuildable() {
    return Buildable.YES;
  }

  /**
   * This method always returns an empty BuilderIssues object because no action is required to
   * finish any contained Property classes.
   *
   * The reasons for this are:
   * <ol>
   * <li>The Property class is immutable and doesn't allow null names or values.</li>
   * <li>This container stores the properties in a Map by property name (so there are no duplicate names).</li>
   * </ol>
   *
   * @return an empty BuilderIssues object.
   */
  public BuilderIssues checkForIssues() {
    return new BuilderIssues();
  }

  /**
   * Return an immutable PropertyContainer including all current Property-s.
   */
  public PropertyContainer build() {
    if ( this.propertiesMap == null || this.propertiesMap.isEmpty() )
      return new PropertyContainer();
    return new PropertyContainer( Collections.unmodifiableMap( this.propertiesMap));
  }
}
