package thredds.catalog2.xml.parser;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URI;

import thredds.catalog.xml.testutil.CatalogXmlAsStringUtil;
import thredds.catalog2.builder.CatalogBuilder;
import thredds.catalog2.builder.BuilderIssues;

/**
 * _more_
 *
 * @author edavis
 * @since 4.1
 */
public class CatalogParserIssuesTest
{
  @Test
  public void checkSimpleCatalogHasNoIssues()
          throws URISyntaxException,
                 ThreddsXmlParserException
  {
    String docBaseUriString = "http://test/thredds/catalog2/xml/parser/CatalogParserIssuesTest/checkSimpleCatalogHasNoIssues.xml";
    URI docBaseUri = new URI( docBaseUriString );
    String catName = "fred";
    String catVersion = "1.0.2";
    String catExpires = "2010-01-27T22:43";
    String catalogAsString = CatalogXmlAsStringUtil.wrapThreddsXmlInCatalog(catName, catVersion, catExpires, null);

    CatalogBuilder catBuilder = CatalogXmlUtils.parseCatalogIntoBuilder(cp.parseIntoBuilder(new StringReader(catalogAsString), docBaseUri));

    BuilderIssues issues = catBuilder.checkForIssues();
    assertNotNull( issues );
    assertTrue( issues.isEmpty());
    CatalogXmlUtils.assertCatalogAsExpected( catBuilder, docBaseUri, catName, catVersion, catExpires );
  }

  @Test
  public void checkSimpleCatalogWithBadExpiresDateHasIssues()
          throws URISyntaxException,
                 ThreddsXmlParserException
  {
    String docBaseUriString = "http://test/thredds/catalog2/xml/parser/CatalogParserIssuesTest/checkSimpleCatalogWithBadExpiresDateHasIssues.xml";
    URI docBaseUri = new URI( docBaseUriString );
    String catName = "fred";
    String catVersion = "1.0.2";
    String catExpires = "2010-01-27badT22:43";
    String catalogAsString = CatalogXmlAsStringUtil.wrapThreddsXmlInCatalog( catName, catVersion, catExpires, null);

    CatalogBuilder catBuilder = CatalogXmlUtils.parseCatalogIntoBuilder(cp.parseIntoBuilder(new StringReader(catalogAsString), docBaseUri));

    BuilderIssues issues = catBuilder.checkForIssues();
    assertNotNull( issues );
    assertFalse( issues.toString(), issues.isEmpty());
  }

}
