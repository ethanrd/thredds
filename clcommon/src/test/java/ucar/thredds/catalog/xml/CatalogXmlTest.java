package ucar.thredds.catalog.xml;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ucar.thredds.catalog.Catalog;
import ucar.thredds.catalog.builder.BuilderIssues;
import ucar.thredds.catalog.builder.CatalogBuilder;
import ucar.thredds.catalog.builder.ThreddsBuilder;
import ucar.thredds.catalog.testutil.CatalogTestUtils;
import ucar.thredds.catalog.util.ThreddsCompareUtils;
import ucar.thredds.catalog.xml.parser.ThreddsXmlParser;
import ucar.thredds.catalog.xml.parser.ThreddsXmlParserException;
import ucar.thredds.catalog.xml.parser.ThreddsXmlParserFactory;
import ucar.thredds.catalog.xml.testutil.CatalogXmlAsStringUtils;


import ucar.nc2.units.DateType;
import ucar.thredds.catalog.xml.writer.ThreddsXmlWriter;
import ucar.thredds.catalog.xml.writer.ThreddsXmlWriterException;
import ucar.thredds.catalog.xml.writer.ThreddsXmlWriterFactory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Formatter;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
public class CatalogXmlTest
{

  // Do we have all these:
    // 1) dataset with urlPath and serviceName attributes
    // 2) dataset with urlPath att and serviceName child element
    // 3) dataset with urlPath att and child metadata element with child serviceName element
    // 4) same as 3 but metadata element has inherited="true" attribute
    // 5) same as 3 but metadata element is in dataset that is parent to dataset with urlPath
    // 6) 1-5 where serviceName points to single top-level service
    // 7) 1-5 where serviceName points to compound service
    // 8) 1-5 where serviceName points to a single service contained in a compound service


    @Test
  public void parseAndWriteCatalogSkeleton()
        throws ThreddsXmlParserException, ThreddsXmlWriterException
    {
      String baseUriString = "http://cat2.CatalogXmlTest/simpleCatalog.xml";
      String name = "Cat Skeleton";
      String version = "1.0.4";
      DateType expires = new DateType( false, new Date( System.currentTimeMillis() + 60*60*1000));
      DateType lastModified = new DateType( false, new Date( System.currentTimeMillis() - 60*60*1000));

      actualTestFor_parseAndWriteCatalogSkeleton( baseUriString, name, version, expires, lastModified );
      actualTestFor_parseAndWriteCatalogSkeleton( baseUriString, name, version, expires, null );
      actualTestFor_parseAndWriteCatalogSkeleton( baseUriString, name, version, null, null );
      actualTestFor_parseAndWriteCatalogSkeleton( baseUriString, name, null, null, null );
      actualTestFor_parseAndWriteCatalogSkeleton( baseUriString, null, null, null, null );
    }

  private void actualTestFor_parseAndWriteCatalogSkeleton( String baseUriString, String name, String version,
                                                           DateType expires, DateType lastModified )
      throws ThreddsXmlParserException, ThreddsXmlWriterException
  {
    String xml = CatalogXmlAsStringUtils.getCatalog( name, version, expires, lastModified);

    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
                                                   baseUriString );
    assertNotNull( catBuilder );

    CatalogTestUtils.assertCatalogAsExpected( catBuilder, baseUriString, name, version, expires, lastModified );

    BuilderIssues builderIssues = catBuilder.checkForIssues();
    assertEquals( ThreddsBuilder.Buildable.YES, catBuilder.isBuildable() );
    assertTrue( builderIssues.isValid() );
    Catalog catalog = catBuilder.build();

    ThreddsXmlWriter txw = ThreddsXmlWriterFactory.newInstance().createThreddsXmlWriter();

    StringWriter catWriter = new StringWriter();
    txw.writeCatalog( catalog, catWriter );
    System.out.println( catWriter.toString());

    CatalogBuilder catBuilder2 = cp.parseIntoBuilder( new StringReader( catWriter.toString()), baseUriString );
    BuilderIssues builderIssues2 = catBuilder2.checkForIssues();
    assertEquals( ThreddsBuilder.Buildable.YES, catBuilder2.isBuildable() );
    assertTrue( builderIssues2.isValid() );
    Catalog catalog2 = catBuilder2.build();

    Formatter compareLog = new Formatter();
    assertTrue( compareLog.toString(),
        ThreddsCompareUtils.compareCatalogBuilders( catBuilder, catBuilder2, compareLog ));
    //ThreddsCompareUtils.compareCatalogBuilders( catalog, catalog2 );
  }

//  @Test
//  public void parseCatalogWithService()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String baseUriString = "http://cat2.CatalogXmlTest/CatalogWithService.xml";
//    URI docBaseUri = new URI( baseUriString );
//    DateType expires = new DateType( false, new Date( System.currentTimeMillis() + 60*60*1000));
//    String xml = CatalogXmlAsStringUtils.getCatalogWithService( expires);
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertCatalogWithServiceAsExpected( catBuilder, docBaseUri, expires);
//  }
//
//  @Test
//  public void parseCatalogWithCompoundService()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String baseUriString = "http://cat2.CatalogXmlTest/CatalogWithCompoundService.xml";
//    URI docBaseUri = new URI( baseUriString );
//    DateType expires = new DateType( false, new Date( System.currentTimeMillis() + 60*60*1000));
//    String xml = CatalogXmlAsStringUtils.getCatalogWithCompoundService( expires);
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertCatalogWithCompoundServiceAsExpected( catBuilder, docBaseUri, expires);
//  }
//
//    @Test
//    public void parseContainerDatasetWithMetadataServicename()
//            throws URISyntaxException,
//                   ThreddsXmlParserException
//    {
//        String docBaseUriString = "http://cat2.ParseContainerDatasetTest/MetadataServiceName.xml";
//        URI docBaseUri = new URI( docBaseUriString);
//        String catXml = CatalogXmlAsStringUtils.wrapThreddsXmlInCatalogDatasetMetadata( "<serviceName>odap</serviceName>\n" );
//
//        CatalogBuilder catBuilder = CatalogXmlUtils.parseCatalogIntoBuilder(cp.parseIntoBuilder(new StringReader(catXml), docBaseUri));
//        assertNotNull( catBuilder );
//    }
//
//    @Test
//  public void parseAccessibleDatasetWithRawServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlAsStringUtils.getCatalogWithSingleAccessDatasetWithRawServiceName();
//    String baseUriString = "http://cat2.ParseAccessibleDatasetTest/RawServiceName.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertCatalogHasSingleAccessDataset( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseAccessibleDatasetWithMetadataServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlAsStringUtils.getCatalogWithSingleAccessDatasetWithMetadataServiceName();
//    String baseUriString = "http://cat2.ParseAccessibleDatasetTest/MetadataServiceName.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertCatalogHasSingleAccessDataset( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseAccessibleDatasetWithInheritedMetadataServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlUtils.getCatalogWithSingleAccessDatasetWithInheritedMetadataServiceName();
//    String baseUriString = "http://cat2.ParseAccessibleDatasetTest/InheritedMetadataServiceName.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertCatalogHasSingleAccessDataset( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseAccessibleDatasetOldStyle()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlAsStringUtils.getCatalogWithSingleAccessDatasetOldStyle();
//    String baseUriString = "http://cat2.ParseAccessibleDatasetTest/OldStyleAccess.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertCatalogHasSingleAccessDataset( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseNestedDatasetWithRawServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlUtils.getNestedDatasetWithRawServiceName();
//    String baseUriString = "http://cat2.ParseNestedDatasetTest/RawServiceNameNotAccessible.xml";
//      System.out.println( "Catalog ["+baseUriString+"]:\n" + xml );
//
//
//      ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertNestedDatasetIsNotAccessible( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseNestedDatasetWithMetadataServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlUtils.getNestedDatasetWithMetadataServiceName();
//    String baseUriString = "http://cat2.ParseNestedDatasetTest/MetadataServiceNameNotAccessible.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertNestedDatasetIsNotAccessible( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseNestedDatasetWithUninheritedMetadataServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlUtils.getNestedDatasetWithUninheritedMetadataServiceName();
//    String baseUriString = "http://cat2.ParseNestedDatasetTest/UninheritedMetadataServiceNameNotAccessible.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertNestedDatasetIsNotAccessible( catBuilder, docBaseUri );
//  }
//
//  @Test
//  public void parseNestedDatasetWithInheritedMetadataServiceName()
//          throws URISyntaxException,
//                 XMLStreamException,
//                 ThreddsXmlParserException
//  {
//    String xml = CatalogXmlUtils.getNestedDatasetWithInheritedMetadataServiceName();
//    String baseUriString = "http://cat2.ParseNestedDatasetTest/InheritedMetadataServiceNameNotAccessible.xml";
//
//    ThreddsXmlParser cp = ThreddsXmlParserFactory.newInstance().getParser();
//    URI docBaseUri = new URI( baseUriString );
//    CatalogBuilder catBuilder = cp.parseIntoBuilder( new StringReader( xml ),
//                                                     docBaseUri );
//    assertNotNull( catBuilder );
//
//    CatalogXmlUtils.assertNestedDatasetIsAccessible( catBuilder, docBaseUri );
//  }
}