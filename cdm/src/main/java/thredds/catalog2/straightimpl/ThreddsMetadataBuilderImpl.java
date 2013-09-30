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

  ThreddsMetadataBuilderImpl()
  {
    this.isBuilt = false;
    this.dataSizeInBytes = -1;
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
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built.");
    if ( this.docs == null )
      this.docs = new ArrayList<DocumentationBuilderImpl>();
    DocumentationBuilderImpl doc = new DocumentationBuilderImpl( docType, title, externalReference );
    this.docs.add( doc );
    return doc;
  }

  public DocumentationBuilder addDocumentation( String docType, String content )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( content == null ) throw new IllegalArgumentException( "Content may not be null." );
    if ( this.docs == null )
      this.docs = new ArrayList<DocumentationBuilderImpl>();
    DocumentationBuilderImpl doc = new DocumentationBuilderImpl( docType, content );
    this.docs.add( doc );
    return doc;
  }

  public boolean removeDocumentation( DocumentationBuilder docBuilder )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( docBuilder == null )
      return false;
    if ( this.docs == null )
      return false;
    return this.docs.remove( (DocumentationBuilderImpl) docBuilder );
  }

  public List<DocumentationBuilder> getDocumentationBuilders()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.docs == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<DocumentationBuilder>( this.docs) );
  }

  public List<Documentation> getDocumentation()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.docs == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Documentation>( this.docs ) );
  }

  public KeyphraseBuilder addKeyphrase( String authority, String phrase )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
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
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( keyphraseBuilder == null )
      return false;
    if ( this.keyphrases == null )
      return false;
    return this.keyphrases.remove( (KeyphraseBuilderImpl) keyphraseBuilder );
  }

  public List<KeyphraseBuilder> getKeyphraseBuilders()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.keyphrases == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<KeyphraseBuilder>( this.keyphrases ) );
  }

  public List<Keyphrase> getKeyphrases()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.keyphrases == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Keyphrase>( this.keyphrases ) );
  }

  public ProjectNameBuilder addProjectName( String namingAuthority, String name )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
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
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( projectNameBuilder == null )
      return false;
    if ( this.projectNames == null )
      return false;
    return this.projectNames.remove( (ProjectNameBuilderImpl) projectNameBuilder );
  }

  public List<ProjectNameBuilder> getProjectNameBuilders()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.projectNames == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ProjectNameBuilder>( this.projectNames ) );
  }

  public List<ProjectName> getProjectNames()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.projectNames == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ProjectName>( this.projectNames ) );
  }

  public ContributorBuilder addCreator()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.creators == null )
      this.creators = new ArrayList<ContributorBuilderImpl>();
    ContributorBuilderImpl contributor = new ContributorBuilderImpl();
    this.creators.add( contributor );
    return contributor;
  }

  public boolean removeCreator( ContributorBuilder creatorBuilder )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( creatorBuilder == null )
      return false;
    if ( this.creators == null )
      return false;
    return this.creators.remove( (ContributorBuilderImpl) creatorBuilder );
  }

  public List<ContributorBuilder> getCreatorBuilder()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.creators == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ContributorBuilder>( this.creators ) );
  }

  public List<Contributor> getCreator()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.creators == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Contributor>( this.creators ) );
  }

  public ContributorBuilder addContributor()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.contributors == null )
      this.contributors = new ArrayList<ContributorBuilderImpl>();
    ContributorBuilderImpl contributor = new ContributorBuilderImpl();
    this.contributors.add( contributor );
    return contributor;
  }

  public boolean removeContributor( ContributorBuilder contributorBuilder )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( contributorBuilder == null )
      return false;
    if ( this.contributors == null )
      return false;
    return this.contributors.remove( (ContributorBuilderImpl) contributorBuilder );
  }

  public List<ContributorBuilder> getContributorBuilder()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.contributors == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ContributorBuilder>( this.contributors ) );
  }

  public List<Contributor> getContributor()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.contributors == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Contributor>( this.contributors ) );
  }

  public ContributorBuilder addPublisher()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.publishers == null )
      this.publishers = new ArrayList<ContributorBuilderImpl>();
    ContributorBuilderImpl contributor = new ContributorBuilderImpl();
    this.publishers.add( contributor );
    return contributor;
  }

  public boolean removePublisher( ContributorBuilder publisherBuilder )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( publisherBuilder == null )
      return false;
    if ( this.publishers == null )
      return false;
    return this.publishers.remove( (ContributorBuilderImpl) publisherBuilder );
  }

  public List<ContributorBuilder> getPublisherBuilder()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.publishers == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<ContributorBuilder>( this.publishers ) );
  }

  public List<Contributor> getPublisher()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.publishers == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<Contributor>( this.publishers ) );
  }

    public DatePointBuilder addOtherDatePointBuilder( String date, String format, String type )
    {
        if ( this.isBuilt )
            throw new IllegalStateException( "This Builder has been built." );
        DatePointType datePointType = DatePointType.getTypeForLabel( type );
        if ( datePointType != DatePointType.Other
             && datePointType != DatePointType.Untyped )
            throw new IllegalArgumentException( "Must use explicit setter method for given type [" + type + "]." );
        if ( this.otherDates == null )
            this.otherDates = new ArrayList<DatePointBuilderImpl>();
        DatePointBuilderImpl dp = new DatePointBuilderImpl( date, format, type);
        this.otherDates.add( dp );
        return dp;
    }

    public boolean removeOtherDatePointBuilder( DatePointBuilder builder )
    {
        if ( this.isBuilt )
            throw new IllegalStateException( "This Builder has been built." );
        if ( builder == null )
            return false;
        if ( this.otherDates == null )
            return false;
        return this.otherDates.remove( (DatePointBuilderImpl) builder );
    }

    public List<DatePointBuilder> getOtherDatePointBuilders()
    {
        if ( this.isBuilt )
            throw new IllegalStateException( "This Builder has been built." );
        if ( this.otherDates == null )
            return Collections.emptyList();
        return Collections.unmodifiableList( new ArrayList<DatePointBuilder>( this.otherDates) );
    }

    public List<DatePoint> getOtherDates()
    {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        if ( this.otherDates == null )
            return Collections.emptyList();
        return Collections.unmodifiableList( new ArrayList<DatePoint>( this.otherDates ) );
    }

    public DatePointBuilder setCreatedDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.createdDate = new DatePointBuilderImpl( date, format, DatePointType.Created.toString());
    return this.createdDate;
  }

  public DatePointBuilder getCreatedDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.createdDate;
  }

    public DatePoint getCreatedDate()
    {
        if ( ! this.isBuilt)
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.createdDate;
    }

  public DatePointBuilder setModifiedDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.modifiedDate = new DatePointBuilderImpl( date, format, DatePointType.Modified.toString() );
    return this.modifiedDate;
  }

  public DatePointBuilder getModifiedDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.modifiedDate;
  }

    public DatePoint getModifiedDate() {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.modifiedDate;
    }

  public DatePointBuilder setIssuedDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.issuedDate = new DatePointBuilderImpl( date, format, DatePointType.Issued.toString() );
    return this.issuedDate;
  }

  public DatePointBuilder getIssuedDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.issuedDate;
  }

    public DatePoint getIssuedDate()
    {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.issuedDate;
    }

    public DatePointBuilder setValidDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.validDate = new DatePointBuilderImpl( date, format, DatePointType.Valid.toString() );
    return this.validDate;
  }

  public DatePointBuilder getValidDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.validDate;
  }

    public DatePoint getValidDate()
    {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.validDate;
    }

  public DatePointBuilder setAvailableDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.availableDate = new DatePointBuilderImpl( date, format, DatePointType.Available.toString() );
    return this.availableDate;
  }

  public DatePointBuilder getAvailableDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.availableDate;
  }

    public DatePoint getAvailableDate()
    {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.availableDate;
    }

  public DatePointBuilder setMetadataCreatedDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.metadataCreatedDate = new DatePointBuilderImpl( date, format, DatePointType.MetadataCreated.toString() );
    return this.metadataCreatedDate;
  }

  public DatePointBuilder getMetadataCreatedDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.metadataCreatedDate;
  }

    public DatePoint getMetadataCreatedDate()
    {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.metadataCreatedDate;
    }

  public DatePointBuilder setMetadataModifiedDatePointBuilder( String date, String format )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.metadataModifiedDate = new DatePointBuilderImpl( date, format, DatePointType.MetadataModified.toString() );
    return this.metadataModifiedDate;
  }

  public DatePointBuilder getMetadataModifiedDatePointBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.metadataModifiedDate;
  }

    public DatePoint getMetadataModifiedDate()
    {
        if ( !this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
        return this.metadataModifiedDate;
    }

    public GeospatialCoverageBuilder setNewGeospatialCoverageBuilder( URI crsUri )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    GeospatialCoverageBuilderImpl gci = new GeospatialCoverageBuilderImpl();
    gci.setCRS( crsUri );
    this.geospatialCoverage = gci;
    return null;
  }

  public void removeGeospatialCoverageBuilder()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.geospatialCoverage = null;
  }

  public GeospatialCoverageBuilder getGeospatialCoverageBuilder()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    return this.geospatialCoverage;
  }

  public GeospatialCoverage getGeospatialCoverage()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    return this.geospatialCoverage;
  }

  public DateRangeBuilder setTemporalCoverageBuilder( String startDate, String startDateFormat,
                                                      String endDate, String endDateFormat,
                                                      String duration, String resolution )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.temporalCoverage = new DateRangeBuilderImpl( startDate, startDateFormat, endDate, endDateFormat, duration, resolution );
    return this.temporalCoverage;
  }

  public DateRangeBuilder getTemporalCoverageBuilder()
  {
      if ( this.isBuilt )
          throw new IllegalStateException( "This Builder has been built." );
      return this.temporalCoverage;
  }

    public DateRange getTemporalCoverage() {
        if ( ! this.isBuilt )
            throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built.");
        return this.temporalCoverage;
    }

  public VariableGroupBuilder addVariableGroupBuilder()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.variableGroups == null )
      this.variableGroups = new ArrayList<VariableGroupBuilderImpl>();
    VariableGroupBuilderImpl varGroup = new VariableGroupBuilderImpl();
    this.variableGroups.add( varGroup);
    return varGroup;
  }

  public boolean removeVariableGroupBuilder( VariableGroupBuilder variableGroupBuilder )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( variableGroupBuilder == null )
      return false;
    if ( this.variableGroups == null )
      return false;
    return this.variableGroups.remove( (VariableGroupBuilderImpl) variableGroupBuilder );
  }

  public List<VariableGroupBuilder> getVariableGroupBuilders()
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    if ( this.variableGroups == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<VariableGroupBuilder>( this.variableGroups ) );
  }

  public List<VariableGroup> getVariableGroups()
  {
    if ( ! this.isBuilt )
      throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
    if ( this.variableGroups == null )
      return Collections.emptyList();
    return Collections.unmodifiableList( new ArrayList<VariableGroup>( this.variableGroups ) );
  }

  public void setDataSizeInBytes( long dataSizeInBytes )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.dataSizeInBytes = dataSizeInBytes;
  }

  public long getDataSizeInBytes()
  {
    return this.dataSizeInBytes;
  }

  public void setDataFormat( DataFormatType dataFormat )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.dataFormat = dataFormat;
  }
  public void setDataFormat( String dataFormat )
  {
    this.setDataFormat( DataFormatType.getType( dataFormat));
  }

  public DataFormatType getDataFormat()
  {
    return this.dataFormat;
  }

  public void setDataType( FeatureType dataType )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.dataType = dataType;
  }
    public void setDataType( String dataType)
    {
        this.setDataType( FeatureType.getType( dataType ));
    }

  public FeatureType getDataType()
  {
    return this.dataType;
  }

  public void setCollectionType( String collectionType )
  {
    if ( this.isBuilt )
      throw new IllegalStateException( "This Builder has been built." );
    this.collectionType = collectionType;
  }

  public String getCollectionType() // ?????
  {
    return this.collectionType;
  }

  public Buildable isBuildable()
  {
    return this.isBuilt;
  }

  public BuilderIssues checkForIssues()
  {
    BuilderIssues issues = new BuilderIssues();

    // Check subordinates.
    if ( this.docs != null )
      for ( DocumentationBuilderImpl doc : this.docs )
        issues.addAllIssues( doc.checkForIssues());
    if ( this.keyphrases != null )
      for( KeyphraseBuilderImpl keyphrase : this.keyphrases )
        issues.addAllIssues( keyphrase.checkForIssues());
    if ( this.creators != null )
      for( ContributorBuilderImpl creator : this.creators )
        issues.addAllIssues( creator.checkForIssues());
    if ( this.contributors != null )
      for( ContributorBuilderImpl contributor : this.contributors )
        issues.addAllIssues( contributor.checkForIssues());
    if ( this.publishers != null )
      for( ContributorBuilderImpl publisher : this.publishers )
        issues.addAllIssues( publisher.checkForIssues());

    if ( this.otherDates != null )
      for( DatePointBuilderImpl date : this.otherDates )
        issues.addAllIssues( date.checkForIssues());
    if ( this.createdDate != null )
      issues.addAllIssues( this.createdDate.checkForIssues() );
    if ( this.modifiedDate != null )
      issues.addAllIssues( this.modifiedDate.checkForIssues() );
    if ( this.issuedDate != null )
      issues.addAllIssues( this.issuedDate.checkForIssues() );
    if ( this.validDate != null )
      issues.addAllIssues( this.validDate.checkForIssues() );
    if ( this.availableDate != null )
      issues.addAllIssues( this.availableDate.checkForIssues() );
    if ( this.metadataCreatedDate != null )
      issues.addAllIssues( this.metadataCreatedDate.checkForIssues() );
    if ( this.metadataModifiedDate != null )
      issues.addAllIssues( this.metadataModifiedDate.checkForIssues() );

    if ( this.geospatialCoverage != null )
      issues.addAllIssues( this.geospatialCoverage.checkForIssues() );
    if ( this.temporalCoverage != null )
      issues.addAllIssues( this.temporalCoverage.checkForIssues() );

    if ( this.variableGroups != null )
      for ( VariableGroupBuilderImpl variableGroup : this.variableGroups )
        issues.addAllIssues( variableGroup.checkForIssues() );

    return issues;
  }

  public ThreddsMetadata build() throws BuilderException
  {
    if ( this.isBuilt )
      return this;

    // Check subordinates.
    if ( this.docs != null )
      for ( DocumentationBuilderImpl doc : this.docs )
        doc.build();
    if ( this.keyphrases != null )
      for ( KeyphraseBuilderImpl keyphrase : this.keyphrases )
        keyphrase.build();
    if ( this.creators != null )
      for ( ContributorBuilderImpl creator : this.creators )
        creator.build();
    if ( this.contributors != null )
      for ( ContributorBuilderImpl contributor : this.contributors )
        contributor.build();
    if ( this.publishers != null )
      for ( ContributorBuilderImpl publisher : this.publishers )
        publisher.build();

    if ( this.otherDates != null )
      for ( DatePointBuilderImpl date : this.otherDates )
        date.build();
    if ( this.createdDate != null )
      this.createdDate.build();
    if ( this.modifiedDate != null )
      this.modifiedDate.build();
    if ( this.issuedDate != null )
      this.issuedDate.build();
    if ( this.validDate != null )
      this.validDate.build();
    if ( this.availableDate != null )
      this.availableDate.build();
    if ( this.metadataCreatedDate != null )
      this.metadataCreatedDate.build();
    if ( this.metadataModifiedDate != null )
      this.metadataModifiedDate.build();

    if ( this.geospatialCoverage != null )
      this.geospatialCoverage.build();
    if ( this.temporalCoverage != null )
      this.temporalCoverage.build();

    if ( this.variableGroups != null )
      for ( VariableGroupBuilderImpl variableGroup : this.variableGroups )
        variableGroup.build();

    this.isBuilt = true;
    return this;
  }

  static class DocumentationBuilderImpl implements DocumentationBuilder
  {
    private boolean isBuilt = false;

    private final boolean isContainedContent;

    private final String docType;
    private final String title;
    private final String externalReferenceUriAsString;
    private final String content;

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

    public boolean isContainedContent()
    {
      return this.isContainedContent;
    }

    public String getDocType()
    {
      return this.docType;
    }

    public String getContent()
    {
      if ( ! this.isContainedContent )
        throw new IllegalStateException( "No contained content, use externally reference to access documentation content." );
      return this.content;
    }

    public String getTitle()
    {
      if ( this.isContainedContent )
        throw new IllegalStateException( "Documentation with contained content has no title." );
      return this.title;
    }

    public String getExternalReferenceUriAsString()
    {
      if ( this.isContainedContent )
        throw new IllegalStateException( "Documentation with contained content has no external reference.");
      return this.externalReferenceUriAsString;
    }

    public Buildable isBuildable()
    {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues()
    {
      return new BuilderIssues();
    }

    public ThreddsMetadata.Documentation build() throws IllegalStateException
    {
        this.isBuilt = true;
        return this;
    }
  }

  static class KeyphraseBuilderImpl implements KeyphraseBuilder
  {
    private boolean isBuilt;
    private final String authority;
    private final String phrase;

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

    public Keyphrase build() throws BuilderException {
        this.isBuilt = true;
        return this;
    }
  }

  static class ProjectNameBuilderImpl implements ProjectNameBuilder
  {
    private boolean isBuilt;
    private String namingAuthority;
    private String projectName;

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

    public ProjectName build() throws BuilderException {
        this.isBuilt = true;
        return this;
    }
  }

    static class DatePointBuilderImpl implements DatePointBuilder
    {
        private boolean isBuilt = false;

        private final String date;
        private final String format;
        private final String type;

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

        public DatePoint build() throws BuilderException {
            this.isBuilt = true;
            return this;
        }
    }


  static class DateRangeBuilderImpl implements DateRangeBuilder
  {
    private boolean isBuilt = false;

    private final String startDateFormat;
    private final String startDate;
    private final String endDateFormat;
    private final String endDate;
    private final String duration;
    private final String resolution;

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

        public DateRange build() throws BuilderException {
            this.isBuilt = true;
            return this;
        }
    }

  static class ContributorBuilderImpl implements ContributorBuilder
  {
    private boolean isBuilt;

    private String authority;
    private String name;
    private String role;
    private String email;
    private String webPage;

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

    public Contributor build() throws BuilderException
    {
      this.isBuilt = true;
      return this;
    }
  }

  static class VariableGroupBuilderImpl implements VariableGroupBuilder
  {
    private boolean isBuilt = false;

    private String vocabularyAuthorityId;
    private String vocabularyAuthorityUrl;

    private List<VariableBuilderImpl> variables;

    private String variableMapUrl;

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

    public Object build() throws BuilderException
    {
      this.isBuilt = true;
      return this;
    }
  }

  static class VariableBuilderImpl implements VariableBuilder
  {
    private boolean isBuilt;

    private String name;
    private String description;
    private String units;
    private String vocabularyId;
    private String vocabularyName;

    private VariableGroupBuilder parent;

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

    public Variable build() throws BuilderException
    {
      this.isBuilt = true;
      return this;
    }
  }

  static class GeospatialCoverageBuilderImpl implements GeospatialCoverageBuilder
  {
    private boolean isBuilt;

    private URI defaultCrsUri;

    private URI crsUri;
    //private boolean is3D;
    private boolean isZPositiveUp;

    private boolean isGlobal;
    private List<GeospatialRangeBuilderImpl> extent;

    GeospatialCoverageBuilderImpl()
    {
      this.isBuilt = false;
      String defaultCrsUriString = "urn:x-mycrs:2D-WGS84-ellipsoid";
      try
      { this.defaultCrsUri = new URI( defaultCrsUriString ); }
      catch ( URISyntaxException e )
      { throw new IllegalStateException( "Bad URI syntax for default CRS URI ["+defaultCrsUriString+"]: " + e.getMessage()); }
      this.crsUri = this.defaultCrsUri;
    }

    public void setCRS( URI crsUri )
    {
      if ( this.isBuilt)
        throw new IllegalStateException( "This Builder has been built.");
      if ( crsUri == null )
        this.crsUri = this.defaultCrsUri;
      this.crsUri = crsUri;
    }

    public URI getCRS()
    {
      return this.crsUri;
    }

    public void setGlobal( boolean isGlobal )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.isGlobal = isGlobal;
    }

    public boolean isGlobal()
    {
      return this.isGlobal;
    }

    public void setZPositiveUp( boolean isZPositiveUp )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      this.isZPositiveUp = isZPositiveUp;
    }

    public boolean isZPositiveUp()   // Is this needed since have CRS?
    {
      return this.isZPositiveUp;
    }

    public GeospatialRangeBuilder addExtentBuilder()
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      if ( this.extent == null )
        this.extent = new ArrayList<GeospatialRangeBuilderImpl>();
      GeospatialRangeBuilderImpl gri = new GeospatialRangeBuilderImpl();
      this.extent.add( gri );
      return gri;
    }

    public boolean removeExtentBuilder( GeospatialRangeBuilder geospatialRangeBuilder )
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      if ( geospatialRangeBuilder == null )
        return true;
      if ( this.extent == null )
        return false;
      return this.extent.remove( (GeospatialRangeBuilderImpl) geospatialRangeBuilder );
    }

    public List<GeospatialRangeBuilder> getExtentBuilders()
    {
      if ( this.isBuilt )
        throw new IllegalStateException( "This Builder has been built." );
      if ( this.extent == null )
        return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<GeospatialRangeBuilder>( this.extent) );
    }

    public List<GeospatialRange> getExtent()
    {
      if ( ! this.isBuilt )
        throw new IllegalStateException( "Sorry, I've escaped from my Builder before being built." );
      if ( this.extent == null )
        return Collections.emptyList();
      return Collections.unmodifiableList( new ArrayList<GeospatialRange>( this.extent ) );
    }

    public Buildable isBuildable()
    {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues()
    {
      return new BuilderIssues();
    }

    public GeospatialCoverage build() throws BuilderException
    {
      return this;
    }
  }

  static class GeospatialRangeBuilderImpl implements GeospatialRangeBuilder
  {
    private boolean isBuilt;

    private boolean isHorizontal;
    private double start;
    private double size;
    private double resolution;
    private String units;

    GeospatialRangeBuilderImpl()
    {
      this.isBuilt = false;
      
      this.isHorizontal = false;
      this.start = 0.0;
      this.size = 0.0;
      this.resolution = 0.0;
      this.units = "";
    }

    public void setHorizontal( boolean isHorizontal )
    {
      if ( this.isBuilt ) throw new IllegalStateException( "This Builder has been built.");
      this.isHorizontal = isHorizontal;
    }

    public boolean isHorizontal()
    {
      return this.isHorizontal;
    }

    public void setStart( double start )
    {
      if ( this.isBuilt ) throw new IllegalStateException( "This Builder has been built." );
      this.start = start;
    }

    public double getStart()
    {
      return this.start;
    }

    public void setSize( double size )
    {
      if ( this.isBuilt ) throw new IllegalStateException( "This Builder has been built." );
      this.size = size;
    }

    public double getSize()
    {
      return this.size;
    }

    public void setResolution( double resolution )
    {
      if ( this.isBuilt ) throw new IllegalStateException( "This Builder has been built." );
      this.resolution = resolution;
    }

    public double getResolution()
    {
      return this.resolution;
    }

    public void setUnits( String units )
    {
      if ( this.isBuilt ) throw new IllegalStateException( "This Builder has been built." );
      this.units = units == null ? "" : units;
    }

    public String getUnits()
    {
      return this.units;
    }

    public Buildable isBuildable()
    {
      return this.isBuilt;
    }

    public BuilderIssues checkForIssues()
    {
      return new BuilderIssues();
    }

    public GeospatialRange build() throws BuilderException
    {
      this.isBuilt = true;
      return this;
    }
  }
}
