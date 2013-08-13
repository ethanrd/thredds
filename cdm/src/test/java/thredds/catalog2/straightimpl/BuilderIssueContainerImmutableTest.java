package thredds.catalog2.straightimpl;

import org.junit.Test;
import thredds.catalog2.builder.BuilderIssues;

import static org.junit.Assert.*;

/**
 * _MORE_
 *
 * @author edavis
 */
public class BuilderIssueContainerImmutableTest {
  @Test
  public void t1() {
    BuilderIssues bldrIssues = new BuilderIssues();
    assertTrue( "BuilderIssues not empty.", bldrIssues.isEmpty());

//    BuilderIssueContainerImmutable bici = bldrIssues.build();
//    assertTrue( "issue container unexpectedly not empty.", bici.isEmpty());
  }
}
