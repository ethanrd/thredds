package ucar.thredds.catalog.xml.writer;

import ucar.thredds.catalog.xml.writer.stax.StaxThreddsXmlWriter;

/**
 * _more_
 *
 * @author edavis
 */
public class ThreddsXmlWriterFactory
{
  private org.slf4j.Logger logger =
      org.slf4j.LoggerFactory.getLogger( ThreddsXmlWriterFactory.class );

  public static ThreddsXmlWriterFactory newInstance() {
    return new ThreddsXmlWriterFactory();
  }

  private ThreddsXmlWriterFactory() {}

  public ThreddsXmlWriter createThreddsXmlWriter() {
    return new StaxThreddsXmlWriter();
  }

}
