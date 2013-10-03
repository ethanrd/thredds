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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
class ThreddsMetadataBuilderImpl implements ThreddsMetadataBuilder
{
  private List<DocumentationBuilderImpl> docs;
  private List<KeyphraseBuilderImpl> keyphrases;
  private List<ProjectNameBuilderImpl> projectNames;
  private List<ContributorBuilderImpl> creators;
  private List<ContributorBuilderImpl> contributors;
  private List<ContributorBuilderImpl> publishers;

  private List<DatePointBuilderImpl> otherDates;
  private DatePointBuilderImpl createdDate;
  private DatePointBuilderImpl modifiedDate;
  private DatePointBuilderImpl issuedDate;
  private DatePointBuilderImpl validDate;
  private DatePointBuilderImpl availableDate;
  private DatePointBuilderImpl metadataCreatedDate;
  private DatePointBuilderImpl metadataModifiedDate;

  private GeospatialCoverageBuilderImpl geospatialCoverage;
  private DateRangeBuilderImpl temporalCoverage;

  private List<VariableGroupBuilderImpl> variableGroups;
  private long dataSizeInBytes;
  private DataFormatType dataFormat;
  private FeatureType dataType;
  private String collectionType;

  private BuilderIssues builderIssues;
  private Buildable isBuildable;


  ThreddsMetadataBuilderImpl()
  {
    this.dataSizeInBytes = -1;
    this.isBuildable = Buildable.DONT_KNOW;
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

  public DocumentationBuilder addDocumentation( String docType, String title, String externalReference )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( this.docs == null )
      this.docs = new ArrayList<DocumentationBuilderImpl>();
    DocumentationBuilderImpl doc = new DocumentationBuilderImpl( docType, title, externalReference );
    this.docs.add( doc );
    return doc;
  }

  public DocumentationBuilder addDocumentation( String docType, String content ) {
    if ( content == null )
      throw new IllegalArgumentException( "Content may not be null." );
    this.isBuildable = Buildable.DONT_KNOW;

    if ( this.docs == null )
      this.docs = new ArrayList<DocumentationBuilderImpl>();
    DocumentationBuilderImpl doc = new DocumentationBuilderImpl( docType, content );
    this.docs.add( doc );
    return doc;
  }

  public boolean removeDocumentation( DocumentationBuilder docBuilder )
  {
    if ( docBuilder == null )
      return false;
    if ( this.docs == null )
      return false;
    this.isBuildable = Buildable.DONT_KNOW;
    return this.docs.remove( (DocumentationBuilderImpl) docBuilder );
  }

  public List<DocumentationBuilder> getDocumentationBuilders()
  {
    if ( this.docs == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<DocumentationBuilder>( this.docs) );
  }

  public KeyphraseBuilder addKeyphrase( String authority, String phrase )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    if ( phrase == null )
      throw new IllegalArgumentException( "Phrase may not be null.");
    if ( this.keyphrases == null )
      this.keyphrases = new ArrayList<KeyphraseBuilderImpl>();
    KeyphraseBuilderImpl keyphrase = new KeyphraseBuilderImpl( authority, phrase);
    this.keyphrases.add( keyphrase );
    return keyphrase;
  }

  public boolean removeKeyphrase( KeyphraseBuilder keyphraseBuilder )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    if ( keyphraseBuilder == null )
      return false;
    if ( this.keyphrases == null )
      return false;
    return this.keyphrases.remove( (KeyphraseBuilderImpl) keyphraseBuilder );
  }

  public List<KeyphraseBuilder> getKeyphraseBuilders()
  {
    if ( this.keyphrases == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<KeyphraseBuilder>( this.keyphrases ) );
  }

  public ProjectNameBuilder addProjectName( String namingAuthority, String name )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    if ( name == null )
      throw new IllegalArgumentException( "Project name may not be null.");
    if ( this.projectNames == null )
      this.projectNames = new ArrayList<ProjectNameBuilderImpl>();
    ProjectNameBuilderImpl projectName = new ProjectNameBuilderImpl( namingAuthority, name);
    this.projectNames.add( projectName );
    return projectName;
  }

  public boolean removeProjectName( ProjectNameBuilder projectNameBuilder )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    if ( projectNameBuilder == null )
      return false;
    if ( this.projectNames == null )
      return false;
    return this.projectNames.remove( (ProjectNameBuilderImpl) projectNameBuilder );
  }

  public List<ProjectNameBuilder> getProjectNameBuilders()
  {
    if ( this.projectNames == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ProjectNameBuilder>( this.projectNames ) );
  }

  public ContributorBuilder addCreator()
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( this.creators == null )
      this.creators = new ArrayList<ContributorBuilderImpl>();
    ContributorBuilderImpl contributor = new ContributorBuilderImpl();
    this.creators.add( contributor );
    return contributor;
  }

  public boolean removeCreator( ContributorBuilder creatorBuilder )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( creatorBuilder == null )
      return false;
    if ( this.creators == null )
      return false;
    return this.creators.remove( (ContributorBuilderImpl) creatorBuilder );
  }

  public List<ContributorBuilder> getCreatorBuilder()
  {
    if ( this.creators == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ContributorBuilder>( this.creators ) );
  }

  public ContributorBuilder addContributor()
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( this.contributors == null )
      this.contributors = new ArrayList<ContributorBuilderImpl>();
    ContributorBuilderImpl contributor = new ContributorBuilderImpl();
    this.contributors.add( contributor );
    return contributor;
  }

  public boolean removeContributor( ContributorBuilder contributorBuilder )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    if ( contributorBuilder == null )
      return false;
    if ( this.contributors == null )
      return false;
    return this.contributors.remove( (ContributorBuilderImpl) contributorBuilder );
  }

  public List<ContributorBuilder> getContributorBuilder()
  {
    if ( this.contributors == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ContributorBuilder>( this.contributors ) );
  }

  public ContributorBuilder addPublisher()
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( this.publishers == null )
      this.publishers = new ArrayList<ContributorBuilderImpl>();
    ContributorBuilderImpl contributor = new ContributorBuilderImpl();
    this.publishers.add( contributor );
    return contributor;
  }

  public boolean removePublisher( ContributorBuilder publisherBuilder )
  {
    if ( publisherBuilder == null )
      return false;
    if ( this.publishers == null )
      return false;
    this.isBuildable = Buildable.DONT_KNOW;
    return this.publishers.remove( (ContributorBuilderImpl) publisherBuilder );
  }

  public List<ContributorBuilder> getPublisherBuilder()
  {
    if ( this.publishers == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ContributorBuilder>( this.publishers ) );
  }

    public DatePointBuilder addOtherDatePointBuilder( String date, String format, String type )
    {
      this.isBuildable = Buildable.DONT_KNOW;

        ThreddsMetadata.DatePointType datePointType = ThreddsMetadata.DatePointType.getTypeForLabel(type);
        if ( datePointType != ThreddsMetadata.DatePointType.Other
             && datePointType != ThreddsMetadata.DatePointType.Untyped )
            throw new IllegalArgumentException( "Must use explicit setter method for given type [" + type + "]." );
        if ( this.otherDates == null )
            this.otherDates = new ArrayList<DatePointBuilderImpl>();
        DatePointBuilderImpl dp = new DatePointBuilderImpl( date, format, type);
        this.otherDates.add( dp );
        return dp;
    }

    public boolean removeOtherDatePointBuilder( DatePointBuilder builder )
    {
        if ( builder == null )
            return false;
        if ( this.otherDates == null )
            return false;
      this.isBuildable = Buildable.DONT_KNOW;
        return this.otherDates.remove( (DatePointBuilderImpl) builder );
    }

    public List<DatePointBuilder> getOtherDatePointBuilders()
    {
        if ( this.otherDates == null )
            return Collections.emptyList();
        return Collections.unmodifiableList( new ArrayList<DatePointBuilder>( this.otherDates) );
    }

    public DatePointBuilder setCreatedDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    this.createdDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.Created.toString());
    return this.createdDate;
  }

  public DatePointBuilder getCreatedDatePointBuilder() {
      return this.createdDate;
  }

  public DatePointBuilder setModifiedDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.modifiedDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.Modified.toString() );
    return this.modifiedDate;
  }

  public DatePointBuilder getModifiedDatePointBuilder() {
      return this.modifiedDate;
  }

  public DatePointBuilder setIssuedDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.issuedDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.Issued.toString() );
    return this.issuedDate;
  }

  public DatePointBuilder getIssuedDatePointBuilder() {
      return this.issuedDate;
  }

  public DatePointBuilder setValidDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.validDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.Valid.toString() );
    return this.validDate;
  }

  public DatePointBuilder getValidDatePointBuilder() {
      return this.validDate;
  }

  public DatePointBuilder setAvailableDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.availableDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.Available.toString() );
    return this.availableDate;
  }

  public DatePointBuilder getAvailableDatePointBuilder() {
      return this.availableDate;
  }

  public DatePointBuilder setMetadataCreatedDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.metadataCreatedDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.MetadataCreated.toString() );
    return this.metadataCreatedDate;
  }

  public DatePointBuilder getMetadataCreatedDatePointBuilder() {
      return this.metadataCreatedDate;
  }

  public DatePointBuilder setMetadataModifiedDatePointBuilder( String date, String format )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.metadataModifiedDate = new DatePointBuilderImpl( date, format, ThreddsMetadata.DatePointType.MetadataModified.toString() );
    return this.metadataModifiedDate;
  }

  public DatePointBuilder getMetadataModifiedDatePointBuilder() {
      return this.metadataModifiedDate;
  }

  public GeospatialCoverageBuilder setGeospatialCoverageBuilder( String crsUri)
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.geospatialCoverage = new GeospatialCoverageBuilderImpl();
    this.geospatialCoverage.setCRS( crsUri );
    return this.geospatialCoverage;
  }

  public void removeGeospatialCoverageBuilder()
  {
    if ( this.geospatialCoverage != null)
      this.isBuildable = Buildable.DONT_KNOW;
    this.geospatialCoverage = null;
  }

  public GeospatialCoverageBuilder getGeospatialCoverageBuilder() {
    return this.geospatialCoverage;
  }

  public DateRangeBuilder setTemporalCoverageBuilder( String startDate, String startDateFormat,
                                                      String endDate, String endDateFormat,
                                                      String duration, String resolution )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.temporalCoverage = new DateRangeBuilderImpl( startDate, startDateFormat, endDate, endDateFormat, duration, resolution );
    return this.temporalCoverage;
  }

  public DateRangeBuilder getTemporalCoverageBuilder() {
      return this.temporalCoverage;
  }

  public VariableGroupBuilder addVariableGroupBuilder()
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( this.variableGroups == null )
      this.variableGroups = new ArrayList<VariableGroupBuilderImpl>();
    VariableGroupBuilderImpl varGroup = new VariableGroupBuilderImpl();
    this.variableGroups.add( varGroup);
    return varGroup;
  }

  public boolean removeVariableGroupBuilder( VariableGroupBuilder variableGroupBuilder )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    if ( variableGroupBuilder == null )
      return false;
    if ( this.variableGroups == null )
      return false;
    return this.variableGroups.remove( (VariableGroupBuilderImpl) variableGroupBuilder );
  }

  public List<VariableGroupBuilder> getVariableGroupBuilders()
  {
    if ( this.variableGroups == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<VariableGroupBuilder>( this.variableGroups ) );
  }

  public void setDataSizeInBytes( long dataSizeInBytes )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.dataSizeInBytes = dataSizeInBytes;
  }

  public long getDataSizeInBytes()
  {
    return this.dataSizeInBytes;
  }

  public void setDataFormat( DataFormatType dataFormat )
  {
    this.isBuildable = Buildable.DONT_KNOW;

    this.dataFormat = dataFormat;
  }
  public void setDataFormat( String dataFormat )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    this.setDataFormat( DataFormatType.getType( dataFormat));
  }

  public DataFormatType getDataFormat()
  {
    return this.dataFormat;
  }

  public void setDataType( FeatureType dataType )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    this.dataType = dataType;
  }
    public void setDataType( String dataType)
    {
      this.isBuildable = Buildable.DONT_KNOW;
      this.setDataType( FeatureType.getType( dataType ));
    }

  public FeatureType getDataType() {
    return this.dataType;
  }

  public void setCollectionType( String collectionType )
  {
    this.isBuildable = Buildable.DONT_KNOW;
    this.collectionType = collectionType;
  }

  public String getCollectionType() { // ?????
    return this.collectionType;
  }

  public Buildable isBuildable() {
    return this.isBuildable;
  }

  public BuilderIssues checkForIssues()
  {
    builderIssues = new BuilderIssues();

    // Check subordinates.
    if ( this.docs != null )
      for ( DocumentationBuilderImpl doc : this.docs )
        builderIssues.addAllIssues( doc.checkForIssues());
    if ( this.keyphrases != null )
      for( KeyphraseBuilderImpl keyphrase : this.keyphrases )
        builderIssues.addAllIssues( keyphrase.checkForIssues());
    if ( this.creators != null )
      for( ContributorBuilderImpl creator : this.creators )
        builderIssues.addAllIssues( creator.checkForIssues());
    if ( this.contributors != null )
      for( ContributorBuilderImpl contributor : this.contributors )
        builderIssues.addAllIssues( contributor.checkForIssues());
    if ( this.publishers != null )
      for( ContributorBuilderImpl publisher : this.publishers )
        builderIssues.addAllIssues( publisher.checkForIssues());

    if ( this.otherDates != null )
      for( DatePointBuilderImpl date : this.otherDates )
        builderIssues.addAllIssues( date.checkForIssues());
    if ( this.createdDate != null )
      builderIssues.addAllIssues( this.createdDate.checkForIssues() );
    if ( this.modifiedDate != null )
      builderIssues.addAllIssues( this.modifiedDate.checkForIssues() );
    if ( this.issuedDate != null )
      builderIssues.addAllIssues( this.issuedDate.checkForIssues() );
    if ( this.validDate != null )
      builderIssues.addAllIssues( this.validDate.checkForIssues() );
    if ( this.availableDate != null )
      builderIssues.addAllIssues( this.availableDate.checkForIssues() );
    if ( this.metadataCreatedDate != null )
      builderIssues.addAllIssues( this.metadataCreatedDate.checkForIssues() );
    if ( this.metadataModifiedDate != null )
      builderIssues.addAllIssues( this.metadataModifiedDate.checkForIssues() );

    if ( this.geospatialCoverage != null )
      builderIssues.addAllIssues( this.geospatialCoverage.checkForIssues() );
    if ( this.temporalCoverage != null )
      builderIssues.addAllIssues( this.temporalCoverage.checkForIssues() );

    if ( this.variableGroups != null )
      for ( VariableGroupBuilderImpl variableGroup : this.variableGroups )
        builderIssues.addAllIssues( variableGroup.checkForIssues() );


    if ( builderIssues.isValid())
      this.isBuildable = Buildable.YES;
    else
      this.isBuildable = Buildable.NO;

    return builderIssues;
  }

  public ThreddsMetadata build() throws IllegalStateException
  {
    if ( this.isBuildable != Buildable.YES )
      throw new IllegalStateException( "CatalogBuilder not buildable.");

    return new ThreddsMetadataImpl( this.docs, this.keyphrases, this.creators, this.contributors,
        this.publishers, this.otherDates, this.createdDate, this.modifiedDate, this.issuedDate,
        this.validDate, this.availableDate, this.metadataCreatedDate, this.metadataModifiedDate,
        this.geospatialCoverage, this.temporalCoverage, this.variableGroups );
  }

  static class DocumentationBuilderImpl implements DocumentationBuilder
  {
    private final boolean isContainedContent;

    private final String docType;
    private final String title;
    private final String externalReferenceUriAsString;
    private final String content;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    DocumentationBuilderImpl(String docType, String title, String externalReferenceUriAsString)
    {
      //if ( title == null ) throw new IllegalArgumentException( "Title may not be null.");
      //if ( externalReferenceUriAsString == null ) throw new IllegalArgumentException( "External reference may not be null.");
      this.isContainedContent = false;
      this.docType = docType;
      this.title = title;
      this.externalReferenceUriAsString = externalReferenceUriAsString;
      this.content = null;
    }

    DocumentationBuilderImpl(String docType, String content)
    {
      if ( content == null ) throw new IllegalArgumentException( "Content may not be null." );
      this.isContainedContent = true;
      this.docType = docType;
      this.title = null;
      this.externalReferenceUriAsString = null;
      this.content = content;
    }

    public boolean isContainedContent() {
      return this.isContainedContent;
    }

    public String getDocType() {
      return this.docType;
    }

    public String getContent() {
      if ( ! this.isContainedContent )
        throw new IllegalStateException( "No contained content, use externally reference to access documentation content." );
      return this.content;
    }

    public String getTitle() {
      if ( this.isContainedContent )
        throw new IllegalStateException( "Documentation with contained content has no title." );
      return this.title;
    }

    public String getExternalReferenceUriAsString() {
      if ( this.isContainedContent )
        throw new IllegalStateException( "Documentation with contained content has no external reference.");
      return this.externalReferenceUriAsString;
    }

    public Buildable isBuildable()
    {
      return this.isBuildable;
    }

    public BuilderIssues checkForIssues()
    {
      return new BuilderIssues();
    }

    public ThreddsMetadata.Documentation build() throws IllegalStateException
    {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.DocumentationImpl();
    }
  }

  static class KeyphraseBuilderImpl implements KeyphraseBuilder
  {
    private boolean isBuilt;
    private final String authority;
    private final String phrase;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    KeyphraseBuilderImpl(String authority, String phrase)
    {
        if ( phrase == null || phrase.length() == 0)
            throw new IllegalArgumentException( "Phrase may not be null.");
        this.authority = authority;
        this.phrase = phrase;
        this.isBuilt = false;
    }

    public String getAuthority()
    {
      return this.authority;
    }

    public String getPhrase()
    {
      return this.phrase;
    }

    public Buildable isBuildable()
    {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues() {
      if ( phrase == null || phrase.length() == 0 )
        return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.WARNING, "Phrase may not be null or empty.", this, null ));
      return new BuilderIssues();
    }

    public ThreddsMetadata.Keyphrase build() throws IllegalStateException {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.KeyphraseImpl();
    }
  }

  static class ProjectNameBuilderImpl implements ProjectNameBuilder
  {
    private boolean isBuilt;
    private String namingAuthority;
    private String projectName;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    ProjectNameBuilderImpl(String namingAuthority, String projectName)
    {
        if ( projectName == null || projectName.length() == 0)
            throw new IllegalArgumentException( "Phrase may not be null.");
        this.namingAuthority = namingAuthority;
        this.projectName = projectName;
        this.isBuilt = false;
    }

    public String getNamingAuthority() {
      return this.namingAuthority;
    }

    public String getName() {
      return this.projectName;
    }

    public Buildable isBuildable() {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues() {
      if ( projectName == null || projectName.length() == 0 )
        return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.WARNING, "Phrase may not be null or empty.", this, null ));
      return new BuilderIssues();
    }

    public ThreddsMetadata.ProjectName build() throws IllegalStateException {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.ProjectNameImpl();
    }
  }

  static class DatePointBuilderImpl implements DatePointBuilder
  {
        private final String date;
        private final String format;
        private final String type;

      private BuilderIssues builderIssues;
      private Buildable isBuildable;

        DatePointBuilderImpl(String date, String format, String type)
        {
            if ( date == null )
                throw new IllegalArgumentException( "Date may not be null.");

            this.date = date;
            this.format = format;
            this.type = type;
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
        public boolean equals( Object obj )
        {
            if ( this == obj ) return true;
            if ( ! ( obj instanceof DatePointBuilderImpl)) return false;
            return obj.hashCode() == this.hashCode();
        }

        @Override
        public int hashCode()
        {
            int result = 17;
            if ( this.date != null )
                result = 37*result + this.date.hashCode();
            if ( this.format != null )
                result = 37*result + this.format.hashCode();
            if ( this.type != null )
                result = 37*result + this.type.hashCode();
            return result;
        }

        public Buildable isBuildable() {
            return this.isBuilt;
        }

        public BuilderIssues checkForIssues() {
          if ( this.date == null )
            return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.ERROR, "Date may not be null.", this, null));
          return new BuilderIssues();
        }

        public ThreddsMetadata.DatePoint build() throws IllegalStateException {
          if ( this.isBuildable != Buildable.YES )
            throw new IllegalStateException( "CatalogBuilder not buildable.");

          return new ThreddsMetadataImpl.DatePointImpl();
        }
    }


  static class DateRangeBuilderImpl implements DateRangeBuilder
  {
    private final String startDateFormat;
    private final String startDate;
    private final String endDateFormat;
    private final String endDate;
    private final String duration;
    private final String resolution;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    DateRangeBuilderImpl(String startDate, String startDateFormat,
                         String endDate, String endDateFormat,
                         String duration, String resolution)
    {
      this.startDateFormat = startDateFormat;
      this.startDate = startDate;
      this.endDateFormat = endDateFormat;
      this.endDate = endDate;
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
          return (this.isBuilt ? "DateRange" : "DateRangeBuilder") +
                   " [" + this.startDate + " <-- " + this.duration + " --> " + this.endDate + "]";
        }

        @Override
        public boolean equals( Object obj )
        {
            if ( this == obj ) return true;
            if ( !( obj instanceof DateRangeBuilderImpl) ) return false;
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

        public Buildable isBuildable() {
            return this.isBuilt;
        }

        public BuilderIssues checkForIssues()
        {
            int specified = 3;
            if ( this.startDate == null || this.startDate.length() == 0 )
                specified--;
            if ( this.endDate == null || this.endDate.length() == 0 )
                specified--;
            if ( this.duration == null || this.duration.length() == 0 )
                specified--;

          if ( specified < 2)
            return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.ERROR, "Underspecified " + this.toString(), this, null));
          else  if ( specified > 2)
            return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.ERROR, "Overspecified " + this.toString(), this, null));
          else
            return new BuilderIssues();
        }

        public ThreddsMetadata.DateRange build() throws IllegalStateException {
          if ( this.isBuildable != Buildable.YES )
            throw new IllegalStateException( "CatalogBuilder not buildable.");

          return new ThreddsMetadataImpl.DateRangeImpl();
        }
    }

  static class ContributorBuilderImpl implements ContributorBuilder
  {
    private String authority;
    private String name;
    private String role;
    private String email;
    private String webPage;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    ContributorBuilderImpl() {}

    public String getNamingAuthority() {
      return this.authority;
    }

    public void setNamingAuthority( String authority )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.authority = authority;
    }

    public String getName() {
      return this.name;
    }

    public void setName( String name )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      if ( name == null )
        throw new IllegalArgumentException( "Name may not be null.");
      this.name = name;
    }

    public String getRole() {
      return this.role;
    }

    public void setRole( String role )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.role = role;
    }

    public String getEmail() {
      return this.email;
    }

    public void setEmail( String email )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.email = email;
    }

    public String getWebPageUrlAsString() {
      return this.webPage;
    }

    public void setWebPageUrl(String webPage)
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.webPage = webPage;
    }

    public Buildable isBuildable() {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues() {
      if ( this.name == null )
        return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.ERROR, "Name may not be null.", this, null));
      return new BuilderIssues();
    }

    public ThreddsMetadata.Contributor build() throws IllegalStateException
    {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.ContributorImpl();
    }
  }

  static class VariableGroupBuilderImpl implements VariableGroupBuilder
  {
    private String vocabularyAuthorityId;
    private String vocabularyAuthorityUrl;

    private List<VariableBuilderImpl> variables;

    private String variableMapUrl;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    VariableGroupBuilderImpl() {}

    public String getVocabularyAuthorityId() {
      return this.vocabularyAuthorityId;
    }

    public void setVocabularyAuthorityId( String vocabAuthId) {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has already been built." );
      this.vocabularyAuthorityId = vocabAuthId;
    }

    public String getVocabularyAuthorityUrlAsString() {
      return this.vocabularyAuthorityUrl;
    }

    public void setVocabularyAuthorityUrl( String vocabAuthUrl) {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has already been built." );
      this.vocabularyAuthorityUrl = vocabAuthUrl;
    }

    public List<Variable> getVariables() {
      if ( ! this.isBuilt)
        throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
      if ( this.variables == null )
        return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<Variable>( variables) );
    }

    public List<VariableBuilder> getVariableBuilders() {
      if ( this.isBuilt)
        throw new IllegalStateException( "This Builder has already been built." );
      if ( this.variables == null )
        return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<VariableBuilder>( variables ) );
    }

    public VariableBuilder addVariableBuilder( String name, String description, String units,
                                               String vocabId, String vocabName )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has already been built." );
      if ( this.variableMapUrl != null )
        throw new IllegalStateException( "Already contains variableMap, can't add variables." );

      VariableBuilderImpl newVar = new VariableBuilderImpl( name, description, units, vocabId, vocabName, this );

      if ( this.variables == null)
        this.variables = new ArrayList<VariableBuilderImpl>();
      this.variables.add( newVar );
      return newVar;
    }

    public String getVariableMapUrlAsString() {
      return this.variableMapUrl;
    }

    public void setVariableMapUrl( String variableMapUrl)
    {
      if ( this.isBuilt)
        throw new IllegalStateException( "This Builder has already been built.");
      if ( variableMapUrl != null && this.variables != null && ! this.variables.isEmpty())
        throw new IllegalStateException( "Already contains variables, can't set variableMap.");
      this.variableMapUrl = variableMapUrl;
    }

    public boolean isEmpty() {
      return variableMapUrl == null && ( this.variables == null || this.variables.isEmpty());
    }

    public Buildable isBuildable()
    {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues()
    {
      if ( variableMapUrl != null && this.variables != null && ! this.variables.isEmpty())
        return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.ERROR, "This VariableGroupBuilder has variables and variableMap.", this, null ));
      return new BuilderIssues();
    }

    public Object build() throws IllegalStateException
    {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.VariableGroupImpl();
    }
  }

  static class VariableBuilderImpl implements VariableBuilder
  {
    private String name;
    private String description;
    private String units;
    private String vocabularyId;
    private String vocabularyName;

    private VariableGroupBuilder parent;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    VariableBuilderImpl(String name, String description, String units,
                        String vocabId, String vocabName, VariableGroupBuilder parent)
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

    public void setName( String name )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.name = name;
    }

    public String getDescription() {
      return this.description;
    }

    public void setDescription( String description )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.description = description;
    }

    public String getUnits() {
      return this.units;
    }

    public void setUnits( String units )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.units = units;
    }

    public String getVocabularyId() {
      return this.vocabularyId;
    }

    public void setVocabularyId( String vocabularyId )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.vocabularyId = vocabularyId;
    }

    public String getVocabularyName() {
      return this.vocabularyName;
    }

    public void setVocabularyName( String vocabularyName )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.vocabularyName = vocabularyName;
    }

    public String getVocabularyAuthorityId() {
      return this.parent.getVocabularyAuthorityId();
    }

    public String getVocabularyAuthorityUrlAsString() {
      return this.parent.getVocabularyAuthorityUrlAsString();
    }

    public Buildable isBuildable()
    {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues()
    {
      if ( this.name == null || this.name.length() == 0 )
        return new BuilderIssues( new BuilderIssue( BuilderIssue.Severity.WARNING, "Variable name is null or empty.", this, null ));
      return new BuilderIssues();
    }

    public ThreddsMetadata.Variable build() throws IllegalStateException
    {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.VariableImpl();
    }
  }

  static class GeospatialCoverageBuilderImpl implements GeospatialCoverageBuilder
  {
    private static String defaultCrsUri = "urn:x-mycrs:2D-WGS84-ellipsoid";
    private static GeospatialRangeBuilder defaultRangeX = new GeospatialRangeBuilderImpl( 0.0, 0.0, Double.NaN, CDM.LON_UNITS );
    private static GeospatialRangeBuilder defaultRangeY = new GeospatialRangeBuilderImpl( 0.0, 0.0, Double.NaN, CDM.LAT_UNITS );
    private static GeospatialRangeBuilder defaultRangeZ = new GeospatialRangeBuilderImpl( 0.0, 0.0, Double.NaN, "km" );

    private String crsUri;
    //private boolean is3D;
    private boolean isZPositiveUp;

    private boolean isGlobal;
    private GeospatialRangeBuilderImpl x, y, z;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    GeospatialCoverageBuilderImpl() {
      this( defaultCrsUri, true, false, defaultRangeX, defaultRangeY, defaultRangeZ );
    }

    GeospatialCoverageBuilderImpl( String crsUri, boolean isZPositiveUp, boolean isGlobal,
                                   GeospatialRangeBuilder geospatialRangeX,
                                   GeospatialRangeBuilder geospatialRangeY,
                                   GeospatialRangeBuilder geospatialRangeZ)
    {
      this.isBuildable = Buildable.DONT_KNOW;
      this.crsUri = crsUri == null ? defaultCrsUri : crsUri;
      this.isZPositiveUp = isZPositiveUp;
      this.isGlobal = isGlobal;
      this.x = geospatialRangeX == null ? null : new GeospatialRangeBuilderImpl( geospatialRangeX.getStart(), geospatialRangeX.getSize(), geospatialRangeX.getResolution(), geospatialRangeX.getUnits());
      this.y = geospatialRangeY == null ? null : new GeospatialRangeBuilderImpl( geospatialRangeY.getStart(), geospatialRangeY.getSize(), geospatialRangeY.getResolution(), geospatialRangeY.getUnits());
      this.z = geospatialRangeZ == null ? null : new GeospatialRangeBuilderImpl( geospatialRangeZ.getStart(), geospatialRangeZ.getSize(), geospatialRangeZ.getResolution(), geospatialRangeZ.getUnits());
    }

    public void setCRS( String crsUri ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.crsUri = crsUri == null ? defaultCrsUri : crsUri;
    }

    @Override
    public void useDefaultCRS() {
      this.isBuildable = Buildable.DONT_KNOW;
      this.crsUri = defaultCrsUri;
    }

    public String getCRS() {
      return this.crsUri;
    }

    public void setGlobal( boolean isGlobal ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.isGlobal = isGlobal;
    }

    public boolean isGlobal() {
      return this.isGlobal;
    }

    public void setZPositiveUp( boolean isZPositiveUp ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.isZPositiveUp = isZPositiveUp;
    }

    public boolean isZPositiveUp() {  // Is this needed since have CRS?
      return this.isZPositiveUp;
    }

    @Override
    public GeospatialRangeBuilder setGeospatialRangeX(double start, double size, double resolution, String units) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.x = new GeospatialRangeBuilderImpl( start, size, resolution, units);
      return this.x;
    }

    @Override
    public GeospatialRangeBuilder getGeospatialRangeX() {
      return this.x;
    }

    @Override
    public GeospatialRangeBuilder setGeospatialRangeY(double start, double size, double resolution, String units) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.y = new GeospatialRangeBuilderImpl( start, size, resolution, units);
      return this.y;
    }

    @Override
    public GeospatialRangeBuilder getGeospatialRangeY() {
      return this.y;
    }

    @Override
    public GeospatialRangeBuilder setGeospatialRangeZ(double start, double size, double resolution, String units) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.z = new GeospatialRangeBuilderImpl( start, size, resolution, units);
      return this.z;
    }

    @Override
    public GeospatialRangeBuilder getGeospatialRangeZ() {
      return this.z;
    }

    public Buildable isBuildable() {
      return this.isBuildable;
    }

    public BuilderIssues checkForIssues()
    {
      this.builderIssues = new BuilderIssues();

      // ToDo all the checks

      return this.builderIssues;
    }

    public ThreddsMetadata.GeospatialCoverage build() throws IllegalStateException {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.GeospatialCoverageImpl(
          this.crsUri, this.isZPositiveUp, this.isGlobal, this.x, this.y, this.z );
    }
  }

  static class GeospatialRangeBuilderImpl implements GeospatialRangeBuilder
  {
    private double start;
    private double size;
    private double resolution;
    private String units;

    private BuilderIssues builderIssues;
    private Buildable isBuildable;

    GeospatialRangeBuilderImpl( double start, double size, double resolution, String units )
    {
      this.isBuildable = Buildable.DONT_KNOW;
      this.start = start;
      this.size = size;
      this.resolution = resolution;
      this.units = units;
    }

    public void setStart( double start ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.start = start;
    }

    public double getStart() {
      return this.start;
    }

    public void setSize( double size ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.size = size;
    }

    public double getSize() {
      return this.size;
    }

    public void setResolution( double resolution ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.resolution = resolution;
    }

    public double getResolution() {
      return this.resolution;
    }

    public void setUnits( String units ) {
      this.isBuildable = Buildable.DONT_KNOW;
      this.units = units == null ? "" : units;
    }

    public String getUnits() {
      return this.units;
    }

    public Buildable isBuildable() {
      return this.isBuildable;
    }

    public BuilderIssues checkForIssues()
    {
      this.builderIssues = new BuilderIssues();
      // ToDo all the checks.
      return this.builderIssues;
    }

    public ThreddsMetadata.GeospatialRange build() throws IllegalStateException
    {
      if ( this.isBuildable != Buildable.YES )
        throw new IllegalStateException( "CatalogBuilder not buildable.");

      return new ThreddsMetadataImpl.GeospatialRangeImpl( this.start, this.size, this.resolution, this.units);
    }
  }
}
