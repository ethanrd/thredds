package thredds.catalog2.straightimpl;

import thredds.catalog2.Access;
import thredds.catalog2.builder.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;

/**
 * _MORE_
 *
 * @author edavis
 */
//@RunWith(Parameterized.class)
public class AccessBuilderTest {
  private ThreddsBuilderFactory threddsBuilderFactory;

  public AccessBuilderTest( ThreddsBuilderFactory threddsBuilderFactory ) {
    this.threddsBuilderFactory = threddsBuilderFactory;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> threddsBuilderFactoryClasses() {
    Object[] classes = new ThreddsBuilderFactory[] {
        new thredds.catalog2.straightimpl.ThreddsBuilderFactoryImpl()
//        , new thredds.catalog2.simpleImpl.ThreddsBuilderFactoryImpl()
    };
    return Collections.singleton( classes );
  }


  @Test(expected=IllegalArgumentException.class)
  public void t() {
    this.threddsBuilderFactory.newAccessBuilder( "", null );
  }

  @Test
  public void checkResultingAccessFoundReferencedService() {

    AccessBuilder accessBuilder =
        threddsBuilderFactory.newAccessBuilder( "dap", "/my/data.nc" );
    assertNotNull( accessBuilder );
    BuilderIssues builderIssues = accessBuilder.checkForIssues();
    assertTrue( builderIssues.isValid());
    assertEquals( ThreddsBuilder.Buildable.YES, accessBuilder.isBuildable());
    Access access = accessBuilder.build();
    assertNotNull( access.getService());
  }

}
