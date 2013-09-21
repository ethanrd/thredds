/*
 * Copyright 1998-2009 University Corporation for Atmospheric Research/Unidata
 *
 * Portions of this software were developed by the Unidata Program at the
 * University Corporation for Atmospheric Research.
 *
 * Access and use of this software shall impose the following obligations
 * and understandings on the user. The user is granted the right, without
 * any fee or cost, to use, copy, modify, alter, enhance and distribute
 * this software, and any derivative works thereof, and its supporting
 * documentation for any purpose whatsoever, provided that this entire
 * notice appears in all copies of the software, derivative works and
 * supporting documentation.  Further, UCAR requests that the user credit
 * UCAR/Unidata in any publications that result from the use of this
 * software or in any product that includes this software. The names UCAR
 * and/or Unidata, however, may not be used in any advertising or publicity
 * to endorse or promote any products or commercial entity unless specific
 * written permission is obtained from UCAR/Unidata. The user also
 * understands that UCAR/Unidata is not obligated to provide the user with
 * any support, consulting, training or assistance of any kind with regard
 * to the use, operation and performance of this software nor to provide
 * the user with any updates, revisions, new versions or "bug fixes."
 *
 * THIS SOFTWARE IS PROVIDED BY UCAR/UNIDATA "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL UCAR/UNIDATA BE LIABLE FOR ANY SPECIAL,
 * INDIRECT OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING
 * FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT,
 * NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION
 * WITH THE ACCESS, USE OR PERFORMANCE OF THIS SOFTWARE.
 */
package thredds.catalog2.straightimpl;

import thredds.catalog.DataFormatType;
import thredds.catalog2.Access;
import thredds.catalog2.Service;
import thredds.catalog2.ThreddsCatalogIssueContainer;
import thredds.catalog2.builder.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
final class AccessImpl implements Access
{
  //private final DatasetImpl parentDs;
  private final Service service;
  private final URI urlPath;
  private final DataFormatType dataFormat;
  private final long dataSize;

  private final ThreddsCatalogIssueContainer threddsCatalogIssueContainer;

  AccessImpl( ServiceBuilder serviceBuilder, String urlPathAsString,
              DataFormatType dataFormat, long dataSize,
              BuilderIssues builderIssues ) {
    if ( serviceBuilder == null  )
      throw new IllegalArgumentException( "Failed to build Access because referenced ServiceBuilder is null.");
    if ( serviceBuilder.isBuildable() != ThreddsBuilder.Buildable.YES )
      throw new IllegalArgumentException( "Failed to build Access because referenced ServiceBuilder is not buildable.");

    // TODO Service has already been built in catalog, should use serviceName instead and dereference with ?
    this.service = null;

    try {
      this.urlPath = new URI( urlPathAsString != null ? urlPathAsString : "");
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException( String.format( "Failed to build Access because urlPath [%s] is not a valid URI", urlPathAsString));
    }

    if ( dataFormat == null )
      this.dataFormat = DataFormatType.NONE;
    else
      this.dataFormat = dataFormat;

    this.dataSize = dataSize >= -1 ? dataSize : -1;

    this.threddsCatalogIssueContainer = new ThreddsCatalogIssuesImpl( builderIssues);


  }
//  AccessImpl( DatasetImpl parentDataset ) {
//    this.parentDs = parentDataset;
//  }

  public Service getService() {
    return service;
  }

  public URI getUrlPath() {
    return urlPath;
  }
  
  public DataFormatType getDataFormat() {
    return dataFormat;
  }

  public long getDataSize() {
    return dataSize;
  }

  @Override
  public ThreddsCatalogIssueContainer getIssues() {
    return this.threddsCatalogIssueContainer;
  }
}
