package thredds.catalog2;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
public interface ThreddsCatalogIssues
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
