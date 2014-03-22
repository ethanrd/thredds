package ucar.thredds.catalog.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * _MORE_
 *
 * @author edavis
 */
@RunWith(Parameterized.class)
public class PropertyBuilderTest {
  private ThreddsBuilderFactory threddsBuilderFactory;

  public PropertyBuilderTest( ThreddsBuilderFactory threddsBuilderFactory ) {
    this.threddsBuilderFactory = threddsBuilderFactory;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> threddsBuilderFactoryClasses() {
    Object[][] classes = {
        {new ucar.thredds.catalog.straightimpl.ThreddsBuilderFactoryImpl()},
        {new ucar.thredds.catalog.simpleimpl.ThreddsBuilderFactoryImpl()}
    };
    return Arrays.asList( classes );
  }

  @Test
  public void checkBasicCatalogBuilderCreation() {
    String docBaseUri = "http://mine/thredds/catalog.html";
    CatalogBuilder catalogBuilder =
        this.threddsBuilderFactory.newCatalogBuilder( "cat", docBaseUri, "1.0", null, null );
    assertNotNull( catalogBuilder );

  }

  public void testCtorNullArgs()
  {
    try
    { new PropertyImpl( null, null ); }
    catch ( IllegalArgumentException e )
    { return; }
    catch ( Exception e )
    { fail( "Non-IllegalArgumentException: " + e.getMessage() ); }
    fail( "No IllegalArgumentException." );
  }

  public void testCtorNullName()
  {
    String value = "a value";
    try
    { new PropertyImpl( null, value ); }
    catch ( IllegalArgumentException e )
    { return; }
    catch ( Exception e )
    { fail( "Non-IllegalArgumentException: " + e.getMessage() ); }
    fail( "No IllegalArgumentException." );
  }

  public void testCtorNullValue()
  {
    String name = "a name";
    try
    { new PropertyImpl( name, null ); }
    catch ( IllegalArgumentException e )
    { return; }
    catch ( Exception e )
    { fail( "Non-IllegalArgumentException: " + e.getMessage() ); }
    fail( "No IllegalArgumentException." );
  }

  public void testNormal()
  {
    String name = "a name";
    String value = "a value";
    Property p = null;
    try
    { p = new PropertyImpl( name, value ); }
    catch ( IllegalArgumentException e )
    { fail( "Unexpected IllegalArgumentException: " + e.getMessage() ); }
    catch ( Exception e )
    { fail( "Unexpected Non-IllegalArgumentException: " + e.getMessage()); }

    assertTrue( "Property name [" + p.getName() + "] not as expected [" + name + "].",
        p.getName().equals( name));
    assertTrue( "Property value [" + p.getValue() + "] not as expected [" + value + "].",
        p.getValue().equals( value));
  }

}
