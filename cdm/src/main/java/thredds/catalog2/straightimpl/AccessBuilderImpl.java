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
import thredds.catalog2.builder.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
class AccessBuilderImpl implements AccessBuilder
{
  //private final DatasetImpl parentDs;
  private String serviceBuilderName;
  private ServiceBuilder service;
  private String urlPath;
  private DataFormatType dataFormat = DataFormatType.NONE;
  private long dataSize = -1;

  private BuilderIssues builderIssues;
  private Buildable isBuildable;

  AccessBuilderImpl( String serviceBuilderName, String urlPath ) {
    this.urlPath = urlPath != null ? urlPath : "";
    this.serviceBuilderName = serviceBuilderName != null ? serviceBuilderName : "";

    this.isBuildable = Buildable.DONT_KNOW;
    this.builderIssues = new BuilderIssues();
  }

  @Override
  public void setUrlPath( String urlPath) {
    this.isBuildable = Buildable.DONT_KNOW;
    this.urlPath = urlPath != null ? urlPath : "";
  }

  @Override
  public void setServiceBuilderName( String serviceBuilderName ) {
    this.serviceBuilderName = serviceBuilderName != null ? serviceBuilderName : "";

    this.isBuildable = Buildable.DONT_KNOW;
  }

  public void setDataFormat( DataFormatType dataFormat ) {
    this.dataFormat = dataFormat != null ? dataFormat : DataFormatType.NONE;
  }

  public void setDataSize( long dataSize ) {
    if ( dataSize < -1 )
      this.dataSize = -1;
    else
      this.dataSize = dataSize;
  }

  public String getServiceBuilderName() {
    return serviceBuilderName;
  }

  public String getUrlPath() {
    return urlPath;
  }
  
  public DataFormatType getDataFormat() {
    return dataFormat;
  }

  public long getDataSize() {
    return dataSize;
  }

  public Buildable isBuildable() {
    return this.isBuildable;
  }

  public BuilderIssues checkForIssues() {
    this.builderIssues = new BuilderIssues();

    try {
      new URI( this.urlPath);
    } catch (URISyntaxException e) {
      this.builderIssues.addIssue( new BuilderIssue( BuilderIssue.Severity.ERROR, String.format( "UrlPath [%s] must be valid URI.", this.urlPath), this));
    }
    if ( this.builderIssues.isValid())
      this.isBuildable = Buildable.YES;
    else
      this.isBuildable = Buildable.NO;

    return this.builderIssues;
  }

  public Access build() throws IllegalStateException {
    if ( this.isBuildable != Buildable.YES )
      throw new IllegalStateException( "AccessBuilder not buildable.");

    return new AccessImpl( this.service, this.urlPath, this.dataFormat, this.dataSize, this.builderIssues);
  }
}
