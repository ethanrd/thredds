package thredds.catalog2.straightimpl;

import org.junit.Test;
import static org.junit.Assert.*;

import thredds.catalog2.builder.BuilderIssues;

/**
 * _MORE_
 *
 * @author edavis
 */
public class PropertyBuilderContainerTest {

  @Test
  public void checkConformance_AddingPropertyReplacesPropertyWithSameName() {
    PropertyBuilderContainer pbc = new PropertyBuilderContainer();
    pbc.addProperty( "name1", "value1");
    assertEquals( 1, pbc.size());
    assertEquals( "value1", pbc.getPropertyValue( "name1"));
    pbc.addProperty( "name1", "value1a");
    assertEquals( 1, pbc.size());
    assertEquals( "value1a", pbc.getPropertyValue( "name1"));

    BuilderIssues builderIssues = pbc.checkForIssues();
    assertTrue( builderIssues.isEmpty());
    assertEquals( 0, builderIssues.getNumWarningIssues());
    assertEquals( 0, builderIssues.getNumErrorIssues());
    assertEquals( 0, builderIssues.getNumFatalIssues());
    assertTrue( builderIssues.isValid());
  }

  /**
   * Double check that not using the mutable list from the Builder in the built container.
   */
  @Test
  public void checkThatChangesToPropertyBuilderContainerDoNotChangeAlreadyBuiltPropertyContainer() {
    PropertyBuilderContainer pbc = new PropertyBuilderContainer();
    pbc.addProperty( "name1", "value1");

    PropertyContainer pc = pbc.build();
    assertEquals( 1, pc.size());

    pbc.addProperty( "name2", "value2");
    assertEquals( 2, pbc.size());
    assertEquals( 1, pc.size());
  }
}
