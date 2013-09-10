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

import thredds.catalog2.Property;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ThreddsBuilder;

import java.util.*;

/**
 * Helper class for those classes that contain properties: ServiceImpl,
 * DatasetNodeImpl, and CatalogImpl.
 *
 * @author edavis
 * @since 4.0
 */
class PropertyBuilderContainer implements ThreddsBuilder
{
  private org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger( getClass() );

  private Map<String, Property> propertiesMap;

  PropertyBuilderContainer() {
    this.propertiesMap = null;
  }

  boolean isEmpty() {
    if ( this.propertiesMap == null )
      return true;
    return this.propertiesMap.isEmpty();
  }

  int size() {
    if ( this.propertiesMap == null )
      return 0;
    return this.propertiesMap.size();
  }

  List<String> getPropertyNames() {
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

  Property getProperty( String name ) {
    if ( name == null )
      return null;

    if ( this.propertiesMap == null )
      return null;

    return this.propertiesMap.get(name);
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

    return Collections.unmodifiableList(new ArrayList<Property>(this.propertiesMap.values()));
  }

  /**
   * Add a Property with the given name and value to this container.
   *
   * @param name the name of the Property to add.
   * @param value the value of the Property to add.
   * @return the previous property with the given name.
   * @throws IllegalArgumentException if name is null or empty or if value is null.
   */
   Property addProperty( String name, String value ) {
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
