package thredds.catalog2.straightimpl;

import org.junit.Test;
import thredds.catalog.ServiceType;
import thredds.catalog2.Access;
import thredds.catalog2.Service;
import thredds.catalog2.builder.AccessBuilder;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ServiceBuilder;
import thredds.catalog2.builder.ThreddsBuilder;

import static org.junit.Assert.*;

/**
 * _MORE_
 *
 * @author edavis
 */
public class AccessBuilderTest {

  @Test(expected=IllegalArgumentException.class)
  public void t() {
    new AccessBuilderImpl( "", null );
  }

  @Test
  public void checkResultingAccessFoundReferencedService() {
    ServiceBuilder sb = new ServiceBuilderImpl( "dap", ServiceType.OPENDAP, "/thredds/dodsC/", new CatalogWideServiceBuilderTracker());

    AccessBuilder accessBuilder = new AccessBuilderImpl( "/my/data.nc", sb);
    BuilderIssues builderIssues = accessBuilder.checkForIssues();
    assertTrue( builderIssues.isValid());
    assertEquals( ThreddsBuilder.Buildable.YES, accessBuilder.isBuildable());
    Access access = accessBuilder.build();
    assertNotNull( access.getService());
  }

}
