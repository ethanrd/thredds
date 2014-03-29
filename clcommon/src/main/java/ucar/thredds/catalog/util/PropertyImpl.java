package ucar.thredds.catalog.util;

import ucar.thredds.catalog.Property;
import ucar.thredds.catalog.ThreddsCatalogIssueContainer;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
public class PropertyImpl implements Property {
  private final String name;
  private final String value;

  PropertyImpl( String name, String value ) {
//    if ( name == null || name.isEmpty())
//      throw new IllegalArgumentException( "Name must not be null or empty.");
    this.name = name != null ? name : "";
    this.value = value != null ? value : "";
  }

  public String getName() {
    return this.name;
  }

  public String getValue() {
    return this.value;
  }
}
