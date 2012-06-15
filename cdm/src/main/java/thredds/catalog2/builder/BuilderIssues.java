package thredds.catalog2.builder;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
public class BuilderIssues
{
  private final List<BuilderIssue> issues;
  private int numFatalIssues = 0;
  private int numErrorIssues = 0;
  private int numWarningIssues = 0;


  public BuilderIssues()
  {
    this.issues = new ArrayList<BuilderIssue>();
  }

  public BuilderIssues( BuilderIssue issue )
  {
    this();
    if ( issue == null )
      return;
    this.issues.add( issue );
  }

  public void addIssue( BuilderIssue issue )
  {
    if ( issue == null )
    {
      return;
    }
    this.issues.add( issue );
    trackSeverity( issue.getSeverity() );
  }

  public void addAllIssues( BuilderIssues issues )
  {
    if ( issues == null )
    {
      return;
    }
    if ( issues.isEmpty() )
    {
      return;
    }
    this.issues.addAll( issues.getIssues() );
    for ( BuilderIssue curIssue : issues.getIssues() )
    {
      trackSeverity( curIssue.getSeverity() );
    }
  }

  public void clear() {
    this.issues.clear();
    this.numWarningIssues = 0;
    this.numErrorIssues = 0;
    this.numFatalIssues = 0;
  }

  public boolean isEmpty() {
    return this.issues.isEmpty();
  }

  public int size() {
    return this.issues.size();
  }

  public int getNumWarningIssues() {
    return this.numWarningIssues;
  }

  public int getNumErrorIssues() {
    return this.numErrorIssues;
  }

  public int getNumFatalIssues() {
    return this.numFatalIssues;
  }

  public List<BuilderIssue> getIssues()
  {
    if ( issues.isEmpty() )
    {
      return Collections.emptyList();
    }
    return Collections.unmodifiableList( this.issues );
  }

  public boolean isValid() {
    if ( this.numFatalIssues > 0 || this.numErrorIssues > 0)
      return false;
    return true;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for ( BuilderIssue bfi : this.issues )
    {
      sb.append( bfi.toString());
    }
    return sb.toString();
  }

  private void trackSeverity( BuilderIssue.Severity severity )
  {
    if ( severity.equals( BuilderIssue.Severity.FATAL ) )
      this.numFatalIssues++;
    else if ( severity.equals( BuilderIssue.Severity.ERROR ) )
      this.numErrorIssues++;
    else if ( severity.equals( BuilderIssue.Severity.WARNING ) )
      this.numWarningIssues++;
  }
}
