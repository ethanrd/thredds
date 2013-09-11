package thredds.catalog2.straightimpl;

import org.junit.Test;
import thredds.catalog.ServiceType;
import thredds.catalog2.Service;
import thredds.catalog2.builder.BuilderIssue;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ServiceBuilder;
import thredds.catalog2.builder.ThreddsBuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * _MORE_
 *
 * @author edavis
 */
public class ServiceBuilderTest {

  @Test
  public void checkConformance_NonCompoundServiceNotValidWithEmptyBaseUri() {
    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "", null);
    BuilderIssues issues = serviceBuilder.checkForIssues();
    assertFalse(issues.isEmpty());
    assertEquals( "Don't have exactly zero WARN issue.", 1, issues.getNumWarningIssues());
    assertEquals( "Don't have exactly one ERROR issue.", 0, issues.getNumErrorIssues());
    assertEquals( "Don't have exactly zero FATAL issue.", 0, issues.getNumFatalIssues());
    assertTrue(issues.isValid());
  }

  @Test
  public void checkConformance_NotAbleToAddServiceToNonCompoundService() {
    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "/thredds/dodsC/", null);
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
    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "/thredds/dodsC/", null);
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
    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( "all", ServiceType.COMPOUND, "", null);
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
    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( "all", ServiceType.COMPOUND, "", null);
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
