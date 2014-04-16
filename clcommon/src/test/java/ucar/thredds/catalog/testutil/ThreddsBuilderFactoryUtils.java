package ucar.thredds.catalog.testutil;

import java.util.Arrays;
import java.util.Collection;

/**
 * _MORE_
 *
 * @author edavis
 */
public class ThreddsBuilderFactoryUtils {
  private ThreddsBuilderFactoryUtils() {}

  /**
   * Utility method for handing a set of ThreddsBuilderFactory impls in as the
   * parameters to be used in parameterized JUnit tests.
   *
   * @return
   */
  public static Collection<Object[]> threddsBuilderFactoryClasses() {
    Object[][] classes = {
        {new ucar.thredds.catalog.straightimpl.ThreddsBuilderFactoryImpl()},
        {new ucar.thredds.catalog.simpleimpl.ThreddsBuilderFactoryImpl()}
    };
    return Arrays.asList( classes );
  }

}
