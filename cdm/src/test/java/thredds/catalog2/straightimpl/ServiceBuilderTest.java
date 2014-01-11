package thredds.catalog2.straightimpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import thredds.catalog.ServiceType;
import thredds.catalog2.Service;
import thredds.catalog2.builder.*;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * _MORE_
 *
 * @author edavis
 */
@RunWith(Parameterized.class)
public class ServiceBuilderTest {
  private ThreddsBuilderFactory threddsBuilderFactory;

  public ServiceBuilderTest( ThreddsBuilderFactory threddsBuilderFactory ) {
    this.threddsBuilderFactory = threddsBuilderFactory;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> threddsBuilderFactoryClasses() {
    Object[] classes = new ThreddsBuilderFactory[] {
      new thredds.catalog2.straightimpl.ThreddsBuilderFactoryImpl()
//      , new thredds.catalog2.simpleImpl.ThreddsBuilderFactoryImpl()
    };
    return Collections.singleton( classes );
  }

  @Test
  public void checkBasicServiceBuilderCreation() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "odap", ServiceType.OPENDAP, "/thredds/dodsC/" );
    assertNotNull( serviceBuilder );

    assertEquals( "odap", serviceBuilder.getName() );
    assertEquals( ServiceType.OPENDAP, serviceBuilder.getType() );
    assertEquals( "/thredds/dodsC/", serviceBuilder.getBaseUri() );
    assertEquals( "", serviceBuilder.getSuffix() );
    assertEquals( "", serviceBuilder.getDescription() );

    assertTrue( serviceBuilder.getServiceBuilders().isEmpty() );
    assertTrue( serviceBuilder.getPropertyNames().isEmpty() );

    BuilderIssues builderIssues = serviceBuilder.checkForIssues();
    assertNotNull( builderIssues );
    assertTrue( builderIssues.isValid());

    serviceBuilder.setName( "all" );
    serviceBuilder.setType( ServiceType.COMPOUND );
    serviceBuilder.setBaseUriAsString( "" );
    serviceBuilder.setSuffix( ".dsr" );
    serviceBuilder.setDescription( "contain all services" );
    serviceBuilder.addProperty( "name", "val" );
    serviceBuilder.addService( "odap", ServiceType.OPENDAP, "/thredds/dodsC" );
  }

  @Test
  public void check() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "all", ServiceType.COMPOUND, "");
    serviceBuilder.removeService( null );
  }

  @Test
  public void checkConformance_NonCompoundServiceNotValidWithEmptyBaseUri() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "odap", ServiceType.OPENDAP, "");
    BuilderIssues issues = serviceBuilder.checkForIssues();
    assertFalse(issues.isEmpty());
    assertEquals( "Don't have exactly zero WARN issue.", 1, issues.getNumWarningIssues());
    assertEquals( "Don't have exactly one ERROR issue.", 0, issues.getNumErrorIssues());
    assertEquals( "Don't have exactly zero FATAL issue.", 0, issues.getNumFatalIssues());
    assertTrue( issues.isValid() );
  }

  @Test
  public void checkConformance_NotAbleToAddServiceToNonCompoundService() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "odap", ServiceType.OPENDAP, "/thredds/dodsC/" );

    serviceBuilder.addService( "wms", ServiceType.WMS, "/thredds/wms/");

    assertEquals(ThreddsBuilder.Buildable.DONT_KNOW, serviceBuilder.isBuildable());
    BuilderIssues issues = serviceBuilder.checkForIssues();
    assertFalse( "Non-compound service should not contain a service.", issues.isEmpty());
    assertTrue( "Non-compound, containing service should be valid.", issues.isValid());
    assertEquals( "Don't have exactly zero WARN issue.", 0, issues.getNumWarningIssues());
    assertEquals( "Don't have exactly one ERROR issue.", 1, issues.getNumErrorIssues());
    assertEquals( "Don't have exactly zero FATAL issue.", 0, issues.getNumFatalIssues());
  }

  @Test
  public void checkConformance_BuildOfServiceWithProperties() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "odap", ServiceType.OPENDAP, "/thredds/dodsC/" );

    serviceBuilder.addProperty( "name1", "value1");
    serviceBuilder.addProperty( "name2", "value2");
    serviceBuilder.addProperty( "name3", "value3");
    serviceBuilder.addProperty( "name4", "value4");
    String value1 = serviceBuilder.getPropertyValue("name1");
    assertEquals( "Property [name1] has unexpected value [" + value1 + "].", "value1", value1);

    assertEquals( "", 4, serviceBuilder.getProperties().size());

    assertEquals("", ThreddsBuilder.Buildable.DONT_KNOW, serviceBuilder.isBuildable());
    BuilderIssues issues = serviceBuilder.checkForIssues();
    assertTrue("", issues.isEmpty());

    ServiceImpl service = (ServiceImpl) serviceBuilder.build();
    assertEquals( "", "odap", service.getName());
  }

  @Test
  public void checkRootServiceTrackingOfServicesByName() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "all", ServiceType.COMPOUND, "");

    serviceBuilder.addService( "odap", ServiceType.OPENDAP, "/thredds/dodsC/" );
    serviceBuilder.addService( "http", ServiceType.HTTP, "/thredds/fileServer/" );
    ServiceBuilder sBGrid = serviceBuilder.addService( "grid", ServiceType.COMPOUND, "" );
    ServiceBuilder sBWcs = sBGrid.addService( "wcs", ServiceType.WCS, "/thredds/wcs/" );
    ServiceBuilder sBWms = sBGrid.addService( "wms", ServiceType.WMS, "/thredds/wms/" );
    ServiceBuilder sBWcs2 = serviceBuilder.addService( "wcs", ServiceType.ADDE, "/thredds/adde/" );

    assertEquals( sBWcs, serviceBuilder.findServiceBuilderByNameGlobally( "wcs"));
  }

  @Test
  public void checkBuildOfNestedService() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "all", ServiceType.COMPOUND, "");

    serviceBuilder.addService( "odap", ServiceType.OPENDAP, "/thredds/dodsC/" );
    serviceBuilder.addService( "http", ServiceType.HTTP, "/thredds/fileServer/" );
    ServiceBuilder sBGrid = serviceBuilder.addService( "grid", ServiceType.COMPOUND, "" );
    ServiceBuilder sBWcs = sBGrid.addService( "wcs", ServiceType.WCS, "/thredds/wcs/" );
    ServiceBuilder sBWms = sBGrid.addService( "wms", ServiceType.WMS, "/thredds/wms/" );
    ServiceBuilder sBWcs2 = serviceBuilder.addService( "wcs", ServiceType.ADDE, "/thredds/adde/" );

    BuilderIssues builderIssues = serviceBuilder.checkForIssues();
    assertFalse( builderIssues.isEmpty());
    assertEquals( 1, builderIssues.getNumWarningIssues());
    assertEquals( 0, builderIssues.getNumErrorIssues());
    assertEquals( 0, builderIssues.getNumFatalIssues());
    assertTrue( builderIssues.isValid());

    assertEquals(ThreddsBuilder.Buildable.YES, serviceBuilder.isBuildable());
    Service service = serviceBuilder.build();
    Service wcs = service.findServiceByNameGlobally("wcs");
    assertEquals("wcs", wcs.getName());
    assertEquals( ServiceType.WCS, wcs.getType());
    assertEquals( "/thredds/wcs/", wcs.getBaseUri().toString());
  }
}
