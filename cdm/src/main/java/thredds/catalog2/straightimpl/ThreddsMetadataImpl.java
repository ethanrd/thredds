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
import thredds.catalog2.ThreddsMetadata;
import thredds.catalog2.builder.BuilderException;
import thredds.catalog2.builder.BuilderIssue;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.ThreddsMetadataBuilder;
import ucar.nc2.constants.CDM;
import ucar.nc2.constants.FeatureType;
import ucar.nc2.units.DateType;
import ucar.nc2.units.TimeDuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
class ThreddsMetadataImpl implements ThreddsMetadata
{
  private List<DocumentationImpl> docs;
  private List<KeyphraseImpl> keyphrases;
  private List<ProjectNameImpl> projectNames;
  private List<ContributorImpl> creators;
  private List<ContributorImpl> contributors;
  private List<ContributorImpl> publishers;

  private List<DatePointImpl> otherDates;
  private DatePointImpl createdDate;
  private DatePointImpl modifiedDate;
  private DatePointImpl issuedDate;
  private DatePointImpl validDate;
  private DatePointImpl availableDate;
  private DatePointImpl metadataCreatedDate;
  private DatePointImpl metadataModifiedDate;

  private GeospatialCoverageImpl geospatialCoverage;
  private DateRangeImpl temporalCoverage;

  private List<VariableGroupImpl> variableGroups;
  private long dataSizeInBytes;
  private DataFormatType dataFormat;
  private FeatureType dataType;
  private String collectionType;

  ThreddsMetadataImpl() {
    this.dataSizeInBytes = -1;
  }

  ThreddsMetadataImpl(  List<ThreddsMetadataBuilder.DocumentationBuilder> docs,
                        List<ThreddsMetadataBuilder.KeyphraseBuilder> keyphrases,
                        List<ThreddsMetadataBuilder.ContributorBuilder> creators,
                        List<ThreddsMetadataBuilder.ContributorBuilder> contributors,
                        List<ThreddsMetadataBuilder.ContributorBuilder> publishers,

                        List<ThreddsMetadataBuilder.DatePointBuilder> otherDates,
                        ThreddsMetadataBuilder.DatePointBuilder createdDate,
                        ThreddsMetadataBuilder.DatePointBuilder modifiedDate,
                        this.issuedDate,
                        this.validDate,
                        this.availableDate,
                        this.metadataCreatedDate,
                        this.metadataModifiedDate,
                        this.geospatialCoverage,
                        this.temporalCoverage,
                        this.variableGroups)
  {

  }

  public boolean isEmpty()
  {
    if ( this.docs != null && ! this.docs.isEmpty() )
      return false;
    if ( this.keyphrases != null && ! this.keyphrases.isEmpty() )
      return false;
    if ( this.projectNames != null && ! this.projectNames.isEmpty() )
      return false;
    if ( this.creators != null && ! this.creators.isEmpty() )
      return false;
    if ( this.contributors != null && ! this.contributors.isEmpty() )
      return false;
    if ( this.publishers != null && ! this.publishers.isEmpty() )
      return false;

    if ( this.createdDate != null || this.modifiedDate != null
         || this.issuedDate != null || this.validDate != null || this.availableDate != null
         || this.metadataCreatedDate != null || this.metadataModifiedDate != null
         || this.geospatialCoverage != null || this.temporalCoverage != null )
      return false;

    if ( this.variableGroups != null && ! this.variableGroups.isEmpty() )
      return false;

    if ( this.dataSizeInBytes != -1 || this.dataFormat != null || this.dataType != null || this.collectionType != null )
      return false;

    return true;
  }

  public List<Documentation> getDocumentation()
  {
    if ( this.docs == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Documentation>( this.docs ) );
  }

  public List<Keyphrase> getKeyphrases()
  {
    if ( this.keyphrases == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Keyphrase>( this.keyphrases ) );
  }

  public List<ProjectName> getProjectNames()
  {
    if ( this.projectNames == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ProjectName>( this.projectNames ) );
  }

  public List<Contributor> getCreator()
  {
    if ( this.creators == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Contributor>( this.creators ) );
  }

  public List<Contributor> getContributor()
  {
    if ( this.contributors == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Contributor>( this.contributors ) );
  }

  public List<Contributor> getPublisher()
  {
    if ( this.publishers == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Contributor>( this.publishers ) );
  }

  public List<DatePoint> getOtherDates()
  {
      if ( this.otherDates == null )
          return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<DatePoint>( this.otherDates ) );
  }

  public DatePoint getCreatedDate() {
    return this.createdDate;
  }

  public DatePoint getModifiedDate() {
      return this.modifiedDate;
  }

  public DatePoint getIssuedDate() {
      return this.issuedDate;
  }

  public DatePoint getValidDate() {
    return this.validDate;
  }

  public DatePoint getAvailableDate() {
    return this.availableDate;
  }

  public DatePoint getMetadataCreatedDate() {
      return this.metadataCreatedDate;
  }

  public DatePoint getMetadataModifiedDate() {
      return this.metadataModifiedDate;
  }

  public GeospatialCoverage getGeospatialCoverage()
  {
    return this.geospatialCoverage;
  }

  public DateRange getTemporalCoverage() {
      return this.temporalCoverage;
  }

  public List<VariableGroup> getVariableGroups() {
    if ( this.variableGroups == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<VariableGroup>( this.variableGroups ) );
  }

  public long getDataSizeInBytes() {
    return this.dataSizeInBytes;
  }

  public DataFormatType getDataFormat() {
    return this.dataFormat;
  }

  public FeatureType getDataType() {
    return this.dataType;
  }

  public String getCollectionType() { // ?????
    return this.collectionType;
  }

  static class DocumentationImpl implements Documentation
  {
    private final boolean isContainedContent;

    private final String docType;
    private final String title;
    private final URI externalRefereneUri;
    private final String content;

    DocumentationImpl( String docType, String title, String externalReferenceUriAsString)
    {
      this.isContainedContent = false;
      this.docType = docType == null ? "" : docType;
      this.title = title == null ? "" : title;
      try {
        this.externalRefereneUri = new URI( externalReferenceUriAsString != null ? externalReferenceUriAsString : "");
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException( String.format( "Failed to build Documentation because externalReferenceUri [%s] is not a valid URI", externalReferenceUriAsString));
      }

      this.content = null;
    }

    DocumentationImpl( String docType, String content )
    {
      this.isContainedContent = true;
      this.docType = docType == null ? "" : docType;
      this.content = content == null ? "" : content;
      this.title = null;
      this.externalRefereneUri = null;
    }

    public boolean isContainedContent() {
      return this.isContainedContent;
    }

    public String getDocType() {
      return this.docType;
    }

    public String getContent() {
      if ( ! this.isContainedContent )
        throw new IllegalStateException( "No contained content, use external reference to access documentation content." );
      return this.content;
    }

    public String getTitle() { if (this.isContainedContent )
        throw new IllegalStateException( "Documentation with contained content has no title." );
      return this.title;
    }

    @Override
    public URI getExternalReferenceUri() {
      if ( this.isContainedContent )
        throw new IllegalStateException( "Documentation with contained content has no external reference.");
      return this.externalRefereneUri;
    }
  }

  static class KeyphraseImpl implements Keyphrase
  {
    private final String authority;
    private final String phrase;

    KeyphraseImpl( String authority, String phrase)
    {
        this.authority = authority == null ? "" : authority;
        this.phrase = phrase == null ? "" : phrase;
    }

    public String getAuthority() {
      return this.authority;
    }

    public String getPhrase() {
      return this.phrase;
    }

  }

  static class ProjectNameImpl implements ProjectName
  {
    private String namingAuthority;
    private String projectName;

    ProjectNameImpl( String namingAuthority, String projectName ) {
      this.namingAuthority = namingAuthority == null ? "" : namingAuthority;
      this.projectName = projectName == null ? "" : projectName;
    }

    public String getNamingAuthority() {
      return this.namingAuthority;
    }

    public String getName() {
      return this.projectName;
    }
  }

  static class DatePointImpl implements DatePoint {
    private final String date;
    private final String format;
    private final String type;

    DatePointImpl(String date, String format, String type) {
      this.date = date == null ? "" : date;
      this.format = format == null ? "" : format;
      this.type = type == null ? "" : type;
    }

    public String getDate() {
      return this.date;
    }

    public String getDateFormat() {
      return this.format;
    }

    public boolean isTyped() {
      return this.type != null || this.type.length() == 0;
    }

    public String getType() {
      return this.type;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof DatePointImpl)) return false;
      return obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
      int result = 17;
      if (this.date != null)
        result = 37 * result + this.date.hashCode();
      if (this.format != null)
        result = 37 * result + this.format.hashCode();
      if (this.type != null)
        result = 37 * result + this.type.hashCode();
      return result;
    }
  }

  static class DateRangeImpl implements DateRange {
    private final String startDate;
    private final String startDateFormat;
    private final String endDate;
    private final String endDateFormat;
    private final String duration;
    private final String resolution;

    public DateRangeImpl( String startDate, String startDateFormat,
                          String endDate, String endDateFormat,
                          String duration, String resolution )
    {
      boolean useStart = startDate != null && !startDate.isEmpty();
      boolean useEnd = endDate != null && !endDate.isEmpty();
      boolean useDuration = duration != null;
      boolean useResolution = resolution != null;

      boolean invalid = true;
      if (useStart && useEnd) {
        invalid = false;
        useDuration = false; // ToDo Test what happens if all three are given and not consistent.
      } else if (useStart && useDuration) {
        invalid = false;
      } else if (useEnd && useDuration) {
        invalid = false;
      }
      if ( invalid)
        throw new IllegalStateException( "Failed to build DateRange, must have two of start date, end date, and duration.");

      DateType startDateType = null;
      if ( useStart ) {
        try {
          startDateType = new DateType( startDate, startDateFormat, null);
        } catch (ParseException e) {
          throw new IllegalArgumentException( String.format( "Failed to build DateRange, could not parse start date [%s] or start date format [%s]: %s", startDate, startDateFormat, e.getMessage()));
        }
      }

      DateType endDateType = null;
      if ( useEnd ) {
        try {
          endDateType = new DateType( endDate, endDateFormat, null);
        } catch (ParseException e) {
          throw new IllegalArgumentException( String.format( "Failed to build DateRange, could not parse end date [%s] or end date format [%s]: %s", endDate, endDateFormat, e.getMessage()));
        }
      }

      TimeDuration durationTimeDuration = null;
      if ( useDuration) {
        try {
          durationTimeDuration = new TimeDuration( duration );
        } catch (ParseException e) {
          throw new IllegalArgumentException( String.format( "Failed to build DateRange, could not parse duration [%s]: %s", duration, e.getMessage()));
        }
      }

      TimeDuration resolutionTimeDuration = null;
      if ( useResolution) {
        try {
          resolutionTimeDuration = new TimeDuration( resolution );
        } catch (ParseException e) {
          throw new IllegalArgumentException( String.format( "Failed to build DateRange, could not parse resolution [%s]: %s", resolution, e.getMessage()));
        }
      }

      try {
        new ucar.nc2.units.DateRange( startDateType, endDateType, durationTimeDuration, resolutionTimeDuration );
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException( "Failed to build DateRange, must have two of start date, end date, and duration.");
      }
      this.startDate = startDate;
      this.startDateFormat = startDateFormat;
      this.endDate = endDate;
      this.endDateFormat = endDateFormat;
      this.duration = duration;
      this.resolution = resolution;
    }

    public String getStartDateFormat() {
      return this.startDateFormat;
    }

    public String getStartDate() {
      return this.startDate;
    }

    public String getEndDateFormat() {
      return this.endDateFormat;
    }

    public String getEndDate() {
      return this.endDate;
    }

    public String getDuration() {
      return this.duration;
    }

    public String getResolution() {
      return this.resolution;
    }

    public String toString() {
      return ( String.format( "DateRange [%s <-- %s --> %s]", this.startDate, this.duration, this.endDate ));
    }

    @Override
    public boolean equals( Object obj )
    {
      if ( this == obj ) return true;
      if ( !( obj instanceof DateRangeImpl ) ) return false;
      return obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode()
    {
      int result = 17;
      if ( this.startDate != null )
        result = 37 * result + this.startDate.hashCode();
      if ( this.startDateFormat != null )
        result = 37 * result + this.startDateFormat.hashCode();
      if ( this.endDate != null )
        result = 37 * result + this.endDate.hashCode();
      if ( this.endDateFormat != null )
        result = 37 * result + this.endDateFormat.hashCode();
      if ( this.duration != null )
        result = 37 * result + this.duration.hashCode();
      return result;
    }

  }

  static class ContributorImpl implements Contributor
  {
    private final String authority;
    private final String name;
    private final String role;
    private final String email;
    private final URI webPageUrl;

    ContributorImpl( String authority, String name, String role, String email, String webPageUrlAsString) {
      this.authority = authority == null ? "" : authority;
      this.name = name == null ? "" : name;
      this.role = role == null ? "" : role;
      this.email = email == null ? "" : email;
      try {
        this.webPageUrl = new URI( webPageUrlAsString == null ? "" : webPageUrlAsString);
      } catch (URISyntaxException e) {
        throw new IllegalArgumentException( String.format( "Failed to build Contributor because webPageUrlAsString [%s] is not a valid URI", webPageUrlAsString));
      }
    }

    public String getNamingAuthority() {
      return this.authority;
    }

    public String getName() {
      return this.name;
    }

    public String getRole() {
      return this.role;
    }

    public String getEmail() {
      return this.email;
    }

    public URI getWebPageUrl() {
      return this.webPageUrl;
    }
  }

  static class VariableGroupImpl implements VariableGroup
  {
    private final String vocabularyAuthorityId;
    private final URI vocabularyAuthorityUrl;

    private final List<VariableImpl> variables;

    private final URI variableMapUrl;

    VariableGroupImpl( String vocabularyAuthorityId, String vocabularyAuthorityUrlAsString,
                       List<VariableBuilderImpl> variableBuilderList, String variableMapUrlAsString) {

    }

    public String getVocabularyAuthorityId() {
      return this.vocabularyAuthorityId;
    }

    public URI getVocabularyAuthorityUrl() {
      return this.vocabularyAuthorityUrl;
    }

    public List<Variable> getVariables() {
      if ( this.variables == null )
        return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<Variable>( variables) );
    }

    public URI getVariableMapUrl() {
      return this.variableMapUrl;
    }

    public boolean isEmpty() {
      return variableMapUrl == null && ( this.variables == null || this.variables.isEmpty());
    }
  }

  static class VariableImpl implements Variable
  {
    private String name;
    private String description;
    private String units;
    private String vocabularyId;
    private String vocabularyName;

    private VariableGroupBuilder parent;

    VariableImpl( String name, String description, String units,
                  String vocabId, String vocabName, VariableGroupBuilder parent )
    {
      this.name = name;
      this.description = description;
      this.units = units;
      this.vocabularyId = vocabId;
      this.vocabularyName = vocabName;
      this.parent = parent;
    }

    public String getName() {
      return this.name;
    }

    public String getDescription() {
      return this.description;
    }

    public String getUnits() {
      return this.units;
    }

    public String getVocabularyId() {
      return this.vocabularyId;
    }

    public String getVocabularyName() {
      return this.vocabularyName;
    }

    public String getVocabularyAuthorityId() {
      return this.parent.getVocabularyAuthorityId();
    }

    public URI getVocabularyAuthorityUrl() {
      return this.parent.getVocabularyAuthorityUrl();
    }
  }

  static class GeospatialCoverageImpl implements GeospatialCoverage
  {
    private static String defaultCrsUri = "urn:x-mycrs:2D-WGS84-ellipsoid";
    private static GeospatialRange defaultRangeX = new GeospatialRangeImpl( 0.0, 0.0, Double.NaN, CDM.LON_UNITS );
    private static GeospatialRange defaultRangeY = new GeospatialRangeImpl( 0.0, 0.0, Double.NaN, CDM.LAT_UNITS );
    private static GeospatialRange defaultRangeZ = new GeospatialRangeImpl( 0.0, 0.0, Double.NaN, "km" );

    private URI crsUri;
    //private boolean is3D;
    private boolean isZPositiveUp;

    private boolean isGlobal;
    private final GeospatialRange x, y, z;

    GeospatialCoverageImpl() {
      this( defaultCrsUri, true, false, defaultRangeX, defaultRangeY, defaultRangeZ);
    }

    GeospatialCoverageImpl( String crsUri, boolean isZPositiveUp, boolean isGlobal,
                            ThreddsMetadataBuilder.GeospatialRangeBuilder x,
                            ThreddsMetadataBuilder.GeospatialRangeBuilder y,
                            ThreddsMetadataBuilder.GeospatialRangeBuilder z )
    {

      try {
        this.crsUri = new URI( crsUri == null ? "" : crsUri );
      }
      catch ( URISyntaxException e ) {
        throw new IllegalStateException( "Bad URI syntax for default CRS URI ["+defaultCrsUriString+"]: " + e.getMessage());
      }
      if (x.isBuildable())
    }

    public URI getCRS() {
      return this.crsUri;
    }

    public boolean isGlobal() {
      return this.isGlobal;
    }

    public boolean isZPositiveUp()   // Is this needed since have CRS?
    {
      return this.isZPositiveUp;
    }

    public List<GeospatialRange> getExtent()
    {
      if ( this.extent == null )
        return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<GeospatialRange>( this.extent ) );
    }
  }

  static class GeospatialRangeImpl implements GeospatialRange
  {
    private double start;
    private double size;
    private double resolution;
    private String units;

    GeospatialRangeImpl( double start, double size, double resolution, String units ) {
      this.start = start;
      this.size = size;
      this.resolution = resolution;
      this.units = units;
    }

    public double getStart() {
      return this.start;
    }

    public double getSize() {
      return this.size;
    }

    public double getResolution() {
      return this.resolution;
    }

    public String getUnits() {
      return this.units;
    }
  }
}
