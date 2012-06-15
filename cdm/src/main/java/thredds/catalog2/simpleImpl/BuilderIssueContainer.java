package thredds.catalog2.simpleImpl;

import thredds.catalog2.builder.BuilderIssues;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
class BuilderIssueContainer
{
  private boolean isBuilt;
  private final BuilderIssues builderIssues;
  private final BuilderIssues externalIssues;

  BuilderIssueContainer() {
    isBuilt = false;
    builderIssues = new BuilderIssues();
    externalIssues = new BuilderIssues();
  }

  BuilderIssues getBuilderIssues( boolean isBuilt) {
          return null;
  }

  BuilderIssues getExternalIssues() { return null;}

  BuilderIssues buildAndGetIssues() { return null;}


}
