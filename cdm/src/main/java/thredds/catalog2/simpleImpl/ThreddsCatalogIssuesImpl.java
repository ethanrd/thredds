package thredds.catalog2.simpleImpl;

import thredds.catalog2.ThreddsCatalogIssueContainer;
import thredds.catalog2.builder.BuilderIssues;
import thredds.catalog2.builder.BuilderIssue;

/**
 * _more_
 *
 * @author edavis
 * @since 4.1
 */
public class ThreddsCatalogIssuesImpl implements ThreddsCatalogIssueContainer
{
  private boolean isValid;

  private int numWarnIssues;
  private int numErrorIssues;
  private int numFatalIssues;

  private String warnMessages;
  private String errorMessages;
  private String fatalMessages;
  private String allMessages;

  ThreddsCatalogIssuesImpl( BuilderIssues issues)
  {
    isValid = issues.isValid();
    numWarnIssues = issues.getNumWarningIssues();
    numErrorIssues = issues.getNumErrorIssues();
    numFatalIssues = issues.getNumFatalIssues();
    StringBuilder warnMessagesBuilder = new StringBuilder();
    StringBuilder errorMessagesBuilder = new StringBuilder();
    StringBuilder fatalMessagesBuilder = new StringBuilder();
    StringBuilder allMessagesBuilder = new StringBuilder();

    for ( BuilderIssue issue : issues.getIssues() )
    {
      if ( issue.getSeverity().equals( BuilderIssue.Severity.WARNING )) {
        warnMessagesBuilder.append( issue.toString());
        allMessagesBuilder.append( issue.toString());
      }
      else if ( issue.getSeverity().equals( BuilderIssue.Severity.ERROR )) {
        errorMessagesBuilder.append( issue.toString());
        allMessagesBuilder.append( issue.toString() );
      }
      else if ( issue.getSeverity().equals( BuilderIssue.Severity.FATAL )) {
        fatalMessagesBuilder.append( issue.toString());
        allMessagesBuilder.append( issue.toString() );
      }
      else
        allMessagesBuilder.append( issue.toString() );
    }
    this.warnMessages = warnMessagesBuilder.toString();
    this.errorMessages = errorMessagesBuilder.toString();
    this.fatalMessages = fatalMessagesBuilder.toString();
    this.allMessages = allMessagesBuilder.toString();
  }

  @Override
  public boolean isValid() {
    return isValid;
  }

  @Override
  public int getNumFatalIssues() {
    return this.numFatalIssues;
  }

  @Override
  public int getNumErrorIssues() {
    return this.numErrorIssues;
  }

  @Override
  public int getNumWarningIssues() {
    return this.numWarnIssues;
  }

  @Override
  public String getIssuesMessage() {
    return this.allMessages;
  }

  @Override
  public String getFatalIssuesMessage() {
    return this.fatalMessages;
  }

  @Override
  public String getErrorIssuesMessage() {
    return this.errorMessages;
  }

  @Override
  public String getWarningIssuesMessage() {
    return warnMessages;
  }
}
