package thredds.catalog2.simpleImpl;

import thredds.catalog2.builder.BuilderIssues;

/**
 * _more_
 *
 * @author edavis
 * @since 4.1
 */
interface BuilderIssueVisitorNode
{
  public BuilderIssues getIssues( BuilderIssueVisitor visitor);
  public BuilderIssues buildWithIssues( BuilderIssueVisitor visitor);

  public BuilderIssues getLocalIssues();

}
