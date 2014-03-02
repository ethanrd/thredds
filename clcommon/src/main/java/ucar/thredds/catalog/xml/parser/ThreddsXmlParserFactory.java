package ucar.thredds.catalog.xml.parser;

import ucar.thredds.catalog.builder.ThreddsBuilderFactory;
import ucar.thredds.catalog.xml.parser.stax.StaxThreddsXmlParser;

/**
 * _MORE_
 *
 * @author edavis
 */
public class ThreddsXmlParserFactory  {

  private ThreddsBuilderFactory threddsBuilderFactory = null;

  public static ThreddsXmlParserFactory newInstance() {
    return new ThreddsXmlParserFactory();
  }
  private ThreddsXmlParserFactory() {}

  public void setCatalogBuilderImpl( ThreddsBuilderFactory threddsBuilderFactory) {
    this.threddsBuilderFactory = threddsBuilderFactory;
  }

  public ThreddsXmlParser getParser() {
    return StaxThreddsXmlParser.newInstance(
        this.threddsBuilderFactory != null
            ? this.threddsBuilderFactory
            : new ucar.thredds.catalog.straightimpl.ThreddsBuilderFactoryImpl() );
  }
}
