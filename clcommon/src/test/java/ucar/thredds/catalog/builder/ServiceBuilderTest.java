package ucar.thredds.catalog.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ucar.thredds.catalog.Property;
import ucar.thredds.catalog.ServiceType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
    Object[][] classes = {
        {new ucar.thredds.catalog.straightimpl.ThreddsBuilderFactoryImpl()},
        {new ucar.thredds.catalog.simpleimpl.ThreddsBuilderFactoryImpl()}
    };
    return Arrays.asList( classes );
  }

  @Test
  public void checkBasicServiceBuilderCreation() {
    String name = "name";
    ServiceType dapServiceType = ServiceType.OPENDAP;
    String baseUri = "/thredds/dodsC/";
    ServiceBuilder serviceBuilder = this.threddsBuilderFactory.newServiceBuilder( name, dapServiceType, baseUri );
    assertNotNull( serviceBuilder );

    assertEquals( name, serviceBuilder.getName() );
    assertEquals( dapServiceType, serviceBuilder.getType() );
    assertEquals( baseUri, serviceBuilder.getBaseUriAsString() );
    assertEquals( "", serviceBuilder.getDescription() );
    assertEquals( "", serviceBuilder.getSuffix() );

    assertEquals( ThreddsBuilder.Buildable.DONT_KNOW, serviceBuilder.isBuildable() );

    BuilderIssues builderIssues = serviceBuilder.checkForIssues();
    assertNotNull( builderIssues );
    assertTrue( builderIssues.isValid() );
    assertEquals( ThreddsBuilder.Buildable.YES, serviceBuilder.isBuildable() );

    String wcsServiceName = "wcs";
    ServiceType wcsServiceType = ServiceType.WCS;
    String wcsServiceBaseUri = "/thredds/wcs/";
    String wcsServiceDescrip = "good descrip";
    String wcsServiceSuffix = ".wcs";

    serviceBuilder.setName( wcsServiceName );
    serviceBuilder.setType( wcsServiceType );
    serviceBuilder.setBaseUriAsString( wcsServiceBaseUri );
    serviceBuilder.setDescription( wcsServiceDescrip );
    serviceBuilder.setSuffix( wcsServiceSuffix );
    assertEquals( ThreddsBuilder.Buildable.DONT_KNOW, serviceBuilder.isBuildable() );

    builderIssues = serviceBuilder.checkForIssues();
    assertNotNull( builderIssues );
    assertTrue( builderIssues.isValid());
    assertEquals( ThreddsBuilder.Buildable.YES, serviceBuilder.isBuildable());

    assertEquals( wcsServiceName, serviceBuilder.getName() );
    assertEquals( wcsServiceType, serviceBuilder.getType() );
    assertEquals( wcsServiceBaseUri, serviceBuilder.getBaseUriAsString() );
    assertEquals( wcsServiceDescrip, serviceBuilder.getDescription() );
    assertEquals( wcsServiceSuffix, serviceBuilder.getSuffix() );
  }

  @Test
  public void checkProperties() {
    ServiceBuilder serviceBuilder =
        this.threddsBuilderFactory.newServiceBuilder( "dap", ServiceType.OPENDAP, "/thredds/dap/" );
    assertNotNull( serviceBuilder );

    serviceBuilder.addProperty( "name1", "value1" );
    serviceBuilder.addProperty( "name2", "value2" );
    serviceBuilder.addProperty( "name3", "value3" );
    List<Property> properties = serviceBuilder.getProperties();
    assertEquals( 3, properties.size() );
    Property property = properties.get( 0 );
    assertEquals( "name1", property.getName() );
    assertEquals( "value1", property.getValue() );
    List<Property> propsName = serviceBuilder.getProperties( "name1" );
    assertEquals( 1, propsName.size() );
    assertEquals( property, propsName.get( 0 ) );

    property = properties.get( 1 );
    assertEquals( "name2", property.getName() );
    assertEquals( "value2", property.getValue() );
    propsName = serviceBuilder.getProperties( "name2" );
    assertEquals( 1, propsName.size() );
    assertEquals( property, propsName.get( 0 ) );

    property = properties.get( 2 );
    assertEquals( "name3", property.getName() );
    assertEquals( "value3", property.getValue() );
    propsName = serviceBuilder.getProperties( "name3" );
    assertEquals( 1, propsName.size() );
    assertEquals( property, propsName.get( 0 ) );

    assertTrue( serviceBuilder.removeProperty( properties.get( 1) ));
    properties = serviceBuilder.getProperties();
    assertEquals( 2, properties.size() );
    property = properties.get( 0 );
    assertEquals( "name1", property.getName() );
    assertEquals( "value1", property.getValue() );
    property = properties.get( 1 );
    assertEquals( "name3", property.getName() );
    assertEquals( "value3", property.getValue() );
  }
}
