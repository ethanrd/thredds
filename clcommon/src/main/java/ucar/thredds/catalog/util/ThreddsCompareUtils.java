package ucar.thredds.catalog.util;

import ucar.thredds.catalog.Property;
import ucar.thredds.catalog.builder.CatalogBuilder;
import ucar.thredds.catalog.Catalog;

import java.util.Formatter;
import java.util.List;

/**
 * _MORE_
 *
 * @author edavis
 */
public class ThreddsCompareUtils {

  public static boolean compareCatalogBuilders( CatalogBuilder orginalCB, CatalogBuilder altCB, Formatter compareLog) {
    if ( compareLog == null )
      throw new IllegalArgumentException( "Must supply a compareLog." );

    ThreddsCompareUtils threddsCompareUtils = new ThreddsCompareUtils( compareLog);
    return threddsCompareUtils.doCompareCatalogBuilders( orginalCB, altCB );
  }

  public static boolean compareCatalog( Catalog orginalCat, Catalog altCat, Formatter compareLog) {
    if ( compareLog == null )
      throw new IllegalArgumentException( "Must supply a compareLog." );

    ThreddsCompareUtils threddsCompareUtils = new ThreddsCompareUtils( compareLog);
    return threddsCompareUtils.doCompareCatalogs( orginalCat, altCat );
  }

  private Formatter comparisonLog;
  private boolean showCompare = false;
  private ThreddsCompareUtils( Formatter compareLog) {
    this.comparisonLog = compareLog;
  }

  private boolean doCompareCatalogBuilders( CatalogBuilder originalCB, CatalogBuilder altCB) {
    if ( originalCB == null || altCB == null )
      throw new IllegalArgumentException( "Catalogs to compare may not be null." );

    boolean ok = true;
    if ( ! originalCB.getDocBaseUriAsString().equals( originalCB.getDocBaseUriAsString() )) {
      this.comparisonLog.format( "Catalog DocBase URIs not the same: first [%s], second [%s]",
          originalCB.getDocBaseUriAsString(), altCB.getDocBaseUriAsString());
      ok = false;
    }
    if ( ! originalCB.getName().equals( altCB.getName() )) {
      this.comparisonLog.format( "Catalog names not the same; first [%s], second [%s]",
          originalCB.getName(), altCB.getName());
      ok = false;
    }
    if ( ! originalCB.getVersion().equals( altCB.getVersion() )) {
      this.comparisonLog.format( "Catalog versions not the same; first [%s], second [%s]",
          originalCB.getVersion(), altCB.getVersion());
      ok = false;
    }
    if ( ! originalCB.getExpires().equals( altCB.getExpires() )) {
      this.comparisonLog.format( "Catalog expires dates not the same; first [%s], second [%s]",
          originalCB.getExpires(), altCB.getExpires());
      ok = false;
    }
    if ( ! originalCB.getLastModified().equals( altCB.getLastModified() )) {
      this.comparisonLog.format( "Catalog lastMod dates not the same; first [%s], second [%s]",
          originalCB.getLastModified(), altCB.getLastModified());
      ok = false;
    }

    // for ( Property curProperty : originalCB.getProperties() ) {
    //   ok &= doCompareProperties(... );
    // }

    return ok;
  }

  private boolean doCompareCatalogs( Catalog originalCat, Catalog altCat ) {
    if ( originalCat == null || altCat == null )
      throw new IllegalArgumentException( "Catalogs to compare may not be null." );

    boolean ok = true;
    if ( ! originalCat.getDocBaseUri().equals( originalCat.getDocBaseUri() )) {
      this.comparisonLog.format( "Catalog DocBase URIs not the same: first [%s], second [%s]",
          originalCat.getDocBaseUri(), altCat.getDocBaseUri() );
      ok = false;
    }
    if ( ! originalCat.getName().equals( altCat.getName() )) {
      this.comparisonLog.format( "Catalog names not the same; first [%s], second [%s]",
          originalCat.getName(), altCat.getName());
      ok = false;
    }
    if ( ! originalCat.getVersion().equals( altCat.getVersion() )) {
      this.comparisonLog.format( "Catalog versions not the same; first [%s], second [%s]",
          originalCat.getVersion(), altCat.getVersion());
      ok = false;
    }
    if ( ! originalCat.getExpires().equals( altCat.getExpires() )) {
      this.comparisonLog.format( "Catalog expires dates not the same; first [%s], second [%s]",
          originalCat.getExpires(), altCat.getExpires());
      ok = false;
    }
    if ( ! originalCat.getLastModified().equals( altCat.getLastModified() )) {
      this.comparisonLog.format( "Catalog lastMod dates not the same; first [%s], second [%s]",
          originalCat.getLastModified(), altCat.getLastModified());
      ok = false;
    }

    List<Property> origProperties = originalCat.getProperties();
    List<Property> altProperties = altCat.getProperties();
    if ( origProperties.size() != altProperties.size() ) {
      this.comparisonLog.format( "Catalog properties not the same size: first[%s], second [%s]",
          origProperties.size(), altProperties.size());
      ok = false;
    } else {
      for ( int i = 0; i < origProperties.size(); i++ ) {
        Property curOrigProp = origProperties.get( i );
        Property curAltProp = altProperties.get( i );
        ok &= doCompareProperty( curOrigProp, curAltProp );
      }
    }

    return ok;
  }

  private boolean doCompareProperty( Property originalProperty, Property altPropery ) {
    if ( originalProperty == null || altPropery == null )
      throw new IllegalArgumentException( "Properties to compare may not be null." );

    boolean ok = true;
    if ( ! originalProperty.getName().equals( originalProperty.getName() )) {
      this.comparisonLog.format( "Property names not the same: first [%s], second [%s]",
          originalProperty.getName(), altPropery.getName() );
      ok = false;
    }
    if ( ! originalProperty.getValue().equals( altPropery.getValue() )) {
      this.comparisonLog.format( "Property values not the same; first [%s], second [%s]",
          originalProperty.getValue(), altPropery.getValue());
      ok = false;
    }

    return ok;
  }
}