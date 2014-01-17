package thredds.catalog2.straightimpl;

import org.junit.Test;
import thredds.catalog.ServiceType;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ServiceBuilder;
import thredds.catalog2.builder.ThreddsBuilder;

import static org.junit.Assert.*;

/**
 * _MORE_
 *
 * @author edavis
 */
public class ServiceBuilderContainerTest {

  @Test
  public void checkBunchOfStuff() {
    ServiceBuilderContainer sbc = new ServiceBuilderContainer( new CatalogWideServiceBuilderTracker() );
    assertNotNull( sbc );
    assertTrue( sbc.isEmpty());
    assertEquals( 0, sbc.size() );
    BuilderIssues builderIssues = sbc.checkForIssues();
    assertNotNull( builderIssues );
    assertTrue( builderIssues.isValid() );

    sbc.addService( "odap", ServiceType.OPENDAP, "/thredds/dodsC/" );
    sbc.addService( "wcs", ServiceType.WCS, "/thredds/wcs/" );
    sbc.addService( "wms", ServiceType.WMS, "/thredds/wms/" );
    assertFalse( sbc.isEmpty());
    assertEquals( 3, sbc.size() );

    sbc.addService( "all", ServiceType.COMPOUND, "" )
        .addService( "ncss", ServiceType.NetcdfSubset, "/thredds/ncss/" );
    assertEquals( 4, sbc.size() );

    assertEquals( "/thredds/wcs/", sbc.getServiceBuilderByName( "wcs" ).getBaseUri() );

    assertNull( sbc.getServiceBuilderByName( "ncss" ));
    ServiceBuilder service = sbc.getServiceByGloballyUniqueName( "ncss" );
    assertNotNull( service );
    assertEquals( "/thredds/ncss/", service.getBaseUri() );

    service = sbc.getServiceBuilderByName( "wms" );
    assertNotNull( service );
    assertEquals( "/thredds/wms/", service.getBaseUri() );

    sbc.removeService( service );
    service = sbc.getServiceBuilderByName( "wms" );
    assertNull( service );
  }
}
