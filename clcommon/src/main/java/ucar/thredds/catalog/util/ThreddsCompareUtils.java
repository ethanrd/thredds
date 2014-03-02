package ucar.thredds.catalog.util;

import ucar.thredds.catalog.builder.CatalogBuilder;

import java.util.Formatter;

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
}
