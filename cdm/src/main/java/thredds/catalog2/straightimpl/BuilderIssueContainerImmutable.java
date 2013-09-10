package thredds.catalog2.straightimpl;

import thredds.catalog2.builder.BuilderIssue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * An Immutable Container for BuilderIssue-s.
 *
 * @author edavis
 */
public final class BuilderIssueContainerImmutable {
  private final List<BuilderIssue> issues;
  private final List<BuilderIssue> fatalIssues;
  private final List<BuilderIssue> errorIssues;
  private final List<BuilderIssue> warnIssues;

  BuilderIssueContainerImmutable(List<BuilderIssue> allIssues) {
    if ( allIssues == null || allIssues.isEmpty()) {
      this.issues = Collections.emptyList();
      this.warnIssues = Collections.emptyList();
      this.errorIssues = Collections.emptyList();
      this.fatalIssues = Collections.emptyList();
      return;
    }

    List<BuilderIssue> tmpIssues = new ArrayList<BuilderIssue>();
    List<BuilderIssue> tmpFatalIssues = new ArrayList<BuilderIssue>();
    List<BuilderIssue> tmpErrorIssues = new ArrayList<BuilderIssue>();
    List<BuilderIssue> tmpWarnIssues = new ArrayList<BuilderIssue>();

    for ( BuilderIssue issue : allIssues) {
      tmpIssues.add( issue);
      if ( issue.getSeverity() == BuilderIssue.Severity.FATAL)
        tmpFatalIssues.add( issue);
      else if ( issue.getSeverity() == BuilderIssue.Severity.ERROR)
        tmpErrorIssues.add( issue);
      else if ( issue.getSeverity() == BuilderIssue.Severity.WARNING)
        tmpWarnIssues.add( issue);
    }
    this.issues = Collections.unmodifiableList( tmpIssues);
    if (tmpFatalIssues.isEmpty() )
      this.fatalIssues = Collections.emptyList();
    else
      this.fatalIssues = Collections.unmodifiableList( tmpFatalIssues);
    if (tmpErrorIssues.isEmpty() )
      this.errorIssues = Collections.emptyList();
    else
      this.errorIssues = Collections.unmodifiableList( tmpErrorIssues);
    if (tmpWarnIssues.isEmpty() )
      this.warnIssues = Collections.emptyList();
    else
      this.warnIssues = Collections.unmodifiableList( tmpWarnIssues);
  }

  public boolean isEmpty() {
    return this.issues.isEmpty();
  }

  public int size() {
    return this.issues.size();
  }

  public int getNumWarningIssues() {
    return this.warnIssues.size();
  }

  public int getNumErrorIssues() {
    return this.errorIssues.size();
  }

  public int getNumFatalIssues() {
    return this.fatalIssues.size();
  }

  public List<BuilderIssue> getIssues() {
    return Collections.unmodifiableList( this.issues );
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    if ( this.issues == null || this.issues.isEmpty() )
      sb.append( "No Issues.\n");
    else
      for ( BuilderIssue bfi : this.issues )
        sb.append( bfi.toString());
    return sb.toString();
  }
}
