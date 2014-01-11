package thredds.catalog2.straightimpl;

import org.junit.runner.RunWith;
import thredds.catalog.DataFormatType;
import thredds.catalog2.Access;
import thredds.catalog2.builder.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * _MORE_
 *
 * @author edavis
 */
@RunWith(Parameterized.class)
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


  @Test // (expected=IllegalArgumentException.class)
  public void checkBasicAccessBuilderCreation() {
    AccessBuilder odapAccessBuilder = this.threddsBuilderFactory.newAccessBuilder( "odap", "good/data.nc" );
    assertNotNull( odapAccessBuilder );

    assertEquals( "odap", odapAccessBuilder.getServiceBuilderName() );
    assertEquals( "good/data.nc", odapAccessBuilder.getUrlPath() );
    assertEquals( DataFormatType.NONE, odapAccessBuilder.getDataFormat() );
    assertEquals( -1, odapAccessBuilder.getDataSize() );

    BuilderIssues builderIssues = odapAccessBuilder.checkForIssues();
    assertNotNull( builderIssues );
    assertFalse( builderIssues.isValid());
//    assertEquals( ThreddsBuilder.Buildable.YES, odapAccessBuilder.isBuildable());
//    Access access = odapAccessBuilder.build();
//    assertNotNull( access.getService());
  }

//  @Test
//  public void checkResultingAccessFoundReferencedService() {
//
//    AccessBuilder accessBuilder =
//        threddsBuilderFactory.newAccessBuilder( "dap", "/my/data.nc" );
//    assertNotNull( accessBuilder );
//    BuilderIssues builderIssues = accessBuilder.checkForIssues();
//    assertTrue( builderIssues.isValid());
//    assertEquals( ThreddsBuilder.Buildable.YES, accessBuilder.isBuildable());
//    Access access = accessBuilder.build();
//    assertNotNull( access.getService());
//  }

}
