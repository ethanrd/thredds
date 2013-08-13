package thredds.catalog2.straightimpl;

import org.junit.Test;
import thredds.catalog.ServiceType;
import thredds.catalog2.builder.BuilderIssues;
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
  public void checkNotAbleToAddServiceToNonCompoundService() {
    ServiceBuilderImpl serviceBuilder = new ServiceBuilderImpl( "odap", ServiceType.OPENDAP, "/thredds/dodsC/", null);
    serviceBuilder.addService( "wms", ServiceType.WMS, "/thredds/wms/");

    assertEquals(ThreddsBuilder.Buildable.DONT_KNOW, serviceBuilder.isBuildable());
    BuilderIssues issues = serviceBuilder.checkForIssues();
    assertFalse( "Non-compound, containing service not empty.", issues.isEmpty());
    assertTrue( "Non-compound, containing service should be valid.", issues.isValid());
    assertEquals( "Don't have exactly zero WARN issue.", 0, issues.getNumWarningIssues());
    assertEquals( "Don't have exactly one ERROR issue.", 1, issues.getNumErrorIssues());
    assertEquals( "Don't have exactly zero FATAL issue.", 0, issues.getNumFatalIssues());
  }
}
