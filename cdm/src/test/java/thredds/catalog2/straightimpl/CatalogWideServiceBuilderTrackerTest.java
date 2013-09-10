package thredds.catalog2.straightimpl;

import org.junit.Test;
import static org.junit.Assert.*;
import thredds.catalog.ServiceType;

/**
 * _MORE_
 *
 * @author edavis
 */
public class CatalogWideServiceBuilderTrackerTest {

  @Test
  public void checkMultipleAdditionsOfSameServiceOK() {
    CatalogWideServiceBuilderTracker cwsbt = new CatalogWideServiceBuilderTracker();
    ServiceBuilderImpl odap1 = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "/thredds/dodsC/", null);
    cwsbt.addService( odap1);
    cwsbt.addService( odap1);
    assertEquals( 1, cwsbt.numberOfReferenceableServices());
  }

  @Test
  public void checkRemoveReferencableServiceWithDuplicateNamePromotesDuplicateNamedService() {
    CatalogWideServiceBuilderTracker cwsbt = new CatalogWideServiceBuilderTracker();
    ServiceBuilderImpl odap1 = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "/thredds/dodsC/", null);
    ServiceBuilderImpl odap2 = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "/thredds/dodsC2/", null);
    ServiceBuilderImpl wcs = new ServiceBuilderImpl( "wcs", ServiceType.WCS, "/thredds/wcs/", null);

    cwsbt.addService( odap1);
    cwsbt.addService( odap2);
    cwsbt.addService( wcs);

    assertEquals( 2, cwsbt.numberOfReferenceableServices());
    assertEquals( 1, cwsbt.numberOfNonReferencableServicess());
    assertEquals( odap1, cwsbt.getReferenceableService( "odap"));
    assertEquals( wcs, cwsbt.getReferenceableService( "wcs"));

    assertTrue( cwsbt.removeService( odap1));
    assertEquals( 2, cwsbt.numberOfReferenceableServices());
    assertEquals( 0, cwsbt.numberOfNonReferencableServicess());
    assertEquals( odap2, cwsbt.getReferenceableService( "odap"));
    assertEquals( wcs, cwsbt.getReferenceableService( "wcs"));

  }
}
