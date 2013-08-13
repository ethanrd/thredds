package thredds.catalog2.straightimpl;

import thredds.catalog2.builder.BuilderIssues;

/**
 * _more_
 *
 * @author edavis
 * @since 4.1
 */
class BuilderIssueVisitor
{
  private BuilderIssues allIssues;

  BuilderIssueVisitor() { }

  void addIssues( BuilderIssues issues )
  {
    if ( allIssues == null )
      allIssues = new BuilderIssues();

    allIssues.addAllIssues( issues );
  }

  BuilderIssues getIssues() {
    return allIssues;
  }
}
