package ucar.thredds.catalog.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ucar.thredds.catalog.ServiceType;
import ucar.thredds.catalog.builder.BuilderIssues;
import ucar.thredds.catalog.builder.ThreddsBuilderFactory;
import ucar.thredds.catalog.testutil.ThreddsBuilderFactoryUtils;

import java.util.Collection;

import static org.junit.Assert.*;

/**
 * _MORE_
 *
 * @author edavis
 */
@RunWith(Parameterized.class)
public class ServiceBuilderContainerTest {
    private ThreddsBuilderFactory threddsBuilderFactory;

    public ServiceBuilderContainerTest( ThreddsBuilderFactory threddsBuilderFactory ) {
      this.threddsBuilderFactory = threddsBuilderFactory;
    }

  @Parameterized.Parameters
  public static Collection<Object[]> threddsBuilderFactoryClasses() {
    return ThreddsBuilderFactoryUtils.threddsBuilderFactoryClasses();
  }

    @Test
  public void checkBunchOfStuff() {
    ServiceBuilderContainer sbc = new ServiceBuilderContainer( this.threddsBuilderFactory, new CatalogWideServiceBuilderTracker() );
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

//    sbc.addService( "all", ServiceType.COMPOUND, "" )
//        .addService( "ncss", ServiceType.NetcdfSubset, "/thredds/ncss/" );
//    assertEquals( 4, sbc.size() );
//
//    assertEquals( "/thredds/wcs/", sbc.getServiceBuilderByName( "wcs" ).getBaseUri() );
//
//    assertNull( sbc.getServiceBuilderByName( "ncss" ));
//    ServiceBuilder service = sbc.getServiceByGloballyUniqueName( "ncss" );
//    assertNotNull( service );
//    assertEquals( "/thredds/ncss/", service.getBaseUri() );
//
//    service = sbc.getServiceBuilderByName( "wms" );
//    assertNotNull( service );
//    assertEquals( "/thredds/wms/", service.getBaseUri() );
//
//    sbc.removeService( service );
//    service = sbc.getServiceBuilderByName( "wms" );
//    assertNull( service );
  }
}
