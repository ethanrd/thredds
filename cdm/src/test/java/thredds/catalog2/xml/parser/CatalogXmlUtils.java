package thredds.catalog2.xml.parser;

import thredds.catalog2.builder.*;
import thredds.catalog2.xml.writer.ThreddsXmlWriter;
import thredds.catalog2.xml.writer.ThreddsXmlWriterFactory;
import thredds.catalog2.xml.writer.ThreddsXmlWriterException;
import thredds.catalog2.Catalog;
import thredds.catalog.ServiceType;

import java.io.StringReader;
import java.util.List;
import java.net.URI;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import ucar.nc2.units.DateType;

/**
 * Utility methods for generating catalog XML. 
 *
 * @author edavis
 * @since 4.0
 */
public class CatalogXmlUtils
{
  private static final String catName = "thredds.catalog2.xml.parser.CatalogXmlUtils";
  private static final String catVersion = "1.0.2";

  private CatalogXmlUtils(){}

    public static CatalogBuilder parseCatalogIntoBuilder( URI docBaseUri, String catalogXml )
            throws ThreddsXmlParserException
    {
      ThreddsXmlParserFactory fac = ThreddsXmlParserFactory.newFactory();
      ThreddsXmlParser cp = fac.getCatalogParser();
      return cp.parseIntoBuilder( new StringReader( catalogXml ), docBaseUri );
    }

    public static void writeCatalogXml( Catalog cat )
    {
        ThreddsXmlWriter txw = ThreddsXmlWriterFactory.newInstance().createThreddsXmlWriter();
        try {
            txw.writeCatalog( cat, System.out );
        }
        catch ( ThreddsXmlWriterException e )
        {
            e.printStackTrace();
            fail( "Failed writing catalog to sout: " + e.getMessage() );
        }
    }

  public static void assertCatalogAsExpected( CatalogBuilder catBuilder, URI docBaseUri, DateType expires ) {
    assertCatalogAsExpected( catBuilder, docBaseUri, catName, catVersion, expires != null ? expires.toString() : null);
  }

  public static void assertCatalogAsExpected( CatalogBuilder catBuilder, URI docBaseUri,
                                              String name, String version, String expires )
  {
      assertNotNull( "DocBase URI is null.", docBaseUri) ;
      assertNotNull( "CatalogBuilder [" + docBaseUri + "] is null.", catBuilder) ;

    assertEquals( catBuilder.getDocBaseUri(), docBaseUri);
    assertEquals( catBuilder.getName(), name);
    assertEquals( catBuilder.getVersion(), version);
    if ( expires != null )
      assertEquals( catBuilder.getExpires().toString(), expires.toString());
  }

  public static void assertCatalogWithServiceAsExpected( CatalogBuilder catBuilder, URI docBaseUri, DateType expires)
  {
    assertCatalogAsExpected( catBuilder, docBaseUri, expires );

    List<ServiceBuilder> serviceBldrs = catBuilder.getServiceBuilders();
    assertFalse( serviceBldrs.isEmpty() );
    assertTrue( serviceBldrs.size() == 1 );
    ServiceBuilder serviceBldr = serviceBldrs.get( 0 );
    assertEquals( serviceBldr.getName(), "odap" );
    assertEquals( serviceBldr.getType(), ServiceType.OPENDAP );
    assertEquals( serviceBldr.getBaseUri().toString(), "/thredds/dodsC/" );
  }

  public static void assertCatalogWithCompoundServiceAsExpected( CatalogBuilder catBuilder, URI docBaseUri, DateType expires )
  {
    assertCatalogAsExpected( catBuilder, docBaseUri, expires );

    List<ServiceBuilder> serviceBldrs = catBuilder.getServiceBuilders();
    assertFalse( serviceBldrs.isEmpty() );
    assertTrue( serviceBldrs.size() == 1 );
    ServiceBuilder serviceBldr = serviceBldrs.get( 0 );
    assertEquals( serviceBldr.getName(), "all" );
    assertEquals( serviceBldr.getType(), ServiceType.COMPOUND );
    assertEquals( serviceBldr.getBaseUri().toString(), "" );

    serviceBldrs = serviceBldr.getServiceBuilders();
    assertFalse( serviceBldrs.isEmpty());
    assertEquals( serviceBldrs.size(), 3 );

    serviceBldr = serviceBldrs.get( 0);
    assertEquals( serviceBldr.getName(), "odap" );
    assertEquals( serviceBldr.getType(), ServiceType.OPENDAP );
    assertEquals( serviceBldr.getBaseUri().toString(), "/thredds/dodsC/" );

    serviceBldr = serviceBldrs.get( 1);
    assertEquals( serviceBldr.getName(), "wcs" );
    assertEquals( serviceBldr.getType(), ServiceType.WCS );
    assertEquals( serviceBldr.getBaseUri().toString(), "/thredds/wcs/" );

    serviceBldr = serviceBldrs.get( 2);
    assertEquals( serviceBldr.getName(), "wms" );
    assertEquals( serviceBldr.getType(), ServiceType.WMS );
    assertEquals( serviceBldr.getBaseUri().toString(), "/thredds/wms/" );
  }

  public static void assertCatalogHasSingleAccessDataset( CatalogBuilder catBuilder,
                                                                            URI docBaseUri )
  {
    assertCatalogWithCompoundServiceAsExpected( catBuilder, docBaseUri, null );

    List<DatasetNodeBuilder> dsBuilders = catBuilder.getDatasetNodeBuilders();
    assertTrue( dsBuilders.size() == 1 );
    DatasetNodeBuilder dsnBuilder = dsBuilders.get( 0 );
    if ( !( dsnBuilder instanceof DatasetBuilder ) )
    {
      fail( "DatasetNode [" + dsnBuilder.getName() + "] not a Dataset." );
      return;
    }
    DatasetBuilder dsBldr = (DatasetBuilder) dsnBuilder;
    List<AccessBuilder> accesses = dsBldr.getAccessBuilders();
    assertFalse( "Dataset [" + dsBldr.getName() + "] not accessible.", accesses.isEmpty() );
    assertTrue( accesses.size() == 1 );
    AccessBuilder access = accesses.get( 0 );
    assertEquals( access.getUrlPath(), "test/test1.nc" );
    assertEquals( access.getServiceBuilderName().getType(), ServiceType.OPENDAP );
    assertEquals( access.getServiceBuilderName().getBaseUri().toString(), "/thredds/dodsC/" );
  }

    public static DatasetBuilder assertCatalogWithContainerDatasetAsExpected( CatalogBuilder catBuilder,
                                                                              URI docBaseUri)
    {
        assertCatalogWithCompoundServiceAsExpected( catBuilder, docBaseUri, null );

        List<DatasetNodeBuilder> dsBuilders = catBuilder.getDatasetNodeBuilders();
        assertTrue( dsBuilders.size() == 1 );
        DatasetNodeBuilder dsnBuilder = dsBuilders.get( 0 );
        assertNotNull( dsnBuilder);
        if ( !( dsnBuilder instanceof DatasetBuilder ) )
        {
            fail( "DatasetNode [" + dsnBuilder.getName() + "] not a Dataset." );
            return null;
        }
        DatasetBuilder dsBldr = (DatasetBuilder) dsnBuilder;

        // Check that the container dataset isn't accessible, two methods:
        // 1)
        assertFalse( dsBldr.isAccessible() );
        // 2)
        List<AccessBuilder> accesses = dsBldr.getAccessBuilders();
        assertTrue( accesses.isEmpty());

        assertEquals( dsBldr.getName(), "container dataset" );
        assertEquals( dsBldr.getId(), "containerDs" );

        assertFalse( dsBldr.isCollection() );
        assertFalse( dsBldr.isBuildable() );

        return dsBldr;
    }

  public static void assertNestedDatasetIsAccessible( CatalogBuilder catBuilder,
                                                      URI docBaseUri )
  {
    DatasetBuilder dsBldr = getAndAssertNestedDataset( catBuilder, docBaseUri );

    List<AccessBuilder> accesses = dsBldr.getAccessBuilders();
    assertFalse( accesses.isEmpty() );
    assertTrue( accesses.size() == 1 );
    AccessBuilder access = accesses.get( 0 );
    assertEquals( access.getUrlPath(), "test/test1.nc" );
    assertEquals( access.getServiceBuilderName().getType(), ServiceType.OPENDAP );
    assertEquals( access.getServiceBuilderName().getBaseUri().toString(), "/thredds/dodsC/" );
  }

  public static void assertNestedDatasetIsNotAccessible( CatalogBuilder catBuilder,
                                                         URI docBaseUri )
  {
    DatasetBuilder dsBldr = getAndAssertNestedDataset( catBuilder, docBaseUri );

    List<AccessBuilder> accesses = dsBldr.getAccessBuilders();
    assertTrue( accesses.isEmpty() );
  }

  public static DatasetBuilder getAndAssertNestedDataset( CatalogBuilder catBuilder,
                                                          URI docBaseUri )
  {
    assertCatalogWithCompoundServiceAsExpected( catBuilder, docBaseUri, null );

    // Get first dataset.
    List<DatasetNodeBuilder> dsBuilders = catBuilder.getDatasetNodeBuilders();
    assertTrue( dsBuilders.size() == 1 );
    DatasetNodeBuilder dsnBuilder = dsBuilders.get( 0 );

    // Get nested dataset.
    dsBuilders = dsnBuilder.getDatasetNodeBuilders();
    assertTrue( dsBuilders.size() == 1 );
    dsnBuilder = dsBuilders.get( 0 );

    if ( !( dsnBuilder instanceof DatasetBuilder ) )
    {
      fail( "DatasetNode [" + dsnBuilder.getName() + "] not a Dataset." );
      return null;
    }

    return (DatasetBuilder) dsnBuilder;
  }

}
