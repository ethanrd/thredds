package thredds.catalog2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * _more_
 *
 * @author edavis
 */
public interface ThreddsCatalogIssueContainer
{
  public boolean isValid();

  public int getNumFatalIssues();
  public int getNumErrorIssues();
  public int getNumWarningIssues();
  public String getIssuesMessage();
  public String getFatalIssuesMessage();
  public String getErrorIssuesMessage();
  public String getWarningIssuesMessage();
}
